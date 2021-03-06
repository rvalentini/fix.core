(ns fix.generator.message-generator
  (:require [fix.parser.xml-parser :as parser]
            [fix.generator.commons :as c]
            [taoensso.timbre :refer [error info warn debug]]))

(defn- assert-message [message]
  {:pre [(= (:tag message) :message)]}
  message)

(defn- build-component [comp-name attrs]
  {:type     :component
   :required (c/char->boolean (:required attrs))
   :name     (keyword comp-name)})

(defn- extract-ordering [content]
  (->> content
       (map (fn [elem]
              (let [attrs (:attrs elem)
                    elem-type (:tag elem)
                    elem-name (:name attrs)
                    elem-content (:content elem)]
                (case elem-type
                  :field (c/build-field attrs elem-content)
                  :component (build-component elem-name attrs)))))))

(defn- generate-source-file [messages]
  (info "Number of messages found:" (count messages))
  (let [gen-messages (map
                         (fn [message]
                           (assert-message message)
                           (let [msg-cat  (get-in message [:attrs :msgcat])
                                 msg-type (get-in message [:attrs :msgtype])
                                 msg-name (get-in message [:attrs :name])
                                 ordering (vec (extract-ordering (:content message)))]
                             {(keyword msg-name) {:category msg-cat
                                                  :type msg-type
                                                  :ordering ordering}}))
                         messages)]
    (spit "resources/messages.edn" (apply merge gen-messages))))

(defn -main [& _]
  (info "Generating FIX5.0 SP2 MESSAGE sources ... !")
  (let [[_ _ messages] (parser/parse "resources/FIX50SP2_FIXT11_combined.xml")]
    (generate-source-file messages)))
