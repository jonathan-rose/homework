(ns homework.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as sets]
            [clojure.data.csv :as csv]
            [cheshire.core :as json])
  (:gen-class))

;; Potential improvements:
;;
;; Consolidate file readers so they are file-type independent
;; Change hard-coded filepaths.
;; Allow user to specify output.csv location.
;; Remove leading colons from keywords in output.
;; Make code generic and not tailored specifically to :Emails key

(def csv-path "resources/data/google.csv")

(def json-path "resources/data/hr.json")

(def testname {:Name "Jonathan Rose", :Email "jonathanrose@griffintest.co.uk"})

(defn read-csv-file
  "Takes a filepath to a CSV file and returns a sequence of vectors of the csv data, 
   headers included. Does not mark the source of the data."
  [file]
  (with-open [r (io/reader file)]
    (doall (csv/read-csv r))))

(defn read-json-file
  "Takes a filepath to a JSON file and returns a sequence of maps containing 
   the data of the JSON, in key-value pairs. Marks the source of the data as 'JSON'."
  [file]
  (let [m
        (with-open [r (io/reader file)]
          (json/parse-stream r true))]
    (map (fn [m] (assoc m :Source "JSON")) m)))

(defn csv-seq-to-maps
  "Takes a sequence returned by read-csv-file and converts it into a sequence of maps,
   with items arranged into key-value pairs matching the output of read-json-file.
   Adds the key :Source to each map to mark the source as CSV."
  [data]
  (let [m (map zipmap
               (->> (first data)
                    (map keyword)
                    repeat)
               (rest data))]
    (map (fn [m] (assoc m :Source "CSV")) m)))

(defn splitname
  "Takes a map and splits the string value of key :Name into a constituent 
   :FirstName and :LastName."
  [m]
  (let [[FirstName LastName] (str/split (:Name m) #" ")]
    (-> m
        (dissoc :Name)
        (assoc :FirstName FirstName :LastName LastName))))

(defn map-splitname
  "Maps the function splitname across all the maps of a provided sequence of maps."
  [maps]
  (map splitname maps))

(defn extract-emails
  "Returns a set of all the values of key :Email in a provided sequence of maps."
  [maps]
  (set (map :Email maps)))

(defn symmetric-diff
  "Given two sets, s1 and s2, returns a set of all the elements that are exclusive to 
   s1 or s2. That is, returns whatever elements aren't in the intersection of both sets."
  [s1 s2]
  (sets/union
   (sets/difference s1 s2)
   (sets/difference s2 s1)))

(defn write-csv
  "Takes a sequence of maps and outputs a CSV file of the values from the maps
   sorted into headers and rows based on the keys in the maps. CSV is saved to
   current working directory."
  [d]
  (let [headers (->> d
                     (mapcat keys)
                     (distinct)
                     (sort))
        rows (map (fn [m] (map m headers)) d)]
    (with-open [w (io/writer "output.csv")]
      (csv/write-csv w (cons headers rows)))))

(defn -main
  "Given the filepath to a CSV file and a JSON file of user data, 
   returns the entries that are not shared between the two files."
  [csv json]
  (let [c (-> csv
              (read-csv-file)
              (csv-seq-to-maps)
              (map-splitname)
              (set))
        j (read-json-file json)
        ce (extract-emails c)
        je (extract-emails j)
        sd (symmetric-diff ce je)
        alldata (concat c j)
        results (filter #(contains? sd (:Email %)) alldata)]
    (write-csv results)))