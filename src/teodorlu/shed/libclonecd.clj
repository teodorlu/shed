(ns teodorlu.shed.libclonecd)

;; shell alias: ,clonecd
;;
;; this library does the heavy lifting
;;
;; we can implement ,clonecd in terms of libclonecd and shellexec

;; Examples
;;
;;      $ libclonecd teodorlu/shed
;;      !SHELLEVAL cd /home/teodorlu/dev/teodorlu/shed

(defn -main [& _argv]
  (println "libclonecd")
  (println "!SHELLEVAL cd /home/teodorlu/dev/teodorlu/shed"))
