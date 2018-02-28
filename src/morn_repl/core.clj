(ns morn-repl.core
  (:gen-class)
  (:require [clojure.string :as string]
            [morn-repl.parser :as parser]))

(defn exit "Exit the repl" []
  (println)
  (println "Bye!")
  (System/exit 0))

(defn eof? "Check if EOF was read" [line] (nil? line))

(defn empty-line? "Check if empty line was read" [line] (empty? line))

(defn repl-loop
  "Read line by line"
  []
  (print "> ")
  (flush)
  (let [line (read-line)]
    (cond (eof? line) (exit)
          (empty-line? line) ()
          :else (println (parser/parse-rule line))))
  (recur))

(defn -main
  "Start morn-repl!"
  []
  (println "Welcome to morn-repl!")
  (repl-loop))