(ns fr33m0nk.ardoq.test-utility
  (:require
    [mount.lite :as mount]
    [fr33m0nk.ardoq.configuration :as config]
    [hato.client :as hc]))

(defn test-fixture-config-only
  [f]
  (mount/start #'config/config)
  (f)
  (mount/stop))

(defn integration-test-fixture
  [f]
  (mount/start)
  (f)
  (mount/stop))

(defn http-client
  []
  (hc/build-http-client {:connect-timeout   10000
                           :redirect-policy :always}))


