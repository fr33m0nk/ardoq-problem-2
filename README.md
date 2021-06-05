# problem-2

Solution to simple Calculator REST API using JSON that supports input of mathematical expressions with the basic 
operations: +, -, *, and /. 
It allows the usage of parens and understand operator precedence.

## Description

* The Web server is built using [Donkey: a high performance server](https://github.com/AppsFlyer/donkey).
* An in-memory database is used for storing and retrieving history

* Endpoints exposed are :

* `POST:/api/v1/calc`
```shell
curl --location --request POST 'http://<host:port>/api/v1/calc' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{"expression": "-1 * (2 * 6 / 3) + 100"}'
```  
* `GET:/api/v1/history`
```shell
curl --location --request GET 'http://<host:port>/api/v1/history' \
--header 'Accept: application/json' \
--data-raw ''
```

## Installation

* Install Clojure CLI using instructions at [Clojure's official website](https://clojure.org/guides/getting_started)
* Download from https://github.com/fr33m0nk/ardoq/problem-2

## Usage

Run the project directly, via `:exec-fn`:

    $ clojure -X:run-x

Run the project directly, via `:main-opts` (`-m fr33m0nk.ardoq.problem-2`):

    $ clojure -M -m fr33m0nk.ardoq.core

Run the project's tests (they'll fail until you edit them):

    $ clojure -X:test:runner

Build an uberjar:

    $ clojure -X:uberjar

Run that uberjar:

    $ java -jar problem-2.jar

## License

Copyright © 2021 Prashant Sinha

Distributed under the Eclipse Public License version 1.0.
