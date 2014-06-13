(ns incise.transformers.impl.ryanmcg-layout
  (:require (incise.transformers [layout :refer [repartial use-layout
                                                 deflayout defpartial]]
                                 [core :refer [register]])
            [incise.transformers.impl.vm-layout :as vm-layout]
            [stefon.core :refer [link-to-asset]]
            (hiccup [def :refer :all]
                    [element :refer :all])))

(defmulti contact (fn [x & _] x))
(defmethod contact :default
  [end-point handle content]
  (let [end-point (name end-point)]
    (link-to {:title (str handle " on " end-point)}
             (str "https://" end-point ".com/" handle)
             content)))

(defmethod contact :email
  [_ email content]
  (mail-to {:title (str "Email me at " email)}
           email content))

(defhtml contact-spec [[end-point handle classname]]
  (let [classname (or classname (str "fa-" (name end-point)))]
    [:div.contact.pure-u-1-3
     (contact end-point handle
              [:i {:class (str "fa fa-4x " classname)}])]))

(def ^:private license-url
  "http://creativecommons.org/licenses/by-sa/3.0/deed.en_US")

(defn license-link-to
  ([attr-map content] (link-to (merge {:rel "license"} attr-map)
                               license-url
                               content))
  ([content] (license-link-to nil content)))

(def ^:private my-btc-addr "1BgW7o3GfsuNQu3eMyEV7oM58YvUZBv4VV")
(def ^:private kcc-btc-addr "13UUaGK8ZDLxjY7RYu2bKEabqjww2KDyxD")
(defhtml feeling-generous []
  [:div#feeling-generous
   [:h6 "Do you like what you read? I accept tips."]
   [:p "Send some BTC to " [:code my-btc-addr]]
   [:p.or "OR"]
   [:p "Donate to "
    (link-to "http://iccf-holland.org/" "Kibaale Children's Centre")
    " @ " [:a {:href "http://iccf-holland.org/bitcoin.html"
               :title "Trust but verify"}
           [:code kcc-btc-addr]]]])

(defpartial footer
  "A footer parital with a Creative Commons license attached."
  [{:keys [contacts author]}]
  [:footer
   [:div.content
    [:div#cc
     [:p#cc-text
      "This website and its content are licensed under the "
      (license-link-to
        "Creative Commons Attribution-ShareAlike 3.0 Unported License")
      " by " author " except where specified otherwise."]]
    [:div#contacts
     [:div.pure-g (map contact-spec contacts)]]
    [:div#donate (feeling-generous)]
    [:p#credit "This website was "
     (link-to "https://github.com/RyanMcG/incise" "incised") "."]]
   (javascript-tag vm-layout/analytics-code)])

(defpartial header
  "Add nav to header"
  [{:keys [site-title]} _ _]
  [:header [:h1#site-title (link-to "/" site-title)]
   [:ul#main-navigation.navigation
    [:li (link-to "/bio/" "Bio")]
    [:li (link-to "/reading/" "Reading")]
    [:li (link-to "/attributions/" "Attributions")]]
   [:div.clearfix]])

(defpartial stylesheets [_ _ [stylesheets]]
  (conj (pop stylesheets) (link-to-asset "stylesheets/ryanmcg.css.stefon")))

(deflayout ryanmcg []
  (repartial vm-layout/stylesheets stylesheets)
  (repartial vm-layout/header header)
  (repartial vm-layout/footer footer)
  (use-layout vm-layout/vm))

(register :ryanmcg-layout ryanmcg)
