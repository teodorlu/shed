(ns teodorlu.shed.bbadd
  (:require
   [babashka.process :refer [shell]]
   [clojure.string :as str]
   [babashka.fs :as fs]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; bbadd: A few things one might wish to add to Clojure projects.
;;
;; Currently supported:
;;
;; - lambdaisland/kaocha - a test runner
;; - lambdaisland/launchpad - a REPL launcher

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; HOW TO INSTALL THIS SCRIPT
;;
;; 1. You can copy the file, make it executable and add a babashka shebang.
;;
;; 2. Or you can use bbin:
;;
;;         bbin install io.github.teodorlu/shed --latest-sha --as bbadd  --main-opts '["-m" "teodorlu.shed.bbadd/-main"]'

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Some time in the future I'd like to support arbitrary templates as data with
;; teodorlu/jorm. But for now, let's just write imperative functions.

(def launchpad-coord 'com.lambdaisland/launchpad)
(def kaocha-coord 'lambdaisland/kaocha)
(def posix-permissions-executable "rwxr-xr-x")

(defn normalize-file-string
  "Ensures that s has no leading or trailing whitespace other than a single newline"
  [s]
  (str (str/trim s) "\n"))

(def kaocha-binstub (normalize-file-string "
#!/usr/bin/env sh
clojure -M:kaocha \"$@\"
"
                              ))

(def launcpad-binstub (normalize-file-string "
#!/usr/bin/env bb

(require '[lambdaisland.launchpad :as launchpad])

(launchpad/main {})

;; (launchpad/main {:steps (into [(partial launchpad/ensure-java-version 17)]
;;                               launchpad/default-steps)})
"))

(defn add-kaocha [{:keys [root]}]
  (let [root (or root ".")]
    (fs/create-dirs (fs/file root "bin"))
    (when (not (fs/exists? (fs/file root "bin" "kaocha")))
      (fs/create-file (fs/file root "bin" "kaocha") {:posix-file-permissions posix-permissions-executable}))
    (spit (fs/file root "bin" "kaocha") kaocha-binstub)
    (shell {:dir root :out nil} "neil add kaocha")))

(defn add-launchpad [{:keys [root]}]
  (let [root (or root ".")]
    (fs/create-dirs (fs/file root "bin"))
    (when (not (fs/exists? (fs/file root "bin" "launchpad")))
      (fs/create-file (fs/file root "bin" "launchpad") {:posix-file-permissions posix-permissions-executable}))
    (spit (fs/file root "bin" "launchpad") launcpad-binstub)
    (shell {:dir root :out nil} "neil dep add" "--lib" launchpad-coord "--deps-file" "bb.edn")))

(defn valid-argv? [argv]
  (and (= 1 (count argv))
       (#{"kaocha" "launchpad"} (first argv))))

(defn -main [& argv]
  (when (not (valid-argv? argv))
    (println "invalid arguments:" (pr-str argv))
    (println "Usage:

  bbadd kaocha

or

  bbadd launchpad")
    (System/exit 1))
  ((get {"kaocha" add-kaocha
         "launchpad" add-launchpad}
        (first argv))
   {}))

;; Support running as a standalone script too
;;
;; If you want to run as a standalone script, remember
;;
;; - add a shebang (#!/usr/bin/env bb) to the top of the file
;; - make the file executable.
(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))
