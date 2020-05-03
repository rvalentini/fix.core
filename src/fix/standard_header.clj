(ns fix.standard-header
  (:require [clojure.spec.alpha :as spec]
            [fix.primitives-spec :as p]))


(spec/def ::standard-header
  {:8 ::p/begin-string
   :9 ::p/body-length
   :35 ::p/msg-type
   :49 ::p/sender-comp-id
   :56 ::p/target-comp-id
   :34 ::p/msg-seq-num
   :52 ::p/sending-time})
