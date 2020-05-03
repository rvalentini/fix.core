(ns fix.order-qty-data
  (:require [clojure.spec.alpha :as spec]
            [fix.primitives-spec :as p]))

(spec/def ::order-qty-data
  {:38 ::p/order-qty
   :152 ::p/cash-order-qty
   :516 ::p/order-percent
   :468 ::p/rounding-direction
   :469 ::p/rounding-modulus})