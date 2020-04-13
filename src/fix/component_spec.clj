(ns fix.component-spec
  (:require [clojure.spec.alpha :as spec]
            [clojure.tools.logging :refer [warn]]))


;<component name='DiscretionInstructions'>
;<field name='DiscretionInst' required='N'/>
;<field name='DiscretionOffsetValue' required='N'/>
;<field name='DiscretionMoveType' required='N'/>
;<field name='DiscretionOffsetType' required='N'/>
;<field name='DiscretionLimitType' required='N'/>
;<field name='DiscretionRoundDirection' required='N'/>
;<field name='DiscretionScope' required='N'/>
;</component>

;{:ordering [:388 :389 :841 :842 :843 :844 :846], :definition {:388 {:required false}, :389 {:required false}, :841 {:required false}, :842 {:required false}, :843 {:required false}, :844 {:required false}, :846 {:required false}}}


(def discretion-instructions [{:tag :388 :value "1"}
                              {:tag :389 :value 23.43434}
                              {:tag :841 :value 0}
                              {:tag :842 :value 2}
                              {:tag :843 :value 1}
                              {:tag :844 :value 0}
                              {:tag :846 :value 1}])





(declare matching-seqs?)

;TODO test this !!!
(defn get-num-in-group-count [[given-head & _] [num-in-group & _]]
  (if (and (= (:tag given-head) (:tag num-in-group))
           (pos-int? (:value given-head)))
    (:value given-head)
    false))


(defn required-component-without-required-fields? [comp]
  (when (and (:required comp) (every? #(not (:required %)) (:ordering comp)))
    (warn (str "Required component without any required fields: " (:name comp)))))


(defn matching-field? [e1 e2]
  (println "--- MATCHING FIELD ---")
  (do (println (str "Compare: " e1 " == " (:tag e2)))
      (= e1 (:tag e2))))

;TODO inline into matching-seqs
(defn matching-nested? [flat-seq head-comp]
  (println "--- NESTED MATCH ---")
  (case (:type head-comp)
    :group (do
             (println (str "[GROUP] Calling again with: " flat-seq " and " head-comp))
              (loop [num-in-group (get-num-in-group-count flat-seq (:ordering head-comp))
                     given-seq flat-seq
                     group-content (drop 1 (:ordering head-comp))]
                (let [result (matching-seqs? given-seq group-content false)]
                  (cond
                    (and (seq? result) (> num-in-group 1)) (recur (dec num-in-group) result group-content)
                    (and (seq? result) (= num-in-group 1)) result
                    (and (true? result) (= num-in-group 1)) true
                    :else false))))     ;TODO test the hell out of this group count impl. !!!
    :component (do
                 (required-component-without-required-fields? head-comp)
                 (println (str "[COMPONENT] Calling again with: " flat-seq " and " head-comp))
                 (matching-seqs? flat-seq head-comp false))))


;TODO PROBLEM given is always flat!!! component is nested
(defn matching-seqs?
  ([given component] (matching-seqs? given component true))
  ([given component is-root-call]
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
                   (recur a-tail b-tail)
                   (if (not (:required b))
                     (do
                       (println "Not required: " b)
                       (recur seq-a b-tail))
                     false))
                 (if-let [rest (matching-nested? seq-a b)]
                   (if (seq? rest)
                     (do
                       (println (str "Unmatched rest bubble up: " rest))
                       (recur rest b-tail))
                     (recur nil b-tail))
                   false)))))))


(spec/def ::component true)

