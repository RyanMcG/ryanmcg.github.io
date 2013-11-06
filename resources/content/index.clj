{:layout :ryanmcg
 :path "index.html"}

(require '[hiccup.core :refer [html h]])
(html
  [:h1 (h "Yeah")]
  [:pre (h (keys tags))]
  [:h2 (str "more")])
