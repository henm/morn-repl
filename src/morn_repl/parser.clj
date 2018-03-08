(ns morn-repl.parser
  (:gen-class)
  (:require [clojure.string :as string]))

(defn split-rule
  "Split the head- and body-clauses of a rule"
  [rule]
  (let [matcher (re-matcher #"(.*?)(?:\s*:-\s*(.*?))?\s*\." rule)]
    (re-find matcher)
    (filter some? (rest (re-groups matcher)))
  ))

(defn atom?
  "Checks if a string represents an atom"
  [term]
  (not (string/includes? term "(")))

(defn compound-term?
  "Checks if a string represents a compound-term"
  [term]
  (string/includes? term "("))

(defn balanced?
  "Check if parenthesis in a string are balanced"
  ([s] (balanced? (string/split s #"") 0))
  ([[x & xs] count]
    (cond (neg? count) false
          (nil? x) (zero? count)
          (= x "(") (recur xs (inc count))
          (= x ")") (recur xs (dec count))
          :else (recur xs count))))

(defn split-comma-separated-terms
  "Split a string of comma-separated terms"
  [terms]
  (if (nil? terms)
      '()
      (letfn [(split-comma-separated-terms-helper [[x & xs]] 
              (cond (nil? x) '()
              (balanced? x) (cons x (split-comma-separated-terms-helper xs))
              :else (split-comma-separated-terms-helper (cons (str x "," (first xs)) (rest xs)))))]
        (map string/trim (split-comma-separated-terms-helper (string/split terms #","))))))
    

(defn split-compound-term
  "Split functor and arguments of a term"
  [term]
  (let [matcher (re-matcher #"(.*?)(\(.*)\s*" term)]
    (re-find matcher)
    (let [groups (re-groups matcher)
          functor (second groups)
          argumentsWithParenthesis (string/trim (nth groups 2))
          arguments (subs argumentsWithParenthesis 1 (- (count argumentsWithParenthesis) 1))]
      (cons functor (split-comma-separated-terms arguments)))))

(defn arguments
  "Get the arguments from a compound term"
  [term]
  (rest (split-compound-term term)))

(defn functor
  "get the functor from a compound term"
  [term]
  (first (split-compound-term term)))

(defn parse-term
  "Parse the string representation of a term."
  [term]
  (let [ trimmed-term (string/trim term) ]
    (cond
      (atom? trimmed-term) { :type :atom, :name trimmed-term }
      (compound-term? trimmed-term) { :type :compound, :functor (functor trimmed-term), :args (map parse-term (arguments trimmed-term))}
      ; TODO Handle error-case
    )))

(defn parse-rule
  "Parse the string representation of a rule."
  [rule]
  (let [[head body] (split-rule rule)]
    { :head (parse-term head), :body (map parse-term (split-comma-separated-terms body))}))

(defn parse-query
  "Parse a query."
  [query]
  (let [query-term (string/replace-first query #"\?" "")]
    (parse-term query-term)))