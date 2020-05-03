(defproject fix.core "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/tools.logging "1.0.0"]        ;TODO remove
                 [com.taoensso/timbre "4.10.0"]]
  :main ^:skip-aot fix.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :gen-fields     {:main ^:skip-aot fix.generator.field-generator}
             :gen-components {:main ^:skip-aot fix.generator.component-generator}
             :gen-messages {:main ^:skip-aot fix.generator.message-generator}}
  :aliases {"gen-fields" ["with-profile" "gen-fields" "run"]
            "gen-components" ["with-profile" "gen-components" "run"]
            "gen-messages" ["with-profile" "gen-messages" "run"]
            "gen" ["do" "gen-fields," "gen-components," "gen-messages"]})
