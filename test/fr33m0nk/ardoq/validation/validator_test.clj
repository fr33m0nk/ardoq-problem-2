(ns fr33m0nk.ardoq.validation.validator-test
  (:require [clojure.test :refer :all])
  (:require [fr33m0nk.ardoq.validation.validator :as v]))

(deftest valid-items-in-expression?-test
  (testing "should return true for valid expressions"
    (are [x] (true? x)
             (v/valid-items-in-expression? "-1 * (2 * 6 / 3) + 100")
             (v/valid-items-in-expression? "-1 * (2 * 6 / 3) + (100 - 200)")))
  (testing "should return false for invalid expressions"
    (are [x] (false? x)
             (v/valid-items-in-expression? "-1 oops (2 * 6 / 3) + 100")
             (v/valid-items-in-expression? "-1 * (2 * 6 / 3) + [100 - 200]")
             (v/valid-items-in-expression? "-1 * (2 * 6 / 3) $ 100 _ 200"))))

(deftest balanced-parentheses?-test
  (testing "should return true when parentheses are balanced"
    (are [x] (true? x)
             (v/balanced-parentheses? "-1 * (2 * 6 / 3) + 100")
             (v/balanced-parentheses? "-1 * 2 * 6 / 3 + 100")))
  (testing "should return false when parentheses are not balanced"
    (are [x] (false? x)
             (v/balanced-parentheses? "-1 * 2 * 6) / 3 + 100")
             (v/balanced-parentheses? "-1 * (2 * 6 / 3 + 100"))))

(deftest has-value?-test
  (testing "should return false when expression is empty or nil"
    (are [x] (false? x)
             (v/has-value? "")
             (v/has-value? nil)))
  (testing "should return true when expression is has some value"
    (is (true? (v/has-value? "-1 * (2 * 6 / 3) + 100")))))
