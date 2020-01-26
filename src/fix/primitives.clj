(ns fix.primitives
  (:require [clojure.spec.alpha :as spec]
            [clojure.string :as s]))

(def utc-timestamp #"^(-?(?:[1-9][0-9]*)?[0-9]{4})(1[0-2]|0[1-9])(3[01]|0[1-9]|[12][0-9])-(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\.[0-9]{3})?$")
(spec/def ::utc-timestamp #(re-matches utc-timestamp %))

(def tz-time #"^(2[0-3]|1[0-9]|0[0-9]):([0-5][0-9])(:[0-5][0-9])?(Z|[\+|-](2[0-3]|[01][0-9])(:[0-5][0-9])?)?$")
(spec/def ::tz-time-only #(re-matches tz-time %))

(spec/def ::string string?)
(spec/def ::int int?)
(spec/def ::char char?)
(spec/def ::float float?)

(spec/def ::amt float?) ;amount
(spec/def ::num-in-group pos-int?)
(spec/def ::data string?)
(spec/def ::length pos-int?)
(spec/def ::percentage float?)
(spec/def ::boolean #(and (char? %) (or (= "Y" %)
                                        (= "N" %))))
(spec/def ::exchange #(and (string? %)
                           (s/starts-with? % "x")
                           (= (count %) 4)))
(spec/def ::price float?)
(spec/def ::qty #(or (float? %) (int? %)))
(spec/def ::currency #(and (string? %) (= (count %) 3)))
(spec/def ::price-offset float?)



(spec/def ::cl-ord-id string?)
(spec/def ::symbol string?)

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
