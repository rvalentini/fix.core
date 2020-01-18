(ns fix.core
  (:gen-class)
  (:require [clojure.spec.alpha :as spec]))

(defn -main
  [& args]
  (println "Hello, World!"))

;;TODO split namespace into smaller parts -> each type gets own namespace + one for primitives
;;TODO make a separate project for simple client, which handles binary encoding with ASCII 01 separator and TCP connection























