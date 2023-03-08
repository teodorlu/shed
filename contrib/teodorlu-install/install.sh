#!/usr/bin/env sh

# Teodor's install script. Works around bbin's "no comma in script name" constraint.

cd "$(git rev-parse --show-toplevel)"
bbin install . --as ,update-repos --main-opts '["-m" "teodorlu.shed.update-repos/-main"]'
bbin install . --as ,browsetxt --main-opts '["-m" "teodorlu.shed.browsetxt/-main"]'
bbin install . --as ,path-lines --main-opts '["-m" "teodorlu.shed.path-lines/-main"]'
