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

    ,cd() {
      exec ,libquickcd "$@"
    }

Then start it off with `$ ,cd`.

Why?
Quickcd avoids creating a stack of shells and babashka instances waiting for each other by using shell `exec` / `babashka.process/exec`.
If we didn't do this, you'd have to press `C-d` (`^D`) multiple times to shut down your terminal.
We consider this a violation of your attention, and we strive to avoid those.
