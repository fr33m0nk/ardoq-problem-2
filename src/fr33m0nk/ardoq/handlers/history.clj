(ns fr33m0nk.ardoq.handlers.history
  (:require
    [fr33m0nk.ardoq.domain.history :as h]))

(defn get-history
  [database _req respond _raise]
  (respond {:status 200
            :body   (h/get-history database)}))
