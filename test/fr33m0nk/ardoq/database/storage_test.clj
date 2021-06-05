(ns fr33m0nk.ardoq.database.storage-test
  (:require
    [clojure.test :refer :all]
    [java-time :as jt]
    [fr33m0nk.ardoq.database.storage :refer :all]
    [fr33m0nk.ardoq.database.in-memory-database :as imdb]))

(defn validate-storage
  [database]
  (testing "should save the expression and its result in database"
    (is (= [{:expression "-1 * (2 * 6 / 3)"
             :result     -4
             :timestamp  "2021-06-05T06:09:34.692270Z"}] (save-expression database "-1 * (2 * 6 / 3)" -4))))
  (testing "should retrieve all past expressions and their result from databse"
    (save-expression database "2 * (2 * 6 / 3)" 8)
    (is (= [{:expression "2 * (2 * 6 / 3)"
             :result     8
             :timestamp  "2021-06-05T06:09:34.692270Z"}
            {:expression "-1 * (2 * 6 / 3)"
             :result     -4
             :timestamp  "2021-06-05T06:09:34.692270Z"}] (list-expressions database)))))

(deftest in-memory-database-test
  (with-redefs [jt/instant (fn [] "2021-06-05T06:09:34.692270Z")]
    (let [db (imdb/init-database)]
      (validate-storage db))))