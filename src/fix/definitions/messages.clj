(ns fix.definitions.messages
  (:require [clojure.edn :as edn]
            [taoensso.timbre :refer [error]])
  (:import (java.io FileNotFoundException)))

(def messages (try
                (edn/read-string (slurp "resources/messages.edn"))
                (catch FileNotFoundException _
                  (error (str "Resource definition file for messages does not exist! Please run "
                              "'lein generate-definitions' to generate all required source definition files.")))))