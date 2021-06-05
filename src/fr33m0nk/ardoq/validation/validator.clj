(ns fr33m0nk.ardoq.validation.validator
  (:require [clojure.string :as str]))

(defn has-value?
  [expression]
  ((complement empty?) expression))

(defn valid-items-in-expression?
  [expression]
  (and (has-value? expression)
       (nil? (re-find #"[A-Za-z!@#$%^&`~,;'\":{}\[\]_=]" expression))))

(defn balanced-parentheses?
  ([expression]
   (if (has-value? expression)
     (balanced-parentheses? (str/split expression #"") 0)
     false))
  ([[x & xs] count]
   (cond (neg? count) false
         (nil? x) (zero? count)
         (= x "(") (recur xs (inc count))
         (= x ")") (recur xs (dec count))
         :else (recur xs count))))
