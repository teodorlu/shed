(ns teodorlu.shed.libquickcd
  (:require
   [babashka.fs :as fs]
   [babashka.process :as process :refer [shell]]
   [clojure.core :exclude [next]]
   [clojure.string :as str]))

;; aimed to be called from a ,cd function
;;
;; libquickcd must be installed with a shell wrapper for tight shell
;; integration. The binary name (libquickcd) must match the name you installed
;; the babashka script with.
;;
;;   ,cd() {
;;     exec libquickcd "$@"
;;     # or
;;     #
;;     #   exec ,libquickcd "$@"
;;     #
;;     # if you installed with commas
;;   }
;;
;; for more details, see:
;;
;;   https://github.com/teodorlu/shed/tree/master/contrib/quickcd/

(defn fzf [choices]
  (let [fzf-result (shell {:out :string :in (str/join "\n" choices) :continue true} "fzf")]
    (when (= 0 (:exit fzf-result))
      (str/trim (:out fzf-result)))))

(defn shell-init-dir-command [shell loc]
  ;; Note: does not yet handle spaces in loc, I need some escaping.
  (cond (= (fs/file-name shell) "zsh") ["zsh" "-c" (str "cd " (fs/absolutize loc) "; exec zsh")]
        (= (fs/file-name shell) "bash") ["bash" "-c" (str "cd " (fs/absolutize loc) "; exec bash")]
        :else ["bash" "-c" (str "cd " (fs/absolutize loc) "; exec bash")]))

(defn walk-loop
  [{:keys [start next]}]
  (loop [loc start]
    (if-let [next-loc (next loc)]
      (recur next-loc)
      (apply process/exec (shell-init-dir-command (System/getenv "SHELL") (fs/absolutize loc))))))

(defn -main [& args]
  (walk-loop {:start (or (first args) ".")
              :next  (fn [loc] (fzf (sort (map str (fs/list-dir loc)))))})
  ,)
