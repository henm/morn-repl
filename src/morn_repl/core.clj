(ns morn-repl.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println args))

(defn split-rule
  "Split the head- and body-clauses of a rule"
  [rule]
  (let
    [matcher (re-matcher #"(.*?)(?:\s*:-(?:\s*(.+?)\s*,)*\s*(.+?))?\." rule)]
    (re-find matcher)
    (filter some? (rest (re-groups matcher)))
  ))