#!/usr/bin/env bash

eval_lines() {
    while read -r line; do
        eval "$line"
    done
}

bb_script=$(cat <<EOF
(doseq [n (range 5)]
  (println "echo" n))
EOF
)

bb -e "$bb_script" | eval_lines
