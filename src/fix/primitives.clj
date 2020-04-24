(ns fix.primitives
  (:require [clojure.spec.alpha :as spec]
            [clojure.string :as s]))

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
(spec/def ::language #(and (string? %) (= (count %) 2)))
(spec/def ::seq-num pos-int?)
(spec/def ::country #(and (string? %) (= (count %) 2)))
(spec/def ::xml-data string?)

;TODO precondition: all values given for validation are still raw string!!! make parsing with (edn/read-string) part of validation
