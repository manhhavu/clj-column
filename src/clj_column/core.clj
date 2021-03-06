(ns clj-column.core
  (:import (org.apache.poi.ss.usermodel WorkbookFactory Workbook Sheet Row Cell DateUtil)
           (java.io FileInputStream)))

(defn read-excel-header [hdr]
  "Read a header row of class org.apache.poi.ss.usermodel.Row to sequence of keywords"
  (map #(-> % 
            (.getRichStringCellValue) 
            (.getString)
            name
            keyword)
       hdr))

(defn read-excel-cell[cell]
  (let [cell-type (.getCellType cell)]
    (cond
     (= cell-type Cell/CELL_TYPE_STRING) (.getString (.getRichStringCellValue cell))
     (= cell-type Cell/CELL_TYPE_BLANK) ""
     (= cell-type Cell/CELL_TYPE_NUMERIC) (if (DateUtil/isCellDateFormatted cell) (.getDateCellValue cell) (.getNumericCellValue cell))
     (= cell-type Cell/CELL_TYPE_BOOLEAN) (.getBooleanCellValue cell)
     (= cell-type Cell/CELL_TYPE_FORMULA) (.getCellFormula cell)     
     (= cell-type Cell/CELL_TYPE_ERROR) "error")))

(defn read-excel-row[row]
  (let [ncols (.getLastCellNum row)]
    (vector 
     (for [c (range 0 (inc ncols))]
       (read-excel-cell (.getCell row c Row/CREATE_NULL_AS_BLANK))))))

(defn read-excel-file [path]
  (with-open [in (FileInputStream. path)]
    (let [wb (WorkbookFactory/create in)
          sh (.getSheetAt wb 0)
          hdr (read-excel-header (.getRow sh 0))]
      (for [r (range 1 (inc (.getLastRowNum sh)))
            row (read-excel-row (.getRow sh r))]
          (zipmap hdr row)))))

(defn parse-csv[s] (if (empty? s) [] (clojure.string/split s #",")))

(defn parse-tsv[s]   
  (clojure.string/split s #"\t"))

(defn read-tsv-file [path cols]
  (with-open [rdr (clojure.java.io/reader path)]
    (doseq [line (line-seq rdr)]
      (let [parsed (parse-tsv line)]
        (zipmap cols parsed)))))

(defn read-csv-file [path cols]
  (with-open [rdr (clojure.java.io/reader path)]
    (doseq [line (line-seq rdr)]
      (let [parsed (parse-csv line)]
        (zipmap cols parsed)))))














