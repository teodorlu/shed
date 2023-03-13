# Teodor's Clojure shed

A collection of CLI and TUI tools made with Clojure.

Purpose: make it as easy as possible to experiment with personal terminal workflow.

## Rationale

I'm using these tools to drive my daily work, and I prefer to avoid branching.
I may push breaking changes.
I may push bugs.
But when I push bugs, I also break my daily driver, so I try to avoid breakage.
Think of `teodorlu/shed` as a rolling-release distribution of Clojure tools.
If a tool gains real traction, I'll consider giving the tool its own repo to treat backwards compatibility seriously.

But for now, I minimize the number of moving pieces (number of repos) to reduce friction with experimentation.
If you want to try something out, you have a few options.

1. Copy-paste individual functions.
   If you find something you like, just copy the code.
   I don't mind!
2. Follow me on master.
   Minimal effort for updates, but potential breakage.
   And please let me know if you're hit by such breakage!
   [Clojurians Slack][clojurians-slack] or Github issue are good options.
3. Maintain your own fork.
   Lets you update at your own pace, and also experiment with stuff.
   If you make something nice, we can consider putting it in [contrib/].

[contrib/]: ./contrib/
[clojurians-slack]: https://clojurians.slack.com/

## Tools

| script         | purpose                         | working?           | used daily? |
|----------------|---------------------------------|--------------------|-------------|
| [browsetxt]    | web browser because why not     | yes                | no          |
| [clonecd]      | clone and cd into a repo        | yes                | yes         |
| [quickcd]      | navigate quicker than with `cd` | yeah, actually     | no          |
| [update-repos] | batch-update git repos          | works, rough edges | no          |

[quickcd]: https://github.com/teodorlu/shed/tree/master/contrib/quickcd
[clonecd]: https://github.com/teodorlu/shed/tree/master/contrib/clonecd
[update-repos]: https://github.com/teodorlu/shed/tree/master/contrib/update-repos
[browsetxt]: https://github.com/teodorlu/shed/tree/master/contrib/browsetxt

## Install one or more scripts

I recommend using [babashka/bbin][babashka-bbin] to install scripts from teodorlu/shed.

[babashka-bbin]: https://github.com/babashka/bbin

### Install a script from Github with bbin

    bbin install io.github.teodorlu/shed --latest-sha --as browsetxt \
        --main-opts '["-m" "teodorlu.shed.browsetxt/-main"]'

### Install a script from source with bbin

    git clone https://github.com/teodorlu/shed.git
    cd shed
    bbin install . --as ,update-repos \
        --main-opts '["-m" "teodorlu.shed.update-repos/-main"]'

### Install _all the scripts_ with predefined local script names

    git clone https://github.com/teodorlu/shed.git
    cd shed
    ./src/teodorlu/shed/install.clj

, then you can

    ,browsetxt
    ,month

, etc.
