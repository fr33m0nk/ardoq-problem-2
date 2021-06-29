(ns fr33m0nk.ardoq.integration-test
  (:require [clojure.test :refer :all]
            [fr33m0nk.ardoq.core :refer :all]
            [fr33m0nk.ardoq.test-utility :as util]
            [hato.client :as hc]
            [java-time :as jt]))

(use-fixtures :once util/integration-test-fixture)

(deftest end-to-end-test
  (let [client (util/http-client)]
    (with-redefs [jt/instant (fn [] "2021-06-05T07:31:36.493088Z")]
      (testing "should process, save the expression in database and return the result"
        (let [result (hc/post "http://localhost:8088/api/v1/calc" {:body         "{\"expression\": \"-1 * (2 * 6 / 3) + 100\"}"
                                                                   :content-type :json
                                                                   :http-client  client})]
          (is (= 201 (:status result)))
          (is (= "{\"result\":96}" (:body result)))))

      (testing "should retrieve past executed expressions and their results and timestamps"
        (let [result (hc/get "http://localhost:8088/api/v1/history" {:http-client client})]
          (is (= 200 (:status result)))
          (is (= "[{\"timestamp\":\"2021-06-05T07:31:36.493088Z\",\"expression\":\"-1 * (2 * 6 / 3) + 100\",\"result\":96}]"
                 (:body result)))))

      (testing "should process expressions using turing calculator, store variable and return response"
        (let [result (hc/post "http://localhost:8088/api/v1/turing" {:body "{\"expression\": \"4 -> b\"}"
                                                                     :headers {"session-id" "test-id"}
                                                                     :http-client client})]
          (is (= 201 (:status result)))
          (is (= "{\"session-id\":\"test-id\",\"result\":4}"
                 (:body result)))))

      (testing "should process expressions using turing calculator, use stored variable and return response"
        (let [result (hc/post "http://localhost:8088/api/v1/turing" {:body "{\"expression\": \"5 + b\"}"
                                                                     :headers {"session-id" "test-id"}
                                                                     :http-client client})]
          (is (= 201 (:status result)))
          (is (= "{\"session-id\":\"test-id\",\"result\":9}"
                 (:body result))))))))
