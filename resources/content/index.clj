{:path "index.html"}

(require '(hiccup [core :refer [html h]]
                  [element :refer [link-to]])
         '(clj-time [format :refer [unparse formatter]]
                    [coerce :refer [from-date]])
         '[incise.parsers.html :refer [Parse->path]])

(defn mformat [date]
  (unparse (formatter "MMM d, yyyy")
           (from-date date)))

(defn post [{:keys [title date] :as parse}]
  (let [path (Parse->path parse)]
    [:li
     [:div.pure-g
      [:div.pure-u-1.pure-u-sm-1-4 [:div.date (mformat date)]]
      [:div.pure-u-1.pure-u-sm-3-4.post-title (link-to path title)]]]))

(html
  [:ul.posts (->> parses
                  (filter :date)
                  (sort-by :date)
                  (reverse)
                  (map post))])
