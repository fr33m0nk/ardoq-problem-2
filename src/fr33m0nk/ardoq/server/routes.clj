(ns fr33m0nk.ardoq.server.routes
  (:require
    [fr33m0nk.ardoq.handlers.calculate :as calc]
    [fr33m0nk.ardoq.handlers.history :as history]
    [com.appsflyer.donkey.middleware.json :as mw]))

(defn routes
  [database]
  [{:path         "/api/v1/calc"
    :methods      [:post]
    :consumes     ["application/json"]
    :produces     ["application/json"]
    :handler-mode :non-blocking
    :handler      (-> (partial calc/calculate database)
                      ((mw/make-serialize-middleware)))}
   {:path         "/api/v1/history"
    :methods      [:get]
    :produces     ["application/json"]
    :handler-mode :non-blocking
    :handler      (-> (partial history/get-history database)
                      ((mw/make-serialize-middleware)))}])
