(ns incise.layouts.impl.ryanmcg
  (:require (incise.layouts [html :refer [use-layout deflayout]]
                            [core :refer [register]])
            [incise.layouts.impl.page :refer [page]]
            (hiccup [def :refer :all]
                    [element :refer :all])))
(deflayout ryanmcg
  (use-layout page))

(register [:ryanmcg] ryanmcg)
