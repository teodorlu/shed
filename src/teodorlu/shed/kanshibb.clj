(ns teodorlu.shed.kanshibb
  (:require
    [babashka.fs :as fs]))

;; why data?
;;
;; because then we can build UI for things we want.
;;
;; why code?
;;
;; because then we can remove boilerplate and allow for extension.

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; example pure data config

{:config
 [{:profile
   [{:description "laptop right of 34inch monitor"}
    {:output "Samsung Electric Company C34H89x H4ZN501754" :position "0,0"}
    {:output "Chimei Innolux Corporation 0x14F2 Unknown" :position "3440,360"}]}
  {:profile
   [{:description "Laptop only"}
    {:output "Chimei Innolux Corporation 0x14F2 Unknown" :position "0,0"}]}
  {:profile
   [{:description "laptop below 34inch monitor"}
    {:output "Samsung Electric Company C34H89x H4ZN501754" :position "0,0"}
    {:output "Chimei Innolux Corporation 0x14F2 Unknown" :position "760,1440"}]}]}

;; example data & code config
;; Note its quotation.

(quote
 {:init (do (def laptop-output "Chimei Innolux Corporation 0x14F2 Unknown")
            (def position-right-of-34inc "3440,360")
            (def position-below-34inch "760,1440"))
  :config
  [{:profile
    [{:description "laptop right of 34inch monitor"}
     {:output "Samsung Electric Company C34H89x H4ZN501754" :position "0,0"}
     {:output laptop-output :position position-right-of-34inch}]}
   {:profile
    [{:description "Laptop only"}
     {:output "Chimei Innolux Corporation 0x14F2 Unknown" :position "0,0"}]}
   {:profile
    [{:description "laptop below 34inch monitor"}
     {:output "Samsung Electric Company C34H89x H4ZN501754" :position "0,0"}
     {:output laptop-output :position position-below-34inch}]}]})

;; Don't load stuff you don't trust! The config file can execute arbitrary code.
;; But that's for a reason: to support stuff like this:

(quote
 {:init (do (def legacy-config (slurp (fs/expand-home "~/kanshi-legacy-config.txt"))))
  :config
  [{:raw-str legacy-config}
   {:profile
    [{:description "laptop below 34inch monitor"}
     {:output "Samsung Electric Company C34H89x H4ZN501754" :position "0,0"}
     {:output laptop-output :position position-below-34inch}]}]})

;; In other words -- you can easily migrate from writing your kanshi config by
;; hand to autokanshi with an autokanshi config like this:

(quote
 {:init (do (def legacy-config (slurp (fs/expand-home "~/.config/kanshi/config"))))
  :config
  [{:raw-string legacy-config}]})

;; Then gradually add new monitors.

(defn -main [& _args]
  (prn 'autokanshi-FTW-yay))
