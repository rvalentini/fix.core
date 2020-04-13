(ns fix.component-spec-test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as spec]
            [fix.component-spec :refer [matching-seqs?]]))


(deftest simple-component-spec-test
  (testing "Basic sequence matching logic for received seqs and seq-definitions is working correctly"
    (is (matching-seqs? [:1] {:ordering [{:tag      :1
                                          :required true
                                          :type     :field}]}))
    (is (matching-seqs? [:1] {:ordering [{:tag      :1
                                          :required true
                                          :type     :field}]}))
    (is (not (matching-seqs? [:1] {:ordering [{:tag      :2
                                               :required false
                                               :type     :field}]})))
    (is (not (matching-seqs? [:1] {:ordering [{:tag      :2
                                               :required true
                                               :type     :field}]})))
    (is (matching-seqs? [] {:ordering []}))
    (is (not (matching-seqs? [:x] {:ordering []})))
    (is (not (matching-seqs? [:1 :3] {:ordering [{:tag      :x
                                                  :required true
                                                  :type     :field}
                                                 {:tag      :1
                                                  :required true
                                                  :type     :field}
                                                 {:tag      :2
                                                  :required false
                                                  :type     :field}
                                                 {:tag      :3
                                                  :required true
                                                  :type     :field}]})))
    (is (matching-seqs? [:1 :3] {:ordering [{:tag      :x
                                             :required false
                                             :type     :field}
                                            {:tag      :1
                                             :required true
                                             :type     :field}
                                            {:tag      :2
                                             :required false
                                             :type     :field}
                                            {:tag      :3
                                             :required true
                                             :type     :field}]}))
    )

  (testing "Tags that are not contained in the definition/seq will fail the matching"
    (is (not (matching-seqs? [:1 :2 :3 :4] {:ordering [{:tag      :1
                                                        :required true
                                                        :type     :field}
                                                       {:tag      :2
                                                        :required true
                                                        :type     :field}
                                                       {:tag      :3
                                                        :required true
                                                        :type     :field}]})))
    (is (not (matching-seqs? [:X] {:ordering [{:tag      :1
                                               :required true
                                               :type     :field}
                                              {:tag      :2
                                               :required true
                                               :type     :field}
                                              {:tag      :3
                                               :required true
                                               :type     :field}]})))
    (is (not (matching-seqs? [:X] {:ordering []}))))

  (testing "Successful matching for given tag sequence will still fail when there are mandatory tags missing"
    (is (matching-seqs? [] {:ordering [{:tag      :b
                                        :required false
                                        :type     :field}]}))
    (is (not (matching-seqs? [] {:ordering [{:tag      :b
                                             :required true
                                             :type     :field}]})))
    (is (not (matching-seqs? [:a :b :c] {:ordering [{:tag      :a
                                                     :required false
                                                     :type     :field}
                                                    {:tag      :b
                                                     :required false
                                                     :type     :field}
                                                    {:tag      :c
                                                     :required true
                                                     :type     :field}
                                                    {:tag      :d
                                                     :required true
                                                     :type     :field}]})))
    (is (matching-seqs? [:a :b :c] {:ordering [{:tag      :a
                                                :required false
                                                :type     :field}
                                               {:tag      :b
                                                :required false
                                                :type     :field}
                                               {:tag      :c
                                                :required true
                                                :type     :field}
                                               {:tag      :d
                                                :required false
                                                :type     :field}]}))))


