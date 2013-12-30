{:path "index.html"}

(require '(hiccup [core :refer [html h]]
                  [element :refer [link-to]])
         '(clj-time [format :refer [unparse formatter]]
                    [coerce :refer [from-date]])
         '[incise.parsers.html :refer [Parse->path]])

(defn mformat [date]
  (unparse (formatter "MMM dd, yyyy")
           (from-date date)))

(defn post [{:keys [title date] :as parse}]
  (let [path (Parse->path parse)]
    [:li
     [:span.post-title (link-to path title)] " "
     [:span.date (mformat date)]]))

(html
  [:ul.posts (->> parses
                  (filter :date)
                  (sort-by :date)
                  (reverse)
                  (map post))])
