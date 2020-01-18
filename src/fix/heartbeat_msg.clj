(ns fix.heartbeat-msg
  (:require [clojure.spec.alpha :as spec]
            [fix.standard-header :as sh]
            [fix.standard-trailer :as st]))


(spec/def ::heartbeat-msg
  (spec/and ::sh/standard-header ::st/standard-trailer))
