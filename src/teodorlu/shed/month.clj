(ns teodorlu.shed.month
  (:require
   [babashka.process :refer [shell]]))

(defn -main [& _args]
  (shell "date +%Y-%m"))
