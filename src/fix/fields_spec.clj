(ns fix.fields-spec
  (:require [clojure.spec.alpha :as spec]
            [fix.fields :as f]
            [fix.primitives :as p]))

(defn key-value? [arg]
  (and (coll? arg)
       (= 2 (count arg))))


(def type-specs
  {:STRING ::p/string
   :INT ::p/int
   :CHAR ::p/char
   :NUMINGROUP ::p/num-in-group
   :FLOAT ::p/float
   :AMT ::p/amt
   :UTCTIMESTAMP ::p/utc-timestamp
   :DATA ::p/data
   :LENGTH ::p/length
   :PERCENTAGE ::p/percent
   :BOOLEAN ::p/boolean
   :EXCHANGE ::p/exchange
   :PRICE ::p/price
   :QTY ::p/qty
   :CURRENCY ::p/currency
   :PRICEOFFSET ::p/price-offset
   :TZTIMEONLY ::p/tz-time-only})

;TODO extends mapping raw data -> primitive spec types
;TODO current position :1079 TZTIMEONLY

(defn raw->spec [raw-data-type]
  (if raw-data-type
    (raw-data-type type-specs)))


(defn field? [arg]
  (let [[tag value] arg
        field-attr (tag f/fields)
        type (raw->spec (:type field-attr))]
    (if (some? field-attr)
      (if-let [enums (:values field-attr)]
        (and (contains? enums value)
             (spec/valid? type value))
        (spec/valid? type value)))))

(spec/def ::field (spec/and key-value? field?))









