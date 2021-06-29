(ns fr33m0nk.ardoq.core
  (:require
    [mount.lite :as mount]
    [fr33m0nk.ardoq.server])
  (:gen-class))

(defn -main
  [& _]
  (mount/start)
  (.addShutdownHook
    (Runtime/getRuntime)
    (Thread. ^Runnable mount/stop)))
