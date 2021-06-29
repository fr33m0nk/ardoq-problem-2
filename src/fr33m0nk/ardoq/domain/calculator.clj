(ns fr33m0nk.ardoq.domain.calculator
  (:require
    [clojure.edn :as edn]
    [fr33m0nk.ardoq.database.storage :as db]))

(defn convert-to-infix
  [expression]
  (if (seq? expression)
    (let [[head second & tail] expression
          f (convert-to-infix head)]
      (cond
        (#{"*" "/"} (str second)) (let [[head-of-tail second-of-tail & tail-of-tail] tail
                                        t (convert-to-infix head-of-tail)]
                                    (if second-of-tail
                                      (list second-of-tail (list second f t) (convert-to-infix tail-of-tail))
                                      (list second f t)))
        (nil? second) f
        :else (list second f (convert-to-infix tail))))
    expression))

;; 2 -> a; a + b
;; atom {:user-id {}}


(defn store-in-user-space
  [user-id user-space var value]
  (swap! user-space assoc-in [user-id var] value))

(defn calculate [input-expression]
  (let [expression (edn/read-string (str "(" input-expression ")"))]
    (eval (convert-to-infix expression))))

(defn calculate-and-save
  [database input-expression]
  (let [result (calculate input-expression)]
    (future (db/save-expression database input-expression result))
    result))

(defn replace-vars-with-vals
  [user-space expression]
  (reduce (fn [acc [k v]]
            (clojure.string/replace acc (name k) (str v))) expression user-space))

(defn curate-expression
  [expression session-vars user-id]
  (let [user-space (user-id @session-vars)
        [expression var] (clojure.string/split expression #" -> ")
        expression (replace-vars-with-vals user-space expression)]
    (if-let [key-var (keyword var)]
      (-> (store-in-user-space user-id session-vars key-var (calculate expression))
          :user-id key-var)
      (calculate expression))))




