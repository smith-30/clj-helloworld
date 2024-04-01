(ns sample
  (:require [camel-snake-kebab.core :as csk]
            [clojure.datafy :as d]
            [clojure.pprint :as pprint]
            [muuntaja.core :as muu]
            [muuntaja.middleware :as muu.middleware]
            [next.jdbc :as jdbc]
            [org.httpkit.server :refer [run-server]]
            [reitit.ring :as ring]
            [ring.util.http-response :as res]
            [walkable.core :as walkable]))

(def db {:dbtype "mysql" :dbname "isucon" :user "root" :password "root" :port "12306"})
(def ds (jdbc/get-datasource db))

;; (defn users-todos [users todos]
;;   (for [user users]
;;     (assoc user :todos (filter #(= (:todos/user_id %) (:users/id user)) todos))))

;; (let [users (jdbc/execute! ds ["SELECT id, email FROM users"])
;;       todos (jdbc/execute! ds ["SELECT user_id, title FROM todos"])]
;;   (pprint/pprint (users-todos users todos)))

;; (defn bind-error [f [val err]]
;;   (if (nil? err)
;;     (f val)
;;     [nil err]))

;; (try
;;   (jdbc/with-transaction [tx ds]
;;     (jdbc/execute! tx ["INSERT INTO todos (user_id, title, description, due_date, priority, status) VALUES
;;   ('1', 'a', 'a', '2024-03-20', 2, 'Pending')"])
;;     (jdbc/execute! tx ["INSERT INTO todos (user_id, title, description, due_date, priority, status) VALUES
;;     ('afsadfa1', 'b', 'b', '2024-03-20', 2, 'Pending')"]))
;;   (catch Exception e
;;     (println e)))

;; (let [result (jdbc/with-transaction [tx ds]
;;                (jdbc/execute! tx ["INSERT INTO todos (user_id, title, description, due_date, priority, status) VALUES
;; ('1', 'a', 'a', '2024-03-20', 2, 'Pending')"])
;;                (jdbc/execute! tx ["INSERT INTO todos (user_id, title, description, due_date, priority, status) VALUES
;;   ('afsadfa1', 'b', 'b', '2024-03-20', 2, 'Pending')"]))]
;;   (println "hoge"))


(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello, World!"})


(defn health
  "ヘルスチェックに対応するためのHandlerとして意図しています"
  [_]
  (res/ok "Application is runnig"))

(defn hello [_]
  (res/ok "Hello world"))

(defn list-todo [_]
  (res/ok {:todo-lists (jdbc/execute! ds ["SELECT *
                                    FROM users "] {:schema {:users/id [:todos/title]}})}))

(defn get-todo [_]
  (res/ok "get-todo"))

(defn create-todo [request]
  (println (:params request))
  (res/ok "create-todo"))

(defn delete-todo [_]
  (res/ok "delete-todo"))

(defn update-todo [_]
  (res/ok "update-todo"))

(def ^:private muuntaja-config
  "https://cljdoc.org/d/metosin/muuntaja/0.6.8/doc/configuration"
  (-> muu/default-options
      ;; JSONにencodeする時にキーをcamelCaseにする
      (assoc-in [:formats "application/json" :encoder-opts]
                {:encode-key-fn csk/->camelCaseString})
      ;; JSON以外のacceptでリクエストされたときに返らないように制限する
      (update :formats #(select-keys % ["application/json"]))
      muu/create))

(defn- not-found-handler [_]
  {:status 404
   :headers {"Content-Type" "text/plain; charset=utf-8"}
   :body "not found"})


(def app
  (ring/ring-handler
   (ring/router
    [["/all" handler]
     ["/api" {:middleware [[muu.middleware/wrap-format muuntaja-config]
                           muu.middleware/wrap-params]}
      ["/todo" {:name ::todo
                :get list-todo
                :post create-todo}]
      ["/todo/{todo-id}" {:name ::todo-detail
                          :parameters {:path {:todo-id string?}}
                          :get get-todo
                          :put update-todo
                          :delete delete-todo}]]
     ["/hoge" {:name ::hoge
               :get hello}]])
   (ring/create-default-handler
    {:not-found not-found-handler})))

(defn start-server []
  (run-server app {:port 8080 :join? false}))
