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

; 4034765	Tuscarora Seamount	Tuscarora Seamount	Banka Tuskarora,Tuscarora Bank,Tuscarora Seamount,Tuscaroro Seamount	-11.83333	-178.25	U	SMU	WF	WF	00				0		-9999	Pacific/Wallis	2012-01-18
(deftest read-lines-from-tsv-file-without-header
  (let [cols [:geonameid 
              :name 
              :asciiname 
              :alternatenames 
              :latitude 
              :longitude 
              :feature-class 
              :feature-code 
              :country-code 
              :cc2 
              :admin1-code 
              :admin2-code 
              :admin3-code 
              :admin4-code 
              :population 
              :elevation 
              :dem 
              :timezone 
              :modification-date]
        tsv-path (.getPath (clojure.java.io/resource "NewWF.txt"))
        lines (read-tsv-file tsv-path cols)
        nb-lines (count lines)]
    (testing "There must be 144 lines"
      (is (= nb-lines 144)))
    (testing "18th line must have"
      (let [line (nth lines 17)]        
        (is (= (:geonameid line) "4034765"))
        (is (= (:name line) "Tuscarora Seamount"))
        (is (= (:asciiname line) "Tuscarora Seamount"))
        (is (= (:alternatenames  line) "Banka Tuskarora,Tuscarora Bank,Tuscarora Seamount,Tuscaroro Seamount"))
        (is (= (:latitude line) "-11.83333"))
        (is (= (:longitude line) "-178.25"))
        (is (= (:feature-classes line) "U"))
        (is (= (:feature-code line) "SMU")) 
        (is (= (:country-code line) "WF"))
        (is (= (:cc2 line) "WF"))
        (is (= (:admin1-code line) 00))
        (is (= (:admin2-code line) ""))
        (is (= (:admin3-code line) ""))
        (is (= (:admin4-code line) ""))
        (is (= (:population line) "0"))
        (is (= (:elevation line) ""))
        (is (= (:dem line) "-9999"))
        (is (= (:timezone line) "Pacific/Wallis"))
        (is (= (:modification-date line) "2012-01-18"))))))