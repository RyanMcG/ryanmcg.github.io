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

(defn- disqus-setting-var [var-name value]
  {:pre [(keyword? var-name) (string? value)]}
  (str "var disqus_" (name var-name) " = '" (s/escape value {\' "\\'"}) "';"))

(defn disqus-script-tag [{:keys [shortname identifier title url] :as settings}]
  {:pre [(string? shortname)
         (every? #{:shortname :identifier :title :url}
                 (keys settings))
         (every? (some-fn nil? string?) [identifier title url])]}
  (javascript-tag
    (str
      "/* * * CONFIGURATION VARIABLES * * */\n"
      (->> settings
           (remove (comp nil? val))
           (map (partial apply disqus-setting-var))
           (s/join "\n"))
      "\n\n/* * * DON'T EDIT BELOW THIS LINE * * */
(function() {
  var dsq = document.createElement('script');
  dsq.type = 'text/javascript';
  dsq.async = true;
  dsq.src = '//' + disqus_shortname + '.disqus.com/embed.js';
  (document.getElementsByTagName('head')[0] ||
   document.getElementsByTagName('body')[0]).appendChild(dsq);
})();")))

(defn- show-disqus-comments? [conf parse]
  (and (map? conf) (conf :shortname) (:date parse)))

(defn- disqus-comments [conf parse]
  {:pre [((some-fn nil? map?) conf)]}
  (if (show-disqus-comments? conf parse)
    (disqus-script-tag {:shortname (conf :shortname)
                        :title (:title parse)
                        :identifier (Parse->path parse)})))

(defpartial footer
  "A footer parital with a Creative Commons license attached."
  [{:keys [contacts author disqus]} parse]
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
  [:header
   [:div.wrapper
    [:div#logo]
    [:h1#site-title (link-to "/" site-title)]
    [:ul#main-navigation.navigation
     [:li (link-to "/bio/" "Bio")]
     [:li (link-to "/reading/" "Reading")]
     [:li (link-to "/attributions/" "Attributions")]]]
   [:div.clearfix]])

(defpartial stylesheets [_ _ [stylesheets]]
  (conj (pop stylesheets) (link-to-asset "stylesheets/ryanmcg.css.stefon")))

(defpartial content [{:keys [disqus]} parse real-content]
  (if (show-disqus-comments? disqus parse)
    (list real-content
          [:hr]
          [:div#disqus_thread])
    real-content))

(deflayout ryanmcg []
  (repartial vm-layout/stylesheets stylesheets)
  (repartial vm-layout/header header)
  (repartial vm-layout/footer footer)
  (repartial base-layout/content content)
  (use-layout vm-layout/vm))

(register :ryanmcg-layout ryanmcg)
