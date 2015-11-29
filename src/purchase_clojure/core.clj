(ns purchase-clojure.core
  (:require [clojure.string :as str]
            [clojure.walk :as walk]
            [clojure.pprint :as pp]
            [ring.adapter.jetty :as j]
            [hiccup.core :as h])
  (:gen-class))

(defn read-purchase []
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
   #_ (spit "filtered_purchases.edn"
          (with-out-str (pp/pprint purchases)))
   purchases))

(defn purchase-html []
  (let [purchases (read-purchase)]
    (map (fn [line]
           [:p (str (:customer_id line)
                    " "
                    (:date line)
                    ""
                    (:credit_card line)
                    ""
                    (:cvv line)
                    ""
                    (:category line))])
         purchases))
  )

(defn handler [request]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (h/html [:html
                     [:body
                      [:br]
                      (purchase-html)
                      ]])}
  )

(defn -main [& args]
  (j/run-jetty #'handler {:port 3000 :join? false})

  )
