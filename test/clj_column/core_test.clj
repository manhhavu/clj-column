(ns clj-column.core-test
  (:use clojure.test
        clj-column.core))

(deftest read-lines-from-excel-file
  (let [path (.getPath (clojure.java.io/resource "test.xls"))
        lines (read-excel-file path)]
    (testing "There must be four lines"
      (is (= (count lines) 4)))
    (testing "First line must have"
      (let [line (nth lines 0)]
        (is (= (:COL_1 line) 1.0))
        (is (= (:COL_2 line) "Name 1"))
        (is (= (:COL_3 line) (.parse (java.text.SimpleDateFormat. "MM/dd/yyyy") "01/02/2014")))
        (is (= (:COL_4 line) 65.12))))
    (testing "Third line must have"
      (let [line (nth lines 2)]
        (is (= (:COL_1 line) 3.0))
        (is (= (:COL_2 line) "Name 3"))
        (is (= (:COL_3 line) (.parse (java.text.SimpleDateFormat. "MM/dd/yyyy") "12/30/2014")))
        (is (= (:COL_4 line) "12+16")))))) ; Formula

; 4034765	Tuscarora Seamount	-11.83333	-178.25	U	SMU
(deftest parse-tsv-line
  (let [input "4034765	Tuscarora Seamount	-11.83333	-178.25	U"
        line (parse-tsv input)]
    (testing "Line must contain correct keys"
      (is (= (nth line 0) "4034765"))
      (is (= (nth line 1) "Tuscarora Seamount"))
      (is (= (nth line 2) "-11.83333"))
      (is (= (nth line 3) "-178.25"))
      (is (= (nth line 4) "U")))))

(deftest parse-csv-line
  (let [input "4034765,Tuscarora Seamount,-11.83333,-178.25,U"
        line (parse-csv input)]
    (testing "Line must contain correct keys"
      (is (= (nth line 0) "4034765"))
      (is (= (nth line 1) "Tuscarora Seamount"))
      (is (= (nth line 2) "-11.83333"))
      (is (= (nth line 3) "-178.25"))
      (is (= (nth line 4) "U")))))
