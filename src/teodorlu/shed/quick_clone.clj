(ns teodorlu.shed.quick-clone
  (:require [babashka.process :refer [shell]]))

(defn -main [repo]
  (shell ["teod-git-clone" repo]))
