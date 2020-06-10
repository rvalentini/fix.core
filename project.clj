(defproject org.clojars.rival/fix.core "1.0.0"
  :description "Financial Information eXchange (FIX) protocol validator using clojure.spec"
  :url "https://github.com/rvalentini/fix.core"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v20.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [com.taoensso/timbre "4.10.0"]]
  :target-path "target/%s"
  :profiles {:gen-fields     {:main ^:skip-aot fix.generator.field-generator}
             :gen-components {:main ^:skip-aot fix.generator.component-generator}
             :gen-messages {:main ^:skip-aot fix.generator.message-generator}}
  :aliases {"gen-fields" ["with-profile" "gen-fields" "run"]
            "gen-components" ["with-profile" "gen-components" "run"]
            "gen-messages" ["with-profile" "gen-messages" "run"]
            "generate-definitions" ["do" "gen-fields," "gen-components," "gen-messages"]})
