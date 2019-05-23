(defproject com.ryanmcg/www "2019"
  :description "My personal website (w/ rendered content on gh-pages branch)."
  :url "https://github.com/RyanMcG/ryanmcg.github.io"
  :license {:name "Creative Commons Attribution-ShareAlike 3.0 Unported License"
            :url "http://creativecommons.org/licenses/by-sa/3.0/deed.en_US"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [manners "0.8.0"]
                 [incise-core "0.5.1"]
                 [incise "0.5.0"]
                 [com.ryanmcg/incise-vm-layout "0.5.0"]]
  :repl-options {:init-ns incise.repl}
  :aliases {"incise" ^:pass-through-help ["run" "-m" "incise.core"]})
