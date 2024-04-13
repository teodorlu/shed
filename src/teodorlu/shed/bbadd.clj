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
echo 'clojure -M:test \"$@\"' >> bin/kaocha
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
(defn add-kaocha [{:keys [root version] :as opts}]
  (let [opts (cond-> opts
               (not (:version opts))
               (assoc :version (latest-version-from-neil kaocha-coord))

               (not (:root opts))
               (assoc :root "."))
        root (:root opts)
        version (:version opts)]
    (fs/create-dirs (fs/file root "bin"))
    (fs/create-file (fs/file root "bin " "kaocha") {:posix-file-permissions posix-permissions-executable})
    (spit (fs/file root "bin " "kaocha") kaocha-binstub)
    ;; TODO
    ;;
    ;; I figured I now have to edit deps.edn, and that's better done for neil.
    ;; Perhaps Neil should provide a "plumbing" API for editing deps.edn and bb.edn?
    ))

(fs/create-file "teodor.sh" {:posix-file-permissions "rwxr-xr-x"})

(defn add-launchpad [])

(defn valid-argv? [argv]
  (and (= 1 (count argv))
       (or (= "kaocha" (first argv))
           (= "launchpad" (first argv)))))

(defn -main [& argv]
  (when (not (valid-argv? argv))
    (println "invalid arguments:" (pr-str argv))
    (println "Usage: bbadd kaocha or bbadd launchpad")
    (System/exit 1))
  ((get {"kaocha" add-kaocha
         "launchpad" add-launchpad}
        (first argv))))
