#!/usr/bin/env bb

(ns teodorlu.shed.install
  (:require [clojure.string :as str]
            [babashka.process :refer [shell]]))

;; a shed script to install all the shed scripts, lol
;;
;; this file is executable, and can be run as a standalone babashka script:
;;
;;    bb ./src/teodorlu/shed/install.clj

(def project-root (str/trim (:out (shell {:out :string}
                                         "git rev-parse --show-toplevel"))))

(defn shell-project-root
  ([s] (shell-project-root {} s))
  ([opts s]
   (shell (merge {:dir project-root} opts) s)))

(defn install-script! [script]
  (let [as (str "," script)
        main-opts (str "'[\"-m\" \"teodorlu.shed." script "/-main\"]'") ]
    (shell-project-root (str/join " " ["bbin" "install" "." "--as" as "--main-opts" main-opts]))))

(defn -main [& _args]
  (install-script! "browsetxt")
  (install-script! "month")
  (install-script! "path-lines")
  (install-script! "quick-clone")
  (install-script! "ukenummer")
  (install-script! "update-repos")
  ,)

;; trigger main when this script is run as an executable
;; see babashka book: https://book.babashka.org/#main_file
(when (= *file* (System/getProperty "babashka.file"))
  (-main))
