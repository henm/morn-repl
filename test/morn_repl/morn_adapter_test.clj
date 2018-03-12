(ns morn-repl.morn-adapter-test
  (:require [clojure.test :refer :all]
            [morn-repl.morn-adapter :refer :all]))

(deftest build-compound-term-test
  (testing "build-compound-term should handle simple compound terms"
    (let [morn-term (build-compound-term { :type :compound, :functor "f", :args '({ :type :atom, :name "X"})})]
      (is (= (. (. morn-term getFunctor) toString) "f"))
      (let [argument (first (. morn-term getArguments))]
        (is (= (. argument toString) "X")))))
  (testing "build-compound-term should handle nested terms"
    (let [morn-term (build-compound-term {
        :type :compound,
        :functor "f",
        :args '({
          :type :compound,
          :functor "g",
          :args ({ :type :atom, :name "X" })
        })
      })]
      (is (= (. (. morn-term getFunctor) toString) "f"))
      (let [inner-term (first (. morn-term getArguments))]
        (is (= (. (. inner-term getFunctor) toString) "g"))
        (let [variable (first (. inner-term getArguments))]
          (is (= (. variable toString) "X")))))))

(deftest build-term-test
  (testing "build-term should handle constants"
    (let [morn-term (build-term { :type :atom, :name "const" })]
      (is (instance? de.henm.morn.core.Constant morn-term))
      (is (= (. morn-term toString) "const"))))
  (testing "build-term should handle variable"
    (let [morn-term (build-term { :type :atom, :name "X" })]
      (is (instance? de.henm.morn.core.Variable morn-term))
      (is (= (. morn-term toString) "X"))))
  (testing "build-term should return very same morn-term for same term"
    (let [morn-term (build-term { :type :atom, :name "X" })
          morn-term-2 (build-term { :type :atom, :name "X" })]
      (is (= morn-term morn-term-2)))))