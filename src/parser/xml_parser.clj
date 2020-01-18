(ns parser.xml-parser
  (:require [clojure.xml :as xml]
            [clojure.spec.alpha :as spec]))


(spec/def ::some-type string?)

;;TODO this is only an example -> generate the real thing with function below
(def fields {:56 {:name :this-is-the-name :type :INT}
             :99 {:name :this-is-the-name :type :INT}
             :101 {:name :this-is-the-name :type ::some-type}})


(defn field? [arg]
  (let [[tag value] arg
        field-attr (tag fields)]
    (if (some? field-attr)
      (spec/valid? (:type field-attr) value))))

(spec/def ::field field?)


(defn escape [field-map]
  ;;TODO extract in loop tp remove outer brakets  {:56 {:name :this-is-the-name :type :INT}
  `(def "fields" {:blah ~@field-map}))



;;TODO generate a new namespace containing the resulting
(defn generate-field-map [fields]
  (let [triple (map (fn [field]
                      (let [name (get-in field [:attrs :name])
                            type (keyword (get-in field [:attrs :type]))
                            number (keyword (get-in field [:attrs :number]))]
                        {number {:name name :type type}}))
                     fields)]
    (println triple)
    (doseq [field triple]
      (println field))
    triple))



(defn parse [file]
  (let [xml (xml/parse file)
        seq (xml-seq xml)
        fields (filter (and
                         (comp #{:field} :tag)
                         (comp #(contains? % :number) :attrs)) seq)
        messages (filter (comp #{:message} :tag) seq)
        components (filter (comp #{:component} :tag) seq)]

    (generate-field-map fields)))

(println
  (spec/conform ::field [:101 "jndskncds"]))

(let [field-map (parse "resources/FIX50SP2.xml")]
  #_(println (escape field-map)))
