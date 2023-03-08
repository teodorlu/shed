(ns teodorlu.shed.quick-clone
  (:require
   [babashka.cli :as cli]
   [babashka.process :refer [shell]]
   [clojure.string :as str]))

(defn -main [& argv]
  (let [{:keys [out exit]}
        (apply shell {:out :string} "teod-git-clone" argv)]
    (if (= 0 exit)
      (println "!SHELLEVAL" (last (str/split-lines out)))
      (println (str/trim out)))))

(comment
  (vec (for [s ["clone repo"
                "clone repo"
                "clone repo -h -v"
                "-h clone repo"
                "-h -- clone repo"]]
         [s (cli/parse-args (str/split s #"\s+"))]))
  #_=>
  [["clone repo"       {:args ["clone" "repo"] , :opts {}}]
   ["clone repo"       {:args ["clone" "repo"] , :opts {}}]
   ["clone repo -h -v" {:args ["clone" "repo"] , :opts {:h true, :v true}}]
   ["-h clone repo"    {:args ["repo"]         , :opts {:h "clone"}}]
   ["-h -- clone repo" {:args ["clone" "repo"] , :opts {:h true}}]]

  )
