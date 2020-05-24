(ns fix.spec.fields-spec
  (:require [clojure.spec.alpha :as spec]
            [clojure.string :as string]
            [fix.definitions.fields :as f]
            [fix.spec.primitives-spec :as p]))

(defn key-value? [arg]
  (and (contains? arg :tag)
       (contains? arg :value)))


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
   :TZTIMEONLY ::p/tz-time-only
   :LOCALMKTDATE ::p/local-mkt-date
   :LANGUAGE ::p/language
   :SEQNUM ::p/seq-num
   :COUNTRY ::p/country
   :MONTHYEAR ::p/month-year
   :MULTIPLECHARVALUE ::p/multiple-char-value
   :MULTIPLESTRINGVALUE ::p/multiple-string-value
   :XMLDATA ::p/xml-data
   :UTCTIMEONLY ::p/utc-time-only
   :UTCDATEONLY ::p/utc-date-only
   :TZTIMESTAMP ::p/tz-timestamp})

(defn raw->spec [raw-data-type]
  (if raw-data-type
    (raw-data-type type-specs)))

(defn contains-repeated? [type]
  (or (= type ::p/multiple-string-value)
      (= type ::p/multiple-char-value)))

(defn is-valid-enum? [type values enums]
  (if (contains-repeated? type)
    (let [enums-as-set (set (map #(key %) enums))
          values-as-seq (string/split values #" ")]
      (every? #(contains? enums-as-set %) values-as-seq))
    (contains? enums values)))

(defn field? [{:keys [tag value]}]
  (let [field-attr (tag f/fields)
        type (raw->spec (:type field-attr))]
    (if (some? field-attr)
      (if-let [enums (:values field-attr)]
        (and
          (spec/valid? type value)
          (is-valid-enum? type value enums))
        (spec/valid? type value)))))

(spec/def ::field (spec/and key-value? field?))