(deftest nested-component-spec-test
  (testing "Matching for complex nested structures containing components"
    (is (matching-seqs? [:a :b :c :c1 :c2 :c3] {:ordering
                                                [{:tag      :a
                                                  :required true
                                                  :type     :field}
                                                 {:tag      :b
                                                  :required true
                                                  :type     :field}
                                                 {:tag      :c
                                                  :required true
                                                  :type     :field}
                                                 {:type     :component
                                                  :required true
                                                  :name     "TestComponent"
                                                  :ordering
                                                            [{:tag      :c1
                                                              :required true
                                                              :type     :field}
                                                             {:tag      :c2
                                                              :required true
                                                              :type     :field}
                                                             {:tag      :c3
                                                              :required true
                                                              :type     :field}]}]}))

    (is (matching-seqs? [:a :b :c :c11 :c12 :c13 :c21 :c22 :c23] {:ordering
                                                                  [{:tag      :a
                                                                    :required true
                                                                    :type     :field}
                                                                   {:tag      :b
                                                                    :required true
                                                                    :type     :field}
                                                                   {:tag      :c
                                                                    :required true
                                                                    :type     :field}
                                                                   {:type     :component
                                                                    :required true
                                                                    :name     "TestComponentA"
                                                                    :ordering
                                                                              [{:tag      :c11
                                                                                :required true
                                                                                :type     :field}
                                                                               {:tag      :c12
                                                                                :required true
                                                                                :type     :field}
                                                                               {:tag      :c13
                                                                                :required true
                                                                                :type     :field}]}
                                                                   {:type     :component
                                                                    :required true
                                                                    :name     "TestComponentB"
                                                                    :ordering
                                                                              [{:tag      :c21
                                                                                :required true
                                                                                :type     :field}
                                                                               {:tag      :c22
                                                                                :required true
                                                                                :type     :field}
                                                                               {:tag      :c23
                                                                                :required true
                                                                                :type     :field}]}]}))


    (is (matching-seqs? [:a :b :c :c1 :c2 :c31 :c321 :c322 :c33 :c4] {:ordering
                                                                      [{:tag      :a
                                                                        :required true
                                                                        :type     :field}
                                                                       {:tag      :b
                                                                        :required true
                                                                        :type     :field}
                                                                       {:tag      :c
                                                                        :required true
                                                                        :type     :field}
                                                                       {:type     :component
                                                                        :required true
                                                                        :name     "TestComponentA"
                                                                        :ordering
                                                                                  [{:tag      :c1
                                                                                    :required true
                                                                                    :type     :field}
                                                                                   {:tag      :c2
                                                                                    :required true
                                                                                    :type     :field}
                                                                                   {:type     :component
                                                                                    :required true
                                                                                    :name     "TestComponentB"
                                                                                    :ordering
                                                                                              [{:tag      :c31
                                                                                                :required true
                                                                                                :type     :field}
                                                                                               {:type     :component
                                                                                                :required true
                                                                                                :name     "TestComponentC"
                                                                                                :ordering
                                                                                                          [{:tag      :c321
                                                                                                            :required true
                                                                                                            :type     :field}
                                                                                                           {:tag      :c322
                                                                                                            :required true
                                                                                                            :type     :field}]}
                                                                                               {:tag      :c33
                                                                                                :required true
                                                                                                :type     :field}]}
                                                                                   {:tag      :c4
                                                                                    :required true
                                                                                    :type     :field}]}]}))


    (is (not (matching-seqs? [:a :b :c :c1 :c2 :c31 :c321 :wrong-tag :c322 :c33 :c4] {:ordering
                                                                                      [{:tag      :a
                                                                                        :required true
                                                                                        :type     :field}
                                                                                       {:tag      :b
                                                                                        :required true
                                                                                        :type     :field}
                                                                                       {:tag      :c
                                                                                        :required true
                                                                                        :type     :field}
                                                                                       {:type     :component
                                                                                        :required true
                                                                                        :name     "TestComponentA"
                                                                                        :ordering
                                                                                                  [{:tag      :c1
                                                                                                    :required true
                                                                                                    :type     :field}
                                                                                                   {:tag      :c2
                                                                                                    :required true
                                                                                                    :type     :field}
                                                                                                   {:type     :component
                                                                                                    :required true
                                                                                                    :name     "TestComponentB"
                                                                                                    :ordering
                                                                                                              [{:tag      :c31
                                                                                                                :required true
                                                                                                                :type     :field}
                                                                                                               {:type     :component
                                                                                                                :required true
                                                                                                                :name     "TestComponentC"
                                                                                                                :ordering
                                                                                                                          [{:tag      :c321
                                                                                                                            :required true
                                                                                                                            :type     :field}
                                                                                                                           {:tag      :c322
                                                                                                                            :required true
                                                                                                                            :type     :field}]}
                                                                                                               {:tag      :c33
                                                                                                                :required true
                                                                                                                :type     :field}]}
                                                                                                   {:tag      :c4
                                                                                                    :required true
                                                                                                    :type     :field}]}]})))

    (is (matching-seqs? [:a :c :c1 :c2 :c31 :c322 :c34 :c4] {:ordering
                                                             [{:tag      :a
                                                               :required true
                                                               :type     :field}
                                                              {:tag      :b
                                                               :required false
                                                               :type     :field}
                                                              {:tag      :c
                                                               :required true
                                                               :type     :field}
                                                              {:type     :component
                                                               :required true
                                                               :name     "TestComponentA"
                                                               :ordering
                                                                         [{:tag      :c1
                                                                           :required false
                                                                           :type     :field}
                                                                          {:tag      :c2
                                                                           :required true
                                                                           :type     :field}
                                                                          {:type     :component
                                                                           :required true
                                                                           :name     "TestComponentB"
                                                                           :ordering
                                                                                     [{:tag      :c31
                                                                                       :required true
                                                                                       :type     :field}
                                                                                      {:type     :component
                                                                                       :required true
                                                                                       :name     "TestComponentC"
                                                                                       :ordering
                                                                                                 [{:tag      :c321
                                                                                                   :required false
                                                                                                   :type     :field}
                                                                                                  {:tag      :c322
                                                                                                   :required true
                                                                                                   :type     :field}]}
                                                                                      {:type     :component
                                                                                       :required false
                                                                                       :name     "TestComponentD"
                                                                                       :ordering
                                                                                                 [{:tag      :c331
                                                                                                   :required false
                                                                                                   :type     :field}
                                                                                                  {:tag      :c322
                                                                                                   :required false
                                                                                                   :type     :field}]}
                                                                                      {:tag      :c34
                                                                                       :required true
                                                                                       :type     :field}]}
                                                                          {:tag      :c4
                                                                           :required true
                                                                           :type     :field}]}]}))

    )
  )


