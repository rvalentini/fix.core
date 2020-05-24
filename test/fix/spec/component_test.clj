(ns fix.spec.component-test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as spec]
            [fix.spec.component-spec :as c :refer [matching-component?]]))

(deftest simple-component-spec-test
  (testing "Basic sequence matching logic for field only components is working correctly"
    (is (matching-component? [{:tag :1, :value "some-value"}]
                             {:ordering [{:tag      :1
                                          :required true
                                          :type     :field}]}
                             true false false))
    (is (matching-component? [{:tag :1, :value "some-value"}]
                             {:ordering [{:tag      :1
                                          :required true
                                          :type     :field}]}
                             true false false))
    (is (not (matching-component? [{:tag :1, :value "some-value"}]
                                  {:ordering [{:tag      :2
                                               :required false
                                               :type     :field}]}
                                  true false false)))
    (is (not (matching-component? [{:tag :1, :value "some-value"}]
                                  {:ordering [{:tag      :2
                                               :required true
                                               :type     :field}]}
                                  true false false)))
    (is (matching-component? [] {:ordering []} true false false))
    (is (not (matching-component? [{:tag :x, :value "some-value"}] {:ordering []} true false false)))
    (is (not (matching-component? [{:tag :1, :value "some-value"}
                                   {:tag :3, :value "some-value"}]
                                  {:ordering [{:tag      :x
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
                                               :type     :field}]}
                                  true false false)))
    (is (matching-component? [{:tag :1, :value "some-value"}
                              {:tag :3, :value "some-value"}]
                             {:ordering [{:tag      :x
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
                                          :type     :field}]}
                             true false false))
    )

  (testing "Tags that are not contained in the definition/seq will fail the matching"
    (is (not (matching-component? [{:tag :1, :value "some-value"}
                                   {:tag :2, :value "some-value"}
                                   {:tag :3, :value "some-value"}
                                   {:tag :4, :value "some-value"}]
                                  {:ordering [{:tag      :1
                                               :required true
                                               :type     :field}
                                              {:tag      :2
                                               :required true
                                               :type     :field}
                                              {:tag      :3
                                               :required true
                                               :type     :field}]}
                                  true false false)))
    (is (not (matching-component? [{:tag :X, :value "some-value"}]
                                  {:ordering [{:tag      :1
                                               :required true
                                               :type     :field}
                                              {:tag      :2
                                               :required true
                                               :type     :field}
                                              {:tag      :3
                                               :required true
                                               :type     :field}]}
                                  true false false)))
    (is (not (matching-component? [{:tag :X, :value "some-value"}] {:ordering []} true false false))))


  (testing "Potentially successful matching for given tag sequence will still fail, when there are mandatory tags missing"
    (is (matching-component? [] {:ordering [{:tag      :b
                                             :required false
                                             :type     :field}]}
                             true false false))
    (is (not (matching-component? [] {:ordering [{:tag      :b
                                                  :required true
                                                  :type     :field}]}
                                  true false false)))
    (is (not (matching-component? [{:tag :a, :value "some-value"}
                                   {:tag :b, :value "some-value"}
                                   {:tag :c, :value "some-value"}]
                                  {:ordering [{:tag      :a
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
                                               :type     :field}]}
                                  true false false)))
    (is (matching-component? [{:tag :a, :value "some-value"}
                              {:tag :b, :value "some-value"}
                              {:tag :c, :value "some-value"}]
                             {:ordering [{:tag      :a
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
                                          :type     :field}]}
                             true false false))))


