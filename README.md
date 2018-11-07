# morn-repl

An experimental REPL form [morn](https://github.com/henm/morn). Currently it's mostly a clojure playground for me.

## Installation

Make sure morn is somewhere leiningen can find it (e.g. by installing it with `mvn install`). The repl can be started with

```
lein run
```

To exit the REPL press Ctrl-D.

## Examples

```bash
$ lein run
Welcome to morn-repl!
> cat(tom).
> animal(X) :- cat(X).
> ?animal(tom)
true
>
Bye!
```