(ns fix.generator.field-generator
  (:require [fix.parser.xml-parser :as parser]
            [taoensso.timbre :refer [info]]))

(defn- spit-to-file [field-map]
  (let [header '(ns fix.definitions.fields)
        var `(def ~'fields ~field-map)]
    (spit "src/fix/definitions/fields.clj" header)
    (spit "src/fix/definitions/fields.clj" "\n\n" :append true)
    (spit "src/fix/definitions/fields.clj" var :append true)))

(defn- extract-enums [value-tags]
  (let [result (map
                 (fn [value-tag]
                   {(get-in value-tag [:attrs :enum])
                    (get-in value-tag [:attrs :description])})
                 value-tags)]
    (reduce merge {} result)))

(defn- extract-definition [fields]
  (info "Number of fields found:" (count fields))
  (let [gen-fields (map
                        (fn [field]
                          (let [name (get-in field [:attrs :name])
                                type (keyword (get-in field [:attrs :type]))
                                number (keyword (get-in field [:attrs :number]))
                                enums (extract-enums (get-in field [:content]))
                                result {number {:name name :type type}}]
                            (if (not-empty enums)
                              (assoc-in result [number :values] enums)
                              result)))
                        fields)]
    (reduce merge {} gen-fields)))

(defn- generate-source-file [fields]
  (spit-to-file
    (extract-definition fields)))

(defn -main [& _]
  (info "Generating FIX5.0 SP2 FIELD sources ... !")
  (let [[fields _] (parser/parse "resources/FIX50SP2_FIXT11_combined.xml")]
    (generate-source-file fields)))
