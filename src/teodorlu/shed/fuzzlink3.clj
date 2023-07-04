(ns teodorlu.shed.fuzzlink3
  (:require
   [net.cgrand.enlive-html :as html]))

;; fuzzlink was a bit hard to create
;;
;; So I start with something easier.
;;
;; Following David Nolen's enlive tutorial: https://github.com/swannodette/enlive-tutorial/
;;
;; Copy-paste from https://github.com/swannodette/enlive-tutorial/blob/master/src/tutorial/scrape1.clj:

(def ^:dynamic *base-url* "https://news.ycombinator.com/")

(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn hn-headlines []
  (map html/text (html/select (fetch-url *base-url*) [:td.title :a])))

(defn hn-points []
  (map html/text (html/select (fetch-url *base-url*) [:td.subtext :> :span.subline html/first-child])))

(defn print-headlines-and-points []
  (doseq [line (map #(str %1 " (" %2 ")") (hn-headlines) (hn-points))]
    (println line)))

(defn requiring-resolve* [sym]
  (try
    (requiring-resolve sym)
    (catch Exception e
      nil)))

(requiring-resolve* 'teodorlu.shed.abc/def)

(when-let [table (requiring-resolve 'nextjournal.clerk/table)]
  (table (map (fn [headline points]
                {"headline" headline "points" points})
              (hn-headlines) (hn-points))))
