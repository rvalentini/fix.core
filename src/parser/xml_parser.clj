(ns parser.xml-parser
  (:require [clojure.xml :as xml]))

(defn parse [file]
  (let [xml (xml/parse file)
        seq (xml-seq xml)
        fields (filter (and
                         (comp #{:field} :tag)
                         (comp #(contains? % :number) :attrs)) seq)
        messages (filter (comp #{:message} :tag) seq)  ;TODO use
        components (filter (comp #{:component} :tag) seq)] ;TODO use
    (doseq [field fields]
      (println field)) ;TODO remove
    fields))

