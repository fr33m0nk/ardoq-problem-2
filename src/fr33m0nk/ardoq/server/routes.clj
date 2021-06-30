(ns fr33m0nk.ardoq.server.routes
  (:require
    [fr33m0nk.ardoq.handlers.calculate :as calc]
    [fr33m0nk.ardoq.handlers.history :as history]
    [fr33m0nk.ardoq.middleware :as mw]
    [com.appsflyer.donkey.middleware.json :as json]))

(defn routes
  [session-storage database]
  [{:path         "/api/v1/calc"
    :methods      [:post]
    :consumes     ["application/json"]
    :produces     ["application/json"]
    :handler-mode :non-blocking
    :handler      (->> calc/calculate
                      (mw/wrap-storage database)
                      ((json/make-serialize-middleware)))}
   {:path         "/api/v1/history"
    :methods      [:get]
    :produces     ["application/json"]
    :handler-mode :non-blocking
    :handler      (->> history/get-history
                       (mw/wrap-storage database)
                      ((json/make-serialize-middleware)))}
   {:path         "/api/v1/turing"
    :methods      [:post]
    :produces     ["application/json"]
    :handler-mode :non-blocking
    :handler      (->> calc/turing
                       (mw/wrap-storage session-storage)
                       ((json/make-serialize-middleware)))}])
