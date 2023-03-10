(ns teodorlu.shed.libquickcd
  (:require
   [babashka.fs :as fs]
   [babashka.process :refer [shell]]
   [clojure.string :as str]))

;; aimed to be called from a ,cd function
;;
;; for installation instructions, see:
;;
;;   https://github.com/teodorlu/shed/tree/master/contrib/quickcd/

(defn fzf [choices]
  (let [fzf-result (shell {:out :string :in (str/join "\n" choices) :continue true} "fzf")]
    (when (= 0 (:exit fzf-result))
      (str/trim (:out fzf-result)))))

(defn walk-select-loop
  [start next-loc]
  (loop [loc start]
    (when-let [next-loc (fzf (next-loc loc))]
      (recur next-loc))))

(defn -main [& args]
  (walk-select-loop (or (first args) ".")
                  (fn [loc]
                    (sort (map str (fs/list-dir loc)))))
  ,)
