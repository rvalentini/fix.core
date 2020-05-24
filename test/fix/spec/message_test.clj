(ns fix.spec.message-test
  (:require [clojure.test :refer :all]
            [fix.spec.message-spec :as m]
            [clojure.spec.alpha :as spec]))


(deftest checksum-calculation-test
  (testing "Calculation of the messages checksum is correct"
    ;8=FIX.4.19=6135=A34=149=EXEC52=20121105-23:24:0656=BANZAI98=0108=3010=003
    (is (m/valid-trailer? [
                           {:tag :8 :value "FIX.4.1"}
                           {:tag :9 :value "61"}
                           {:tag :35 :value "A"}
                           {:tag :34 :value "1"}
                           {:tag :49 :value "EXEC"}
                           {:tag :52 :value "20121105-23:24:06"}
                           {:tag :56 :value "BANZAI"}
                           {:tag :98 :value "0"}
                           {:tag :108 :value "30"}
                           {:tag :10 :value "003"}]))

    ;8=FIX.4.19=8235=334=949=EXEC52=20121105-23:25:2556=BANZAI45=758=Unsupported message type10=002
    (is (m/valid-trailer? [
                           {:tag :8 :value "FIX.4.1"}
                           {:tag :9 :value "82"}
                           {:tag :35 :value "3"}
                           {:tag :34 :value "9"}
                           {:tag :49 :value "EXEC"}
                           {:tag :52 :value "20121105-23:25:25"}
                           {:tag :56 :value "BANZAI"}
                           {:tag :45 :value "7"}
                           {:tag :58 :value "Unsupported message type"}
                           {:tag :10 :value "002"}]))

    (is (not (m/valid-trailer? [
                                {:tag :8 :value "FIX.4.1"}
                                {:tag :9 :value "82"}
                                {:tag :35 :value "3"}
                                {:tag :34 :value "9"}
                                {:tag :49 :value "EXEC"}
                                {:tag :52 :value "20121105-23:25:25"}
                                {:tag :56 :value "BANZAI"}
                                {:tag :45 :value "7"}
                                {:tag :58 :value "Unsupported message type"}
                                {:tag :10 :value "111"}])))
    ))


(deftest message-destructuring-test
  (testing "Message is correctly destructured in [head body tail]"
    (is (= (m/destructure-msg
             [{:tag :8 :value "FIXT.1.1" :size 8}
              {:tag :9 :value "39" :size 3}
              {:tag :35 :value "BR" :size 2}
              {:tag :49 :value "SENDER" :size 6}
              {:tag :56 :value "RECEIVER" :size 8}
              {:tag :34 :value "1" :size 1}
              {:tag :52 :value "some-time" :size 9}
              {:tag :1111 :value "some-msg-body" :size 13}
              {:tag :10 :value "some-checksum" :size 13}])
           [[{:tag :8 :value "FIXT.1.1" :size 8}
             {:tag :9 :value "39" :size 3}
             {:tag :35 :value "BR" :size 2}
             {:tag :49 :value "SENDER" :size 6}
             {:tag :56 :value "RECEIVER" :size 8}
             {:tag :34 :value "1" :size 1}
             {:tag :52 :value "some-time" :size 9}]
            [{:tag :1111 :value "some-msg-body" :size 13}]
            [{:tag :10 :value "some-checksum" :size 13}]]))


    (is (= (m/destructure-msg
             [{:tag :8 :value "FIXT.1.1" :size 8}
              {:tag :9 :value "39" :size 3}
              {:tag :35 :value "BR" :size 2}
              {:tag :49 :value "SENDER" :size 6}
              {:tag :56 :value "RECEIVER" :size 8}
              {:tag :34 :value "1" :size 1}
              {:tag :144 :value "some-location" :size 13}
              {:tag :52 :value "some-time" :size 9}
              {:tag :1111 :value "some-msg-body" :size 13}
              {:tag :93 :value "111" :size 3}
              {:tag :89 :value "some-signature" :size 14}
              {:tag :10 :value "some-checksum" :size 13}])

           [[{:tag :8 :value "FIXT.1.1" :size 8}
             {:tag :9 :value "39" :size 3}
             {:tag :35 :value "BR" :size 2}
             {:tag :49 :value "SENDER" :size 6}
             {:tag :56 :value "RECEIVER" :size 8}
             {:tag :34 :value "1" :size 1}
             {:tag :144 :value "some-location" :size 13}
             {:tag :52 :value "some-time" :size 9}]
            [{:tag :1111 :value "some-msg-body" :size 13}]
            [{:tag :93 :value "111" :size 3}
             {:tag :89 :value "some-signature" :size 14}
             {:tag :10 :value "some-checksum" :size 13}]]))))

(deftest message-spec-header-test
  (testing "StandardHeader component is extracted and validated correctly"
    (let [seq [{:tag :8 :value "FIXT.1.1" :size 9}
               {:tag :9 :value "65" :size 3}
               {:tag :35 :value "BR" :size 4}
               {:tag :49 :value "SENDER" :size 8}
               {:tag :56 :value "RECEIVER" :size 10}
               {:tag :34 :value "1" :size 3}
               {:tag :52 :value "19990101-00:00:00" :size 11}
               {:tag :1111 :value "some-msg-body" :size 17}
               {:tag :10 :value "some-checksum" :size 15}]
          [head _ _] (m/destructure-msg seq)]
      (is (m/evaluate-header head seq)))))

