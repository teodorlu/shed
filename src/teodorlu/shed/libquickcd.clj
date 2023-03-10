(ns teodorlu.shed.libquickcd
  (:require
   [babashka.process :refer [shell]]
   [clojure.java.shell :as shell]
   [babashka.fs :as fs]
   [clojure.string :as str]))

;; aimed to be called from a ,cd function
;;
;; see https://github.com/teodorlu/shed/tree/master/contrib/quickcd/

(defn fzf [choices]
  (let [fzf-result (shell {:out :string :in (str/join "\n" choices)} "fzf")]
    (when (= 0 (:exit fzf-result))
      (str/trim (:out fzf-result)))))

(defn lessless [s]
  (println s)
  (println "Press ENTER to continue")
  (read-line))

(defn walk-show-loop-with-exit
  [start show next-loc-options]
  (loop [loc start]
    (show loc)
    (when-let [next-loc (fzf (next-loc-options loc))]
      (recur next-loc))))

(defn -main [& args]
  (walk-show-loop-with-exit (or (first args) ".")
                            (constantly nil)
                            (fn [loc]
                              (sort (map str (fs/list-dir loc)))))
  ,)
