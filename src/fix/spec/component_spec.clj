(ns fix.spec.component-spec
  (:require [clojure.spec.alpha :as spec]
            [clojure.tools.logging :refer [warn]]
            [fix.definitions.components :as c]
            [fix.spec.fields-spec :as f]))

(declare matching-component?)

;TODO check if the num-in-group tag is actually of type NUMINGROUP
(defn- get-num-in-group-count [[given-head & _] [num-in-group & _]]
  (if (and (= (:tag given-head) (:tag num-in-group))
           (pos-int? (:value given-head)))
    (:value given-head)
    false))

(defn- required-component-without-required-fields? [comp]
  (when (and (:required comp) (every? #(not (:required %)) (:ordering comp)))
    (warn (str "Required component without any required fields: " (:name comp)))))


(defn- matching-field? [e1 e2]
  (println "--- MATCHING FIELD ---")
  (do (println (str "Compare: " (:tag e1) " == " (:tag e2)))
      (= (:tag e1) (:tag e2))))


(defn- matching-group? [seq group deep-eval]
  (println (str "--- GROUP MATCH ---"))
  (println (str "[GROUP] Calling again with: " seq " and " group))
  (loop [num-in-group (get-num-in-group-count seq (:ordering group))
         given-seq (vec (drop 1 seq))
         group-content-as-component {:ordering (second (:ordering group))}]
    (if-let [result (matching-component? given-seq group-content-as-component false deep-eval)]
      (if (false? num-in-group)
        (cond
          (true? result) true
          (seq? result) false)
        (let [final-iteration (= num-in-group 1)]
          (cond
            (and final-iteration (true? result)) true
            (and final-iteration (seq? result)) result
            (and (not final-iteration) (true? result)) false
            (and (not final-iteration) (seq? result)) (recur (dec num-in-group) result group-content-as-component))))
      false)))

;TODO inline when debugging can be removed
(defn- matching-sub-component? [seq component deep-eval]
  (println "--- SUB COMPONENT MATCH ---")
  (required-component-without-required-fields? component)
  (println (str "[COMPONENT] Calling again with: " seq " and " component))
  (matching-component? seq component false deep-eval))

(defn matching-component?
  ([given component] (matching-component? given component true true))
  ([given component is-root-call deep-eval]
   (println "##### MATCHING SEQS #####")
   (println (str "Called with: " given " and " component))
   (loop [seq-a given
          seq-b (:ordering component)]
     (println (str "Inside loop - Called with: " seq-a " and " seq-b))
     (cond
       (and (empty? seq-a) (empty? seq-b)) true
       (and (seq seq-a) (empty? seq-b)) (if is-root-call false seq-a) ;this bubbles up the remaining tags to match
       :else (let [[a & a-tail] seq-a
                   [b & b-tail] seq-b]
               (if (= :field (:type b))
                 (if (matching-field? a b)
                   (if (or (spec/valid? ::f/field a)(not deep-eval))
                     (recur a-tail b-tail)
                     (do (println "Field is not valid: " a)
                         false))
                   (if (not (:required b))
                     (do
                       (println "Not required: " b)
                       (recur seq-a b-tail))
                     false))
                 (if-let [a-rest (case (:type b)
                                   :group (matching-group? seq-a b deep-eval)
                                   :component (matching-sub-component? seq-a b deep-eval))]
                   (if (seq? a-rest)
                     (do
                       (println (str "Unmatched a-rest bubble up: " a-rest))
                       (recur a-rest b-tail))
                     (recur nil b-tail))
                   false)))))))

(defn- is-component? [[seq comp]]
  (cond
    (keyword? comp) (matching-component? seq (comp c/components))
    (and (map? comp) (contains? comp :ordering)) (matching-component? seq comp)
    :else false))

(spec/def ::component is-component?)

;TODO remove printlns and insert useful logging
