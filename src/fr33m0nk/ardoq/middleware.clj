(ns fr33m0nk.ardoq.middleware)

(defn wrap-storage
  [session db handler]
  (fn [request respond raise]
    (-> (assoc request :db db :session session)
        (handler respond raise))))
