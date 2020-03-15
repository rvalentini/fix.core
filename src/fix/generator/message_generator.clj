(ns fix.generator.message-generator
  (:require [fix.parser.xml-parser :as parser]
            [clojure.pprint :refer [pprint]]
            [fix.generator.commons :as c]))

(defn- assert-message [message]
  {:pre [(= (:tag message) :message)]}
  message)

(defn- build-component [attrs content]
  (c/assert-empty-content content)
  (let [name (keyword (:name attrs))
        required (c/char->boolean (:required attrs))]
    {name {:required required}}))

(defn- spit-to-file [message-map]
  (let [header '(ns fix.definitions.messages)
        var `(def ~'messages ~message-map)]
    (spit "src/fix/definitions/messages.clj" header)
    (spit "src/fix/definitions/messages.clj" "\n\n" :append true)
    (spit "src/fix/definitions/messages.clj" var :append true)))

(defn- extract-definitions [content]
  (->> content
       (map (fn [elem]
              (let [attrs (:attrs elem)
                    elem-type (:tag elem)
                    elem-content (:content elem)]
                (case elem-type
                  :field (c/build-field attrs elem-content)
                  :component (build-component attrs elem-content)
                  (throw (IllegalArgumentException. (str "Wrong input: component contains unknown type: " elem-type)))))))
       (reduce merge {})))

(defn- extract-ordering [content]
  (->> content
       (map (fn [elem]
              (let [attrs (:attrs elem)
                    elem-type (:tag elem)
                    elem-name (:name attrs)
                    elem-tag (c/get-field-tag-by-name (:name attrs))]
                (case elem-type
                  :field elem-tag
                  :component (keyword elem-name)))))))

(defn- generate-source-file [messages]
  (println (str "Number of messages found: " (count messages)))
  (let [gen-messages (map
                         (fn [message]
                           (assert-message message)
                           #_(println " ------------------ NEW MESSAGE ------------------")
                           #_(pprint message)
                           (let [msg-cat  (get-in message [:attrs :msgcat])
                                 msg-type (get-in message [:attrs :msgtype])
                                 msg-name (get-in message [:attrs :name])
                                 definitions (extract-definitions (:content message))
                                 ordering (extract-ordering (:content message))]
                             {(keyword msg-name) {:category msg-cat
                                                  :type msg-type
                                                  :definitions definitions
                                                  :ordering ordering}}))
                         messages)]
    (spit-to-file (apply merge gen-messages))))

(defn -main [& _]
  (println "Generating FIX5.0 SP2 MESSAGE sources ... !")
  (let [[_ _ messages] (parser/parse "resources/FIX50SP2_FIXT11_combined.xml")]
    (generate-source-file messages)))
