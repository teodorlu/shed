# Teodor's Clojure shed

A collection of CLI and TUI tools made with Clojure.

Purpose: make it as easy as possible to experiment with personal terminal workflow.

## Rationale

I'm using these tools to drive my daily work, and I prefer to work without branching.
I may push breaking changes.
I may push bugs.
But when I push bugs, I also break my daily driver, so I prefer to avoid that.
Think of `teodorlu/shed` as a rolling-release distribution of Clojure tools.
If a tool gains real traction, , I'll consider spinning it out into its own repo to treat backwards compatibilty seriously.
But for now, I prefer to minimize moving pieces (number of repos repos) to reduce any experimentation friction.

If you want to try something out, you have a few options.

1. Copy-paste individual functions.
   If you find something you like, just copy the code.
   I don't mind at all! :)
2. Follow me on master.
   Minimal effort for updates, but potential breakage.
   And please let me know if you're hit by such breakage!
   [Clojurians Slack][clojurians-slack] or Github issue are good options.
3. Maintain your own fork.
   Lets you update at your own pace, and also experiment with stuff.
   If you make something nice, we can consider putting it in [contrib/]

[contrib/]: ./contrib/
[clojurians-slack]: https://clojurians.slack.com/

## Tools

| script         | purpose                         | working?           | used daily? |
|----------------|---------------------------------|--------------------|-------------|
| [quickcd]      | navigate quicker than with `cd` | no, experimental   | no          |
| [update-repos] | batch-update git repos          | works, rough edges | no          |
| [browsetxt]    | web browser because why not     | yes                | no          |

[quickcd]: https://github.com/teodorlu/shed/tree/master/contrib/quickcd
[update-repos]: https://github.com/teodorlu/shed/tree/master/contrib/update-repos
[browsetxt]: https://github.com/teodorlu/shed/tree/master/contrib/browsetxt

## Install one or more scripts

The recommended way to install these scripts is with [babashka/bbin][babashka-bbin].

[babashka-bbin]: https://github.com/babashka/bbin

<!-- Note: commenting out the following because bbin does not seem to install multiple binaries. -->
<!-- ### Install all scripts from Github `master` with bbin: -->

<!--     bbin install io.github.teodorlu/shed --latest-sha -->

<!-- ### Install all scripts from source with bbin: -->

<!--     git clone https://github.com/teodorlu/shed.git -->
<!--     cd shed -->
<!--     bbin install . -->

<!-- Any changes you make to the Clojure source files will now be reflected instantly in your locally installed scripts. -->

### Install a single script from source with bbin

    git clone https://github.com/teodorlu/shed.git
    cd shed
    bbin install . --as ,update-repos \
        --main-opts '["-m" "teodorlu.shed.update-repos/-main"]'

### Tailored install for Teodor

    git clone https://github.com/teodorlu/shed.git
    cd shed
    ./src/teodorlu/shed/install.clj

