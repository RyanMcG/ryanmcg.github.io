{:layout :ryanmcg
 :path "index.html"}

(require '(hiccup [core :refer [html h]]
                  [element :refer [link-to]]))

(defn truncate [cotent])

(defn post [{:keys [title date] :as parse}]
  (let [path (incise.parsers.helpers/Parse->path parse)]
    [:li
     [:span.post-title (link-to path title)] " "
     [:span.date date]]))

(defn parse-is-index? [parse] (= (:path parse) "index.html"))

(html [:ul.posts (map post (remove parse-is-index? parses))])
