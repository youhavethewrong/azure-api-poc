(ns azure-api-poc.vision
  (:require
   [clj-http.client :as client]
   [clojure.data.json :as json]
   [clojure.string :as str]))

(def vision-url "https://api.projectoxford.ai/vision/v1.0")
