(ns fr33m0nk.ardoq.server
  (:require
    [mount.lite :refer [defstate]]
    [fr33m0nk.ardoq.configuration :as config]
    [fr33m0nk.ardoq.server.routes :as routes]
    [fr33m0nk.ardoq.database.in-memory-database :as imdb]
    [com.appsflyer.donkey.core :as dc]
    [com.appsflyer.donkey.server :as ds]
    [com.appsflyer.donkey.result :as dr]
    [com.appsflyer.donkey.middleware.json :as mw]))

(defstate verticle
          :start (dc/create-donkey)
          :stop (dc/destroy @verticle))

(defstate webserver
          :start (let [server (dc/create-server @verticle {:port                (-> (or (System/getenv "PORT")
                                                                                        (:port @config/config))
                                                                                    (Integer.))
                                                           :middleware          [(mw/make-deserialize-middleware)]
                                                           :routes              (routes/routes @imdb/database)
                                                           :content-type-header true})]
                   (-> server
                       ds/start
                       (dr/on-success (fn [_] (println "Server started listening on port 8080")))
                       (dr/on-fail (fn [_] (println "Failed to start server"))))
                   server)
          :stop (ds/stop @webserver))
