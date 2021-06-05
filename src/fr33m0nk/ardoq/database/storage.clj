(ns fr33m0nk.ardoq.database.storage)

(defprotocol Storage
  (save-expression
    [this expression result]
    "stores the expression in database for maintaining history")
  (list-expressions
    [this]
    "lists the expression in database"))
