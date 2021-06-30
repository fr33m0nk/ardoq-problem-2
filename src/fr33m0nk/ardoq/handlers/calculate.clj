(ns fr33m0nk.ardoq.handlers.calculate
  (:require
    [fr33m0nk.ardoq.domain.calculator :as calc]
    [fr33m0nk.ardoq.configuration :as config]
    [fr33m0nk.ardoq.validation.validator :as v])
  (:import (java.util UUID)))

(defn calculate
  [{:keys [db session headers] :as _req} respond _raise]
  (let [session-id (get headers "session-id" (.toString (UUID/randomUUID)))
        expression (-> _req :body :expression)
        validator (juxt v/balanced-parentheses? v/valid-items-in-expression?)]
    (if (every? true? (validator expression))
      (respond {:status 201
                :body   {:result (calc/calculate-and-save db session session-id expression)}})
      (respond {:status 400
                :body   {:error (-> @config/config :message :validation-error)}}))))


(defn turing
  [{:keys [db session headers] :as _req} respond _raise]
  (let [session-id (get headers "session-id" (.toString (UUID/randomUUID)))
        expression (-> _req :body :expression)]
    (if (v/balanced-parentheses? expression)
      (respond {:status 201
                :body   {:session-id session-id
                         :result     (calc/calculate-and-save db session session-id expression)}})
      (respond {:status 400
                :body   {:error (-> @config/config :message :validation-error)}}))))