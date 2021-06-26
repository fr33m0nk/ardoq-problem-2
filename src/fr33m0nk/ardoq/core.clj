(ns fr33m0nk.ardoq.core
  (:require
    [mount.lite :as mount])
  (:gen-class))

(defn -main
  [& _]
  (mount/start)
  (.addShutdownHook
    (Runtime/getRuntime)
    (Thread. ^Runnable mount/stop)))
