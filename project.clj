(defproject morn-repl "0.1.0-SNAPSHOT"
  :description "REPL for morn"
  :url "https://github.com/henm/morn-repl"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.cache "0.7.1"]
                 [de.henm/morn.core "0.1-SNAPSHOT"]]
  :repositories { "local" "~/.m2/repository/"}
  :main ^:skip-aot morn-repl.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
