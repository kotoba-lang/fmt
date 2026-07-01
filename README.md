# kotoba-lang/fmt

[![CI](https://github.com/kotoba-lang/fmt/actions/workflows/ci.yml/badge.svg)](https://github.com/kotoba-lang/fmt/actions/workflows/ci.yml)

**Layer 4 (tooling) of the kotoba foundational stdlib** — a deterministic EDN
formatter. Reads EDN, re-emits it with canonical whitespace/indentation so the
same data always yields the same text — the rustfmt / dprint equivalent for
kotoba/EDN source. No third-party deps; every namespace is `.cljc` (JVM / SCI /
ClojureScript / GraalVM / kotoba-WASM). See
[`docs/adr/ADR-kotoba-lang-foundational-stdlib.md`](https://github.com/kotoba-lang/kotoba-lang/blob/main/docs/adr/ADR-kotoba-lang-foundational-stdlib.md).

## Current surface

`kotoba.lang.fmt`:

- `format` — EDN string → canonical EDN string (parse → re-emit)
- `format-str` — Clojure data → canonical EDN string (emit)
- options: `:indent` (default 2), `:margin` (line width before force-break)

The emitter prints collections with one element per line when they exceed
`:margin`, otherwise inline. Map keys are kept in insertion order (EDN is
order-preserving on read), not sorted — canonicalization of key order is a
separate concern (see `dag-cbor` for content-addressed sorting).

## Install

```clojure
io.github.kotoba-lang/fmt {:git/sha "<sha>"}
```

## Use

```clojure
(require '[kotoba.lang.fmt :as fmt])

(fmt/format "{:a 1,    :b [2 3 4 5 6 7 8 9 10]}")
;;=> a multi-line, indented canonical form
(fmt/format-str {:a 1 :b [2 3]})
```

## Verify

```sh
clojure -M:test
```
