(ns fix.generator.commons
  (:require [fix.definitions.fields :as f])
  (:import (java.util NoSuchElementException)))

(defn assert-empty-content [content]
  {:pre [(empty? content)]}
  content)

(defn char->boolean [char]
  (case char
    "N" false
    "Y" true
    (throw (IllegalArgumentException. (str "Given char is neither 'N' nor 'Y':" char)))))

(defn get-component-by-name [components name]
  (let [matches (filter #(= (get-in % [:attrs :name]) name) components)]
    (if (nil? matches)
      (throw (NoSuchElementException. (str "Component" name "does not exist!")))
      (first matches))))

(defn get-field-tag-by-name [field-name]
  (let [matches (->> f/fields
                     (filter (fn [[_ v]] (= (:name v) field-name)))
                     (map first))]
    (if (nil? matches)
      (throw (NoSuchElementException. (str "Field" field-name "does not exist!")))
      (first matches))))


