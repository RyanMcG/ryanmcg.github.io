(ns incise.layouts.impl.ryanmcg
  (:require (incise.layouts [utils :refer [repartial use-layout
                                           deflayout defpartial]]
                            [core :refer [register]])
            [incise.layouts.impl.vm :as vm-layout]
            [stefon.core :refer [link-to-asset]]))

(defpartial stylesheets [_ _ [stylesheets]]
  (conj (pop stylesheets) (link-to-asset "stylesheets/ryanmcg.css.stefon")))

(deflayout ryanmcg []
  (repartial vm-layout/stylesheets stylesheets)
  (use-layout vm-layout/vm))

(register [:ryanmcg] ryanmcg)
