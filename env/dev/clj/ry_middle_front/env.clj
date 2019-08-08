(ns ry-middle-front.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [ry-middle-front.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[ry-middle-front started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[ry-middle-front has shut down successfully]=-"))
   :middleware wrap-dev})
