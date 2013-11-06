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

(let [[posts pages] (split-with :date (remove parse-is-index? parses))]
  (html
    [:h1 "Posts"]
    [:ul.posts (map post posts)]
    [:h1 "Pages"]
    [:ul.pages (map post pages)]))
