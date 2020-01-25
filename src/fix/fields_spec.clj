(ns fix.fields-spec
  (:require [clojure.spec.alpha :as spec]
            [fix.fields :as f]
            [fix.primitives :as p]))

(defn key-value? [arg]
  (and (coll? arg)
       (= 2 (count arg))))


(def type-specs
  {:STRING ::p/string}) ;TODO extends mapping raw data -> primitive spec types


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









