(ns fix.component-spec
  (:require [clojure.spec.alpha :as spec]))


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

;TODO e1 must be large list with all available "flat" tags left
;TODO the matching must go on until it can be decided if definition can be satisfied
;TODO case 1: definition satisfied -> return REST OF THE FLAT LIST which bubbles up and matching continues
;TODO case 2: definition cannot be satisfied -> false
(defn matching-type? [e1 e2]
  (if (and (not (seq? e1))
           (= :field (:type e2)))
    (do (println (str "Compare: " e1 " == " (:tag e2)))
        (= e1 (:tag e2)))  ;TODO
    (matching-seqs? e1 e2)))


(defn matching-elements? [e1 e2]

  )

;TODO PROBLEM given is always flat!!! component is nested
(defn matching-seqs? [given component]
  (println (str "Called with: " given " and " component))
  (loop [seq-a given
         seq-b (:ordering component)]
    (println (str "Inside loop - Called with: " seq-a " and " seq-b))
    (cond
      (and (empty? seq-a) (empty? seq-b)) true
      (and (seq seq-a) (empty? seq-b)) false
      :else (let [[a & a-tail] seq-a ;TODO head destructuring should happen inside matching-type function
                  [b & b-tail] seq-b]
              (if (matching-type? a b)
                (recur a-tail b-tail)
                (if (not (:required b))
                  (recur seq-a b-tail)
                  false))))))


(spec/def ::component true)

