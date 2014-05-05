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
    Every coach will be executed.

```clojure
(def even-and-then-db3-coach (manner  even-coach div-by-3-coach))
(def even-and-db3-coach      (manners even-coach div-by-3-coach))

(even-and-then-db3-coach 2) ; => ("must be divisible by 3")
(even-and-then-db3-coach 3) ; => ("must be even")
(even-and-then-db3-coach 1) ; => ("must be even")
(even-and-db3-coach 2)      ; => ("must be divisible by 3")
(even-and-db3-coach 3)      ; => ("must be even")
(even-and-db3-coach 1)      ; => ("must be even" "must be divisible by 3")
```

Whoa, **wait**, *what?*

How was the usage of the `manner` function the same in those examples?
Well, manner expects a sequence of coaches or predicate message pairs.
It iterates through its arguments if it is a coach, great, if it is just a normal function, it assumes it is a predicate and will use the next argument as a message.

```clojure
(def pred-msg-coach (manner      true?       "must be true"))
;    A coach        created with a predicate message pair.

(def stuff-coach (manner      (as-coach :errors)))
;    A coach     created with another coach.

;; Notice the use of the `as-coach` function to mark a function as a coach.
;; This sets the :coach metadata to true.

(stuff-coach {:errors []})                   ; => ()
(stuff-coach {:errors ["I have a boo boo"]}) ; => ("I have a boo boo")
(pred-msg-coach true)                        ; => ()
(pred-msg-coach "truthy")                    ; => ("must be true")

(def no-errors-and-good (manner      stuff-coach
;    A coach            created with a coach ...
                                     (comp true? :good) "all good"))
;                            ... and a predicate      message pair

(no-errors-and-good {:good true, :errors []})        ; => ()
(no-errors-and-good {:good true, :errors ["blarg"]}) ; => ("blarg")
(no-errors-and-good {:good false, :errors []})       ; => ("not good")
(no-errors-and-good {:good false, :errors ["wort"]}) ; => ("wort")
```

This means rather complex groupings of messages can be specified with nested coaches.

## A realistic example

Validating maps/hashes/dicts/objects is common task regardless of language.
In Clojure, maps are everything, or perhaps everything is a map (or a record).
This is, generally, the Clojure way.

Validating maps is so necessary in Clojure that many of its validation libraries only work on maps.
*manners* is not limited to maps but it can still deal with them well.

Let's suppose we have a login request that is parsed into the following map (`a-user`).

```clojure
(def a-user {:login "batman"
             :password "password123"
             :doge "D5MGiMCmxkpJkLBhKe9oN7h49X3t81e3kp"
             :address {:street "Wayne Lane"
                       :number 123
                       :zip 98765
                       :city "Gotham"
                       :state "WA"}})
```

We start of by defining a couple coaches to validate the login is well formed.
We'll build up `user-coach` throughout this example.

```clojure
(def login-coach (manner (partial re-find #"[a-z][a-z0-9]+")
                         "must be alpha numeric starting with a letter"))
(def user-coach
  (manners (as-coach
             (comp (partial map (partial str "login "))
                   login-coach
                   :login))))
```

And we can see they behave as expected

```clojure
(user-coach a-user)                     ; => ()
(user-coach (assoc a-user :login "12")) ; => ("login must be alpha numeric starting with a letter")
(user-coach (assoc a-user :login "a2")) ; => ()
```

I need helper functions for dealing with maps and to use a namespace qualified symbol for the coach metadata.

## Are you still interested?

If so, I suggest you give the [project's README][project] a gander.
There is also codox generated [API documentation][api-docs] available.

Now, go forth and teach your data some manners.

[Clojure]: http://clojure.org/
[api-docs]: http://www.ryanmcg.com/manners/
[project]: https://github.com/RyanMcG/manners
[comparisons]: https://github.com/RyanMcG/manners#comparisons
[others]: http://www.clojure-toolbox.com/
[funjs]: http://www.amazon.com/gp/product/1449360726/ref=as_li_ss_tl?ie=UTF8&camp=1789&creative=390957&creativeASIN=1449360726&linkCode=as2&tag=ryanvirtmach-20
