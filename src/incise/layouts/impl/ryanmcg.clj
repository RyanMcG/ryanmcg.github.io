(ns incise.layouts.impl.ryanmcg
  (:require (incise.layouts [html :refer [repartial use-layout
                                          deflayout defpartial]]
                            [core :refer [register]])
            [robert.hooke :refer [add-hook]]
            (incise.layouts.impl [page :as page-layout]
                                 [base :as base-layout])
            (hiccup [def :refer :all]
                    [util :refer [to-uri]]
                    [page :refer [include-js]]
                    [element :refer :all])))

(defmulti contact (fn [x & _] x))
(defmethod contact :default
  [end-point handle content]
  (let [end-point (name end-point)]
    (link-to {:class "mbtn"
              :title (str handle " on " end-point)}
                  (str "https://" end-point ".com/" handle)
                  content)))
(defmethod contact :email
  [_ email content]
  (mail-to {:class "mbtn"
            :title (str "Email me at " email)}
           email content))

(defhtml contact-spec [[end-point handle classname]]
  (let [classname (or classname (str "fa-" (name end-point)))]
    [:div.contact.col-xs-4
     (contact end-point handle
              [:i {:class (str "fa fa-4x " classname)}])]))

(def ^:private license-url
  "http://creativecommons.org/licenses/by-sa/3.0/deed.en_US")

(defn license-link-to
  ([attr-map content] (link-to (merge {:rel "license"} attr-map)
                               license-url
                               content))
  ([content] (license-link-to nil content)))

(defhtml donate-button []
  (link-to {:class "coinbase-button"
            :data-code "24680873e506998448b8c15fb4f9846e"
            :data-button-style "donation_small"}
           "https://coinbase.com/checkouts/24680873e506998448b8c15fb4f9846e"
           "Donate Bitcoins")
  (include-js "https://coinbase.com/assets/button.js"))

(def analytics-code
  "(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-47124253-1', 'ryanmcg.com');
  ga('send', 'pageview');")

(defpartial footer
  "A footer parital with a Creative Commons license attached."
  [{:keys [contacts author]}]
  [:footer
   [:div#cc.col-md-6
    (license-link-to
      {:id "cc-logo"}
      (image "http://i.creativecommons.org/l/by-sa/3.0/88x31.png"
             "Creative Commons License"))
    [:p#cc-text
     "This website and its content are licensed under the "
     (license-link-to
       "Creative Commons Attribution-ShareAlike 3.0 Unported License")
     " by " author " except where specified otherwise."]]
   [:div#donate.col-md-2 (donate-button)]
   [:div#contacts.col-md-4
    [:div.row (map contact-spec contacts)]]
   [:p#credit "This website was "
    (link-to "https://github.com/RyanMcG/incise" "incised") "."]
   (javascript-tag analytics-code)])

(defpartial header
  "Add nav to header"
  [_ _ [header]]
  (conj header
        [:ul#main-navigation.navigation
         [:li (link-to "/bio/" "Bio")]
         [:li (link-to "/reading/" "Reading")]
         [:li (link-to "/attributions/" "Attributions")]]
        [:div.clearfix]))

(deflayout ryanmcg
  "Stuff"
  []
  (repartial base-layout/javascripts
             #(cons "http://code.jquery.com/jquery-2.0.3.min.js" %))
  (repartial base-layout/stylesheets
             #(cons "//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" %))
  (repartial base-layout/head
             #(conj % [:link {:rel "icon"
                              :type "image/png"
                              :href (to-uri "/assets/images/vm.png")}]))
  (repartial base-layout/footer footer)
  (repartial page-layout/header header)
  (use-layout page-layout/page))

(register [:ryanmcg] ryanmcg)
