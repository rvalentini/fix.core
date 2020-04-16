(ns fix.message-spec
  (:require [clojure.spec.alpha :as spec]
            [fix.definitions.components :as c]
            [fix.component-spec :as s]))


(defn- extract-header [seq]
  (take-while #(% (:definition (:Header c/components))) seq))

(defn- validate-header [seq]
  (let [header (extract-header seq)]
    (if (empty? header)
      (throw (IllegalArgumentException. "Cannot extract any header information from message!"))
      (if (spec/valid? ::s/component [header :Header])
        ;TODO extract all relevant fields for further processing e.g. msg-type
        (let [relevant-tags (filter #((:tag %) #{ :ALL-RELEVANT-TAGS}) header)
              relevant-tags-as-map (reduce #(assoc %1 (:tag %2) (:value %2)) {} relevant-tags)])
        ;TODO use threading
        )

      )
    )
  )


(defn- validate-trailer [seq]
  ;TODO extract trailer and validate (only grab last 3 tags?)
  )

(defn- is-message? [seq]



  )

(spec/def ::message  is-message?)
