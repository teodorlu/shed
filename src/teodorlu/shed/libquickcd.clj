(ns teodorlu.shed.libquickcd
  (:require
   [babashka.process :refer [shell]]
   [clojure.java.shell :as shell]
   [clojure.string :as str]))

;; aimed to be called from a ,cd function
;;
;; see https://github.com/teodorlu/shed/tree/master/contrib/quickcd/

(defn fzf [choices]
  (let [fzf-result (shell {:out :string :in (str/join "\n" choices)} "fzf")]
    (when (= 0 (:exit fzf-result))
      (str/trim (:out fzf-result)))))

(defn stupidless [s]
  (println s)
  (println "Press ENTER to continue")
  (read-line)
  )

(defn walk-show-loop-with-exit
  [start show next-loc-options]
  (loop [loc start]
    (show loc)
    (when-let [next-loc (fzf (next-loc-options loc))]
      (recur next-loc))))

(defn animal-walk [start-loc]
  (walk-show-loop-with-exit start-loc
                            stupidless
                            (fn [loc] ["dog" "cat" "cangaroo"])))

(defn -main [& _args]
  (animal-walk "dog")
  ,)
