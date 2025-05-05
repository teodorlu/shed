(ns teodorlu.shed.gcloudfriendly
  (:require [babashka.process :as p]))

(defn logged-in? []
  (zero? (:exit (p/shell {:continue true} "gcloud auth print-access-token"))))
#_(logged-in?)
