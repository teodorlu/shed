(ns teodorlu.shed.clojure-deref-fzf-tui
  (:require
   [clojure.xml]
   [clojure.data.xml]))

(comment

  (def clojure-deref-rss-uri "https://clojure.org/feed.xml")

  (def clojure-deref-text (slurp clojure-deref-rss-uri))

  (def clojure-deref-data
    (-> clojure-deref-text
        (.getBytes "UTF-8")
        (java.io.ByteArrayInputStream.)
        clojure.xml/parse)))

(def clojure-deref-rss-uri "https://clojure.org/feed.xml")
(def clojure-deref-text (slurp clojure-deref-rss-uri))

(def xml (clojure.data.xml/parse (java.io.StringReader. clojure-deref-text)))
