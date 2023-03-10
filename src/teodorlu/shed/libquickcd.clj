(ns teodorlu.shed.libquickcd
  (:require
   [babashka.fs :as fs]
   [babashka.process :refer [shell]]
   [clojure.string :as str]
   [clojure.core :exclude [next]]))

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
  [{:keys [start next]}]
  (loop [loc start]
    (when-let [next-loc (fzf (next loc))]
      (recur next-loc))))

(defn -main [& args]
  (walk-select-loop {:start (or (first args) ".")
                     :next  (fn [loc] (sort (map str (fs/list-dir loc))))})
  ,)

;; It would be interesting to try the same pattern from Emacs.
;;
;;   List next options: lines in a buffer.
;;   Pick next: RET
