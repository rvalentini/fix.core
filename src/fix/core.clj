(ns fix.core
  (:gen-class)
  (:require [parser.xml-parser :as parser]
            [generator.field-generator :as generator]))

(defn -main
  [& args]
  (println "Hello, World!")
  (let [fields (parser/parse "resources/FIX50SP2.xml")]
    (generator/generate-source-file fields)))


(-main)
;;TODO NEXT:
;;TODO (2) Define possible field types as Spec






