;TODO test new group implementation
;TODO the given flat-seq must already have the format: [{:tag :c33 :value "blahblah"}] otherwise we cannot parse "num-in-group"
(deftest new-test
  (testing "Everything is fine"
    (is (matching-seqs? [:a :c :c1 :c2 :c31 :c322 :c34 :c4] {:ordering
                                                          [{:tag      :a
                                                            :required true
                                                            :type     :field}
                                                           {:tag      :b
                                                            :required false
                                                            :type     :field}
                                                           {:tag      :c
                                                            :required true
                                                            :type     :field}
                                                           {:type     :component
                                                            :required true
                                                            :name     "TestComponentA"
                                                            :ordering
                                                                      [{:tag      :c1
                                                                        :required false
                                                                        :type     :field}
                                                                       {:tag      :c2
                                                                        :required true
                                                                        :type     :field}
                                                                       {:type     :component
                                                                        :required true
                                                                        :name     "TestComponentB"
                                                                        :ordering
                                                                                  [{:tag      :c31
                                                                                    :required true
                                                                                    :type     :field}
                                                                                   {:type     :component
                                                                                    :required true
                                                                                    :name     "TestComponentC"
                                                                                    :ordering
                                                                                              [{:tag      :c321
                                                                                                :required false
                                                                                                :type     :field}
                                                                                               {:tag      :c322
                                                                                                :required true
                                                                                                :type     :field}]}
                                                                                   {:type     :component
                                                                                    :required false
                                                                                    :name     "TestComponentD"
                                                                                    :ordering
                                                                                              [{:tag      :c331
                                                                                                :required false
                                                                                                :type     :field}
                                                                                               {:tag      :c322
                                                                                                :required false
                                                                                                :type     :field}]}
                                                                                   {:tag      :c34
                                                                                    :required true
                                                                                    :type     :field}]}
                                                                       {:tag      :c4
                                                                        :required true
                                                                        :type     :field}]}]}))

    ))


;(nested-component-spec-test)
;(run-tests)
(new-test)