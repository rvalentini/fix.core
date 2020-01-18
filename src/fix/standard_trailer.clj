(ns fix.standard-trailer
  (:require [clojure.spec.alpha :as spec]
            [fix.primitives :as p]))


(spec/def ::standard-trailer
  {:10 ::p/check-sum})