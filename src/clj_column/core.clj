(ns clj-column.core
  (:import [org.apache.poi.ss.usermodel WorkbookFactory Workbook Sheet Row Cell]
           [java.io.FileInputStream]))

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
          (= cell-type Cell/CELL_TYPE_NUMERIC) (if (org.apache.poi.ss.usermodel.DateUtil/isCellDateFormatted cell) (.getDateCellValue cell) (.getNumericCellValue cell))
          (= cell-type Cell/CELL_TYPE_BOOLEAN) (.getBooleanCellValue cell)
          (= cell-type Cell/CELL_TYPE_FORMULA) (.getCellFormula cell)
          (= cell-type Cell/CELL_TYPE_ERROR) nil)))

(defn read-excel-file [path]
  (let [excel (File. path)
        fis (FileInputStream. excel)
        wb (HSSFWorkbook. fis)
        ws (.getSheetAt wb 0)
        hdr (.getRow 0)]
        
  []))

(defn read-excel-file [path]
  (let [wb (WorkbookFactory/create (FileInputStream. path))
        sh (.getSheetAt wb 0)]
    (map (fn [row]
           (map read-excel-cell row)) sh)))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
