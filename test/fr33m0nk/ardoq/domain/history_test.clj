(ns fr33m0nk.ardoq.domain.history-test
  (:require
    [clojure.test :refer :all]
    [fr33m0nk.ardoq.database.in-memory-database :as imdb]
    [fr33m0nk.ardoq.domain.history :as h]
    [java-time :as jt]
    [fr33m0nk.ardoq.database.storage :as db]))

(deftest get-history-test
  (testing "should get the contents of the supplied database"
    (with-redefs [jt/instant (fn [] "2021-06-05T06:09:34.692270Z")]
      (let [db (imdb/init-database)]
        (db/save-expression db "-1 * (2 * 6 / 3)" -4)
        (db/save-expression db "2 * (2 * 6 / 3)" 8)
        (is (= [{:expression "2 * (2 * 6 / 3)"
                 :result     8
                 :timestamp  "2021-06-05T06:09:34.692270Z"}
                {:expression "-1 * (2 * 6 / 3)"
                 :result     -4
                 :timestamp  "2021-06-05T06:09:34.692270Z"}]
               (h/get-history db)))))))
