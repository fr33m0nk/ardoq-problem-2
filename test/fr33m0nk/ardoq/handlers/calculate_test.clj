(ns fr33m0nk.ardoq.handlers.calculate-test
  (:require
    [clojure.test :refer :all]
    [fr33m0nk.ardoq.domain.calculator :as calc]
    [fr33m0nk.ardoq.handlers.calculate :as handler]
    [fr33m0nk.ardoq.database.in-memory-database :as imdb]
    [fr33m0nk.ardoq.test-utility :as util]))

(use-fixtures :once util/test-fixture-config-only)

(deftest calculate-test
  (testing "should process the expression and return the clojure map for response"
    (with-redefs [calc/calculate-and-save (fn [_ _] -4)]
      (let [db (imdb/init-database)
            req {:body {:expression "-1 * (2 * 6 / 3) + 100"}}]
        (is (= {:body   {:result -4}
                :status 201} (handler/calculate db req identity identity))))))
  (testing "should validate and return error if expression not present in request"
    (let [db (imdb/init-database)
          req {:body {}}]
      (is (= {:body   {:error "Provided expression is not supported"}
              :status 400} (handler/calculate db req identity identity)))))
  (testing "should validate and return error if expression contains unbalanced parentheses"
    (let [db (imdb/init-database)
          req {:body {:expression "-1 * 2 * 6) / 3 + 100"}}]
      (is (= {:body   {:error "Provided expression is not supported"}
              :status 400} (handler/calculate db req identity identity)))))
  (testing "should validate and return error if expression contains unsupported characters"
    (let [db (imdb/init-database)
          req {:body {:expression "-1 * 2 * 6 and / 3 + 100"}}]
      (is (= {:body   {:error "Provided expression is not supported"}
              :status 400} (handler/calculate db req identity identity))))))


(deftest turing-test
  (testing "should calculate and allocate for turing calculator for a session"
    (let [session (atom {})
          req {:headers {"session-id" "test-session"}
               :body {:expression "-1 -> a"}}]
      (is (= {:body {:session-id "test-session", :result -1}
              :status 201}
             (handler/turing session req identity identity)))))

  (testing "should reused allocated var for turing calculator for a session"
    (let [session (atom {"test-session" {:a -1}})
          req {:headers {"session-id" "test-session"}
               :body {:expression "-4 + a"}}]
      (is (= {:body {:session-id "test-session", :result -5}
              :status 201}
             (handler/turing session req identity identity))))))