(deftest nested-component-spec-test
  (testing "Complex structures containing nested components are matched correctly for required and non-required fields"
    (is (matching-component? [{:tag :a, :value "some-value"}
                              {:tag :b, :value "some-value"}
                              {:tag :c, :value "some-value"}
                              {:tag :c1, :value "some-value"}
                              {:tag :c2, :value "some-value"}
                              {:tag :c3, :value "some-value"}]

                             {:ordering
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
                                            :type     :field}]}]}

                             true false false))

    (is (matching-component? [{:tag :a, :value "some-value"}
                              {:tag :b, :value "some-value"}
                              {:tag :c, :value "some-value"}
                              {:tag :c11, :value "some-value"}
                              {:tag :c12, :value "some-value"}
                              {:tag :c13, :value "some-value"}
                              {:tag :c21, :value "some-value"}
                              {:tag :c22, :value "some-value"}
                              {:tag :c23, :value "some-value"}]

                             {:ordering
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
                                            :type     :field}]}]}

                             true false false))

    (is (matching-component? [{:tag :a, :value "some-value"}
                              {:tag :b, :value "some-value"}
                              {:tag :c, :value "some-value"}
                              {:tag :c1, :value "some-value"}
                              {:tag :c2, :value "some-value"}
                              {:tag :c31, :value "some-value"}
                              {:tag :c321, :value "some-value"}
                              {:tag :c322, :value "some-value"}
                              {:tag :c33, :value "some-value"}
                              {:tag :c4, :value "some-value"}]

                             {:ordering
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
                                            :type     :field}]}]}

                             true false false))

    (is (not (matching-component? [{:tag :a, :value "some-value"}
                                   {:tag :b, :value "some-value"}
                                   {:tag :c, :value "some-value"}
                                   {:tag :c1, :value "some-value"}
                                   {:tag :c2, :value "some-value"}
                                   {:tag :c31, :value "some-value"}
                                   {:tag :c321, :value "some-value"}
                                   {:tag :wrong-tag, :value "some-value"}
                                   {:tag :c322, :value "some-value"}
                                   {:tag :c33, :value "some-value"}
                                   {:tag :c4, :value "some-value"}]

                                  {:ordering
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
                                                 :type     :field}]}]}

                                  true false false)))

    (is (matching-component? [{:tag :a, :value "some-value"}
                              {:tag :c, :value "some-value"}
                              {:tag :c1, :value "some-value"}
                              {:tag :c2, :value "some-value"}
                              {:tag :c31, :value "some-value"}
                              {:tag :c322, :value "some-value"}
                              {:tag :c34, :value "some-value"}
                              {:tag :c4, :value "some-value"}]

                             {:ordering
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
                                            :type     :field}]}]}

                             true false false))))


(deftest basic-group-spec-test
  (testing "Num-in-group repetitions are correctly applied and simple groups, which only contain fields are matched correctly"
    (is (matching-component? [{:tag :146 :value 1}
                              {:tag :63 :value "some-value"}
                              {:tag :1617 :value "some-value"}
                              {:tag :1500 :value "some-value"}
                              {:tag :1502 :value "some-value"}]
                             {:ordering
                              [{:type     :group,
                                :required true,
                                :name     "TestGroupA",
                                :ordering
                                          [{:tag :146, :required true, :type :field}
                                           [{:tag :63, :required true, :type :field}
                                            {:tag :1617, :required true, :type :field}
                                            {:tag :1500, :required true, :type :field}
                                            {:tag :1502, :required true, :type :field}]]}]}
                             true false false))

    (is (matching-component? [{:tag :146 :value 2}
                              {:tag :63 :value "some-value"}
                              {:tag :1617 :value "some-value"}
                              {:tag :63 :value "some-value"}
                              {:tag :1617 :value "some-value"}]
                             {:ordering
                              [{:type     :group,
                                :required true,
                                :name     "TestGroupA",
                                :ordering
                                          [{:tag :146, :required true, :type :field}
                                           [{:tag :63, :required true, :type :field}
                                            {:tag :1617, :required true, :type :field}
                                            ]]}]}
                             true false false))

    (is (not (matching-component? [{:tag :146 :value 2}
                                   {:tag :63 :value "some-value"}
                                   {:tag :1617 :value "some-value"}
                                   {:tag :63 :value "some-value"}
                                   {:tag :534 :value "this-does-not-fit"}]
                                  {:ordering
                                   [{:type     :group,
                                     :required true,
                                     :name     "TestGroupA",
                                     :ordering
                                               [{:tag :146, :required true, :type :field}
                                                [{:tag :63, :required true, :type :field}
                                                 {:tag :1617, :required true, :type :field}
                                                 ]]}]}
                                  true false false)))

    ;146 is NUMINGROUP which must be pos-int, so 0 is not valid
    (is (not (matching-component? [{:tag :146 :value 0}]
                                  {:ordering [{:type     :group,
                                               :required true,
                                               :name     "TestGroupA",
                                               :ordering
                                                         [{:tag :146, :required true, :type :field}
                                                          [{:tag :63, :required true, :type :field}
                                                           {:tag :1617, :required true, :type :field}
                                                           ]]}]}
                                  true false false)))))


