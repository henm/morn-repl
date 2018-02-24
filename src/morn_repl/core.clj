(ns morn-repl.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println args))

(defn splitHeadAndBody
  "Split the head and body of a rule"
  [rule]
  (let
    [matcher (re-matcher #"(.*?)(?::-(?:(.+?),)*(.+?))?\." rule)]
    (re-find matcher)
    (rest (re-groups matcher))
  ))