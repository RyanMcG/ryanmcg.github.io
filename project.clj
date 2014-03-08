(defproject com.ryanmcg/www "2014"
  :description "My personal website (w/ rendered content on gh-pages branch)."
  :url "https://github.com/RyanMcG/ryanmcg.github.io"
  :license {:name "Creative Commons Attribution-ShareAlike 3.0 Unported License"
            :url "http://creativecommons.org/licenses/by-sa/3.0/deed.en_US"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [incise-stefon "0.1.0"]
                 [incise "0.3.1"]]
  :repl-options {:init-ns incise.repl}
  :aliases {"incise" ^:pass-through-help ["run" "-m" "incise.core"]}
  :main incise.core)
