(ns azure-api-poc.core
  (:require
   [clj-http.client :as client])
  (:gen-class))


(def base-url "https://westus.api.cognitive.microsoft.com/text/analytics/v2.0/topics")

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
