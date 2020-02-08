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
;;TODO (1) remove deprecated spec ns

;;TODO (2) how to keep order and fast :tag access in groups? -> data within hashmap and separate vec for order
;;TODO when the order is needed (e.g. Header/Trailer, repeating groups)
;;TODO (3) how to handle repeating tags in hashmap? -> sub-maps repeating group as vec of maps

(def message {:order [:345 :54 :9 :11]
              :data {:345 "some_stuff"
                     :54  "some_other_stuff"
                     :9   "Fred"
                     :10 3
                     :11 [{:8 "A" :4 "B"} ;;TODO could be repeating group
                          {:8 "C" :4 "D"}
                          {:8 "E" :4 "F"}]}})




















