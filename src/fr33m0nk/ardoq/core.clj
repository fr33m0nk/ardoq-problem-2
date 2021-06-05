(ns fr33m0nk.ardoq.core
  (:require
    [mount.lite :as mount]
    [fr33m0nk.ardoq.server :as server])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& _]
  (mount/start)
  (.addShutdownHook
    (Runtime/getRuntime)
    (Thread. ^Runnable mount/stop)))
