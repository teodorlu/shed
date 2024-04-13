(ns teodorlu.shed.bbadd
  (:require
   [babashka.process :refer [shell]]
   [clojure.edn :as edn]
   [clojure.string :as str]
   [babashka.fs :as fs]))

;; A ragtag collection of things to add
;;
;; A more beautiful solution would be to use jorm
;;
;;     https://github.com/teodorlu/jorm
;;
;; But I just get stuck in analysis paralysis, so let's start here.

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

#_{:clj-kondo/ignore [:unused-binding]}
(defn add-kaocha [{:keys [root] :as opts}]
  (let [opts (cond-> opts
               (not (:root opts))
               (assoc :root "."))
        root (:root opts)]
    (fs/create-dirs (fs/file root "bin"))
    (fs/create-file (fs/file root "bin" "kaocha") {:posix-file-permissions posix-permissions-executable})
    (spit (fs/file root "bin" "kaocha") kaocha-binstub)
    (shell {:dir root :out nil} "neil add kaocha")))

(defn add-launchpad [])

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
