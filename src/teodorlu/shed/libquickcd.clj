(ns teodorlu.shed.libquickcd
  (:require
   [babashka.process :refer [shell]]
   [clojure.edn :as edn]
   [clojure.string :as str]
   [babashka.fs :as fs]))

;; aimed to be called from a ,cd function
;;
;; see https://github.com/teodorlu/shed/tree/master/contrib/quickcd/

(defn fzf [choices]
  (let [fzf-result (shell {:out :string :in (str/join "\n" choices)} "fzf")]
    (when (= 0 (:exit fzf-result))
      (str/trim (:out fzf-result)))))

(defn edn-parse-orelse [s orelse]
  (try (edn/read-string s)
       (catch Exception _ orelse)))

(defn fzf-edn [choices default]
  (let [next (fzf (map pr-str choices))]
    (edn-parse-orelse next default)))

(defn walk-show-loop-with-exit
  [start show next-loc quit?]
  (loop [loc start]
    (show loc)
    (when-let [next-loc (fzf-edn (next-loc loc) nil)]
      (when-not (quit? next-loc)
        (recur next-loc)))))

(defn less [s]
  (shell {:in s} "less"))

(defn ls [loc]
  (shell {:out :string :dir loc} "ls"))

(defn file-walk [start-loc]
  (let [pager less
        show (fn [loc] (pager (ls loc)))
        next-loc (fn [loc]
                   (let [subdirs (map str/trim (str/split-lines (:out (shell {:dir loc :out :string} "ls"))))]
                     (fzf-edn subdirs nil)))]
    (walk-show-loop-with-exit start-loc
                              show
                              next-loc
                              (fn quit? [loc] (= :quit loc)))))



(defn -main [& _args]
  (file-walk (str (fs/cwd)))
  #_#_

  (prn "libquickcd")
  (shell "ls")
  ,)
