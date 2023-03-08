# Teodor's clojure shed

Stability: potentially very unstable.
I'm using tools here as my daily driver, and may push breaking changes to master.
But that breaks my daily workflow, so I want to avoid that.
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

## Installing scripts

The recommended way to install these scripts is with [babashka/bbin][babashka-bbin].

[babashka-bbin]: https://github.com/babashka/bbin
   
### To install from Github `master` with bbin:

    bbin install io.github.teodorlu/bb-scripts --latest-sha

### To install from source with bbin:

    git clone https://github.com/teodorlu/bb-scripts.git
    cd bb-scripts
    bbin install .

Any changes you make to the Clojure source files will now be reflected instantly in your locally installed scripts.


## `update-repos`

Run to update any git repos found in the current folder.

Example:

    $ pwd
    /home/teodorlu/dev/babashka
    $ update-repos
    update-repos: updating /home/teodorlu/dev/babashka/scittle
    Already up to date.
    update-repos: updating /home/teodorlu/dev/babashka/babashka
    Already up to date.
    update-repos: updating /home/teodorlu/dev/babashka/sci
    Already up to date.
    update-repos: updating /home/teodorlu/dev/babashka/bbin
    Already up to date.
    update-repos: updating /home/teodorlu/dev/babashka/http-server
    Already up to date.
    update-repos: updating /home/teodorlu/dev/babashka/cli
    Already up to date.
    update-repos: updating /home/teodorlu/dev/babashka/fs
    Already up to date.
    update-repos: updating /home/teodorlu/dev/babashka/process
    Already up to date.
    update-repos: updating /home/teodorlu/dev/babashka/pods
    Already up to date.
    update-repos: updating /home/teodorlu/dev/babashka/book
    Already up to date.
    update-repos: updating /home/teodorlu/dev/babashka/pod-babashka-fswatcher
    Already up to date.
