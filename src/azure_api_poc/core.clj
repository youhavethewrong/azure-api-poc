(ns azure-api-poc.core
  (:require
   [clj-http.client :as client]
   [clojure.data.json :as json]
   [clojure.string :as str])
  (:gen-class))

(def sub-key "cdd27a149c7542e3b543579b08a05f72")
(def text-analytics-url "https://westus.api.cognitive.microsoft.com/text/analytics/v2.0/")
(def phrases-url (str text-analytics-url "keyPhrases"))
(def topics-url (str text-analytics-url "topics"))

(def corpii
  ["abominations.of.yondo.txt"
   "last.incantation.txt"
   "last.hieroglyph.txt"])

(defn slurp-corpus
  [text]
  (slurp (clojure.java.io/resource text)))

(defn parse-corpus
  [text]
  (str/split text #"\n"))

(defn chunk-corpus
  [text-seq prefix]
  (->> text-seq
       (map-indexed
        (fn [i t]
          {:id (str prefix "-" i)
           :text t}))
       (filter #(not (str/blank? (:text %))))
       vec))

(defn build-body
  [texts]
  (vec (flatten (map #(chunk-corpus (parse-corpus (slurp-corpus %)) %) texts))))

(defn topics-body-template
  [texts]
  (json/write-str
   {:stopWords []
    :topicsToExclude []
    :documents (build-body texts)}))

(defn phrases-body-template
  [texts]
  (json/write-str
   {:documents (mapv
                #(assoc % :language "en")
                (build-body texts))}))

(defn analyze-text
  [url body-fn texts]
  (let [options {:content-type :json
                 :accept :json
                 :throw-exceptions false
                 :headers {"Ocp-Apim-Subscription-Key" sub-key}
                 :body (body-fn texts)}]
    (client/post url options)))

(defn analyze-text-topics
  [texts]
  (analyze-text (str topics-url "?minimumNumberOfDocuments=10") topics-body-template texts))

(defn analyze-text-phrases
  [texts]
  (analyze-text phrases-url phrases-body-template texts))

(defn get-analysis
  [location]
  (client/get location
              {:accept :json
               :throw-exceptions false
               :headers {"Ocp-Apim-Subscription-Key" sub-key}}))

(comment
  (analyze-text-phrases corpii)

  (analyze-text-topics corpii)
  ;; get from 202
  (get-analysis "https://westus.api.cognitive.microsoft.com/text/analytics/v2.0/operations/f999c42315034ffcb0874b877cb2019b")
  (get-analysis "https://westus.api.cognitive.microsoft.com/text/analytics/v2.0/operations/2ed3b844752d4ad8aefa52ab56df27ce")
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
