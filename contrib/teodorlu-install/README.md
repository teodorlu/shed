# Install all the scripts with comma prefix

I'm playing around with the idea of having all personal scrpts prefixed with comma:

    $ ,update-repos

Then I can quickly discover:

1. Is this script something I've made, or something someone else has made?

2. What are my installed scripts?

        ,<TAB>

    Shell completion gives me all the information I need!

I also don't need to worry about my own stuff vs other people's stuff.
=cd= is builtin, =,cd= is mine.

## Shed script for installing shed scripts with commas

From the project root, run

    bb src/teodorlu/shed/install.clj

Done!
