(ns user
  "Userspace functions you can run by default in your local REPL."
  (:require
    [ry-middle-front.config :refer [env]]
    [clojure.spec.alpha :as s]
    [expound.alpha :as expound]
    [mount.core :as mount]
    [ry-middle-front.core :refer [start-app]]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(add-tap (bound-fn* clojure.pprint/pprint))

(defn start 
  "Starts application.
  You'll usually want to run this on startup."
  []
  (mount/start-without #'ry-middle-front.core/repl-server))

(defn stop 
  "Stops application."
  []
  (mount/stop-except #'ry-middle-front.core/repl-server))

(defn restart 
  "Restarts application."
  []
  (stop)
  (start))


