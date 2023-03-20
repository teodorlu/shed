# browsetxt

Hypertext as plaintext.

## Motivation

Demonstrate that Clojure, Pandoc and FZF is enought to create a minimal web browser

## Prerequiesites

Please install [babashka][babashka], [babashka/bbin][bbin], [fzf][fzf] and [Pandoc][pandoc].

[babashka]: https://babashka.org/
[bbin]: https://github.com/babashka/bbin
[fzf]: https://github.com/junegunn/fzf
[pandoc]: https://pandoc.org/

## Installing

    bbin install io.github.teodorlu/shed --latest-sha --as browsetxt --main-opts '["-m" "teodorlu.shed.browsetxt/-main"]'

## Usage

In a terminal, run

    browsetxt URL

For example:

    browsetxt https://en.wikipedia.org/wiki/Krakatoa

## Markdown syntax highlighting

It's possible to get colors in your document rendering by relying on [bat][bat].
Install `bat`, then use the `--bat-mardown` CLI argument:

    browsetxt https://en.wikipedia.org/wiki/Krakatoa --bat-markdown

[bat]: https://github.com/sharkdp/bat
