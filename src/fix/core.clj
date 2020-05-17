(ns fix.core
  (:require [fix.parser.fix-parser :refer [parse]]
            [fix.spec.message-spec :as m]
            [clojure.spec.alpha :as s]))

(defn valid?
  "This function validates the syntactical correctness of [FIX protocol]
  (https://en.wikipedia.org/wiki/Financial_Information_eXchange) messages. The `msg` must be given as a single
  `String` parameter. By default, the SOH control code (ASCII 01) is used as delimiter,
  which separates the individual tag value pairs. As optional second parameter `delimiter`, a custom delimiter
  can be used. The function returns `true` if the given message is syntactically correct, `false` otherwise.
  The function validates the syntactical correctness of header and trailer of the message first. Then the body of the
  message is validated with regard to the particular message type. Although the `msg` is given as a flat sequence of
  tag value pairs, the validation takes into account that the body of the message can potentially represent heavily
  nested structures of `COMPONENTS` and `GROUPS`. E.g. An incorrect `NUMINGROUP` field that indicates the numbers of
  repetitions within a `GROUP` would result in an invalid message, although the individual `GROUP` repetitions might
  very well be syntactically correct. For a more detailed explanation of the validation features see
  [fix.core](https://github.com/rvalentini/fix.core).
  The only supported protocol version is currently FIX 5.0 SP2"
  ([msg] (s/valid? ::m/message (parse msg)))
  ([msg delimiter] (s/valid? ::m/message (parse msg delimiter))))