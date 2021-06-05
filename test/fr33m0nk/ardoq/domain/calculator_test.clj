(ns fr33m0nk.ardoq.domain.calculator-test
  (:require
    [clojure.test :refer :all]
    [fr33m0nk.ardoq.domain.calculator :as calc]
    [clojure.edn :as edn]
    [fr33m0nk.ardoq.database.in-memory-database :as imdb]))

(deftest convert-to-infix-test
  (testing "should return properly formed infix clojure expression"
    (is (= '(* (+ -1 10) (/ (* 2 6) 3)) (calc/convert-to-infix (edn/read-string "((-1 + 10) * (2 * 6 / 3))"))))))

(deftest calculate-test
  (testing "should return take an arithmetic expression and return the result
  after evaluation per arithmetic rules"
    (is (= -4 (calc/calculate "-1 * (2 * 6 / 3)")))))

(deftest calculate-and-save-test
  (testing "should parse and calculate the result of provided expression along with storing it in database"
    (let [db (imdb/init-database)]
      (is (= -4 (calc/calculate-and-save db "-1 * (2 * 6 / 3)"))))))
