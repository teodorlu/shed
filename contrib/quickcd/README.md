# quickcd: walk around quickly

Does it work?
Yeah, actually.
Who would have thought!

## Installing

First, install the Clojure script:

    bbin install io.github.teodorlu/shed --latest-sha --as libquickcd --main-opts '["-m" "teodorlu.shed.libquickcd/-main"]'

Then add the shell wrapper to your shell rc files:

    ,cd() {
      exec libquickcd "$@"
    }

Then start it off with `$ ,cd`.
