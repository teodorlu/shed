(ns teodorlu.shed.fuzzlink2
  (:require
   [net.cgrand.enlive-html :as html]
   [clojure.data.xml]
   [clojure.string :as str]))

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

(html/html-resource (java.io.StringReader. (first deref-bodies)))

;; ønsker å finne en h2 som har noe om "her er det artikler"
