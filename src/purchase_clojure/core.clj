(ns purchase-clojure.core
  (:require [clojure.string :as str]
            [clojure.walk :as walk]
            [clojure.pprint :as pp]
            [ring.adapter.jetty :as j]
            [hiccup.core :as h])
  (:gen-class))

(defn read-purchases []
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
        #_input #_(read-line)
        #_purchases #_(filter (fn [line]
                            (= input (:category line)))
                          purchases)]
   #_ (spit "filtered_purchases.edn"
          (pr-str purchases))
   purchases))

(defn purchases-html []
  (let [purchases (read-purchases)]
    (map (fn [line]
           [:p (str "Customer Id:"
                    (:customer_id line)
                    " Date: "
                    (:date line)
                    " CC#: "
                    (:credit_card line)
                    " CVV: "
                    (:cvv line)
                    " Category: "
                    (:category line))])
         purchases))
  )



(defn handler [request]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (h/html [:html
                     [:body
                      [:br]
                      (purchases-html)
                      ]])}
  )

(defn -main [& args]
  (j/run-jetty #'handler {:port 3000 :join? false})
  )
