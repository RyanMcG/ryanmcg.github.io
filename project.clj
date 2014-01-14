(defproject com.ryanmcg/www "0.1.0-SNAPSHOT"
  :description "My personal website (w/ rendered content on gh-pages branch)."
  :url "https://github.com/RyanMcG/ryanmcg.github.io"
  :license {:name "Creative Commons Attribution-ShareAlike 3.0 Unported License"
            :url "http://creativecommons.org/licenses/by-sa/3.0/deed.en_US"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [incise "0.1.0"]]
  :repl-options {:init-ns incise.repl}
  :main incise.core)
