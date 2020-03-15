(ns fix.parser.xml-parser
  (:require [clojure.xml :as xml]))

(defn- extract-fields [seq]
  (->> seq                                     ;count 1452
       (filter (comp #{:field} :tag))
       (filter (comp #(contains? % :number) :attrs))))

(defn- extract-components [seq]
  (->> seq                                 ;count 176
       (filter (comp #{:component} :tag))
       (filter (comp #(not (contains? % :required)) :attrs))))

(defn- extract-messages [seq]
  (filter (comp #{:message} :tag) seq))  ;count 115

(defn parse [file]
  (let [xml (xml/parse file)
        seq (xml-seq xml)
        fields (extract-fields seq)
        components (extract-components seq)
        messages (extract-messages seq)]
    [fields components messages]))


