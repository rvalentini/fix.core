(ns fix.definitions.fields
  (:require [clojure.edn :as edn]
            [taoensso.timbre :refer [error]])
  (:import (java.io FileNotFoundException)))

(def fields (try
              (edn/read-string (slurp "resources/fields.edn"))
              (catch FileNotFoundException _
                (error (str "Resource definition file for fields does not exist! Please run "
                            "'lein generate-definitions' to generate all required source definition files.")))))