(ns fix.spec.component-spec
  (:require [clojure.spec.alpha :as spec]
            [taoensso.timbre :refer [error info warn debug]]
            [fix.definitions.components :as c]
            [fix.spec.fields-spec :as f]))

(declare matching-component?)

(defn- get-num-in-group-count [[given-head & _] [num-in-group & _]]
  (if (and (= (:tag given-head) (:tag num-in-group))
           (pos-int? (:value given-head)))
    (:value given-head)
    false))

(defn- required-component-without-required-fields? [comp]
  (when (and (:required comp) (every? #(not (:required %)) (:ordering comp)))
    (warn "Required component without any required fields: " (:name comp))))

(defn- matching-field? [e1 e2]
  (debug "--- MATCHING FIELD ---")
  (debug "Comparing the field tags:" (:tag e1) "==" (:tag e2))
  (= (:tag e1) (:tag e2)))

(defn- extract-index [seq e]
  (.indexOf (map :tag seq) (:tag e)))

(defn- is-field-out-of-order? [seq-a seq-b within-group]
  (and (not= (extract-index seq-b (first seq-a)) -1) ;do not consider ooo when it's not the same component depth
       (not= (extract-index seq-a (first seq-b)) -1)
       (not within-group))) ;do not consider ooo when field is within a group

(defn- switch-indices [seq idx1 idx2]
  (if (= idx1 idx2)
    seq
    (let [idx1-elem (nth seq idx1 nil)
          idx2-elem (nth seq idx2 nil)]
      (if (and (some? idx1-elem) (some? idx2-elem))
        (-> (assoc seq idx1 idx2-elem)
            (assoc idx2 idx1-elem))
        (do
          (debug "Cannot switch indices: Index out of bounds: " idx1 "," idx2)
          nil)))))

(defn- matching-group? [seq group deep-eval]
  (debug "--- MATCHING GROUP ---")
  (debug "Group match called with:" seq "and" group)
  (loop [num-in-group (get-num-in-group-count seq (:ordering group))
         given-seq (vec (drop 1 seq))
         group-content-as-component {:ordering (second (:ordering group))}]
    (if-let [result (matching-component? given-seq group-content-as-component false deep-eval true)]
      (if (false? num-in-group)
        (cond
          (true? result) true
          (seq? result) (do
                          (error "Missing num-in-group definition for repeating values for group" (:tag (first (:ordering group))))
                          false))
        (let [final-iteration (= num-in-group 1)]
          (cond
            (and final-iteration (true? result)) true
            (and final-iteration (seq? result)) result
            (and (not final-iteration) (true? result)) (do
                                                         (error "Group" (:tag (first (:ordering group)))
                                                                "contains less repetitions than expected")
                                                         false)
            (and (not final-iteration) (seq? result)) (recur (dec num-in-group) result group-content-as-component))))
      false)))

(defn- matching-sub-component? [seq component deep-eval]
  (debug "--- MATCHING SUB-COMPONENT ---")
  (debug "Sub-component match called with:" seq "and" component)
  (required-component-without-required-fields? component)
  (matching-component? seq component false deep-eval false))

(defn matching-component?
  ([given component] (matching-component? given component true true false))
  ([given component is-root-call deep-eval within-group]
   (debug "--- MATCHING COMPONENT ---")
   (debug "Matching component called with:" given "and" component)
   (loop [seq-a given
          seq-b (:ordering component)]
     (cond
       (and (empty? seq-a) (empty? seq-b)) true
       (and (seq seq-a) (empty? seq-b)) (if is-root-call false seq-a) ;this bubbles up the remaining tags to match
       :else (let [[a & a-tail] seq-a
                   [b & b-tail] seq-b]
               (if (= :field (:type b))
                 (if (matching-field? a b)
                   (if (or (spec/valid? ::f/field a) (not deep-eval))
                     (recur a-tail b-tail)
                     (do
                       (error "Field is invalid:" a)
                       false))
                   (if (is-field-out-of-order? seq-a seq-b within-group)
                     (recur (switch-indices (vec seq-a) 0 (extract-index seq-a b)) seq-b)
                     (if (not (:required b))
                       (do
                         (debug "Field is not required:" b)
                         (recur seq-a b-tail))
                       (do
                         (error "Field tag does not match the definition:" (:tag a) "vs" (:tag b))
                         false))))
                 (if-let [a-rest (case (:type b)
                                   :group (matching-group? seq-a b deep-eval)
                                   :component (matching-sub-component? seq-a b deep-eval))]
                   (if (seq? a-rest)
                     (do
                       (debug "Bubbling up the remaining message part up the call hierarchy:" a-rest)
                       (recur a-rest b-tail))
                     (recur nil b-tail))
                   false)))))))

(defn- is-component? [[seq comp]]
  (cond
    (keyword? comp) (matching-component? seq (comp c/components))
    (and (map? comp) (contains? comp :ordering)) (matching-component? seq comp)
    :else false))

(spec/def ::component is-component?)

