(ns fix.spec.primitives-test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as spec]
            [fix.spec.primitives-spec :as p ]))


(deftest utc-timestamp-test
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

(deftest utc-time-only-test
  (testing "spec for UTC time-only data type is correctly defined"
    (is (spec/valid? ::p/utc-time-only "22:10:00.845"))
    (is (spec/valid? ::p/utc-time-only "18:10:44.845"))
    (is (spec/valid? ::p/utc-time-only "00:00:00.845"))
    (is (not (spec/valid? ::p/utc-time-only "99:99:99.999")))
    (is (spec/valid? ::p/utc-time-only "15:22:10"))
    (is (not (spec/valid? ::p/utc-time-only "15:22:100")))
    (is (not (spec/valid? ::p/utc-time-only "something totally different")))
    (is (not (spec/valid? ::p/utc-time-only "15:22:10.-1")))
    (is (not (spec/valid? ::p/utc-time-only "60:00:10.783")))
    (is (spec/valid? ::p/utc-time-only "23:59:59.000"))
    (is (not (spec/valid? ::p/utc-time-only "22:10.845")))
    (is (not (spec/valid? ::p/utc-time-only "15:22:10.sss")))))

(deftest utc-date-only-test
  (testing "spec for UTC date-only data type is correctly defined"
    (is (spec/valid? ::p/utc-date-only "20090101"))
    (is (spec/valid? ::p/utc-date-only "00000101"))
    (is (spec/valid? ::p/utc-date-only "99991231"))
    (is (not (spec/valid? ::p/utc-date-only "20100001")))
    (is (not (spec/valid? ::p/utc-date-only "20100100")))
    (is (not (spec/valid? ::p/utc-date-only "20100132")))))

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

(deftest tz-timestamp-test
  (testing "spec for TZ-timestamp data type is correctly defined"
    (is (spec/valid? ::p/tz-timestamp "20060901-07:39Z"))
    (is (spec/valid? ::p/tz-timestamp "20060901-02:39-05"))
    (is (spec/valid? ::p/tz-timestamp "20060901-15:39+08"))
    (is (spec/valid? ::p/tz-timestamp "20060901-13:09+05:30"))
    (is (not (spec/valid? ::p/tz-timestamp "13:09+05:99")))
    (is (not (spec/valid? ::p/tz-timestamp "99:09+05")))
    (is (spec/valid? ::p/tz-timestamp "20060901-00:00+01"))
    (is (not (spec/valid? ::p/tz-timestamp "10:10-")))))

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
    (is (spec/valid? ::p/qty "34"))
    (is (spec/valid? ::p/qty "0"))
    (is (spec/valid? ::p/qty "1.345"))
    (is (spec/valid? ::p/qty "2453.0"))
    (is (not (spec/valid? ::p/qty "ABC")))
    (is (spec/valid? ::p/qty "10.0"))
    (is (not (spec/valid? ::p/qty "1/2")))))

(deftest multiple-char-value-test
  (testing "spec for multiple-char-value is correctly defined"
    (is (spec/valid? ::p/multiple-char-value "j d t d O C X y"))
    (is (spec/valid? ::p/multiple-char-value "z"))
    (is (spec/valid? ::p/multiple-char-value "a b c"))
    (is (not (spec/valid? ::p/multiple-char-value "j d t d O C XU")))
    (is (not (spec/valid? ::p/multiple-char-value " ")))
    (is (not (spec/valid? ::p/multiple-char-value "ab")))
    (is (not (spec/valid? ::p/multiple-char-value "tt f r d")))
    (is (spec/valid? ::p/multiple-char-value "1 2 3"))
    (is (spec/valid? ::p/multiple-char-value ". . ."))
    (is (spec/valid? ::p/multiple-char-value "{ } Z 2"))
    (is (not (spec/valid? ::p/multiple-char-value "..")))))

(deftest multiple-string-value-test
  (testing "spec for multiple-string-value is correctly defined"
    (is (spec/valid? ::p/multiple-string-value "j d t d O C X y"))
    (is (spec/valid? ::p/multiple-string-value "z"))
    (is (spec/valid? ::p/multiple-string-value "a b c"))
    (is (spec/valid? ::p/multiple-string-value "j d t d O C XU"))
    (is (not (spec/valid? ::p/multiple-string-value " ")))
    (is (spec/valid? ::p/multiple-string-value "ab"))
    (is (spec/valid? ::p/multiple-string-value "tt f r d"))
    (is (spec/valid? ::p/multiple-string-value "1 2 3"))
    (is (spec/valid? ::p/multiple-string-value "aa//bh ZH;:[[ZH6 ss"))))

(run-tests)                                                 ;TODO remove
