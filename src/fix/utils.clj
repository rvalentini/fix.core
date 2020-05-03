(ns fix.utils
  (:require [clojure.edn :as edn]
            [taoensso.timbre :refer [error]]))

(defn parse-number [arg]
  (when (string? arg)
    (try
      (edn/read-string arg)
      (catch NumberFormatException e
        (error e "Cannot parse field value:" arg "is not a number!")))))