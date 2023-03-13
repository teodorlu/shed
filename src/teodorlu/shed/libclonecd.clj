(ns teodorlu.shed.libclonecd
  (:require
   [clojure.string :as str]
   [babashka.fs :as fs]
   [babashka.process :refer [shell]]))

;; shell alias: ,clonecd
;;
;; this library does the heavy lifting
;;
;; we can implement ,clonecd in terms of libclonecd and shellexec

;; Examples
;;
;;      $ libclonecd teodorlu/shed
;;      !SHELLEVAL cd /home/teodorlu/dev/teodorlu/shed

;; ignore parameterization while prototyping.
;;
;; git repos are stored in ~/dev/ORGANIZATION/PROJECT
;;
;; a REPOSPEC is ORG/PROJECT as a string, or {:org ORG :project PROJECT} as data

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn repospec->path [repospec]
  (let [{:keys [org project]} repospec]
    (str (fs/expand-home "~/dev") "/" org "/" project)))

(defn repospec->git-clone-arg [repospec]
  (let [{:keys [org project]} repospec]
    (str "git@github.com:" org "/" project ".git")))

(defn run [repospec]
  (let [path (repospec->path repospec)]
    (if (fs/exists? path)
      (println "!SHELLEVAL cd" path)
      (do
        (shell ["git" "clone" (repospec->git-clone-arg repospec)])
        (println "!SHELLEVAL cd" path)))))

(defn str->repospec [argv]
  (let [[org project] (str/split (first argv) #"/")]
    {:org org :project project}))

(defn valid-args? [argv]
  (and argv
       (first argv)
       (= 2 (count (str/split (first argv) #"/")))))

(defn -main [& argv]
  (when (not (valid-args? argv))
    (println "invalid arguments:" (pr-str argv))
    (println "Usage: libclonecd ORG/REPO")
    (System/exit 1))
  (let [repospec (str->repospec argv)]
    (run repospec)))
