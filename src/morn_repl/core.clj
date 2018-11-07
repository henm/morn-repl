(ns morn-repl.core
  (:gen-class)
  (:require [clojure.string :as string]
            [morn-repl.parser :as parser]
            [morn-repl.morn-adapter :as morn-adapter]))

(defn exit "Exit the repl" []
  (println)
  (println "Bye!")
  (System/exit 0))

(defn eof? "Check if EOF was read" [line] (nil? line))

(defn empty-line? "Check if empty line was read" [line] (empty? line))

(defn query? "Check if an entry is a query" [line] (string/starts-with? (string/trim line) "?"))

(defn repl-loop
  "Read line by line"
  [kb]
  (print "> ")
  (flush)
  (let [line (read-line)]
    (try
      (cond (eof? line) (exit)
            (empty-line? line) ()
            (query? line) (println (morn-adapter/run-query kb (parser/parse-query line)))
            :else (morn-adapter/add-rule kb (parser/parse-rule line)))
      (catch Exception e (println (str "Error: "  (.getMessage e))))))
  (recur kb))

(defn -main
  "Start morn-repl!"
  []
  (println "Welcome to morn-repl!")
  (let [kb (morn-adapter/knowledge-base)]
    (repl-loop kb)))