(deftest complete-message-spec-test
  (testing "Complete FIX message validation for empty Heartbeat message type"
    ;8=FIXT.1.1^9=65^35=0^49=BuySide^56=SellSide^34=3^52=20190605-12:45:24.919^1128=9^10=064^
    (is (spec/valid? ::m/message [{:tag :8 :value "FIXT.1.1" :size 9}
                                  {:tag :9 :value "65" :size 3}
                                  {:tag :35 :value "0" :size 3}
                                  {:tag :49 :value "BuySide" :size 9}
                                  {:tag :56 :value "SellSide" :size 10}
                                  {:tag :34 :value "3" :size 3}
                                  {:tag :52 :value "20190605-12:45:24.919" :size 23}
                                  {:tag :1128 :value "9" :size 5}
                                  {:tag :10 :value "064" :size 5}])))
  (testing "Complete FIX message validation for Heartbeat message type with :112 content"
    ;same message as above but with :112 content this time (body length and checksum adjusted)
    (is (spec/valid? ::m/message [{:tag :8 :value "FIXT.1.1" :size 9}
                                  {:tag :9 :value "82" :size 3}
                                  {:tag :35 :value "0" :size 3}
                                  {:tag :49 :value "BuySide" :size 9}
                                  {:tag :56 :value "SellSide" :size 10}
                                  {:tag :34 :value "3" :size 3}
                                  {:tag :52 :value "20190605-12:45:24.919" :size 23}
                                  {:tag :1128 :value "9" :size 5}
                                  {:tag :112 :value "some-test-id" :size 15}
                                  {:tag :10 :value "172" :size 5}])))

  (testing "Complete FIX message validation for Heartbeat message with random ordering of tags"
    (is (spec/valid? ::m/message [{:tag :8 :value "FIXT.1.1" :size 9}
                                  {:tag :9 :value "82" :size 3}
                                  {:tag :35 :value "0" :size 3}
                                  {:tag :52 :value "20190605-12:45:24.919" :size 23}
                                  {:tag :49 :value "BuySide" :size 9}
                                  {:tag :34 :value "3" :size 3}
                                  {:tag :1128 :value "9" :size 5}
                                  {:tag :56 :value "SellSide" :size 10}
                                  {:tag :112 :value "some-test-id" :size 15}
                                  {:tag :10 :value "172" :size 5}])))

  (testing "Complete FIX message validation for Heartbeat message with invalid start of header"
    (is (not (spec/valid? ::m/message [{:tag :9 :value "82" :size 3}
                                       {:tag :8 :value "FIXT.1.1" :size 9} ;this should be always first tag of header
                                       {:tag :35 :value "0" :size 3}
                                       {:tag :52 :value "20190605-12:45:24.919" :size 23}
                                       {:tag :49 :value "BuySide" :size 9}
                                       {:tag :34 :value "3" :size 3}
                                       {:tag :1128 :value "9" :size 5}
                                       {:tag :56 :value "SellSide" :size 10}
                                       {:tag :112 :value "some-test-id" :size 15}
                                       {:tag :10 :value "172" :size 5}]))))

  (testing "Complete FIX message validation for NewOrderSingle message with mixed tag ordering"
    (is (spec/valid? ::m/message [{:tag :8 :value "FIXT.1.1" :size 9}
                                  {:tag :9 :value "237" :size 3}
                                  {:tag :35 :value "D" :size 3}
                                  {:tag :49 :value "myAwsomeComp" :size 14}
                                  {:tag :56 :value "yourAwesomeComp" :size 17}
                                  {:tag :52 :value "20200605-12:45:24.919" :size 23}
                                  {:tag :34 :value "1" :size 3}
                                  {:tag :115 :value "aThirdAwesomeComp" :size 20}
                                  {:tag :11 :value "id394850345de" :size 15}
                                  {:tag :1 :value "acc1" :size 5}
                                  {:tag :18 :value "l m p" :size 7}
                                  {:tag :55 :value "[N/A]" :size 7}
                                  {:tag :460 :value "5" :size 4}
                                  {:tag :167 :value "CS" :size 5}
                                  {:tag :470 :value "GB" :size 5}
                                  {:tag :48 :value "AMZN" :size 6}
                                  {:tag :22 :value "8" :size 3}
                                  {:tag :54 :value "1" :size 3}
                                  {:tag :60 :value "20200605-12:45:24.919" :size 23}
                                  {:tag :38 :value "3000" :size 6}
                                  {:tag :40 :value "4" :size 3}
                                  {:tag :44 :value "462.49" :size 8}
                                  {:tag :99 :value "430.00" :size 8}
                                  {:tag :15 :value "CHF" :size 5}
                                  {:tag :10 :value "230" :size 5}]))))
