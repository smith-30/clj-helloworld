(defproject helloworld "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring/ring-core "1.12.0"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [ring/ring-devel "1.6.3"]
                 [metosin/reitit "0.7.0-alpha7"]
                 [http-kit "2.7.0"]
                 [metosin/ring-http-response "0.9.3"]
                 [com.stuartsierra/component "1.1.0"]
                 [metosin/muuntaja "0.6.10"]
                 [camel-snake-kebab "0.4.3"]
                 [com.github.seancorfield/next.jdbc "1.3.925"]
                 [com.zaxxer/HikariCP "5.0.1"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [walkable "1.3.0-alpha0"]
                 [prismatic/schema "1.4.1"]
                 [metosin/malli "0.14.0"]
                 [integrant "0.8.1"]]
  :ring {:handler sample/app}
  :plugins [[lein-ring "0.12.5"]]
  :main ^:skip-aot helloworld.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
             :dev {:extra-paths ["test"]
                   :resource-paths ["dev-resources/"]}})
