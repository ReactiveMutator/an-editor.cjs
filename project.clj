(defproject an-editor-cljs "0.0.1-SNAPSHOT"
  :description "Monospace Text Editor"
  :license "MIT license"
  :url "https://github.com/ReactiveMutator/an-editor"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.946"]]
  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-codox "0.10.3"]]
  :cljsbuild {
    :builds [{
        :source-paths ["src"]
        :compiler {
          :main an_editor.core
          :output-to "out/main.js"
          :output-dir "out"
          :optimizations :none
          :source-map true
          :pretty-print true}}]})
