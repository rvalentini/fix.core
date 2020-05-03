(ns fix.primitives-spec
  (:require [clojure.spec.alpha :as spec]
            [clojure.string :as s]
            [fix.utils :refer [parse-number]]))

(def utc-timestamp #"^(-?(?:[1-9][0-9]*)?[0-9]{4})(1[0-2]|0[1-9])(3[01]|0[1-9]|[12][0-9])-(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\.[0-9]{3})?$")
(spec/def ::utc-timestamp #(re-matches utc-timestamp %))

(def utc-time-only #"^(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\.[0-9]{3})?$")
(spec/def ::utc-time-only #(re-matches utc-time-only %))

(def utc-date-only #"^(-?(?:[1-9][0-9]*)?[0-9]{4})(1[0-2]|0[1-9])(3[01]|0[1-9]|[12][0-9])")
(spec/def ::utc-date-only #(re-matches utc-date-only %))

(def tz-time #"^(2[0-3]|1[0-9]|0[0-9]):([0-5][0-9])(:[0-5][0-9])?(Z|[\+|-](2[0-3]|[01][0-9])(:[0-5][0-9])?)?$")
(spec/def ::tz-time-only #(re-matches tz-time %))

(def tz-timestamp #"^(-?(?:[1-9][0-9]*)?[0-9]{4})(1[0-2]|0[1-9])(3[01]|0[1-9]|[12][0-9])-(2[0-3]|1[0-9]|0[0-9]):([0-5][0-9])(:[0-5][0-9])?(Z|[\+|-](2[0-3]|[01][0-9])(:[0-5][0-9])?)?$")
(spec/def ::tz-timestamp #(re-matches tz-timestamp %))

(def local-mkt-date #"^(\d{4})(1[0-2]|0[1-9])([1-2][0-9]|0[1-9]|3[0-1])$")
(spec/def ::local-mkt-date #(re-matches local-mkt-date %))

(def month-year #"^(\d{4})(1[0-2]|0[1-9])(([1-2][0-9]|0[1-9]|3[0-1])|(w[1-5]))?$")
(spec/def ::month-year #(re-matches month-year %))

(def multiple-char-value #"^(?:[\w|\p{Punct}]\s)*[\w|\p{Punct}]{1}$")
(spec/def ::multiple-char-value #(re-matches multiple-char-value %))

(def multiple-string-value #"^(?:[\w|\p{Punct}]*\s)*[\w|\p{Punct}]+$")
(spec/def ::multiple-string-value #(re-matches multiple-string-value %))

(spec/def ::size-1 #(= (count %) 1))
(spec/def ::size-2 #(= (count %) 2))
(spec/def ::size-3 #(= (count %) 3))
(spec/def ::size-4 #(= (count %) 4))

(spec/def ::string string?)
(spec/def ::int #(int? (parse-number %)))
(spec/def ::pos-int #(pos-int? (parse-number %)))
(spec/def ::char (spec/and ::string ::size-1))
(spec/def ::float #(float? (parse-number %)))

(spec/def ::amt ::float) ;amount
(spec/def ::num-in-group ::pos-int)
(spec/def ::data ::string)
(spec/def ::length ::pos-int)
(spec/def ::percentage ::float)
(spec/def ::boolean (spec/and ::char #(or (= "Y" %) (= "N" %))))
(spec/def ::exchange (spec/and ::string #(s/starts-with? % "x") ::size-4))
(spec/def ::price ::float)
(spec/def ::qty (spec/or :int ::int :float ::float))
(spec/def ::currency (spec/and ::string ::size-3))
(spec/def ::price-offset ::float)
(spec/def ::language (spec/and ::string ::size-2))
(spec/def ::seq-num ::pos-int)
(spec/def ::country (spec/and ::string ::size-2))
(spec/def ::xml-data ::string)

