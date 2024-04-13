(ns teodorlu.shed.bbadd
  (:require
   [babashka.process :refer [shell]]
   [clojure.edn :as edn]
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
;;         bbin install io.github.teodorlu/shed --latest-sha --as bbadd \
;;             --main-opts '["-m" "teodorlu.shed.bbadd/-main"]'

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Some time in the future I'd like to support arbitrary templates as data with
;; Jorm. But for now, let's just write imperative functions.
;;
;;     https://github.com/teodorlu/jorm

(defn parse-neil-line [line]
  (->> (str/split line #"\s+")
       (partition 2)
       (map (fn [[k v]]
              [(edn/read-string k) v]))
       (into {})))

(comment
  (parse-neil-line ":lib com.lambdaisland/launchpad :version 0.28.129-alpha")
  :rcf)

(defn latest-version-from-neil [mvn-coord]
  (let [latest-info (-> (shell {:out :string} "neil dep versions" mvn-coord)
                        :out
                        str/split-lines
                        first
                        parse-neil-line)]
    (:version latest-info)))

(def launchpad-coord 'com.lambdaisland/launchpad)
(def kaocha-coord 'lambdaisland/kaocha)
(def posix-permissions-executable "rwxr-xr-x")

(def kaocha-binstub (str/trim "
mkdir -p bin
echo '#!/usr/bin/env sh' > bin/kaocha
echo 'clojure -M:kaocha \"$@\"' >> bin/kaocha
chmod +x bin/kaocha
"
                     ))

(def launcpad-binstub (str/trim "
#!/usr/bin/env bb

(require '[lambdaisland.launchpad :as launchpad])

(launchpad/main {})

;; (launchpad/main {:steps (into [(partial launchpad/ensure-java-version 17)]
;;                               launchpad/default-steps)})
"))

(comment
  (latest-version-from-neil launchpad-coord)
  ;; => "0.28.129-alpha"

  (latest-version-from-neil kaocha-coord)
  ;; => "1.88.1376"
  :rcf)

(defn add-kaocha [{:keys [root]}]
  (let [root (or root ".")]
    (fs/create-dirs (fs/file root "bin"))
    (fs/create-file (fs/file root "bin" "kaocha") {:posix-file-permissions posix-permissions-executable})
    (spit (fs/file root "bin" "kaocha") kaocha-binstub)
    (shell {:dir root :out nil} "neil add kaocha")))

(defn add-launchpad [{:keys [root]}]
  (let [root (or root ".")]
    (fs/create-dirs (fs/file root "bin"))
    (fs/create-file (fs/file root "bin" "launchpad") {:posix-file-permissions posix-permissions-executable})
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
(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))
