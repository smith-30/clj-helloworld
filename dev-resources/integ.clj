(ns integ
  (:require [integrant.core :as ig]
            [ring.util.response :as response]
            [ring.adapter.jetty :as jetty]))

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

(ig/init {:my/y 2
          :my/hoge 0
          :my/z {:x (ig/ref :my/hoge)
                 :y (ig/ref :my/y)}})

(defn hello-world [_]
  (response/response "Hello, World!"))

(defmethod ig/init-key ::app [_ _]
  hello-world)

(defmethod ig/init-key ::server [_ {:keys [app options]}]
  (jetty/run-jetty app options))

(defmethod ig/halt-key! ::server [_ server]
  (.stop server))

(def config
  {::app {}
   ::server {:app (ig/ref ::app)
             :options {:port 3000
                       :join? false}}})

(defn -main [& _]
  (-> config
      ig/prep
      ig/init))