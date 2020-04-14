(ns fix.generator.component-generator
  (:require [clojure.pprint :refer [pprint]]
            [fix.parser.xml-parser :as parser]
            [fix.generator.commons :as c]))

(defn- assert-component [component]
  {:pre [(= (:tag component) :component)]}
  component)

(defn- spit-to-file [component-map]
  (let [header '(ns fix.definitions.components)
        var `(def ~'components ~component-map)]
    (spit "src/fix/definitions/components.clj" header)
    (spit "src/fix/definitions/components.clj" "\n\n" :append true)
    (spit "src/fix/definitions/components.clj" var :append true)))

(defn- build-field [field-attrs field-content]
  (c/assert-empty-content field-content)
  (let [field-tag (c/get-field-tag-by-name (:name field-attrs))
        required (c/char->boolean (:required field-attrs))]
    {:tag field-tag
     :required required
     :type :field}))

(defn- flatten-vec-of-vec [arg]
  (if (and (sequential? arg) (= (count arg) 1))
    (first arg)
    (vec arg)))


;TODO rename ordering to content or similar
;TODO check if "definition" can now be deleted completely
(defn- extract-ordering [content all-components]
  (->> content
       (map (fn [elem]
              (let [attrs (:attrs elem)
                    elem-type (:tag elem)
                    elem-name (:name attrs)
                    elem-content (:content elem)
                    elem-tag (c/get-field-tag-by-name (:name attrs))]
                (case elem-type
                  :field (build-field attrs elem-content)
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
                  (throw (IllegalArgumentException. (str "Wrong input: component contains unknown type: " elem-type)))))))
       flatten-vec-of-vec))

;TODO definition is now basically obsolete
(defn- extract-definition [content all-components]
  (->> content
       (map (fn [elem]
              (let [attrs (:attrs elem)
                    elem-type (:tag elem)
                    elem-name (:name attrs)
                    elem-content (:content elem)
                    elem-tag (c/get-field-tag-by-name (:name attrs))]
                (case elem-type
                  :field (build-field attrs elem-content)
                  :component {(keyword elem-name) {:required (c/char->boolean (:required attrs))}
                              (keyword (str elem-name "-block"))
                              (extract-definition (:content (c/get-component-by-name all-components elem-name)) all-components)}
                  :group {(keyword elem-tag)                         {:required (c/char->boolean (:required attrs))}
                          (keyword (subs (str elem-tag "-group") 1)) (extract-definition elem-content all-components)}
                  (throw (IllegalArgumentException. (str "Wrong input: component contains unknown type: " elem-type)))))))
       (reduce merge {})))

(defn- generate-source-file [components]
  (println (str "Number of components found: " (count components)))
  (let [gen-components (map
                            (fn [component]
                              (assert-component component)
                              #_(println " ------------------ NEW COMPONENT ------------------")
                              (let [name (get-in component [:attrs :name])
                                    definition (extract-definition (:content component) components)
                                    ordering (extract-ordering (:content component) components)] ;TODO remove definition -> ordering now contains all information
                                #_(println (str "Name: " name " and Length: " (count definition)))
                                #_(pprint ordering)
                                {(keyword name) {:ordering ordering
                                                 :definition definition}}))
                            components)]
    (spit-to-file (apply merge gen-components))))

(defn -main [& _]
  (println "Generating FIX5.0 SP2 COMPONENT sources ... !")
  (let [[_ components _] (parser/parse "resources/FIX50SP2_FIXT11_combined.xml")]
    (generate-source-file components)))

(-main)
