(ns fix.parser.fix-parser-test
  (:require [clojure.test :refer :all]
            [fix.parser.fix-parser :refer [parse]]
            [fix.utils :refer [soh-delimiter]]))

(deftest parser-test
  (testing "parser return correct map given valid input"
    (is (= (parse "1=ABC&2=ABC&44=34something" #"&")
           [{:tag :1, :value "ABC", :size 4} {:tag :2, :value "ABC", :size 4} {:tag :44, :value "34something", :size 13}]))
    (is (= (parse "1=ABC" #"&")
           [{:tag :1, :value "ABC", :size 4}]))
    (is (= (parse (str "1=ABC" soh-delimiter "2=DEF"))
           [{:tag :1, :value "ABC", :size 4} {:tag :2, :value "DEF", :size 4}]))
    (is (= (parse "1=1&2=2&&" #"&") ;empty delimited section in the end
           [{:tag :1, :value "1", :size 2}{:tag :2, :value "2", :size 2}]))
    (is (= (parse "14=abcde&43=11.3534&902=TEF&1120=something else" #"&")
           [{:tag :14, :value "abcde", :size 7}
            {:tag :43, :value "11.3534", :size 9}
            {:tag :902, :value "TEF", :size 6}
            {:tag :1120, :value "something else", :size 18}])))

  (testing "parser returns nil given invalid input"
    (is (= (parse "2222=ABC" #"&") ;TAG too large
           nil))
    (is (= (parse "1=" #"&")
           nil))
    (is (= (parse "=something" #"&")
           nil))
    (is (= (parse "=" #"&")
           nil))
    (is (= (parse "" #"&")
           nil))
    (is (= (parse "AA=something" #"&") ;TAG must be number
           nil))
    (is (= (parse "1=1&&2=2" #"&") ;empty delimited section in the middle
           nil))
    (is (= (parse "1=1&123=&2=2" #"&")
           nil))
    (is (= (parse "1=1&123&2=2" #"&")
           nil))))


(run-tests)


