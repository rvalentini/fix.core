(ns fix.spec.message-spec
  (:require [clojure.spec.alpha :as spec]
            [fix.definitions.components :as c]
            [fix.spec.component-spec :as s]
            [fix.definitions.messages :as m]
            [taoensso.timbre :refer [error info debug]]
            [fix.utils :refer [parse-number]]))

(def supported-versions #{"FIXT.1.1"})

(defn destructure-msg [seq]
  (let [[head rest] (split-with #((:tag %) (:definition (:Header c/components))) seq)
        [body tail] (split-with #(nil? ((:tag %) (:definition (:Trailer c/components)))) rest)]
    [head body tail]))

(defn- get-message-name-by-type [type]
  (if-let [msg (some #(if (= (:type (second %)) type) %) (seq m/messages))]
    (first msg)
    (error "Unknown message type:" type)))

(defn- valid-body-length? [[_ _ & rest] length-in-bytes]
  (let [without-checksum (drop-last rest)
        checksum-actual (+ (* 2 (count without-checksum) 1) ;all '=' and delimiters
                           (reduce #(+ %1 (:size %2)) 0 without-checksum))
        checksum-given (parse-number length-in-bytes)]
    (or
      (= checksum-actual checksum-given)
      (error "Header evaluation failed: body length is invalid! Body length given:"
             checksum-given " vs actual body length:" checksum-actual))))

(defn- to-bytes [a]
  (reduce + (map int (if (keyword? a)
                       (drop 1 (seq (str a)))
                       (seq (str a))))))

(defn- valid-start-of-header? [head]
  (and (>= (count head) 3)
       (= (:tag (first head)) :8)
       (= (:tag (second head)) :9)
       (= (:tag (nth head 2)) :35)))

(defn evaluate-header [head seq]
  "Extracts and validates the FIX <StandardHeader> component block.
   If the validation is successful the method returns the message name, otherwise nil
   The following TAGs are extracted and used for validation:
   :8 - BeginString
   :9 - BodyLength
   :35 - MessageType
   "
  (if (empty? head)
    (error "Header evaluation failed: header section is empty!")
    (if (spec/valid? ::s/component [head :Header])
      (let [relevant-tags (->> head
                               (filter #((:tag %) #{:8 :9 :35}))
                               (reduce #(assoc %1 (:tag %2) (:value %2)) {}))]
        (and
          (valid-start-of-header? head)
          (contains? supported-versions (:8 relevant-tags))
          (valid-body-length? seq (:9 relevant-tags))
          (if-let [msg-name (get-message-name-by-type (:35 relevant-tags))]
            (do
              (info "Received message-type is:" msg-name)
              msg-name))))
      (error "Header component is not valid!"))))


(defn valid-trailer? [seq]
  "Extracts and validates the FIX <StandardTrailer> component block.
   The following TAGs are extracted and used for validation:
   :10 - Checksum
   "
  (if-not (= :10 (:tag (last seq)))
    (error "Trailer evaluation failed: trailer section does not contain any checksum!")
    (let [checksum-given (Integer/parseInt (:value (last seq)))
          sum-bytes (reduce #(+ %1 (to-bytes (:tag %2)) (to-bytes (:value %2))) 0 (drop-last seq))
          sum-delimiters (count (drop-last seq))
          sum-equal-symbols (* (count (drop-last seq)) (int \=))
          checksum-actual (mod (+ sum-bytes sum-delimiters sum-equal-symbols) 256)]
      (debug "Message checksum given" checksum-given "vs. calculated checksum:" checksum-actual)
      (= checksum-given checksum-actual))))

(defn- is-message? [seq]
  (let [[head body _] (destructure-msg seq)]
    (if-let [msg-name (evaluate-header head seq)]
      (if (valid-trailer? seq)
        (do
          (debug "Header and Trailer valid! Continuing with body validation for msg-type:" msg-name)
          (let [message (:ordering (msg-name m/messages))
                as-component {:ordering (map #(if (= (:type %) :component)
                                                (assoc % :ordering [(:ordering ((:name %) c/components))])
                                                %) message)}]
            (spec/valid? ::s/component [body as-component])))
        (error "Trailer evaluation failed: checksum is not correct!")))))

(spec/def ::message is-message?)