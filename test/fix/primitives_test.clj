(ns fix.primitives-test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as spec]
            [fix.primitives :as p ]))


(deftest utc-timestamp-regex-test
  (testing "UTC timestamp regex is correctly defined for sss and mm timestamps"
    (is (spec/valid? ::p/utc-timestamp "20190713-15:22:10.845"))
    (is (spec/valid? ::p/utc-timestamp "20190713-00:18:10.845"))
    (is (spec/valid? ::p/utc-timestamp "20190713-23:23:00.845"))
    (is (not (spec/valid? ::p/utc-timestamp "20190713-15:22:10.845734")))
    (is (spec/valid? ::p/utc-timestamp "20190713-15:22:10"))
    (is (not (spec/valid? ::p/utc-timestamp "20190713-15:22:100")))
    (is (not (spec/valid? ::p/utc-timestamp "something totally different")))
    (is (not (spec/valid? ::p/utc-timestamp "15:22:10.845")))
    (is (not (spec/valid? ::p/utc-timestamp "15:22:10")))
    (is (spec/valid? ::p/utc-timestamp "19220101-10:10:00.000"))
    (is (not (spec/valid? ::p/utc-timestamp "A0190713-15:22:10.845")))
    (is (not (spec/valid? ::p/utc-timestamp "20190713-15:22:10.sss")))))


(deftest tz-time-only-test
  (testing "TZ-time-only regex is correctly defined"
    (is (spec/valid? ::p/tz-time-only "07:39Z"))
    (is (spec/valid? ::p/tz-time-only "11:49Z"))
    (is (spec/valid? ::p/tz-time-only "22:00Z"))
    (is (not (spec/valid? ::p/tz-time-only "07:39z")))
    (is (spec/valid? ::p/tz-time-only "02:39-05"))
    (is (spec/valid? ::p/tz-time-only "15:39+08"))
    (is (spec/valid? ::p/tz-time-only "13:09+05:30"))
    (is (not (spec/valid? ::p/tz-time-only "13:09+05:99")))
    (is (not (spec/valid? ::p/tz-time-only "99:09+05")))
    (is (spec/valid? ::p/tz-time-only "00:00+00"))
    (is (not (spec/valid? ::p/tz-time-only "10:10-")))
    ))

(deftest qty-datatype-test
  (testing "Spec for 'qty' is correctly defined according to FIX specification"
    (is (spec/valid? ::p/qty 34))
    (is (spec/valid? ::p/qty 0))
    (is (spec/valid? ::p/qty 1.345))
    (is (spec/valid? ::p/qty 2453.0))
    (is (spec/valid? ::p/qty 10.0))
    (is (not (spec/valid? ::p/qty 1/2)))))


(utc-timestamp-regex-test) ;TODO remove
(qty-datatype-test) ;TODO remove
(tz-time-only-test)
