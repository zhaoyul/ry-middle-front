(ns ry-middle-front.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[ry-middle-front started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[ry-middle-front has shut down successfully]=-"))
   :middleware identity})
