(ns clj-column.core-test
  (:use clojure.test
        clj-column.core))

(def excel-path "test.xls")

(deftest read-lines-from-excel-file
  (let [lines (read-excel-file excel-path)]
    (testing "There must be four lines"
      (is (= (count lines) 4)))
    (testing "First line must have"
      (let [line (nth lines 0)]
        (is (= (:COL_1 line) 1))
        (is (= (:COL_2 line) "Name 1"))
        (is (= (:COL_3 line) (.parse (java.text.SimpleDateFormat "MM/dd/yyyy") "01/02/2014")))
        (is (= (:COL_4 line) 65.12))))
    (testing "Third line must have"
      (let [line (nth lines 2)]
        (is (= (:COL_1 line) 3))
        (is (= (:COL_2 line) "Name 2"))
        (is (= (:COL_3 line) (.parse (java.text.SimpleDateFormat "MM/dd/yyyy") "12/30/2014")))
        (is (= (:COL_4 line) 28))))))
