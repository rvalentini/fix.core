(ns fix.new-order-single
  (:require [clojure.spec.alpha :as spec]
            [fix.primitives-spec :as p]
            [fix.standard-header :as sh]
            [fix.standard-trailer :as st]
            [fix.instrument :as i]
            [fix.order-qty-data :as o]))


(spec/def ::new-order-single
  (spec/and ::sh/standard-header
            {:11 ::p/cl-ord-id}
            ::i/instrument
            {:54 ::p/side :60 ::p/transact-time}
            ::o/order-qty-data
            ::st/standard-trailer))