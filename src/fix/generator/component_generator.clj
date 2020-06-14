(ns fix.generator.component-generator
  (:require [fix.parser.xml-parser :as parser]
            [fix.generator.commons :as c]
            [taoensso.timbre :refer [info]]))

(defn- assert-component [component]
  {:pre [(= (:tag component) :component)]}
  component)

(defn- flatten-vec-of-vec [arg]
  (if (and (sequential? arg) (= (count arg) 1))
    (first arg)
    (vec arg)))

(defn- extract-ordering [content all-components]
  (->> content
       (map (fn [elem]
              (let [attrs (:attrs elem)
                    elem-type (:tag elem)
                    elem-name (:name attrs)
                    elem-content (:content elem)
                    elem-tag (c/get-field-tag-by-name (:name attrs))]
                (case elem-type
                  :field (c/build-field attrs elem-content)
                  :component {:type     :component
                              :required (c/char->boolean (:required attrs))
                              :name     (keyword elem-name)
                              :ordering [(extract-ordering (:content (c/get-component-by-name all-components elem-name)) all-components)]}
                  :group  {:type :group
                           :required (c/char->boolean (:required attrs))
                           :name (keyword elem-name)
                           :ordering [{:tag (keyword elem-tag)
                                       :required (c/char->boolean (:required attrs))
                                       :type :field}
                                      (extract-ordering elem-content all-components)]}
                  (throw (IllegalArgumentException. (str "Wrong input: component contains unknown type:" elem-type)))))))
       flatten-vec-of-vec))

(defn- extract-definition [content all-components]
  (->> content
       (map (fn [elem]
              (let [attrs (:attrs elem)
                    elem-type (:tag elem)
                    elem-name (:name attrs)
                    elem-content (:content elem)
                    elem-tag (c/get-field-tag-by-name (:name attrs))]
                (case elem-type
                  :field (c/get-field-tag-by-name (:name attrs))
                  :component [(extract-definition (:content (c/get-component-by-name all-components elem-name)) all-components)]
                  :group [(keyword elem-tag)
                          (extract-definition elem-content all-components)]
                  (throw (IllegalArgumentException. (str "Wrong input: component contains unknown type:" elem-type)))))))
       flatten))

(defn- generate-source-file [components]
  (info "Number of components found:" (count components))
  (let [gen-components (map
                            (fn [component]
                              (assert-component component)
                              (let [name (get-in component [:attrs :name])
                                    definition (extract-definition (:content component) components)
                                    ordering (extract-ordering (:content component) components)]
                                {(keyword name) {:ordering   ordering
                                                 :definition (into #{} definition)}}))
                            components)]
    (spit "resources/components.edn" (apply merge gen-components))))

(defn -main [& _]
  (info "Generating FIX5.0 SP2 COMPONENT sources ... !")
  (let [[_ components _] (parser/parse "resources/FIX50SP2_FIXT11_combined.xml")]
    (generate-source-file components)))
