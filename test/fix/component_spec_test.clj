(ns fix.component-spec-test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as spec]
            [fix.component-spec :refer [matching-seqs?]]))


(deftest component-spec-test
  (testing "Basic sequence matching logic for received seqs and seq-definitions is working correctly"
    (is (matching-seqs? [:1] {:ordering   [:1]
                              :definition {:1 {:required true}}}))
    (is (matching-seqs? [:1] {:ordering   [:1]
                              :definition {:1 {:required false}}}))
    (is (not (matching-seqs? [:1] {:ordering   [:2]
                                   :definition {:2 {:required false}}})))
    (is (not (matching-seqs? [:1] {:ordering   [:2]
                                   :definition {:2 {:required true}}})))
    (is (matching-seqs? [] {:ordering   []
                            :definition {:2 {:required true}}}))
    (is (matching-seqs? [] {:ordering   []
                            :definition {}}))
    (is (not (matching-seqs? [:x] {:ordering   []
                                   :definition {}})))
    (is (not (matching-seqs? [:1 :3] {:ordering   [:x :1 :2 :3]
                                      :definition {:x {:required true}
                                                   :1 {:required true}
                                                   :2 {:required false}
                                                   :3 {:required true}}})))
    (is (matching-seqs? [:1 :3] {:ordering   [:x :1 :2 :3]
                                 :definition {:x {:required false}
                                              :1 {:required true}
                                              :2 {:required false}
                                              :3 {:required true}}})))

  (testing "Tags that are not contained in the definition/seq will fail the matching"
    (is (not (matching-seqs? [:1 :2 :3 :4] {:ordering   [:1 :2 :3]
                                            :definition {:1 {:required true}
                                                         :2 {:required true}
                                                         :3 {:required true}}})))
    (is (not (matching-seqs? [:X] {:ordering   [:1 :2 :3]
                                   :definition {:1 {:required true}
                                                :2 {:required true}
                                                :3 {:required true}}})))
    (is (not (matching-seqs? [:X] {:ordering   []
                                   :definition {}}))))

  (testing "Successful matching for given tag sequence will still fail when there are mandatory tags missing"
    (is (matching-seqs? [] {:ordering   [:b]
                            :definition {:b {:required false}}}))
    (is (not (matching-seqs? [] {:ordering   [:b]
                                 :definition {:b {:required true}}})))
    (is (not (matching-seqs? [:a :b :c] {:ordering   [:a :b :c :d]
                                         :definition {:a {:required false}
                                                      :b {:required false}
                                                      :c {:required true}
                                                      :d {:required true}}})))
    (is (matching-seqs? [:a :b :c] {:ordering   [:a :b :c :d]
                                    :definition {:a {:required false}
                                                 :b {:required false}
                                                 :c {:required true}
                                                 :d {:required false}}}))))


;(component-spec-test)
(run-tests)
