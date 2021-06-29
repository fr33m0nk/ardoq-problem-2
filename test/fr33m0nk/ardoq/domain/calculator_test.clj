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

(deftest curate-expression-test
  (testing "Should calculate and return the result in case of no assignment"
    (let [result (calc/curate-expression "3 + 1" (atom {:user-id {}}) :user-id)]
      (is (= 4 result))))

  (testing "Should store variable in atom if assignment is received"
    (let [result (calc/curate-expression "3 -> a" (atom {:user-id {}}) :user-id)]
      (is (= 3 result))))

  (testing "Should store the output of an expression assigned to a var in atom if assignment is received"
    (let [result (calc/curate-expression "3 + 1 -> a" (atom {:user-id {}}) :user-id)]
      (is (= 4 result))))

  (testing "Should use the value of a var stored in atom if expression uses the var"
    (let [session-storage (atom {:user-id {}})]
      (calc/curate-expression "3 + 1 -> a" session-storage :user-id)
      (is (= 7 (calc/curate-expression "3 + a" session-storage :user-id))))))

(deftest store-in-user-space-test
  (testing "Should store variable in atom in user space"
    (let [result (calc/store-in-user-space :user-id (atom {}) :a 3)]
      (is (= 3 (-> result :user-id :a))))))

(deftest replace-vars-with-vals-test
  (testing "should replace the variables in the expression with their values from userspace"
    (is (= "1 + 3 + 5" (calc/replace-vars-with-vals {:a 1 :b 3} "a + b + 5")))))
