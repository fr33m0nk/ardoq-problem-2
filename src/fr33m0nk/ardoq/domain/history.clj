(ns fr33m0nk.ardoq.domain.history
  (:require
    [fr33m0nk.ardoq.database.storage :as db]))

(defn get-history
  [database]
  (db/list-expressions database))