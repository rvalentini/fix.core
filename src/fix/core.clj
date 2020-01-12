(ns fix.core
  (:gen-class)
  (:require [clojure.spec.alpha :as spec]))

(defn -main
  [& args]
  (println "Hello, World!"))

;;TODO split namespace into smaller parts -> each type gets own namespace + one for primitives
;;TODO make a separate project for simple client, which handles binary encoding with ASCII 01 separator and TCP connection

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



(spec/def ::cl-ord-id string?)
(spec/def ::symbol string?)

(spec/def ::instrument
  {:55 ::symbol
   ;;TODO some more -> none of the TAGs are required which is strange
   })

(spec/def ::qty #(or (float? %) (int? %)))
(spec/def ::percent float?)

(spec/def ::order-qty ::qty)
(spec/def ::cash-order-qty ::qty)
(spec/def ::order-percent ::percent)
(spec/def ::rounding-direction char?)
(spec/def ::rounding-modulus float?)
(spec/def ::side char?)
(spec/def ::transact-time ::utc-timestamp)

(spec/def ::order-qty-data
  {:38 ::order-qty
   :152 ::cash-order-qty
   :516 ::order-percent
   :468 ::rounding-direction
   :469 ::rounding-modulus})

(spec/def ::new-order-single
  (spec/and ::standard-header
            {:11 ::cl-ord-id}
            ::instrument
            {:54 ::side :60 ::transact-time}
            ::order-qty-data
            ::standard-trailer))