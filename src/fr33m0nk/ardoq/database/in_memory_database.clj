(ns fr33m0nk.ardoq.database.in-memory-database
  (:require
    [mount.lite :refer [defstate]]
    [java-time :as jt]
    [fr33m0nk.ardoq.database.storage :as s]))


(defn save-expression*
  [!storage expression result]
  (swap! !storage conj {:timestamp (jt/instant)
                        :expression expression
                        :result result}))

(defn list-expressions*
  [!storage]
  @!storage)

(defn init-database
  []
  (let [!storage (atom (list))]
    (reify s/Storage
      (save-expression [_ expression result] (save-expression* !storage expression result))
      (list-expressions [_] (list-expressions* !storage)))))

(defstate database
          :start (init-database))