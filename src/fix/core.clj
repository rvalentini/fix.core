(ns fix.core
  (:gen-class))


;;TODO NEXT:
;;TODO (1) remove deprecated spec ns

;;TODO (2) how to keep order and fast :tag access in groups? -> data within hashmap and separate vec for order
;;TODO when the order is needed (e.g. Header/Trailer, repeating groups)
;;TODO (3) how to handle repeating tags in hashmap? -> sub-maps repeating group as vec of maps

(def message {:ordering [:345 :54 :9 :11]  ;keep ordering of tags in array
              :data {:345 "some_stuff"
                     :54  "some_other_stuff"
                     :9   "Fred"
                     :10 3
                     :10-groups [{:8 "A" :4 "B"} ;;TODO could be repeating group --- does TAG ":11" exist for this context? maybe introcude :11-groups []
                                 {:8 "C" :4 "D"}
                                 {:8 "E" :4 "F"}]}})

; component :content :attrs -> always "No..." of type NUMINGROUP == first field in group
; component :content :content is array of all repeated fields -> multiple of NUMINGROUP

; message > component > group -> group never directly part of message
; component > group > component -> groups can contain components

; components and fields can have the same name "DerivativeSecurityXML"

; component definitions don't have "required" within top-level attrs -> only the usages have

;TODO can groups contain other groups ??? -> otherwise "recur" in components when 2nd level component appears





;insights:

; FIX messages will be flat in the end
; FIX message is a sequence of key value pairs

;TODO big picture implementation
; message type is always defined in the mandatory HEADER
;parsing:
;(1) parse header/trailer and check if valid
;(2) pull the specific message definition and validate content


(def parsed-fix [{:tag :345 :value "djcnjsdncjk"}
                 {:tag :346 :value "djcnjsdncjk"}
                 {:tag :347 :value "djcnjsdncjk"}
                 {:tag :348 :value "djcnjsdncjk"}
                 {:tag :349 :value "djcnjsdncjk"}
                 {:tag :350 :value "djcnjsdncjk"}
                 {:tag :351 :value "djcnjsdncjk"}
                 {:tag :352 :value "djcnjsdncjk"}])



















