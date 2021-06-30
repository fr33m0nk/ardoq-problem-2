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

(defn store-in-user-space
  [user-id user-space var value]
  (swap! user-space assoc-in [user-id var] value))

(defn calculate [input-expression]
  (let [expression (edn/read-string (str "(" input-expression ")"))]
    (eval (convert-to-infix expression))))

(defn replace-vars-with-vals
  [user-space expression]
  (reduce (fn [acc [k v]]
            (clojure.string/replace acc (name k) (str v))) expression user-space))

(defn turing-exp-calculate
  [expression session session-id]
  (let [user-space (get @session session-id {})
        [expression var] (->> (clojure.string/split expression #"->")
                              (map clojure.string/trim))]
    (if-let [key-var (keyword var)]
      (-> (store-in-user-space session-id session key-var (calculate expression))
          (get-in [session-id key-var]))
      (-> (replace-vars-with-vals user-space expression)
           calculate))))

(defn calculate-and-save
  [database session session-id input-expression]
  (let [result (turing-exp-calculate input-expression session session-id)]
    (future (db/save-expression database input-expression result))
    result))




