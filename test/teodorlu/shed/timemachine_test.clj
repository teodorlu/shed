(ns teodorlu.shed.timemachine-test
  (:require [babashka.fs :as fs]
            [clojure.test :refer [deftest is]]
            [teodorlu.shed.timemachine :as timemachine]))

(deftest rev-parse
  (is (= (timemachine/rev-parse "." "b6f4472")
         "b6f44726b0d8f668467865c6943034c93c8e9698"))
  (is (not= (timemachine/rev-parse "." "master")
            "a28876b"))
  )

(deftest worktree-add-remove
  (let [tempdir (fs/create-temp-dir)
        sha "b48384b87e5dbbc9492fe34020cc360b49280a9c"
        repo-dir "."
        worktree-dir (str (fs/file tempdir sha))
        status #(when (fs/exists? worktree-dir)
                  (fs/list-dir worktree-dir))]
    (is (empty? (status)))
    (timemachine/worktree-add repo-dir worktree-dir sha)
    (try
      (is (not-empty (status)))
      (finally
        (timemachine/worktree-remove repo-dir worktree-dir)))
    (is (empty? (status)))))

(deftest at-revision-do
  (is (= (timemachine/do-at "HEAD" (constantly ::result))
         ::result))

  (is (contains? (timemachine/do-at "b48384b87e5dbbc9492fe34020cc360b49280a9c"
                   (fn [dir]
                     (->> (fs/list-dir dir)
                          (map fs/file-name)
                          (into (sorted-set)))))
                 "bb.edn"))
  )
