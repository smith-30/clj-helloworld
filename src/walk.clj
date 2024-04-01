(ns walk
  (:require [next.jdbc :as jdbc]
            [walkable.core :as walkable]
            [next.jdbc.result-set :as rs]
            [com.wsscode.pathom.core :as p]
            [com.wsscode.pathom.connect :as pc]))

(def db {:dbtype "mysql" :dbname "isucon" :user "root" :password "root" :port "12306"})
(def ds (jdbc/get-datasource db))

(def registry
  [{:key :users/list
    :type :root
    :table "users"
    :output [:users/id :users/email]}])

(def query-env #(jdbc/execute! (:db %1) %2 {:builder-fn rs/as-unqualified-lower-maps}))

(def walkable-parser
  (p/parser
   {::p/env {::p/reader [p/map-reader
                         pc/reader3
                         pc/open-ident-reader
                         p/env-placeholder-reader]}
    ::p/plugins [(pc/connect-plugin {::pc/register []})
                 (walkable/connect-plugin {:db-type :mysql
                                           :registry registry
                                           :query-env query-env})
                 p/elide-special-outputs-plugin
                 p/error-handler-plugin]}))

(walkable-parser
 {:db ds}
 [{:users/list [:users/id :users/email]}]) 