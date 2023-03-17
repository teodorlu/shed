(ns teodorlu.shed.libclonecd
  (:require
   [clojure.string :as str]
   [babashka.fs :as fs]
   [babashka.process :refer [shell]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; HOW TO INSTALL, HOW TO USE
;;
;; See https://github.com/teodorlu/shed/tree/master/contrib/clonecd/README.org

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; MODULE DOCUMENTATION
;;
;; a REPOSPEC represents a Github repository, qualified with its organization.
;;
;; Repospec as data:
;;
;;     {:org "babashka" :project "neil}
;;
;; Repospec as github http url:
;;
;;     https://github.com/babashka/neil
;;
;; Repospec as local folder:
;;
;;     ~/dev/babashka/neil

(defn repospec->repo-path [repospec]
  (let [{:keys [org project]} repospec]
    (str (fs/expand-home "~/dev") "/" org "/" project)))

(defn repospec->org-path [repospec]
  (let [{:keys [org]} repospec]
    (str (fs/expand-home "~/dev") "/" org)))

(defn repospec->git-url [repospec]
  (let [{:keys [org project]} repospec]
    (str "git@github.com:" org "/" project ".git")))

(defn run [repospec]
  (let [path (repospec->repo-path repospec)]
    (if (fs/exists? path)
      (println "!SHELLEVAL cd" path)
      (do
        (fs/create-dirs (repospec->org-path repospec))
        (shell ["git" "clone" (repospec->git-url repospec) (repospec->repo-path repospec)])
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
