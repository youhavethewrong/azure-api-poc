(ns azure-api-poc.vision
  (:require
   [clj-http.client :as client]
   [clojure.data.json :as json]
   [clojure.string :as str]))

;; Vision API key2
(def sub-key "taco")

(def vision-url "https://api.projectoxford.ai/vision/v1.0/")
(def analyze-url (str vision-url "analyze?visualFeatures=Categories,Tags,Description,Faces,Color"))
(def describe-url (str vision-url "describe"))
(def generate-thubnail-url (str vision-url "generateThumbnail?width=50&height=50&smartCropping=true"))

(def sandwich-url "http://i.imgur.com/2ZQVHcp.jpg")

(defn analyze-image
  [url image-url]
  (let [options {:content-type :json
                 :accept :json
                 :throw-exceptions false
                 :headers {"Ocp-Apim-Subscription-Key" sub-key}
                 :body (json/write-str {:url image-url})}]
    (client/post url options)))

(comment
  (analyze-image analyze-url sandwich-url)
  (analyze-image describe-url sandwich-url)

  ;; produces image/jpeg
  (analyze-image generate-thubnail-url sandwich-url)
  )
