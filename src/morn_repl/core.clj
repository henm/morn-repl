(ns morn-repl.core
  (:gen-class)
  (:require [clojure.string :as string]
            [morn-repl.parser :as parser]))

(defn -main
  "I don't do a whole lot ... yet."
  []
  (println "Welcome to morn-repl!")
  (let [line (read)]
    (println line)))