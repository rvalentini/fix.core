(ns fix.primitives
  (:require [clojure.spec.alpha :as spec]))

(def utc-timestamp #"^(-?(?:[1-9][0-9]*)?[0-9]{4})(1[0-2]|0[1-9])(3[01]|0[1-9]|[12][0-9])-(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\.[0-9]{3})?$")
(spec/def ::utc-timestamp #(re-matches utc-timestamp %))

(spec/def ::string string?)

(spec/def ::cl-ord-id string?)
(spec/def ::symbol string?)

(spec/def ::qty #(or (float? %) (int? %)))
(spec/def ::percent float?)

(spec/def ::order-qty ::qty)
(spec/def ::cash-order-qty ::qty)
(spec/def ::order-percent ::percent)
(spec/def ::rounding-direction char?)
(spec/def ::rounding-modulus float?)
(spec/def ::side char?)
(spec/def ::transact-time ::utc-timestamp)

(spec/def ::begin-string string?)    ;TODO must be first tag in message
(spec/def ::body-length pos-int?)     ;TODO must be second tag in message
(spec/def ::msg-type string?)     ;TODO must be third tag in message
(spec/def ::sender-comp-id string?)
(spec/def ::target-comp-id string?)
(spec/def ::msg-seq-num pos-int?)
(spec/def ::sending-time ::utc-timestamp)

(spec/def ::check-sum string?)

(spec/def ::on-behalf-of-comp-id string?)
