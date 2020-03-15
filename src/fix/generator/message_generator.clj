(ns fix.generator.message-generator
  (:require [fix.parser.xml-parser :as parser]
            [clojure.pprint :refer [pprint]]))

(defn- assert-message [message]
  {:pre [(= (:tag message) :message)]}
  message)

(defn- spit-to-file [message-map]
  (let [header '(ns fix.definitions.messages)
        var `(def ~'messages ~message-map)]
    (spit "src/fix/definitions/messages.clj" header)
    (spit "src/fix/definitions/messages.clj" "\n\n" :append true)
    (spit "src/fix/definitions/messages.clj" var :append true)))

(defn- extract-definitions [content]
  ;TODO implement
  content
  )

(defn- extract-ordering [content]
  ;TODO implement
  content
  )

(defn- generate-source-file [messages]
  (println (str "Number of messages received: " (count messages)))
  (let [gen-messages (map
                         (fn [message]
                           (assert-message message)
                           (println " ------------------ NEW MESSAGE ------------------")
                           (pprint message)
                           (let [msg-cat  (get-in message [:attrs :msgcat])
                                 msg-type (get-in message [:attrs :msgtype])
                                 msg-name (get-in message [:attrs :name])
                                 definitions (extract-definitions 111)
                                 ordering (extract-ordering 111)]
                             {(keyword msg-name) {:category msg-cat
                                                  :type msg-type
                                                  :definitions definitions
                                                  :ordering ordering}}
                             )
                           )
                         messages)]
    (count gen-messages)
    (spit-to-file (apply merge gen-messages))))

(defn -main [& _]
  (println "Generating FIX5.0 SP2 MESSAGE sources ... !")
  (let [[_ _ messages] (parser/parse "resources/FIX50SP2_FIXT11_combined.xml")]
    (generate-source-file messages)))

(-main)