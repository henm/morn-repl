(ns morn-repl.core-test
  (:require [clojure.test :refer :all]
            [morn-repl.core :refer :all]))

;(deftest a-test
;  (testing "FIXME, I fail."
;    (is (= 0 1))))

(deftest splitAndHeadShouldReturnOnlyBodyForFacts
  (testing "Was ist das hier?"
    (is (= (first (splitHeadAndBody "a.")) "a"))))