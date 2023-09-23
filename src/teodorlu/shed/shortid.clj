(ns teodorlu.shed.shortid
  (:require
   [babashka.cli :as cli]))

(def alphabet "abcdefghijklmnopqrstuvwxyz01234567890")

(defn shortid [n]
  (apply str (repeatedly n #(rand-nth alphabet))))

(defn -main [& args]
  (let [opts (merge {:n 6}
                    (cli/parse-opts args))]
    (println (shortid (:n opts)))))

(comment
  (cli/parse-opts ["-n" "10"])

  )
