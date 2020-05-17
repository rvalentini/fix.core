(ns fix.parser.fix-parser
  (:require [clojure.string :refer [split blank? includes?]]
            [fix.utils :refer [parse-number soh-delimiter]]
            [taoensso.timbre :refer [error info]])
  (:import (java.util.regex Pattern)))

(def soh-delimiter-rgx (Pattern/compile (str soh-delimiter)))
(def equals #"=")

(defn- is-tag? [arg]
  ((every-pred int? #(< % 1621) #(> % 0)) arg))

(defn- tag-valid? [elem]
  (let [tag-empty (some-> (:tag elem) name blank?)
        value-empty (blank? (:value elem))
        valid-tag (some-> (:tag elem) name parse-number is-tag?)]
    (if tag-empty (error "Tag must no be empty:" elem))
    (if value-empty (error "Value must not be empty:" elem))
    (if-not valid-tag (error "The following Tag is not valid:" (:tag elem)))
    (and (not tag-empty) (not value-empty) valid-tag)))

(defn- input-valid? [msg delimiter]
  (let [msg-valid (string? msg)
        delimiter-valid (and (= (type delimiter) Pattern) (count (.pattern delimiter)))]
    (if-not msg-valid (error "Given message is not a String!"))
    (if-not delimiter-valid (error "Given delimiter is not a single Char Regex Pattern!"))
    (and msg-valid delimiter-valid)))

(defn- assoc-size [elem]
  (assoc elem :size (+ (count (:tag elem)) (count (:value elem)))))

(defn parse
  ([msg] (parse msg soh-delimiter-rgx))
  ([msg delimiter]
   (if (input-valid? msg delimiter)
     (let [parsed (->> (split msg delimiter)
                       (map #(split % equals))
                       (map #(identity {:tag (first %) :value (second %)}))
                       (map assoc-size)
                       (map #(update % :tag keyword)))]
       (when (every? tag-valid? parsed) (vec parsed))))))




