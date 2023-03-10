(ns teodorlu.shed.libquickcd
  (:require
   [babashka.fs :as fs]
   [babashka.process :as process :refer [shell]]
   [clojure.core :exclude [next]]
   [clojure.java.io :as io]
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

(def -program-resolver (requiring-resolve 'babashka.process/-program-resolver))
(def default-escape (requiring-resolve 'babashka.process/-program-resolver))
(defmacro ^:no-doc
  if-has-exec [then else]
  (if (requiring-resolve 'babashka.process/has-exec?)
    then else))

(defn exec2
  "Copied from babashka.process/exec -- but modified to support :dir"

  {:arglists '([opts? & args])}
  [& args]
  (let [{:keys [cmd opts]} (process/parse-args args)]
    (let [{:keys [escape env extra-env]
           :or {escape default-escape}
           :as opts} opts
          cmd (if (and (string? cmd)
                       (not (.exists (io/file cmd))))
                (process/tokenize cmd)
                cmd)
          str-fn (comp escape str)
          _ (prn {:mapv mapv :str-fn str-fn :cmd cmd})
          cmd (mapv str-fn cmd)
          _ (prn "confused.")
          arg0 (or (:arg0 opts)
                   (first cmd))
          cmd (let [program-resolver (:program-resolver opts -program-resolver)
                    [program & args] cmd]
                (into [(program-resolver program)] args))
          [program & args] cmd
          args (cons arg0 args)
          ^java.util.Map env (into (or env (into {} (System/getenv))) extra-env)]
      (if-has-exec
       (org.graalvm.nativeimage.ProcessProperties/exec (or (:dir opts) (fs/path program))
                                                       (into-array String args)
                                                       env)
       (throw (ex-info "exec is not supported in non-GraalVM environments" {:cmd cmd}))))))

(defn walk-loop
  [{:keys [start next]}]
  (loop [loc start]
    (if-let [next-loc (next loc)]
      (recur next-loc)
      (do
        (prn loc)
        (prn (fs/absolutize loc))
        (read-line)
        (exec2 #_ {:dir "/tmp" #_ (fs/absolutize loc)}
               (or (System/getenv "SHELL") "bash"))))))

(defn -main [& args]
  (walk-loop {:start (or (first args) ".")
              :next  (fn [loc] (fzf (sort (map str (fs/list-dir loc)))))})
  ,)


(comment


  )
