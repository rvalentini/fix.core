(ns fix.message-spec
  (:require [clojure.spec.alpha :as spec]
            [fix.definitions.components :as c]
            [fix.component-spec :as s]
            [fix.definitions.messages :as m]))

(def supported-versions #{"FIXT.1.1"})

(defn destructure-msg [seq]
  (let [[head rest] (split-with #((:tag %) (:definition (:Header c/components))) seq)
        [body tail] (split-with #(nil? ((:tag %) (:definition (:Trailer c/components)))) rest)]
      [head body tail]))

(defn- extract-header [seq]
  (split-with #((:tag %) (:definition (:Header c/components))) seq))

(defn- get-message-name-by-type [type]
  (if-let [msg (some #(if (= (:type (second %)) type) %) (seq m/messages))]
    (first msg)
    (throw (IllegalArgumentException. (str "Unknown message type: " type)))))

(defn- valid-body-length? [[_ _ & rest] length-in-bytes]
  (println "REST: " rest)
  (println "Actual size: " (reduce #(+ %1 (:size %2)) 0 (drop-last rest)))
  (= (reduce #(+ %1 (:size %2)) 0 (drop-last rest)) length-in-bytes))

(defn- evaluate-header [seq] ;TODO destructuring into [header body trailer] should happen outside
  "Extracts and validates the FIX <StandardHeader> component block.
   The following TAGs are extracted and used for validation:
   :8 - BeginString
   :9 - BodyLength
   :35 - MessageType
   "
  (let [[header body] (extract-header seq)]
    (if (empty? header)
      (throw (IllegalArgumentException. "Cannot extract any header information from message!"))
      (if (spec/valid? ::s/component [header :Header])
        (let [relevant-tags (->> header
                                       (filter #((:tag %) #{:8 :9 :35}))
                                       (reduce #(assoc %1 (:tag %2) (:value %2)) {}))]
          (println "relevant tags: " relevant-tags)
          (and
            (contains? supported-versions (:8 relevant-tags))
            (valid-body-length? seq (:9 relevant-tags))
            (if-let [msg-name (get-message-name-by-type (:35 relevant-tags))]
              (do
                (println "Validating messagetype: " msg-name " with body: " body)
                msg-name))))
        (println "Header if not valid!")))))

;TODO TEST checksum calculation with 8=FIX.4.19=6135=A34=149=EXEC52=20121105-23:24:0656=BANZAI98=0108=3010=003
(int \a)


(defn- valid-trailer? [seq]
  ;TODO extract trailer and validate (only grab last 3 tags?)
  )

(defn- is-message? [seq]

  ;TODO destructuring for [header body trailer]
  ;TODO call each validation method chained with and (header eval must return msgName when valid)
  (let [[head body tail] (destructure-msg seq)]

    ;TODO use head only for header eval

    (let [msg-name (evaluate-header seq)]
      msg-name
      )

    )


  )

(spec/def ::message  is-message?)
