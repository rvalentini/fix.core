(ns fix.fields_test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as spec]
            [fix.fields-spec :as fs]))


(deftest fields-spec-test
  (testing "Spec for field data type is correctly defined"
    (is (spec/valid? ::fs/field [:1450 "SD"]))
    (is (spec/valid? ::fs/field [:1450 "SR"]))
    (is (spec/valid? ::fs/field [:1450 "SB"]))
    (is (not (spec/valid? ::fs/field [:1450 "SD+something-else"])))
    (is (not (spec/valid? ::fs/field [:000 "SD"])))
    (is (not (spec/valid? ::fs/field [:1450 289])))
    (is (not (spec/valid? ::fs/field [:1450 []])))
    (is (not (spec/valid? ::fs/field [:1450 {:blah 111}])))
    (is (not (spec/valid? ::fs/field [:1450 :something])))
    (is (not (spec/valid? ::fs/field [:1450 "SD" "SD"])))
    (is (spec/valid? ::fs/field [:862 62]))
    (is (spec/valid? ::fs/field [:277 "4 3"]))
    (is (spec/valid? ::fs/field [:277 "w T d z AH"]))
    (is (not (spec/valid? ::fs/field [:277 "Ah"])))))

(fields-spec-test) ;TODO remove