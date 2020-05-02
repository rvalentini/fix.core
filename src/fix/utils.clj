(ns fix.utils
  (:require [clojure.edn :as edn]))

(defn parse-number [arg]
  (when (string? arg)
    (try
      (edn/read-string arg)
      (catch NumberFormatException e
        (println (type e) "Cannot parse field value:" arg "is not a number!")))))