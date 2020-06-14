(ns fix.definitions.components
  (:require [clojure.edn :as edn]
            [taoensso.timbre :refer [error]])
  (:import (java.io FileNotFoundException)))

(def components (try
                  (edn/read-string (slurp "resources/components.edn"))
                  (catch FileNotFoundException _
                         (error (str "Resource definition file for components does not exist! Please run "
                                     "'lein generate-definitions' to generate all required source definition files.")))))