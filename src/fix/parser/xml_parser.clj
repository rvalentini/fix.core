(ns fix.parser.xml-parser
  (:require [clojure.xml :as xml]))

(defn- extract-fields [seq]
  {:post [(= (count %) 1452)]}
  (->> seq
       (filter (comp #{:field} :tag))
       (filter (comp #(contains? % :number) :attrs))))

(defn- extract-components [seq]
  {:post [(= (count %) 176)]}
  (->> seq
       (filter (comp #{:component} :tag))
       (filter (comp #(not (contains? % :required)) :attrs))))

(defn- extract-messages [seq]
  {:post [(= (count %) 115)]}
  (filter (comp #{:message} :tag) seq))

(defn parse [file]
  (let [xml (xml/parse file)
        seq (xml-seq xml)
        fields (extract-fields seq)
        components (extract-components seq)
        messages (extract-messages seq)]
    [fields components messages]))


