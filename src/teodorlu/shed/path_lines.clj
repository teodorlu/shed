(ns teodorlu.shed.path-lines
  (:require
   [clojure.string :as str]))

;; Print $PATH - per line.

(defn -main [& _args]
  (doseq [x (sort (str/split (System/getenv "PATH") #":"))]
    (println x)))
