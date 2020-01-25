(ns parser.xml-parser
  (:require [clojure.xml :as xml]
            [clojure.spec.alpha :as spec]))


(spec/def ::some-type string?)
(spec/def ::STRING string?)

;;TODO this is only an example -> generate the real thing with function below
(def fields {:56 {:name :this-is-the-name :type ::STRING :values {"SD" "SENIOR_SECURED", "SR" "SENIOR", "SB" "SUBORDINATED"}}
             :99 {:name :this-is-the-name :type :INT}
             :101 {:name :this-is-the-name :type ::some-type}})

(defn key-value? [arg]
  (and (coll? arg)
       (= 2 (count arg))))

(defn field? [arg]
  (let [[tag value] arg
        field-attr (tag fields)
        type (:type field-attr)]
    (if (some? field-attr)
      (if-let [enums (:values field-attr)]
        (and (contains? enums value)
             (spec/valid? type value))
        (spec/valid? type value)))))

(spec/def ::field (spec/and key-value? field?))



;;TODO move to generator namespace
(defn generate-source-file [fields]
  (let [header '(ns fix.fields)
        var `(def ~'fields ~fields)]
    (spit "src/fix/fields.clj" header)
    (spit "src/fix/fields.clj" "\n\n" :append true)
    (spit "src/fix/fields.clj" var :append true)))

;;TODO NEXT:
;;TODO (1) Take care of enum restrictions for field values as Spec
;;TODO (2) Define possible field types as Spec


(defn extract-enums [value-tags]
  (let [result (map
                 (fn [value-tag]
                   {(get-in value-tag [:attrs :enum])
                    (get-in value-tag [:attrs :description])})
                 value-tags)]
    (reduce merge {} result)))


(defn build-field-map [fields]
  (let [field-entries (map
                        (fn [field]
                          (let [name (get-in field [:attrs :name]) ;;TODO get possible INT values (=enum) from content for some fields
                                type (keyword (get-in field [:attrs :type])) ;;TODO e.g. [{:tag :value, :attrs {:description NERC_EASTERN_OFF_PEAK, :enum 0}, :content nil}
                                number (keyword (get-in field [:attrs :number]))
                                enums (extract-enums (get-in field [:content]))
                                result {number {:name name :type type}}]
                            (if (not-empty enums)
                              (assoc-in result [number :values] enums)
                              result)))
                        fields)]
    (reduce merge {} field-entries)))



(defn parse [file]
  (let [xml (xml/parse file)
        seq (xml-seq xml)
        fields (filter (and
                         (comp #{:field} :tag)
                         (comp #(contains? % :number) :attrs)) seq)
        messages (filter (comp #{:message} :tag) seq)
        components (filter (comp #{:component} :tag) seq)]
    (doseq [field fields]
      (println field))
    (build-field-map fields)))

(println (spec/conform ::field [:56 "SD"]))
(println (spec/conform ::field [:56 "GDFJ"]))
(println (spec/conform ::field [:566 "SD"]))
(println (spec/conform ::field 66))
(println (spec/conform ::field [1 2 3]))
(println (spec/conform ::field [:101 "jckjdsnds"]))

(let [field-map (parse "resources/FIX50SP2.xml")]
  (println field-map)
  (generate-source-file field-map))
