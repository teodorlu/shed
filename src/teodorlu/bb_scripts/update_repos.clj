(ns teodorlu.bb-scripts.update-repos
  (:require
   [babashka.fs :as fs]
   [babashka.process :refer [shell]]
   [clojure.string :as str]))

(defn current-branch-name
  "Return the current repo branch name as a string, or nil"
  [path]
  (let [{:keys [exit out]}
        (shell {:out :string, :continue true, :dir path}
               "git symbolic-ref --short HEAD")]
    (when (zero? exit)
      (str/trim out))))

(defn pending-changes? [path]
  (not (str/blank? (str/trim  (:out (shell {:out :string, :continue true, :dir path}
                                           "git status --short"))))))

(defn do-update-repos! [path]
  (doseq [path (->> (fs/glob path "**/.git" {:hidden true})
                    (map fs/parent)
                    (map str)
                    (filter (fn [path]
                              (and (#{"main" "master"} (current-branch-name path))
                                   (not (pending-changes? path))))))]
    (println "update-repos: updating" path)
    (shell {:dir path, :continue true}
           "git pull")))

(comment
  ;; motivation: I learn a lot from reading other people's source code. That
  ;; leaves me with lots of repos I've cloned, yet never done anything to. So I
  ;; want a 1-op "update everyting command".
  ;;
  ;; Apparently, I've got 245 repos right now.

  (defn count-repos [path]
    (count (fs/glob path "**/.git" {:hidden true})))
  (count-repos (fs/expand-home "~/dev"))
  ;; => 245
  )

(comment
  ;; destructuring question on Slack
  ;; https://clojurians.slack.com/archives/C053AK3F9/p1678280301686469

  (let [x {:type "test" :meta {:rate 0.1}}
        {type :type} x
        {{rate :rate} :meta} x
        ]
    [type rate])
  ;; => ["test" 0.1]

  (let [{type :type {:keys [rate]} :meta}
        {:type "test" :meta {:rate 0.1}}]
    [type rate])
  ;; => ["test" 0.1]

  (let [{:keys [type] {:keys [rate]} :meta}
        {:type "test" :meta {:rate 0.1}}]
    [type rate])
  ;; => ["test" 0.1]

  (let [m {:type "test" :meta {:rate 0.1}}
        f (fn [{:keys [type] {:keys [rate]} :meta}] [type rate])]
    (f m))
  ;; => ["test" 0.1]


  )

(defn -main [& _args]
  (do-update-repos! (fs/cwd)))
