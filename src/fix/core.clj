(ns fix.core
  (:gen-class)
  (:require [fix.parser.xml-parser :as parser]
            [fix.generator.field-generator :as field-generator]
            [fix.generator.component-generator :as component-generator]))

(defn -main
  [& args]
  (println "Generating FIX5.0 SP2 sources ... !")
  (let [[fields components] (parser/parse "resources/FIX50SP2.xml")]
    (field-generator/generate-source-file fields)
    (component-generator/generate-source-file components)))


(-main)
;;TODO NEXT:
;;TODO (1) remove deprecated spec ns

;;TODO (2) how to keep order and fast :tag access in groups? -> data within hashmap and separate vec for order
;;TODO when the order is needed (e.g. Header/Trailer, repeating groups)
;;TODO (3) how to handle repeating tags in hashmap? -> sub-maps repeating group as vec of maps

(def message {:order [:345 :54 :9 :11]  ;keep ordering of tags in array
              :data {:345 "some_stuff"
                     :54  "some_other_stuff"
                     :9   "Fred"
                     :10 3
                     :11 [{:8 "A" :4 "B"} ;;TODO could be repeating group --- does TAG ":11" exist for this context?
                          {:8 "C" :4 "D"}
                          {:8 "E" :4 "F"}]}})

; component :content :attrs -> always "No..." of type NUMINGROUP == first field in group
; component :content :content is array of all repeated fields -> multiple of NUMINGROUP

; message > component > group -> group never directly part of message
; component > group > component -> groups can contain components

; components and fields can have the same name "DerivativeSecurityXML"

; component definitions don't have "required" within top-level attrs -> only the usages have

;TODO can groups contain other groups ??? -> otherwise "recur" in components when 2nd level component appears

















