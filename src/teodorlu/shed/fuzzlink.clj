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

(defn remove-whitespace [x]
  (cond
    (vector? x) (into [] (remove #{"" "\n"} x))
    (seq? x) (remove #{"" "\n"} x)
    :else x))

(defn peek-deref-body [body]
  {:sections
   (->> (htmlparse/parse-fragment body)
        (map htmlparse/as-hiccup)
        (prewalk remove-whitespace))})

(->>
 deref-bodies
 (map peek-deref-body)
 )

(defn -main [& args]
  (prn (count deref-bodies)))
