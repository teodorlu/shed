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

(comment
  (-> (p/shell {:out :string} "git status") :out str/split-lines)
  ;; => ["On branch master"
  ;;     "Your branch is ahead of 'origin/master' by 3 commits."
  ;;     "  (use \"git push\" to publish your local commits)"
  ;;     ""
  ;;     "Untracked files:"
  ;;     "  (use \"git add <file>...\" to include in what will be committed)"
  ;;     "\tsrc/teodorlu/shed/timemachine.clj"
  ;;     "\ttest/teodorlu/shed/timemachine_test.clj"
  ;;     ""
  ;;     "nothing added to commit but untracked files present (use \"git add\" to track)"]

  (-> (p/shell {:out :string} "git status --porcelain") :out str/split-lines)
  ;; => ["?? src/teodorlu/shed/timemachine.clj"
  ;;     "?? test/teodorlu/shed/timemachine_test.clj"]

  (defonce identifier (str (random-uuid)))
  (defonce tempdir (fs/create-temp-dir))

  (spit (fs/file tempdir "lol.txt") "haha lol")
  (map fs/file-name (fs/list-dir tempdir))
  (fs/delete (fs/file tempdir "lol.txt"))

  )
