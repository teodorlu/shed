# quickcd: walk around quickly

Does it work?
Nope.

## Problem: pager

I'm sending commands to my shell because I need to be able to evaluate output.
But when I'm invoking `less` from Clojure, it just terminates because it's output is being redirected.
With `fzf`, I have no problems.

Possible cause: `less` uses stdout, `fzf` uses stderr.
In that case, possible solution: use a pager that writes to stderr instead.

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
