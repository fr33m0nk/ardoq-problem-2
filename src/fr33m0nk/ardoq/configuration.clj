(ns fr33m0nk.ardoq.configuration
  (:require
    [mount.lite :refer [defstate]]
    [clojure.java.io :as io]
    [clojure.edn :as edn]))

(defstate config
          :start (->> "config.edn"
                      io/resource
                      slurp
                      edn/read-string))


