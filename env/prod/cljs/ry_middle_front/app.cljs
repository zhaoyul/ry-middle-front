(ns ry-middle-front.app
  (:require [ry-middle-front.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init! false)
