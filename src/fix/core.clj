(ns fix.core
  (:gen-class)
  (:require [fix.parser.xml-parser :as parser]
            [fix.generator.field-generator :as generator]))

(defn -main
  [& args]
  (println "Hello, World!")
  (let [fields (parser/parse "resources/FIX50SP2.xml")]
    (generator/generate-source-file fields)))


(-main)
;;TODO NEXT:
;;TODO (1) Define all possible field types as Spec
;;TODO (2) remove deprecated spec ns























