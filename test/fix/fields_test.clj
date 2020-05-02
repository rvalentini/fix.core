(ns fix.fields_test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as spec]
            [fix.fields-spec :as fs]))


(deftest fields-spec-test
  (testing "Spec for field data type is correctly defined"
    (is (spec/valid? ::fs/field {:tag :1450 :value "SD"}))
    (is (spec/valid? ::fs/field {:tag :1450 :value "SR"}))
    (is (spec/valid? ::fs/field {:tag :1450 :value "SB"}))
    (is (not (spec/valid? ::fs/field {:tag :1450 :value "SD+something-else"})))
    (is (not (spec/valid? ::fs/field {:tag :000 :value "SD"})))
    (is (not (spec/valid? ::fs/field {:tag :1450 :value "289"})))
    (is (not (spec/valid? ::fs/field {:tag :1450})))
    (is (not (spec/valid? ::fs/field {:tag :1450 :value {:blah "111"}})))
    (is (not (spec/valid? ::fs/field {:tag :1450 :value :something})))
    (is (not (spec/valid? ::fs/field {:tag :1450 :value ["SD" "SD"]})))
    (is (spec/valid? ::fs/field {:tag :862 :value "62"}))
    (is (spec/valid? ::fs/field {:tag :277 :value "4 3"}))
    (is (spec/valid? ::fs/field {:tag :277 :value "w T d z AH"}))
    (is (not (spec/valid? ::fs/field {:tag :277 :value "Ah"})))))

(run-tests)                                                 ;TODO remove
