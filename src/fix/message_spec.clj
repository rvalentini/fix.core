(ns fix.message-spec
  (:require [clojure.spec.alpha :as spec]))


(defn- validate-header [seq]
  ;TODO extract header and validate (via definition)
  )


(defn- validate-trailer [seq]
  ;TODO extract trailer and validate (only grab last 3 tags?)
  )

(defn- is-message? [seq]



  )

(spec/def ::message  is-message?)