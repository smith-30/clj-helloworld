(ns example (:require [schema.core :as s
                       :include-macros true ;; cljs only
                       ]
                      [malli.core :as m]
                      [malli.experimental :as mx]
                      [malli.clj-kondo :as mc]))

(s/defschema Data
  "A schema for a nested data type"
  {:a {:b s/Str
       :c s/Int}
   :d [{:e s/Keyword
        :f [s/Num]}]})

(s/defschema User
  {:id s/Int
   :email s/Str
   (s/optional-key :first_name) s/Str})

;; (s/validate
;;  User
;;  {:id 1
;;   :email "hoge"
;;   :first_name 1})

(s/defn newUser :- User [param] {:id 1})

(let [user (newUser {:id 1
                     :email "hoge"
                     :first_name "w"})]
  (println user))

(s/defn foo :- s/Num
  [x :- s/Int
   y :- s/Num]
  "1")

(def hoge (foo 1 2))

;; (s/validate
;;  User
;;  {:id 1
;;   :email hoge
;;   :first_name 1})


;; (def Address
;;   [:map
;;    [:street :string]])

;; (m/validate Address {:street "test"})

;; (mx/defn multi :- int
;;   [x :- int
;;    y :- int]
;;   (* x y))

;; (-> (mc/collect *ns*) (mc/linter-config))

;; (defn square [x] (* x x))
;; (m/=> square [:=> [:cat int?] nat-int?])



;; (square 1)

;; (comment
;;   (square "1")
;;   (square "2"))