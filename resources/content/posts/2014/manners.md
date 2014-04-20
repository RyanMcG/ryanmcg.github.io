{:title "Code with manners"
 :date "2014-2-15"
 :category :code
 :publish false
 :tags [:code :clojure :manners :validation :projects]}

# Code with [manners][project]

I have often seen code to be quite rude.
I think this is somewhat unavoidable, mistakes happen and conditions are not met.
However, it is often ideal to find out when those conditions are not met sooner rather than later.
We have seen this before.
A problem with user input is expected.
Allowing that input to be saved in the database, for instance, was straight up rude.

Clearly there is some value to data validation.
I am sure I am not the first to think so.
Consider [the many wonderful validation libraries already available][others] (see those listed under the *Validation* header) to suit your Clojure programming needs.

Unfortunately for me, [I was a bit disenchanted][comparisons] with those libraries.
They did not allow arbitrary data or used an odd DSL or some other, arguably valuable, but wholly unnecessary magic.
After doing a bit of reading, I was struck by the simplicity of the validators created in [Michael Fogus's *Functional JavaScript*][funjs].
I wanted to do something similar in [Clojure][].
The result is [manners][project].

Manners is pretty minimal.
I will not explain [the basics of the library][project] or [its API][api-docs] here.
The goal of this post is to show *you* what manners can do.

## Let's get down to business

The core of the library is in the `manners.victorian` namespace.

```clojure
(use 'manners.victorian)
```

We'll start off by creating some very simple coaches.
What is a coach?
It is function that returns a sequence of messages.
Typically, these messages are strings but they really can be anything.
Coaches are the core of *manners*.
There are several functions which create coaches; everything else is sugar.

```clojure
(def even-coach (manner even? "must be even"))
(def div-by-3-coach (manner (= (mod % 3) 0) "must be divisible by 3"))

(even-coach 2)        ; => ()
(even-coach 3)        ; => ("must be even")
(div-by-3-coach 9)    ; => ()
(div-by-3-coach 1000) ; => ("must be divisible by 3")
```

Coaches can be composed.
This can happen in two different ways:

1.  *As a manner:*
    Coaches in a single manner will short circuit.
    Only messages from the first failing coach will be returned in the sequence.

2.  *As manners:*
    Coaches combined with the `manners` function are disjoint.
    Every coach will be execute.

```clojure
(def even-and-then-db3-coach (manner  even-coach div-by-3-coach))
(def even-and-db3-coach      (manners even-coach div-by-3-coach))

(even-and-then-db3-coach 2) ; => ("must be divisble by 3")
(even-and-then-db3-coach 3) ; => ("must be even")
(even-and-then-db3-coach 1) ; => ("must be even")
(even-and-db3-coach 2)      ; => ("must be divisble by 3")
(even-and-db3-coach 3)      ; => ("must be even")
(even-and-db3-coach 1)      ; => ("must be even" "must be divisible by 3")
```

MORE TO DO

---

Are you still interested?
I suggest you give the [project's README][project] a gander.
There is also codox generated [API documentation][api-docs] available.

Now, go forth and avow that your data will, at times, have bad manners.

[Clojure]: http://clojure.org/
[api-docs]: http://www.ryanmcg.com/manners/
[project]: https://github.com/RyanMcG/manners
[comparisons]: https://github.com/RyanMcG/manners#comparisons
[others]: http://www.clojure-toolbox.com/
