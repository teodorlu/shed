(ns teodorlu.shed.explore)

;; not for reuse! Simply change, and evaluate.
;; Please don't depend on this file.

(comment
  ;; 2023-03-08
  ;; destructuring question on Slack
  ;; https://clojurians.slack.com/archives/C053AK3F9/p1678280301686469

  (let [x {:type "test" :meta {:rate 0.1}}
        {type :type} x
        {{rate :rate} :meta} x
        ]
    [type rate])
  ;; => ["test" 0.1]

  (let [{type :type {:keys [rate]} :meta}
        {:type "test" :meta {:rate 0.1}}]
    [type rate])
  ;; => ["test" 0.1]

  (let [{:keys [type] {:keys [rate]} :meta}
        {:type "test" :meta {:rate 0.1}}]
    [type rate])
  ;; => ["test" 0.1]

  (let [m {:type "test" :meta {:rate 0.1}}
        f (fn [{:keys [type] {:keys [rate]} :meta}] [type rate])]
    (f m))
  ;; => ["test" 0.1]


  ;; destructuring:
  (let [{:keys [type] {:keys [rate]} :meta}
        {:type "test" :meta {:rate 0.1}}]
    [type rate])

  ;; straightforward:
  (let [data {:type "test" :meta {:rate 0.1}}]
    [(:type data) (:rate (:meta data))])

  )

(comment
  ;; 2023-03-08
  ;; https://clojurians.slack.com/archives/C053AK3F9/p1678288277210489

  [:app :data :people 12346]
  [:app :data :things 123456]
  (let [some-data {:app {:data {:people {12346 {:name "Ryan"}}
                                :things {123456 {:make "Toyota" :color "Blue"}}}}}
        people (fn [data id] (get-in data [:app :data :people id]))
        things (fn [data id] (get-in data [:app :data :things id]))]
    [(people some-data 12346)
     (things some-data 123456)])
  ;; => [{:name "Ryan"} {:make "Toyota", :color "Blue"}]
  )

(comment
  ;; 2023-03-08
  ;; exploring how babashka.process/shell really works

  (let [shell babashka.process/shell]
    (prn "works:")
    (shell ["bbin" "ls"])

    (prn "works:")
    (shell {} "bbin" "ls")

    (prn "crashes:")
    (shell {} ["bbin" "ls"])))
