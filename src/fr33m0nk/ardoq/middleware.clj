(ns fr33m0nk.ardoq.middleware)

(defn wrap-storage
  [storage handler]
  (fn [request respond raise]
    (-> (assoc request :storage storage)
        (handler respond raise))))
