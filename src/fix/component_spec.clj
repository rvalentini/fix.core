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

;TODO write some tests for this bad boy

(defn matching-seqs? [given {:keys [definition] :as component}]
  (loop [seq-a given
         seq-b (:ordering component)]
    (println (str "Called with: " seq-a " and " seq-b))
    (cond
      (and (empty? seq-a) (empty? seq-b)) true
      (or (empty? seq-a) (empty? seq-b)) false
      :else (let [[a & a-tail] seq-a
                  [b & b-tail] seq-b]
              (if (= a b)
                (recur a-tail b-tail)
                (if (not (:required (b definition)))
                  (recur seq-a b-tail)
                  false))))))


(spec/def ::component true)

(matching-seqs? [:1 :3] {:ordering [:x :1 :2 :3]
                          :definition {:x {:required true}
                                       :1 {:required true}
                                       :2 {:required false}
                                       :3 {:required true}}})
