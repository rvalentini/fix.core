(ns fix.core-test
  (:require [clojure.test :refer :all]
            [fix.core :refer [valid?]]))

(deftest end-to-end-message-spec-test
  (testing "Complete end to end FIX message validation for Heartbeat message"
    (is (valid? "8=FIXT.1.19=6535=049=BuySide56=SellSide34=352=20190605-12:45:24.9191128=910=064")))

  (testing "Complete end to end FIX message validation for Heartbeat message with custom delimiter"
    (is (valid? "8=FIXT.1.1%9=65%35=0%49=BuySide%56=SellSide%34=3%52=20190605-12:45:24.919%1128=9%10=064"
                #"%")))
  (testing "Complete end to end FIX message validation for NewOrderSingle message"
    (is (valid? "8=FIXT.1.19=23735=D49=myAwsomeComp56=yourAwesomeComp52=20200605-12:45:24.91934=1115=aThirdAwesomeComp11=id394850345de1=acc118=l m p55=[N/A]460=5167=CS470=GB48=AMZN22=854=160=20200605-12:45:24.91938=300040=444=462.4999=430.0015=CHF10=230")))

  (testing "Complete end to end FIX message validation for NewOrderSingle message with custom delimiter"
    (is (valid? "8=FIXT.1.1%9=237%35=D%49=myAwsomeComp%56=yourAwesomeComp%52=20200605-12:45:24.919%34=1%115=aThirdAwesomeComp%11=id394850345de%1=acc1%18=l m p%55=[N/A]%460=5%167=CS%470=GB%48=AMZN%22=8%54=1%60=20200605-12:45:24.919%38=3000%40=4%44=462.49%99=430.00%15=CHF%10=230"
                #"%"))))
