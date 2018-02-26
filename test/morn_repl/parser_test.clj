(ns morn-repl.parser-test
  (:require [clojure.test :refer :all]
            [morn-repl.parser :refer :all]))

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

(deftest parse-rule-test
  (testing "parse-rule should parse facts"
    (let [r (parse-rule "a.")]
      (is (= (r :head) { :type :atom, :name "a"}))
      (is (= (r :body) ()))))
  (testing "parse-rule should parse rules with body"
    (let [r (parse-rule "a(X) :- g(X), g(p(Z), X).")
          head (r :head)
          body (r :body)]
      (is (= head { :type :compound, :functor "a", :args '({ :type :atom, :name "X"})}))
      (is (= (first body) { :type :compound, :functor "g", :args '({ :type :atom, :name "X"})}))
      (let [second-term (second body)
            nested-terms (second-term :args)]
        (is (= (second-term :type) :compound))
        (is (= (second-term :functor) "g"))
        (is (= ((first nested-terms) :type) :compound))
        (is (= ((first nested-terms) :functor) "p"))
        (is (= ((first nested-terms) :args) '({ :type :atom, :name "Z"})))
        (is (= ((second nested-terms) :type) :atom)
        (is (= ((second nested-terms) :name) "X")))))))