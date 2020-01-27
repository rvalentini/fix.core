(ns fix.primitives-test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as spec]
            [fix.primitives :as p ]))


(deftest utc-timestamp-regex-test
  (testing "spec for UTC timestamp data type is correctly defined"
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
  (testing "spec for TZ-time-only data type is correctly defined"
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
    (is (not (spec/valid? ::p/tz-time-only "10:10-")))))

(deftest local-mkt-date-test
  (testing "spec for local-mkt-date data type is correctly defined"
    (is (spec/valid? ::p/local-mkt-date "00001210"))
    (is (spec/valid? ::p/local-mkt-date "99990131"))
    (is (spec/valid? ::p/local-mkt-date "19890929"))
    (is (spec/valid? ::p/local-mkt-date "22900201"))
    (is (not (spec/valid? ::p/local-mkt-date "00001301")))
    (is (not (spec/valid? ::p/local-mkt-date "00001232")))
    (is (not (spec/valid? ::p/local-mkt-date "00000011")))
    (is (not (spec/valid? ::p/local-mkt-date "00000000")))
    (is (not (spec/valid? ::p/local-mkt-date "00000800")))))

(deftest month-year-date-test
  (testing "spec for month-year date data type is correctly defined"
    (is (spec/valid? ::p/month-year "00001210"))
    (is (spec/valid? ::p/month-year "99990131"))
    (is (spec/valid? ::p/month-year "19890929"))
    (is (spec/valid? ::p/month-year "229002"))
    (is (spec/valid? ::p/month-year "229002w1"))
    (is (spec/valid? ::p/month-year "229002w4"))
    (is (not (spec/valid? ::p/month-year "229002w0")))
    (is (not (spec/valid? ::p/month-year "000012ww")))
    (is (not (spec/valid? ::p/month-year "00000011")))
    (is (not (spec/valid? ::p/month-year "00000000")))
    (is (not (spec/valid? ::p/month-year "000000")))))


(deftest qty-datatype-test
  (testing "spec for 'qty' data type is correctly defined"
    (is (spec/valid? ::p/qty 34))
    (is (spec/valid? ::p/qty 0))
    (is (spec/valid? ::p/qty 1.345))
    (is (spec/valid? ::p/qty 2453.0))
    (is (spec/valid? ::p/qty 10.0))
    (is (not (spec/valid? ::p/qty 1/2)))))


(deftest multiple-char-value-test
  (testing "spec for multiple-char-value is correctly defined"
    (is (spec/valid? ::p/multiple-char-value "j d t d O C X y"))
    (is (spec/valid? ::p/multiple-char-value "z"))
    (is (spec/valid? ::p/multiple-char-value "a b c"))
    (is (not (spec/valid? ::p/multiple-char-value "j d t d O C XU")))
    (is (not (spec/valid? ::p/multiple-char-value " ")))
    (is (not (spec/valid? ::p/multiple-char-value "ab")))
    (is (not (spec/valid? ::p/multiple-char-value "tt f r d")))
    (is (not (spec/valid? ::p/multiple-char-value "1 2 3")))))

(utc-timestamp-regex-test) ;TODO remove
(qty-datatype-test) ;TODO remove
(tz-time-only-test) ;TODO remove
(local-mkt-date-test) ;TODO remove
(month-year-date-test) ;TODO remove
(multiple-char-value-test) ;TODO remove
