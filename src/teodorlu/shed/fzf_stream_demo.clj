(ns teodorlu.shed.fzf-stream-demo
  (:require [babashka.process :refer [shell process]]))

;; purpose: try streaming with fzf and babashka/process

(defn stream-demo []
  (prn 123)
  (let [ls-handle (shell "ls")]
    @(shell {:in (:out ls-handle)} "fzf")
    nil))

(defn stream-demo-2 []
  (prn 123)
  (let [ls-handle (shell "process")]
    @(shell {:in (:out ls-handle)} "fzf")
    nil))

(defn demo-3 []
  (-> (process "ls")
      (select-keys [:out])
      (shell "fzf")))

(defn demo-4 []
  (let [{:keys [out]} (process "ls")]
    (shell {:in out} "fzf")))

(defn -main [& _args]
  #_
  (stream-demo)
  (demo-4)
  ,)

;; trigger main when this script is run as an executable
;; see babashka book: https://book.babashka.org/#main_file
(when (= *file* (System/getProperty "babashka.file"))
  (-main))
