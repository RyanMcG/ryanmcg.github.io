(ns incise.transformers.impl.ryanmcg-layout
  (:require (incise.transformers [layout :refer [repartial use-layout
                                                 deflayout defpartial]]
                                 [core :refer [register]])
            [incise.transformers.impl.vm-layout :as vm-layout]
            [incise.transformers.impl.base-layout :as base-layout]
            [incise.parsers.html :refer [Parse->path]]
            [stefon.core :refer [link-to-asset]]
            [clojure.string :as s]
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

(defpartial footer
  "A footer parital with a Creative Commons license attached."
  [{:keys [contacts author]} parse]
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
    [:p#credit "This website was "
     (link-to "https://github.com/RyanMcG/incise" "incised") "."]]
   (javascript-tag vm-layout/analytics-code)])

(defpartial header
  "Add nav to header"
  [{:keys [site-title]} _ _]
  [:header
   [:div.wrapper
    [:div#logo]
    [:h1#site-title (link-to "/" site-title)]
    [:ul#main-navigation.navigation]]
   [:div.clearfix]])

(defpartial stylesheets [_ _ [stylesheets]]
  (conj (pop stylesheets) (link-to-asset "stylesheets/ryanmcg.css.stefon")))

(deflayout ryanmcg []
  (repartial vm-layout/stylesheets stylesheets)
  (repartial vm-layout/header header)
  (repartial vm-layout/footer footer)
  (use-layout vm-layout/vm))

(register :ryanmcg-layout ryanmcg)
