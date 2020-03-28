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



;TODO this can be done by checking :type only now with new structure
(defn type-of [elem]
  (if (seq? elem)
      (if (is-group? (first elem))                          ;TODO what if empty?
        :group
        :component)
      :field))


;TODO pass sub-ordering structure
(defn matching-type? [e1 e2]
  (let [type-e1 (type-of e1)
        type-e2 (type-of e2)]
    (if (not= type-e1 type-e2)
      false
      ;TODO call matching-seqs again with substructure
      )

    )
  )

(defn matching-elements? [e1 e2]

  )


;TODO adapt to the new structure of component ordering (do NOT use definition anymore)
(defn matching-seqs? [given {:keys [definition] :as component}]
  (loop [seq-a given
         seq-b (:ordering component)]
    (println (str "Called with: " seq-a " and " seq-b))
    (cond
      (and (empty? seq-a) (empty? seq-b)) true
      (and (seq seq-a) (empty? seq-b)) false
      :else (let [[a & a-tail] seq-a
                  [b & b-tail] seq-b]
              (if (= a b)
                (recur a-tail b-tail)
                (if (not (:required (b definition)))
                  (recur seq-a b-tail)
                  false))))))


(spec/def ::component true)

