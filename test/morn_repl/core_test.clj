(ns morn-repl.core-test
  (:require [clojure.test :refer :all]
            [morn-repl.core :refer :all]))

;(deftest a-test
;  (testing "FIXME, I fail."
;    (is (= 0 1))))

(deftest split-rule-test
  (testing "split-rule should handle tests"
    (is (= (first (split-rule "a.")) "a")))
  (testing "split-rule should handle rules with body"
    (let [r (split-rule "a(X) :- b(X), c(X).")]
      (is (= (first r) "a(X)"))
      (is (= (second r) "b(X)"))
      (is (= (nth r 2) "c(X)")))))