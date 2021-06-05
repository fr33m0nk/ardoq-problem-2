(ns fr33m0nk.ardoq.handlers.history-test
  (:require
    [clojure.test :refer :all]
    [fr33m0nk.ardoq.handlers.history :as handler]
    [fr33m0nk.ardoq.domain.history :as h]
    [fr33m0nk.ardoq.database.in-memory-database :as imdb]))

(deftest get-history-test
  (testing "should process the expression and return the clojure map for response"
    (with-redefs [h/get-history (fn [_] [{:expression "2 * (2 * 6 / 3)"
                                          :result     8
                                          :timestamp  "2021-06-05T06:09:34.692270Z"}
                                         {:expression "-1 * (2 * 6 / 3)"
                                          :result     -4
                                          :timestamp  "2021-06-05T06:09:34.692270Z"}])]
      (let [db (imdb/init-database)
            req {}]
        (is (= {:body   [{:expression "2 * (2 * 6 / 3)"
                          :result     8
                          :timestamp  "2021-06-05T06:09:34.692270Z"}
                         {:expression "-1 * (2 * 6 / 3)"
                          :result     -4
                          :timestamp  "2021-06-05T06:09:34.692270Z"}]
                :status 200} (handler/get-history db req identity identity)))))))
