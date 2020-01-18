(ns fix.instrument
  (:require [clojure.spec.alpha :as spec]
            [fix.primitives :as p]))


(spec/def ::instrument
  {:55 ::p/symbol
   ;;TODO some more -> none of the TAGs are required which is strange
   })