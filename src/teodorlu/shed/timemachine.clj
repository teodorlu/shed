(ns teodorlu.shed.timemachine
  (:require [babashka.fs :as fs]
            [babashka.process :as p]
            [clojure.string :as str]))

;; I want to be able to run CI as-of HEAD (without dirty local changes) easily.
;; CLI draft:
;;
;;   timemachine HEAD -- make test
;;
;; should do the following:
;;
;; 1. Create a new temporary Git worktree tracking HEAD
;; 2. Run `make test` in the new folder, passing stdout to the user
;; 3. Remove the worktree and file folder
;; 4. Return the same exit code as we got from the underlying process
;;
;; Extra options:
;;
;;   --keep - don't delete the worktree and folder after execution

(defn rev-parse [dir git-revision]
  (-> (p/shell {:out :string :dir dir}
               "git rev-parse" git-revision)
      :out str/trim))

(defn worktree-add [dir path commit-ish & [extra-process-opts]]
  (-> (p/shell (merge {:dir dir} extra-process-opts)
               "git worktree add" path commit-ish)
      p/check))

(defn worktree-remove [dir worktree & [extra-process-opts]]
  (-> (p/shell (merge {:dir dir} extra-process-opts)
               "git worktree remove" worktree)
      p/check))

(defn ^{:indent 1} do-at
  "Pass handle-fn a dir argument where dir is the Git repo checked out at given
  Git revision

  git-revision: eg HEAD or 91fa7c32 or trunk
  handle-fn: function of directory where files have been checked out."
  [git-revision handle-fn]
  (let [tempdir (fs/create-temp-dir)
        repo-dir "."
        sha (rev-parse repo-dir git-revision)
        worktree-dir (str (fs/file tempdir sha))]
    (worktree-add repo-dir worktree-dir sha)
    (try
      (handle-fn worktree-dir)
      (finally
        (worktree-remove repo-dir worktree-dir)))))
