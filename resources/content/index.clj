{:path "index.html"}

(require '(hiccup [core :refer [html h]]
                  [element :refer [link-to]])
         '[incise.parsers.html :refer [Parse->path]])

(defn post [{:keys [title date] :as parse}]
  (let [path (Parse->path parse)]
    [:li
     [:span.post-title (link-to path title)] " "
     [:span.date date]]))

(html
  [:ul.posts (->> parses
                  (filter :date)
                  (sort-by :date)
                  (reverse)
                  (map post))])
