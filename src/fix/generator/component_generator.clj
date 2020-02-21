(ns fix.generator.component-generator
  (:require [fix.fields :as f])
  (:import (java.util NoSuchElementException)))

;TODO better make this part of fields ns?
(defn- get-field-tag-for-name [field-name]
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



;TODO make use of :pre
(defn- assert-component [component]
  (when (not= (:tag component) :component)
    (throw (IllegalArgumentException. "Wrong input: called component generator with non-component data!")))
  component)

;TODO make use of :pre
(defn- assert-empty-content [content]
  (when (seq content)
    (throw (AssertionError. (str "Field content should always be empty inside components! Instead found: " content))))
  content)


(defn- spit-to-file [component-map]
  (let [header '(ns fix.components)
        var `(def ~'components ~component-map)]
    (spit "src/fix/components.clj" header)
    (spit "src/fix/components.clj" "\n\n" :append true)
    (spit "src/fix/components.clj" var :append true)))

(defn- char->boolean [char]
  (case char
    "N" false
    "Y" true
    "default" (throw (IllegalArgumentException. (str "Given char is neither 'N' nor 'Y': " char)))))

(defn- build-component [component-attrs component-content all-components]
  (assert-empty-content component-content)
  (println "Found nested component:")
  (println (str (get-component-by-name all-components (:name component-attrs))))

  )

(defn- build-field [field-attrs field-content]
  (assert-empty-content field-content)
  (let [field-tag (get-field-tag-for-name (:name field-attrs))
        required (char->boolean (:required field-attrs))]
    {field-tag {:required required}}))

;TODO SOLUTION: call this function recursively with content vectors for :group and :component
;TODO SOLUTION: break condition for recursion is :field
;TODO SOLUTION: group must be encoded as {:44 {:required false} :44-group {...}}
;TODO SOLUTION: ordering should be persisted next to content -> "two aggregators"
;TODO SOLUTION: component must be encoded as {:nameOfComponent-block {...}
(defn- extract-content [content-vec all-components]
  (println "------------- NEW COMPONENT ----------------")
  (doseq [elem content-vec]
    (case (:tag elem)
      :field (println (build-field (:attrs elem) (:content elem)))
      :component (build-component (:attrs elem) (:content elem) all-components )
      :group ()                       ;TODO implement
      "default" (throw (IllegalArgumentException. (str "Wrong input: component contains unknown tag: " (:tag elem))))
      )
    )
  )

(defn generate-source-file [components]
  (println (str "Number of components received: " (count components)))
  (let [component-entries (map
                            (fn [component]
                              (assert-component component)
                              #_(println component)
                              (let [name (get-in component [:attrs :name])
                                    content (extract-content (:content component) components)])
                              )
                            components)]
    (reduce merge {} component-entries)
    )

  ;TODO create component structure as described in core.clj
  ;TODO write to file
  )
