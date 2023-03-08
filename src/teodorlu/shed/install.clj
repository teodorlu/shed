#!/usr/bin/env bb

(ns teodorlu.shed.install
  (:require [clojure.string :as str]
            [babashka.process :refer [shell]]))

;; a shed script to install all the shed scripts, lol

(def project-root (str/trim (:out (shell {:out :string}
                                         "git rev-parse --show-toplevel"))))

(defn shell-project-root
  ([s] (shell-project-root {} s))
  ([opts s]
   (shell (merge {:dir project-root} opts) s)))

(defn -main [& _args]
  (shell-project-root "bbin install . --as ,update-repos --main-opts '[\"-m\" \"teodorlu.shed.update-repos/-main\"]'")
  (shell-project-root "bbin install . --as ,browsetxt    --main-opts '[\"-m\" \"teodorlu.shed.browsetxt/-main\"]'"))

;; trigger main when this script is run as an executable
;; see babashka book: https://book.babashka.org/#main_file

(when (= *file* (System/getProperty "babashka.file"))
  (-main))
