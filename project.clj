(defproject com.ryanmcg/www "2014"
  :description "My personal website (w/ rendered content on gh-pages branch)."
  :url "https://github.com/RyanMcG/ryanmcg.github.io"
  :license {:name "Creative Commons Attribution-ShareAlike 3.0 Unported License"
            :url "http://creativecommons.org/licenses/by-sa/3.0/deed.en_US"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.ryanmcg/incise-vm-layout "0.1.0"]]
  :repl-options {:init-ns incise.repl}
  :aliases {"incise" ^:pass-through-help ["run" "-m" "incise.core"]})
