(ns teodorlu.shed.fuzzlink
  (:require
   [clojure.data.xml]
   [clojure.string :as str]
   [hickory.core :as htmlparse]
   [clojure.walk :refer [prewalk]]))

(def clojure-deref-rss-uri "https://clojure.org/feed.xml")
(def clojure-deref-text (slurp clojure-deref-rss-uri))

(def xml (clojure.data.xml/parse (java.io.StringReader. clojure-deref-text)))

(def deref-bodies
  (->> xml
       :content
       (mapcat :content)
       (mapcat :content)
       (mapcat :content)
       (filter #(str/includes? % "Clojure Deref"))
       (map str/trim)
       (filter #(str/starts-with? %  "<div"))))

(->>
 (map htmlparse/as-hiccup
      (htmlparse/parse-fragment (first deref-bodies)))
 (prewalk (fn [x]
            (if (vector? x)
              (into [] (remove #{} x))
              x)))
 (filter (fn [section]
           (and
            (= :div (first section)))))
 )

(defn -main [& args]
  (prn (count deref-bodies)))
