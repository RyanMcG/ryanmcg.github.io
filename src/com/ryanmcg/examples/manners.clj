(ns com.ryanmcg.examples.manners
  (:require [clojure.repl :refer :all]
            [clojure.test :refer :all]
            [clojure.string :as s]
            [clojure.pprint :as pp]))

(defmacro peachy [& exprs]
  (->> exprs
       (map pr-str)
       (partition 2)
       (map #(s/join " ; → " %))
       (s/join "\n")
       (println))
  `(are [x# y#] (= x# (quote y#)) ~@exprs))

(use 'manners.victorian)

(def even-coach (manner even? "must be even"))
(def div-by-3-coach (manner #(= (mod % 3) 0) "must be divisible by 3"))

(peachy
  (even-coach 2)        ()
  (even-coach 3)        ("must be even")
  (div-by-3-coach 9)    ()
  (div-by-3-coach 1000) ("must be divisible by 3"))

(def even-and-then-db3-coach (manner  even-coach div-by-3-coach))
(def even-and-db3-coach      (manners even-coach div-by-3-coach))

(peachy
  (even-and-then-db3-coach 2) ("must be divisible by 3")
  (even-and-then-db3-coach 3) ("must be even")
  (even-and-then-db3-coach 1) ("must be even")
  (even-and-db3-coach 2)      ("must be divisible by 3")
  (even-and-db3-coach 3)      ("must be even")
  (even-and-db3-coach 1)      ("must be even" "must be divisible by 3"))

(def pred-msg-coach (manner true? "must be true"))
(def stuff-coach (manner (as-coach :errors)))
                                ; A coach
(def no-errors-and-good (manner stuff-coach
                                ; A predicate message pair
                                (comp true? :good) "not good"))

(peachy
  (stuff-coach {:errors []}) ()
  (stuff-coach {:errors ["I have a boo boo"]}) ("I have a boo boo")
  (pred-msg-coach true) ()
  (pred-msg-coach "truthy") ("must be true")
  (no-errors-and-good {:good true :errors []}) ()
  (no-errors-and-good {:good true :errors ["blarg"]}) ("blarg")
  (no-errors-and-good {:good false :errors []}) ("not good")
  (no-errors-and-good {:good false :errors ["wort"]}) ("wort"))

(def a-user {:login "batman"
             :password "password123"
             :password-confirm "password123"
             :doge "D5MGiMCmxkpJkLBhKe9oN7h49X3t81e3kp"
             :address {:street "Wayne Lane"
                       :number 123
                       :zip 98765
                       :city "Gotham"
                       :state "WA"}})

(require '[manners.bellman :refer [at]])

(defn- remove-trailing-? [string]
  (if (= (last string) \?)
    (str string)
    string))

(defn- replace-dash-with-space [string] (s/replace string "-" " "))

(use 'manners.really)
(def string-coach (manner string? "must be a string"))
(def login-coach (manner string-coach
                         (as-coach (really "must have length" >= 6) count)
                         (partial re-find #"^[a-z][a-z0-9]+$")
                         "must be alpha numeric starting with a letter"))
(def user-coach
  (manners
    (at login-coach :login)
    [#(= (get % :password) (get % :password-confirm))
     "password must equal password confirmation"
     (at (manner string-coach
                (as-coach (really "must have length" >= 6) count))
        :password)]))

(peachy
  (user-coach a-user) ()
  (user-coach (assoc a-user :login "1ssj")) ("login must have length >= 6")
  (user-coach (assoc a-user :login "1ssessj")) ("login must be alpha numeric starting with a letter")
  (user-coach (assoc a-user :password-confirm "ssj")) ("password must equal password confirmation")
  (user-coach (assoc a-user :login "abcdef2")) ())

(defn dogecoin-address? [address]
  (boolean (re-find #"^D{1}[5-9A-HJ-NP-U]{1}[1-9A-HJ-NP-Za-km-z]{32}$" address)))

(peachy
  (dogecoin-address? (a-user :doge)) true)

(def user-with-doge-coach
  (manners user-coach
           (at (really "must be a valid" dogecoin-address?) :doge)))

(peachy
  (user-with-doge-coach a-user) ()
  (user-with-doge-coach (assoc a-user :doge "1")) ("doge must be a valid dogecoin address"))

(defmannerisms user user-coach)
