(ns morn-repl.term-parser-test
  (:require [clojure.test :refer :all]
            [morn-repl.term-parser :refer :all]))

(deftest split-rule-test
  (testing "split-rule should handle tests"
    (is (= (first (split-rule "a.")) "a")))
  (testing "split-rule should handle rules with body"
    (let [r (split-rule "a(X) :- b(X), c(X).")]
      (is (= (first r) "a(X)"))
      (is (= (second r) "b(X), c(X)")))))

(deftest atom?-test
  (testing "atom? should recognize atoms"
    (is (atom? "X")))
  (testing "atom? should not recognize compound-terms as atoms"
    (is (not (atom? "p(X)")))))

(deftest compound-term?-test
  (testing "compound-term? should recognize compound-terms"
    (is (compound-term? "p(X)"))
  (testing "compound-term? should not recognize atoms as compound-terms"
    (is (not (compound-term? "X"))))))

(deftest arguments-test
  (testing "arguments should return the arguments of a term"
    (is (= (first (arguments "p(X)")) "X"))))

(deftest split-compound-term-test
  (testing "split-compound-term should return functor as first element"
    (is (= (first (split-compound-term "p(X)")) "p")))
  (testing "split-compound-term should return arguments as rest"
    (is (= (rest (split-compound-term "p(X)")) '("X")))
    (is (= (rest (split-compound-term "p(X, Y)")) '("X", "Y"))))
  (testing "split-compound-term should handle nested compound-terms"
    (is (= (first (split-compound-term "p(X, g(Y))")) "p"))
    (is (= (rest (split-compound-term "p(X, g(Y))")) '("X" "g(Y)")))))

(deftest parse-term-test
  (testing "parse-term should parse atoms"
    (let [p (parse-term "X")]
      (is (= (p :type) :atom)
      (is (= (p :name) "X")))))
  (testing "parse-term should parse compound terms"
    (let [p (parse-term "p(X)")]
      (is (= (p :type) :compound))
      (is (= (p :functor) "p"))
      (is (= (p :args) '({ :type :atom, :name "X" })))))
  (testing "parse-term should parse nested compound-terms"
    (let [p (parse-term "p(X, g(Y, Z))")]
      (is (= (p :type) :compound))
      (is (= (p :functor) "p"))
      (is (= (first (p :args)) { :type :atom, :name "X"}))
      (let [nested (second (p :args))]
        (is (= (nested :type) :compound))
        (is (= (nested :functor) "g"))
        (is (= (first (nested :args)) { :type :atom, :name "Y"}))
        (is (= (second (nested :args)) { :type :atom, :name "Z"}))))))