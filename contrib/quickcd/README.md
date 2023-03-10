# quickcd: walk around quickly

Does it work?
Not yet.

## Installing

First, install the Clojure script:

    bbin install io.github.teodorlu/shed --latest-sha --as libquickcd --main-opts '["-m" "teodorlu.shed.libquickcd/-main"]'

Then add the shell wrapper to your shell rc files:

    with_shelleval() {
      while read -r line; do
        if echo "$line" | grep '^!SHELLEVAL' >/dev/null; then
          cmd=$(echo "$line" | sed 's/!SHELLEVAL//g')
          eval "$cmd"
        else
          echo "$line"
        fi
      done
    }

    ,cd() {
      libquickcd "$@" | with_shelleval
    }

Then start it off with `$ ,cd`.
