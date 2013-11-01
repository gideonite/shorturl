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

;; a user is a map { :username :password }
;; users is a map { username -> user }
(def users (atom {}))

(def hash-with-salt
  (let [salt (slurp "resources/salt")]
    (fn [s] (hash (str s salt)))))

(defn user?
  "checks whether a user exists
  user -> boolean
  "
  [user]
  (if-let [fetched-user (@users (:username user))]
    (= (:password fetched-user) (:password user))
    false))

(defn create-user
  [user]
  (swap! users #(assoc % (:username user)
                       (update-in user [:password] hash-with-salt))))

(defroutes app-routes
  (GET "/" [] (resp/file-response "resources/public/index.html"))

  (GET "/login" [] (resp/file-response "resources/public/login.html"))
  (POST "/login" [username password]
        (if (user? {:username username :password password})
          (resp/file-response "resources/public/index.html")
          (resp/response
            (str "username \"" username "\" not found, please sign up"))))

  (GET "/signup" [] (resp/file-response "resources/public/signup.html"))
  (POST "/signup" [username password]
        (let [user {:username username :password password}]
          (if (user? user)
            (resp/response "Sorry bud. You had hope that you remember your password.")
            (create-user user)))
        (resp/response (str  "Thank you " username " you are now signed up.")))

  (GET "/user/:name" [name] (str name))
  (GET "/users" [] (str @users))

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
