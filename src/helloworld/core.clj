(ns helloworld.core
  (:gen-class)
  (:require [clojure.pprint :as pprint]
            [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]))

(defmethod ig/init-key :default
  [_ x]
  (println ":default" 1)
  x)

(defmethod ig/init-key :my/x
  [_ x]
  (* x 10))

(defmethod ig/init-key :my/y
  [_ y]
  (+ y 5))

(defmethod ig/init-key :my/z
  [_ {:keys [x y]}]
  (- x y))

(defn hello-world [_]
  (println "start")
  (Thread/sleep 10000)
  (println "end")
  (response/response "Hello, World!"))

(defmethod ig/init-key ::app [_ _]
  hello-world)

(defmethod ig/halt-key! ::app [_ _]
  (println "halt-app"))


(defn conf
  [jetty]
  (println "aaaaa")
  (.setStopTimeout jetty 10000000)
  (.setStopAtShutdown jetty true))

(defmethod ig/init-key ::server [_ {:keys [app options]}]
  (jetty/run-jetty app options))

(defmethod ig/halt-key! ::server [_ server]
  (println "halt-server")
  (.stop server))

(def config
  {::app {}
   ::server {:app (ig/ref ::app)
             :options {:port 3000
                       :join? false
                       :configurator conf}}})

(defn -main [& _]
  (-> config
      ig/prep
      ig/init))

