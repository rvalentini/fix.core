(ns fix.message-spec-test
  (:require [clojure.test :refer :all]
            [fix.message-spec :as m]
            [clojure.spec.alpha :as spec]))



(deftest message-destructuring-test
  (testing "Message is correctly destructured in [head body tail]"
    (is (= (m/destructure-msg
             [{:tag :8 :value "FIXT.1.1" :size 8}
              {:tag :9 :value 39 :size 3}
              {:tag :35 :value "BR" :size 2}
              {:tag :49 :value "SENDER" :size 6}
              {:tag :56 :value "RECEIVER" :size 8}
              {:tag :34 :value 1 :size 1}
              {:tag :52 :value "some-time" :size 9}
              {:tag :1111 :value "some-msg-body" :size 13}
              {:tag :10 :value "some-checksum" :size 13}])
           [[{:tag :8 :value "FIXT.1.1" :size 8}
             {:tag :9 :value 39 :size 3}
             {:tag :35 :value "BR" :size 2}
             {:tag :49 :value "SENDER" :size 6}
             {:tag :56 :value "RECEIVER" :size 8}
             {:tag :34 :value 1 :size 1}
             {:tag :52 :value "some-time" :size 9}]
            [{:tag :1111 :value "some-msg-body" :size 13}]
            [{:tag :10 :value "some-checksum" :size 13}]]))


    (is (= (m/destructure-msg
             [{:tag :8 :value "FIXT.1.1" :size 8}
              {:tag :9 :value 39 :size 3}
              {:tag :35 :value "BR" :size 2}
              {:tag :49 :value "SENDER" :size 6}
              {:tag :56 :value "RECEIVER" :size 8}
              {:tag :34 :value 1 :size 1}
              {:tag :144 :value "some-location" :size 13}
              {:tag :52 :value "some-time" :size 9}
              {:tag :1111 :value "some-msg-body" :size 13}
              {:tag :93 :value 111 :size 3}
              {:tag :89 :value "some-signature" :size 14}
              {:tag :10 :value "some-checksum" :size 13}])

           [[{:tag :8 :value "FIXT.1.1" :size 8}
             {:tag :9 :value 39 :size 3}
             {:tag :35 :value "BR" :size 2}
             {:tag :49 :value "SENDER" :size 6}
             {:tag :56 :value "RECEIVER" :size 8}
             {:tag :34 :value 1 :size 1}
             {:tag :144 :value "some-location" :size 13}
             {:tag :52 :value "some-time" :size 9}]
            [{:tag :1111 :value "some-msg-body" :size 13}]
            [{:tag :93 :value 111 :size 3}
             {:tag :89 :value "some-signature" :size 14}
             {:tag :10 :value "some-checksum" :size 13}]]))

    ))

;TODO way more tests, also negative cases

(deftest message-spec-header-test
  (testing "StandardHeader component is extracted and validated correctly"
    (is (spec/valid? ::m/message
                     [{:tag :8 :value "FIXT.1.1" :size 8}
                      {:tag :9 :value 39 :size 3}
                      {:tag :35 :value "BR" :size 2}
                      {:tag :49 :value "SENDER" :size 6}
                      {:tag :56 :value "RECEIVER" :size 8}
                      {:tag :34 :value 1 :size 1}
                      {:tag :52 :value "some-time" :size 9}
                      {:tag :1111 :value "some-msg-body" :size 13}
                      {:tag :10 :value "some-checksum" :size 13}]))

    ))




(message-destructuring-test)