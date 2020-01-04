(ns fix.core
  (:gen-class)
  (:require [clojure.spec.alpha :as spec]))

(defn -main
  [& args]
  (println "Hello, World!"))


;;TODO write some test for utc-timestamp with and without millis
(def utc-timestamp #"^(-?(?:[1-9][0-9]*)?[0-9]{4})(1[0-2]|0[1-9])(3[01]|0[1-9]|[12][0-9])-(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\.[0-9]{3})?$")

(spec/def ::utc-timestamp #(re-matches utc-timestamp %))

(spec/def ::standard-header
  {:8 ::begin-string
   :9 ::body-length
   :35 ::msg-type
   :49 ::sender-comp-id
   :56 ::target-comp-id
   :34 ::msg-seq-num
   :52 ::sending-time})

;;TODO following block always unencrypted
(spec/def ::begin-string string?)    ;TODO must be first tag in message
(spec/def ::body-length pos-int?)     ;TODO must be second tag in message
(spec/def ::msg-type string?)     ;TODO must be third tag in message
(spec/def ::sender-comp-id string?)
(spec/def ::target-comp-id string?)
(spec/def ::msg-seq-num pos-int?)
(spec/def ::sending-time ::utc-timestamp)


(spec/def ::standard-trailer
  {:10 ::check-sum})

(spec/def ::check-sum string?)


(spec/def ::heartbeat-msg (spec/and ::standard-header ::standard-trailer))