(ns fix.parser.xml-parser
  (:require [clojure.xml :as xml]))

(defn- extract-fields [seq]
  (->> seq                                     ;count 1452
       (filter (comp #{:field} :tag))
       (filter (comp #(contains? % :number) :attrs))))

(defn- extract-components [seq]
  (->> seq                                 ;count 174
       (filter (comp #{:component} :tag))
       (filter (comp #(not (contains? % :required)) :attrs))))

(defn parse [file]
  (let [xml (xml/parse file)
        seq (xml-seq xml)
        fields (extract-fields seq)
        messages (filter (comp #{:message} :tag) seq)  ;TODO use
        components (extract-components seq)]
    (doseq [component components]
      (println component)) ;TODO remove
    #_(doseq [field fields]
      (println field)) ;TODO remove
    [fields componentps]))
