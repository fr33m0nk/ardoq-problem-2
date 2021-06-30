(ns fr33m0nk.ardoq.handlers.history
  (:require
    [fr33m0nk.ardoq.domain.history :as h]))

(defn get-history
  [_req respond _raise]
  (let [database (:db _req)]
    (respond {:status 200
              :body   (h/get-history database)})))
