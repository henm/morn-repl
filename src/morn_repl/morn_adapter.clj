(ns morn-repl.morn-adapter
  (:gen-class)
  (:require [clojure.string :as string]))

(defn knowledge-base
  "Create a morn knowledge base"
  []
  (new de.henm.morn.KnowledgeBase))

(defn add-rule
  [kb { head :head, body :body }]
  (let [morn-rule (new de.henm.morn.core.Rule head body)]))

(defn variable?
  "Check if an atom refers a variable."
  [name]
  (= name (string/upper-case name)))

(defn constant?
  "Check if an atom refers a constant"
  [name]
  (not (= name (string/upper-case name))))

(declare build-term)

(defn build-compound-term
  "Build a morn-compound-term"
  [{ type :type, functor :functor, args :args }]
  (let [morn-functor (new de.henm.morn.core.Functor functor)
        morn-arguments (map build-term args)
        compound-term-factory (new de.henm.morn.core.CompoundTermFactory)]
    (. compound-term-factory build morn-functor morn-arguments)))

(defn build-term
  "Build a morn-term"
  [{ type :type :as term }]
  (cond (and (= type :atom) (variable? (term :name))) (new de.henm.morn.core.Variable (term :name))
        (and (= type :atom) (constant? (term :name))) (new de.henm.morn.core.Constant (term :name))
        :else (build-compound-term term)))