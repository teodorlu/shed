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

(defn install-script!
  ([main] (install-script! main (str "," main)))
  ([main as]
   (let [main-opts (str "'[\"-m\" \"teodorlu.shed." main "/-main\"]'") ]
     (println "Installing:" as)
     (shell-project-root (str/join " " ["bbin" "install" "." "--as" as "--main-opts" main-opts])))))

(defn -main [& _args]
  (install-script! "browsetxt")
  (install-script! "fuzzlink")
  (install-script! "kanshibb")
  (install-script! "libclonecd")
  (install-script! "libquickcd")
  (install-script! "month")
  (install-script! "path-lines")
  (install-script! "quick-clone")
  (install-script! "shortid")
  (install-script! "title2slug")
  (install-script! "ukenummer")
  (install-script! "update-repos")
  (install-script! "bbadd")

  ,)

;; trigger main when this script is run as an executable
;; see babashka book: https://book.babashka.org/#main_file
(when (= *file* (System/getProperty "babashka.file"))
  (-main))

(comment
  (-main)
  )
