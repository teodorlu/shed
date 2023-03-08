#!/usr/bin/env bash

eval_lines() {
  while read -r line; do
    eval "$line"
  done
}

demo_1() {
  bb_script=$(cat <<EOF
(doseq [n (range 5)]
  (println "echo" n))
EOF
)
  bb -e "$bb_script" | eval_lines
}

eval_some_lines() {
  while read -r line; do
    if echo "$line" | grep '^!SHELLEVAL'; then
      echo IF
      cmd=$(echo "$line" | sed 's/!SHELLEVAL//g')
      eval "$cmd"
    else
      echo ELSE
      echo "$line"
    fi
  done
}

demo_2() {
  eval_some_lines <<EOF
normal passthrough
!SHELLEVAL echo "this is fine"
!SHELLEVAL cd ..
!SHELLEVAL pwd
another passthrough
EOF
}

demo_2
