(ns teodorlu.shed.browsetxt
  (:require
   [babashka.process :as process]
   [babashka.cli :as cli]
   [clojure.edn :as edn]
   [clojure.string :as str]
   [cheshire.core :as json]
   [clojure.walk :as walk]))

(defn fzf
  "Choose a thing with fzf

  (fzf [\"apples\" \"pears\" \"pizza\")
  ;; => \"apples\"     ; depending on the user's choice!

  returns nil on failure."
  [choices]
  (let [fzf-result (process/shell {:out :string
                                   :in (str/join "\n" choices)}
                                  "fzf")]
    (when (= 0 (:exit fzf-result))
      (str/trim (:out fzf-result)))))

(defn less [s]
  (process/shell {:in s} "less"))

(defn bat-markdown
  "bat-markdown ~ batman"
  [s]
  (process/shell {:in s} "bat" "--paging" "always" "--file-name" "browsetxt.md"))

(defn edn-parse-orelse [s orelse]
  (try (edn/read-string s)
       (catch Exception _ orelse)))

(defn fzf-edn [choices default]
  (let [next (fzf (map pr-str choices))]
    (edn-parse-orelse next default)))

(defn walk-show-loop-with-exit
  [start show next-loc quit?]
  (loop [loc start]
    ;; (prn [:on loc])
    (show loc)
    (when-let [next-loc (fzf-edn (next-loc loc) nil)]
      (when-not (quit? next-loc)
        (recur next-loc)))))

(defn pandoc-url->md [url]
  (let [cmd ["pandoc" "--reference-links" url "-t" "markdown"]]
    (:out (apply process/shell {:out :string} cmd))))

(defn pandoc-url->plain [url]
  (let [cmd ["pandoc" url "-t" "plain"]]
    (:out (apply process/shell {:out :string} cmd))))

(defn pandoc-url->data [url]
  (let [process-result (process/shell {:out :string} "pandoc" "-t" "json" url)]
    (when (= 0 (:exit process-result))
      (json/parse-string (:out process-result) keyword))))

(defn pandoc-inline->plain
  "Note: this function is SLOW.

  It shells out to pandoc for single links, which causes tremendous overhead
  starting and stopping functions. A pure Clojure og pure java implementation
  would be way faster. A pod would help too, then we don't have to restart
  pandoc."
  [inline]
  (when (vector? inline)
    (let [data {:pandoc-api-version [1 22 2 1], :meta {}, :blocks [{:t "Para" :c  inline}]}
          pandoc-result (process/shell {:out :string :in (json/generate-string data)}
                                       "pandoc" "-f" "json" "-t" "plain")]
      (when (= 0 (:exit pandoc-result))
        (str/trim (:out pandoc-result))))))

(comment
  (pandoc-inline->plain [{:t "Str" :c "tihi"}])

  )

(defn page->links
  "Extract a sequence of liks from an url"
  [url]
  (let [link-nodes (atom [])
        resolve-link (fn [origin target]
                       (str (.normalize (.resolve (.normalize (java.net.URI. (str origin "/")))
                                                  target))))]
    (walk/prewalk (fn [x] (when (= "Link" (:t x))
                            (swap! link-nodes conj x))
                    x)
                  (-> url pandoc-url->data :blocks))
    (->> @link-nodes
         (map (fn [link]
                {:url (resolve-link url (get-in link [:c 2 0]))}))
         (remove (comp nil? :url)))))

(comment
  (let [link {:t "Link", :c [["" [] []] [{:t "Str", :c "Aphorisms"}] ["./aphorisms/" ""]]}]
    (get-in link [:c 2 0]))

  (let [link {:t "Link", :c [["" [] []] [{:t "Str", :c "Aphorisms"}] ["./aphorisms/" ""]]}
        linktext (get-in link [:c 1])]
    (pandoc-inline->plain linktext))

  )

(defn url-walk [startpage opts]
    (let [pager (cond (:bat-markdown opts) bat-markdown
                      :else less)
          show (fn [loc]
                 (cond (:plain opts)
                       (-> loc :url pandoc-url->plain pager)

                       :else
                       (-> loc :url pandoc-url->md pager)))
          next-loc (fn [loc]
                     (concat [:quit loc]
                             (for [target (page->links (:url loc))]
                               (let [{:keys [url title]} target]
                                 {:url url :title title}))))]
      (walk-show-loop-with-exit {:url startpage}
                                show
                                next-loc
                                (fn quit? [loc] (= :quit loc)))))

(defn -main [url & args]
  (url-walk url (cli/parse-opts args)))
