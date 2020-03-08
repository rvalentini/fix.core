(ns fix.generator.component-generator
  (:require [fix.definitions.fields :as f]
            [clojure.pprint :refer [pprint]]
            [fix.parser.xml-parser :as parser])
  (:import (java.util NoSuchElementException)))


(defn- get-field-tag-by-name [field-name]
  (let [matches (->> f/fields
                     (filter (fn [[_ v]] (= (:name v) field-name)))
                     (map first))]
    (if (nil? matches)
      (throw (NoSuchElementException. (str "Field " field-name " does not exist!")))
      (first matches))))

(defn- get-component-by-name [components name]
  (let [matches (filter #(= (get-in % [:attrs :name]) name) components)]
    (if (nil? matches)
      (throw (NoSuchElementException. (str "Component " name " does not exist!")))
      (first matches))))

(defn- assert-component [component]
  {:pre [(= (:tag component) :component)]}
  component)

(defn- assert-empty-content [content]
  {:pre [(empty? content)]}
  content)

(defn- spit-to-file [component-map]
  (let [header '(ns fix.definitions.components)
        var `(def ~'components ~component-map)]
    (spit "src/fix/definitions/components.clj" header)
    (spit "src/fix/definitions/components.clj" "\n\n" :append true)
    (spit "src/fix/definitions/components.clj" var :append true)))

(defn- char->boolean [char]
  (case char
    "N" false
    "Y" true
    (throw (IllegalArgumentException. (str "Given char is neither 'N' nor 'Y': " char)))))

(defn- build-field [field-attrs field-content]
  (assert-empty-content field-content)
  (let [field-tag (get-field-tag-by-name (:name field-attrs))
        required (char->boolean (:required field-attrs))]
    {field-tag {:required required}}))

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
                    elem-tag (get-field-tag-by-name (:name attrs))]
                (case elem-type
                  :field elem-tag
                  :component (extract-ordering (:content (get-component-by-name all-components elem-name)) all-components)
                  :group [(keyword elem-tag) (extract-ordering elem-content all-components)]
                  (throw (IllegalArgumentException. (str "Wrong input: component contains unknown type: " elem-type)))))))
       flatten-vec-of-vec))

(defn- extract-definitions [content all-components]
  (->> content
       (map (fn [elem]
              (let [attrs (:attrs elem)
                    elem-type (:tag elem)
                    elem-name (:name attrs)
                    elem-content (:content elem)
                    elem-tag (get-field-tag-by-name (:name attrs))]
                (case elem-type
                  :field (build-field attrs elem-content)
                  :component {(keyword (str elem-name "-block"))
                              (extract-definitions (:content (get-component-by-name all-components elem-name)) all-components)}
                  :group {(keyword elem-tag)                         {:required (char->boolean (:required attrs))}
                          (keyword (subs (str elem-tag "-group") 1)) (extract-definitions elem-content all-components)}
                  (throw (IllegalArgumentException. (str "Wrong input: component contains unknown type: " elem-type)))))))
       (reduce merge {})))


(defn- generate-source-file [components]
  (println (str "Number of components received: " (count components)))
  (let [gen-components (map
                            (fn [component]
                              (assert-component component)
                              (println " ------------------ NEW COMPONENT ------------------")
                              (let [name (get-in component [:attrs :name])
                                    definitions (extract-definitions (:content component) components)
                                    ordering (extract-ordering (:content component) components)]
                                (println (str "Name: " name " and Length: " (count definitions)))
                                (pprint ordering)
                                {(keyword name) {:ordering ordering
                                                 :definitions definitions}}))
                            components)]
    (spit-to-file (apply merge gen-components))))

(defn -main [& _]
  (println "Generating FIX5.0 SP2 COMPONENT sources ... !")
  (let [[_ components] (parser/parse "resources/FIX50SP2.xml")]
    (generate-source-file components)))
