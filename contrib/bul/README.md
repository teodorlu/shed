# Babashka Unix Layer

I really like Babashka, and I really like Unix.
So let's make a compatibility layer.

## THIS CODE HAS MOVED.

New name: **Babashka Unix Bridge**

New repository: https://github.com/teodorlu/bub

## Examples

Directory contents, sorted:

    # BUL
    ls | ,,from-lines | ,,sort | ,,to-lines

    # Unix
    ls | sort

JSON keys, as JSON:

    # BUL
    cat data.json | ,,from-json | ,,keys | ,,to-json

    # Unix
    cat data.sjon | jq 'keys'

What about threading?

Ideas:

- Provide ,,thread-last such that
  `echo '[1 2 3]' | ,,thread-last '(map inc) sort'`
  transpiles to
  `(->> [1 2 3] (map inc) sort)`
- Provide defaults to each function so that they know to thread first or thread last, such that
  `echo '[1 2 3]' | ,,map inc | sort`
  produces equivalent output to
  `(->> [1 2 3] (map inc) sort)`
  - Problem with this approach: we'll spawn lots of processes.
    But we can _both_ provide predefined thread-first or thread-last preference for functions, and at the same time operations for `,,thread-last` or `,,thread-first`.

## What proble are we solving?

I believe there's a middle ground where:

- Unix pipes composed of sed, grep and awk get hard to reason about
- And you'll want to explore the problem with plain Unix pipes rather than fire up an interpreter and work in a REPL.

I need a list of those problems.
If I had the list of those problems, I could compare solving those problems with Unix pipes, a custom Babashka script and with BUL primitives.
