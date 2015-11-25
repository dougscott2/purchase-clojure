(ns purchase-clojure.core
  (:require [clojure.string :as str]
            [clojure.walk :as walk]
            [clojure.pprint :as pp])
  (:gen-class))

(defn -main [& args]
  (println "what do you want to sort by?")
  (let [purchases (slurp "purchases.csv")         ;slurp the file
        purchases (str/split-lines purchases)
        purchases (map (fn [line]
                         (str/split line #","))
                       purchases)
        header (first purchases)
        purchases (rest purchases)
        purchases (map (fn [line]
                         (interleave header line))
                       purchases)
        purchases (map (fn [line]
                         (apply hash-map line))
                       purchases)
        purchases (walk/keywordize-keys purchases)
        input (read-line)
        purchases (filter (fn [line]
                            (= input (:category line)))
                          purchases)]
    (spit "filtered_purchases.edn"
          (with-out-str (pp/pprint purchases)))))
