# Teodor's Clojure shed

A collection of CLI and TUI tools made with Clojure.

## Rationale

I'm using these tools to drive my daily work, and I prefer to work without branching.
I may push breaking changes.
I may push bugs.
But when I push bugs, I also break my daily driver, so I prefer to avoid that.
Think of `teodorlu/shed` as a rolling-release distribution of Clojure tools.
If some of this stuff actually gets good & use, I'll consider spinning it out into its own thing.
But for now, I prefer to minimize moving pieces (repos) to reduce all possible friction in order to get started.

If you want to use these tools, you have a few options.

1. Copy-paste individual functions.
   If you find something you like, just copy the code.
   I don't mind at all! :)
2. Follow me on master.
   Minimal effort for updates, but potential breakage.
   And please let me know if you're hit by such breakage!
   Clojurians Slack or Github issue are good options.
3. Maintain your own fork.
   Lets you update at your own pace, and also experiment with stuff.
   If you make something nice, we can consider putting it in [contrib/]

[contrib/]: ./contrib/

## Scripts

| script         | purpose                         | completion          | usage |
|----------------|---------------------------------|---------------------|-------|
| [quickcd]      | navigate quicker than with =cd= | experimental        | no    |
| [update-repos] | batch-update git repos          | workds, rough edges | some  |

[quickcd]: https://github.com/teodorlu/shed/tree/master/contrib/quickcd
[update-repos]: https://github.com/teodorlu/shed/tree/master/contrib/update-repos

## Install one or more scripts

The recommended way to install these scripts is with [babashka/bbin][babashka-bbin].

[babashka-bbin]: https://github.com/babashka/bbin
   
### Install all scripts from Github `master` with bbin:

    bbin install io.github.teodorlu/shed --latest-sha

### Install all scripts from source with bbin:

    git clone https://github.com/teodorlu/shed.git
    cd shed
    bbin install .

Any changes you make to the Clojure source files will now be reflected instantly in your locally installed scripts.

### Install a single script from source with bbin

    git clone https://github.com/teodorlu/shed.git
    cd shed
    bbin install . --as ,update-repos --main-opts '["-m" "teodorlu.shed.update-repos/-main"]'

### Tailored for Teodor

    git clone https://github.com/teodorlu/shed.git
    cd shed
    contrib/teodorlu/install.sh