(deftest complex-nested-group-spec-test
  (testing "Groups containing nested components, which also contain groups a matched correctly"


    (is (matching-component? [{:tag :889 :value "some-value"}
                              {:tag :890 :value "some-value"}
                              ;TestGroupA
                              {:tag :146 :value 2}
                              ;TestGroupA - 1st repetition
                              {:tag :453 :value "some-value"}
                              {:tag :c1 :value "some-value"}
                              ;TestGroupB
                              {:tag :111 :value 3}
                              ;TestGroupB - 1st repetition
                              {:tag :22 :value "some-value"}
                              {:tag :23 :value "some-value"}
                              ;TestGroupB - 2nd repetition
                              {:tag :22 :value "some-value"}
                              {:tag :23 :value "some-value"}
                              ;TestGroupB - 3rd repetition
                              {:tag :22 :value "some-value"}
                              {:tag :23 :value "some-value"}
                              {:tag :c2 :value "some-value"}
                              {:tag :542 :value "some-value"}
                              ;TestGroupA - 2nd repetition
                              {:tag :453 :value "some-value"}
                              {:tag :c1 :value "some-value"}
                              ;TestGroupB
                              {:tag :111 :value 2}
                              ;TestGroupB - 1st repetition
                              {:tag :22 :value "some-value"}
                              {:tag :23 :value "some-value"}
                              ;TestGroupB - 2nd repetition
                              {:tag :22 :value "some-value"}
                              {:tag :23 :value "some-value"}
                              {:tag :c2 :value "some-value"}
                              {:tag :542 :value "some-value"}
                              {:tag :891 :value "some-value"}]

                             {:ordering
                              [{:tag :889, :required true, :type :field}
                               {:tag :890, :required true, :type :field}
                               {:type     :group,
                                :required true,
                                :name     "TestGroupA",
                                :ordering
                                          [{:tag :146, :required true, :type :field}
                                           [{:tag :453, :required true, :type :field}
                                            {:type     :component
                                             :required true
                                             :name     "TestComponentA"
                                             :ordering
                                                       [{:tag      :c1
                                                         :required true
                                                         :type     :field}
                                                        {:type     :group,
                                                         :required true,
                                                         :name     "TestGroupB",
                                                         :ordering
                                                                   [{:tag :111, :required true, :type :field}
                                                                    [{:tag :22, :required true, :type :field}
                                                                     {:tag :23, :required true, :type :field}]]}
                                                        {:tag      :c2
                                                         :required true
                                                         :type     :field}]}
                                            {:tag :542, :required true, :type :field}]]}
                               {:tag :891, :required true, :type :field}]}

                             true false false))

    (is (matching-component? [{:tag :889 :value "some-value"}
                              {:tag :890 :value "some-value"}
                              ;TestGroupA
                              {:tag :146 :value 2}
                              ;TestGroupA - 1st repetition
                              {:tag :453 :value "some-value"}
                              ;TestGroupB
                              {:tag :111 :value 3}
                              ;TestGroupB - 1st repetition
                              {:tag :23 :value "some-value"}
                              ;TestGroupB - 2nd repetition
                              {:tag :22 :value "some-value"}
                              {:tag :23 :value "some-value"}
                              ;TestGroupB - 3rd repetition
                              {:tag :23 :value "some-value"}
                              {:tag :c2 :value "some-value"}
                              {:tag :542 :value "some-value"}
                              ;TestGroupA - 2nd repetition
                              {:tag :453 :value "some-value"}
                              {:tag :c1 :value "some-value"}
                              ;TestGroupB
                              {:tag :111 :value 2}
                              ;TestGroupB - 1st repetition
                              {:tag :22 :value "some-value"}
                              {:tag :23 :value "some-value"}
                              ;TestGroupB - 2nd repetition
                              {:tag :23 :value "some-value"}
                              {:tag :c2 :value "some-value"}
                              {:tag :542 :value "some-value"}
                              {:tag :891 :value "some-value"}]

                             {:ordering
                              [{:tag :889, :required true, :type :field}
                               {:tag :890, :required true, :type :field}
                               {:type     :group,
                                :required true,
                                :name     "TestGroupA",
                                :ordering
                                          [{:tag :146, :required true, :type :field}
                                           [{:tag :453, :required true, :type :field}
                                            {:type     :component
                                             :required true
                                             :name     "TestComponentA"
                                             :ordering
                                                       [{:tag      :c1
                                                         :required false ;NOT REQUIRED anymore
                                                         :type     :field}
                                                        {:type     :group,
                                                         :required true,
                                                         :name     "TestGroupB",
                                                         :ordering
                                                                   [{:tag :111, :required true, :type :field}
                                                                    [{:tag :22, :required false, :type :field} ;NOT REQUIRED anymore
                                                                     {:tag :23, :required true, :type :field}]]}
                                                        {:tag      :c2
                                                         :required true
                                                         :type     :field}]}
                                            {:tag :542, :required true, :type :field}]]}
                               {:tag :891, :required true, :type :field}]}

                             true false false))

    (is (not (matching-component? [{:tag :889 :value "some-value"}
                                   {:tag :890 :value "some-value"}
                                   ;TestGroupA
                                   {:tag :146 :value 2}
                                   ;TestGroupA - 1st repetition
                                   {:tag :453 :value "some-value"}
                                   ;TestGroupB
                                   {:tag :111 :value 3}
                                   ;TestGroupB - 1st repetition
                                   {:tag :23 :value "some-value"}
                                   ;TestGroupB - 2nd repetition
                                   {:tag :22 :value "some-value"}
                                   {:tag :23 :value "some-value"}
                                   ;TestGroupB - 3rd repetition
                                   {:tag :23 :value "some-value"}
                                   {:tag :c2 :value "some-value"}
                                   {:tag :542 :value "some-value"}
                                   ;TestGroupA - 2nd repetition
                                   {:tag :453 :value "some-value"}
                                   {:tag :c1 :value "some-value"}
                                   ;TestGroupB
                                   {:tag :111 :value 1}     ;FAIL -> single repetition defined but two given
                                   ;TestGroupB - 1st repetition
                                   {:tag :22 :value "some-value"}
                                   {:tag :23 :value "some-value"}
                                   ;TestGroupB - 2nd repetition
                                   {:tag :23 :value "some-value"}
                                   {:tag :c2 :value "some-value"}
                                   {:tag :542 :value "some-value"}
                                   {:tag :891 :value "some-value"}]

                                  {:ordering
                                   [{:tag :889, :required true, :type :field}
                                    {:tag :890, :required true, :type :field}
                                    {:type     :group,
                                     :required true,
                                     :name     "TestGroupA",
                                     :ordering
                                               [{:tag :146, :required true, :type :field}
                                                [{:tag :453, :required true, :type :field}
                                                 {:type     :component
                                                  :required true
                                                  :name     "TestComponentA"
                                                  :ordering
                                                            [{:tag      :c1
                                                              :required false ;NOT REQUIRED anymore
                                                              :type     :field}
                                                             {:type     :group,
                                                              :required true,
                                                              :name     "TestGroupB",
                                                              :ordering
                                                                        [{:tag :111, :required true, :type :field}
                                                                         [{:tag :22, :required false, :type :field} ;NOT REQUIRED anymore
                                                                          {:tag :23, :required true, :type :field}]]}
                                                             {:tag      :c2
                                                              :required true
                                                              :type     :field}]}
                                                 {:tag :542, :required true, :type :field}]]}
                                    {:tag :891, :required true, :type :field}]}

                                  true false false)))))

(deftest component-spec-facade
  (testing "Invoking the matching logic via the component spec/def is working as intended"
    (is (spec/valid? ::c/component
                     [[{:tag :8 :value "FIX5.0"}
                       {:tag :9 :value "111"}
                       {:tag :35 :value "AE"}
                       {:tag :49 :value "my-comp-id"}
                       {:tag :56 :value "your-comp-id"}
                       {:tag :34 :value "4"}
                       {:tag :143 :value "some-target-location-id"}
                       {:tag :52 :value "20190202-01:00:00"}
                       {:tag :347 :value "some-very-fancy-encoding"}]
                      :Header]))))