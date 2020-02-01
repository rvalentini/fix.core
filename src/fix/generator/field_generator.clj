(ns fix.generator.field-generator)


(defn- spit-to-file [field-map]
  (let [header '(ns fix.fields)
        var `(def ~'fields ~field-map)]
    (spit "src/fix/fields.clj" header)
    (spit "src/fix/fields.clj" "\n\n" :append true)
    (spit "src/fix/fields.clj" var :append true)))


(defn- extract-enums [value-tags]
  (let [result (map
                 (fn [value-tag]
                   {(get-in value-tag [:attrs :enum])
                    (get-in value-tag [:attrs :description])})
                 value-tags)]
    (reduce merge {} result)))


(defn- build-field-map [fields]
  (let [field-entries (map
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
    (reduce merge {} field-entries)))


(defn generate-source-file [fields]
  (spit-to-file
    (build-field-map fields)))




