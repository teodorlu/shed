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

(defn install-script! [as main-opts]
  (shell-project-root (str/join " " ["bbin" "install" "." "--as" as "--main-opts" main-opts])))

(defn -main [& _args]
  (install-script! ",update-repos" "'[\"-m\" \"teodorlu.shed.update-repos/-main\"]'")
  (install-script! ",browsetxt"    "'[\"-m\" \"teodorlu.shed.browsetxt/-main\"]'")
  (install-script! ",path-lines"   "'[\"-m\" \"teodorlu.shed.path-lines/-main\"]'")
  )

;; trigger main when this script is run as an executable
;; see babashka book: https://book.babashka.org/#main_file
(when (= *file* (System/getProperty "babashka.file"))
  (-main))
