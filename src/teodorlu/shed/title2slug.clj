(ns teodorlu.shed.title2slug
  (:require
    [clojure.string :as str]))

;; convert from
;;
;;     Parametrisert analyse av søylepark med Unicad og Excel
;;
;; to
;;
;;     parametrisert-analyse-av-soylepark-med-unicad-og-excel

(defn dash-separated [s]
  (str/join "-" (str/split s #"\s+")))

(defn title2slug [s]
  (-> s
      str/lower-case
      (str/replace #"æ" "a")
      (str/replace #"ø" "o")
      (str/replace #"å" "a")
      dash-separated))

(comment
  (title2slug "Parametrisert analyse av søylepark med Unicad og Excel")
  ;; => "parametrisert-analyse-av-soylepark-med-unicad-og-excel"
  )

(defn -main [& args]
  (println (title2slug (str/join " " args))))

;; usage:
;;
;;   $ ,title2slug Parametrisert analyse av søylepark med Unicad og Excel
;;
