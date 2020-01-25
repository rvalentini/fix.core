(ns fix.core-test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as spec]
            [fix.primitives :as p ]))


(deftest utc-timestamp-regex-test
  (testing "UTC timestamp regex is correctly defined for sss and mm timestamps"
    (is (spec/valid? ::p/utc-timestamp "20190713-15:22:10.845"))
    (is (not (spec/valid? ::p/utc-timestamp "20190713-15:22:10.845734")))
    (is (spec/valid? ::p/utc-timestamp "20190713-15:22:10"))
    (is (not (spec/valid? ::p/utc-timestamp "20190713-15:22:100")))
    (is (not (spec/valid? ::p/utc-timestamp "something totally different")))
    (is (not (spec/valid? ::p/utc-timestamp "15:22:10.845")))
    (is (not (spec/valid? ::p/utc-timestamp "15:22:10")))
    (is (spec/valid? ::p/utc-timestamp "19220101-10:10:00.000"))
    (is (not (spec/valid? ::p/utc-timestamp "A0190713-15:22:10.845")))
    (is (not (spec/valid? ::p/utc-timestamp "20190713-15:22:10.sss")))))


(deftest qty-datatype-test
  (testing "Spec for 'qty' is correctly defined according to FIX specification"
    (is (spec/valid? ::p/qty 34))
    (is (spec/valid? ::p/qty 0))
    (is (spec/valid? ::p/qty 1.345))
    (is (spec/valid? ::p/qty 2453.0))
    (is (spec/valid? ::p/qty 10.0))
    (is (not (spec/valid? ::p/qty 1/2)))))


(utc-timestamp-regex-test)
(qty-datatype-test)
