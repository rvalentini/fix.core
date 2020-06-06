(ns fix.utils
  (:require [clojure.edn :as edn]
            [taoensso.timbre :refer [error]]))

(def soh-delimiter (char 1))

(defn parse-number [arg]
  (when (string? arg)
    (try
      (edn/read-string arg)
      (catch NumberFormatException e
        (error e "Cannot parse field value:" arg "is not a number!")))))

(defn flatten-seq [arg]
  (if (and (sequential? arg) (= (count arg) 1) (sequential? (first arg)))
    (first arg)
    (vec arg)))

(defn fix->str [msg delimiter]
  (reduce
    (fn [acc i]
      (str acc (name (:tag i)) "=" (:value i) (when (not= (:tag i) :10) delimiter)))
    "" msg))