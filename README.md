# Teodor's Babashka scripts

A collection of Babashka scripts. Feel free to share / copy / use at your pleasure.

## Installing

To install directly from Github, run:

    bbin install io.github.teodorlu/browsetxt --latest-sha

To install from source,

1. First, install [babashka/bbin][babashka-bbin].

2. Then, run the following in a terminal:

        git clone https://github.com/teodorlu/bb-scripts.git
        cd bb-scripts
        bbin install .

    Any changes you make to the Clojure source files will now be reflected instantly in your locally installed scripts.

[babashka-bbin]: https://github.com/babashka/bbin

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
