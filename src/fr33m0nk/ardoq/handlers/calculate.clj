(ns fr33m0nk.ardoq.handlers.calculate
  (:require
    [fr33m0nk.ardoq.domain.calculator :as calc]
    [fr33m0nk.ardoq.configuration :as config]
    [fr33m0nk.ardoq.validation.validator :as v]))

(defn calculate
  [database _req respond _raise]
  (let [expression (-> _req :body :expression)
        validator (juxt v/balanced-parentheses? v/valid-items-in-expression?)]
    (if (every? true? (validator expression))
      (respond {:status 201
                :body   {:result (calc/calculate-and-save database expression)}})
      (respond {:status 400
                :body   {:error (-> @config/config :message :validation-error)}}))))


(defn turing
  [session-storage _req respond _raise]
  (let [session-id (-> _req :headers :session-id)
        expression (-> _req :body :expression)]
    (if (v/balanced-parentheses? expression)
      (respond {:status 201
                :body   {:result (calc/curate-expression session-storage expression session-id)}})
      (respond {:status 400
                :body   {:error (-> @config/config :message :validation-error)}}))))