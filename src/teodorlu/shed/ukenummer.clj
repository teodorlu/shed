(ns teodorlu.shed.ukenummer
  (:require [babashka.process :refer [shell]]))

(defn -main [& _args]
  (shell "date +%V"))
