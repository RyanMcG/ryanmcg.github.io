{:layout :ryanmcg
 :path "index.html"}

(require '(hiccup [core :refer [html h]]
                  [element :refer [link-to]]))

(defn post [{:keys [title date] :as parse}]
  (let [path (incise.parsers.helpers/Parse->path parse)]
    [:li
     [:span.post-title (link-to path title)] " "
     [:span.date date]]))

(html
  [:ul.posts (map post (filter :date parses))])
