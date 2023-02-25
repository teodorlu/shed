(ns teodorlu.bb-scripts.update-repos
  (:require
   [babashka.fs :as fs]
   [babashka.process :refer [shell]]
   [clojure.string :as str]))

(defn current-branch-name
  "Return the current repo branch name as a string, or nil"
  [path]
  (let [{:keys [exit out]}
        (shell '[git symbolic-ref --short HEAD]
               {:out :string
                :continue true
                :dir path})]
    (when (zero? exit)
      (str/trim out))))

(defn pending-changes? [path]
  (not (str/blank? (str/trim  (:out (shell '[git status --short]
                                           {:out :string
                                            :continue true
                                            :dir path}))))))

(defn do-update-repos! [path]
  (doseq [path (->> (fs/glob path "**/.git" {:hidden true})
                    (map fs/parent)
                    (map str)
                    (filter (fn [path]
                              (and (#{"main" "master"} (current-branch-name path))
                                   (not (pending-changes? path))))))]
    (println "update-repos: updating" path)
    (shell "git pull" {:dir path
                       :continue true})))

(defn -main [& args]
  (do-update-repos! (fs/cwd)))
