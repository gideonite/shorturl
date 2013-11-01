(ns shorturl.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :as resp]))

(def url-records (atom []))

(defn store-url-return-id
  [url]
  (swap! url-records conj {:url url :count 0})
  (dec (count @url-records)))

(defroutes app-routes
  (GET "/" [] (slurp "resources/public/index.html"))
  (GET "/:id" [id] (let [id (read-string id)
                         record (nth @url-records id)]
                     (swap! url-records #(update-in % [id :count] inc))
                     (resp/redirect (:url (nth @url-records id)))))
  (POST "/" [url] (let [record (store-url-return-id url)]
                    (str "<a href='" url "'>" (nth @url-records 0) "</a>" " " (:count record))))
  ;(route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
