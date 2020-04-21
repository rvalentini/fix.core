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

(defn- get-message-name-by-type [type]
  (if-let [msg (some #(if (= (:type (second %)) type) %) (seq m/messages))]
    (first msg)
    (throw (IllegalArgumentException. (str "Unknown message type: " type)))))

(defn- valid-body-length? [[_ _ & rest] length-in-bytes]
  (println "REST: " rest)
  (println "Actual size: " (reduce #(+ %1 (:size %2)) 0 (drop-last rest)))
  (= (reduce #(+ %1 (:size %2)) 0 (drop-last rest)) length-in-bytes))

(defn- evaluate-header [head] ;TODO destructuring into [header body trailer] should happen outside
  "Extracts and validates the FIX <StandardHeader> component block.
   The following TAGs are extracted and used for validation:
   :8 - BeginString
   :9 - BodyLength
   :35 - MessageType
   "
  (if (empty? head)
    (throw (IllegalArgumentException. "Cannot extract any information: header section is empty!"))
    (if (spec/valid? ::s/component [head :Header])
      (let [relevant-tags (->> head
                               (filter #((:tag %) #{:8 :9 :35}))
                               (reduce #(assoc %1 (:tag %2) (:value %2)) {}))]
        (println "relevant tags: " relevant-tags)
        (and
          (contains? supported-versions (:8 relevant-tags))
          (valid-body-length? seq (:9 relevant-tags))
          (if-let [msg-name (get-message-name-by-type (:35 relevant-tags))]
            (do
              (println "Validating messagetype: " msg-name)
              msg-name))))
      (println "Header if not valid!"))))

;TODO TEST checksum calculation with 8=FIX.4.19=6135=A34=149=EXEC52=20121105-23:24:0656=BANZAI98=0108=3010=003
(+ (int \A) (+ (int \2)) (+ (int \3)) (+ (int \5)))

(seq (str 33333))



(defn- to-bytes [a]
  (reduce + (map int (if (keyword? a)
                       (drop 1 (seq (str a)))
                       (seq (str a))))))

(to-bytes 222)
(drop 1 (seq (str :111)))

(defn valid-trailer? [seq]
  (let [checksum (:value (last seq))                       ;TODO make sure the last one is :10
        sum-bytes (reduce #(+ %1 (to-bytes (:tag %2)) (to-bytes (:value %2))) 0 (drop-last seq))
        sum-delimiters (count (drop-last seq))
        sum-equal-symbols (* (count (drop-last seq)) (int \=))]
    (println "checksum: " checksum " and calc: " (mod (+ sum-bytes sum-delimiters sum-equal-symbols) 256))
    (= checksum  (mod (+ sum-bytes sum-delimiters sum-equal-symbols) 256))))

(defn- is-message? [seq]

  ;TODO destructuring for [header body trailer]
  ;TODO call each validation method chained with and (header eval must return msgName when valid)
  (let [[head body tail] (destructure-msg seq)]

    ;TODO use head only for header eval

    (if-let [msg-name (evaluate-header head)]
      msg-name
      )

    )


  )

(spec/def ::message  is-message?)
