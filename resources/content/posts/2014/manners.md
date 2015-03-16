{:title "Code with manners"
 :date "2015-3-15"
 :category :code
 :publish false
 :tags [:code :clojure :manners :validation :projects]}

# Code with [manners][project]

Data without manners is problematic.
You already know that. You are trying to decide if *manners* is right for you.
It might not be.
*[Vlad][]* is a great library that has the same basics as *manners*.
I would like to humbly suggest that *manners* is just a bit more fun.
Also, one place desperate for a bit of joy is validation code.

Manners is pretty minimal.
I will not explain [the basics of the library][project] or [its API][api-docs] here.
The goal of this post is to show *you* what manners can do and what makes it fun.

## Funny words

I am not suggesting we pick use words that misrepresent behaviour.
I am suggesting that we pick more amusing words. Pick *better* words.

For instance, I do not think validate is a good name for a function. What
does it output? Errors? An exception? Some special object? It depends on
the library. There are too many good words to choose from to suffer this
ambiguity.

```clojure
(require '[manners.victorian :refer :all])
```

The victorian namespace defines the core of the language. Again, read the
[README][project]. Its functions use these words: avow, falter, proper,
rude, and manner. "Avow" is a good name for a function.

1.  No one else is using it (no conflicts).
2.  It fits the definition (throws an exception when errors are found):

    > to declare or state (something) in an open and public way
3.  It is a *fucking* __awesome__ word.

If you are a linguist or something and you feel the need to point out that the etymology of the words used by *manners* are not victorian enough or some crap feel free to send you notes [here](mailto:/dev/null).

## _Anti_-interface

I appreciate the simplicity of functions.
Most Clojurist do.
*Manners* is based on higher-order functions called coaches.

In *manners*, much like in the extra-programming world, coaches tells us what we did wrong.
They are functions which take a value and return a sequence of messages describing what is wrong with that value.
There are several functions which create coaches; everything else is sugar.

The simplest coach creator is [`manner`][manner].

```clojure
(def even-coach (manner even? "must be even"))
(def div-by-3-coach (manner (= (mod % 3) 0) "must be divisible by 3"))

(even-coach 2)        ; → ()
(even-coach 3)        ; → ("must be even")
(div-by-3-coach 9)    ; → ()
(div-by-3-coach 1000) ; → ("must be divisible by 3")
```

### Composition

Coaches can be composed in two different ways:

1.  *In series:* Coaches in a single manner will short circuit. Only messages from the first failing coach will be returned in the sequence.

2.  *In parallel:* Coaches combined with the `manners` function are all executed, allowing messages from all coaches.

```clojure
(def even-and-then-db3-coach (manner  even-coach div-by-3-coach))
(def even-and-db3-coach      (manners even-coach div-by-3-coach))

(even-and-then-db3-coach 2) ; → ("must be divisible by 3")
(even-and-then-db3-coach 3) ; → ("must be even")
(even-and-then-db3-coach 1) ; → ("must be even")
(even-and-db3-coach 2)      ; → ("must be divisible by 3")
(even-and-db3-coach 3)      ; → ("must be even")
(even-and-db3-coach 1)      ; → ("must be even" "must be divisible by 3")
```

This means rather complex groupings of messages can be specified with nested coaches.

## A "real" example

Validating maps/hashes/dicts/objects is common task regardless of
language. In Clojure, maps are everything, or perhaps everything is a map.

Validating maps is so necessary in Clojure that many of its validation libraries only work on maps.
*manners* is not limited to maps but it still offers good support.

Let's suppose we have a login request that is parsed into the following map.

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

Working with maps is easier with *bellman*.

```clojure
(require '[manners.bellman :refer [at]])
(def login-coach (manner (partial re-find #"[a-z][a-z0-9]+")
                         "must be alpha numeric starting with a letter"))
(def user-coach (at login-coach :login))
```

Oh, and it works!

```clojure
(user-coach a-user)                     ; → ()
(user-coach (assoc a-user :login "12"))
; → ("login must be alpha numeric starting with a letter")
(user-coach (assoc a-user :login "a2")) ; → ()
```

### Messages are not special

Messages can be anything, they do not have to be strings.
However, if they are you can do some other fun stuff.


TODO: Use subset for predicate and difference in message

```clojure
(require '[manners.bellman :refer [invoking]])
(def req-keys #{:login :password :doge :address})
(def has-keys-coach (invoking (manner (comp (partial = req-keys)
                                       set keys)
                              (fn [v] (str "must have the keys ("
                                           req-keys ") not ("
                                           (keys v) \)))))
```

### DRY as the desert

Being [DRY][] is important. Fancy macros means it is possible to get very
DRY.

```clojure
(require '[manners.really :refer [really verily]])
(def address-coach (at (really "must be a" number?) :number))

(defn match [re] (partial re-find re))
(def doge-regex #"^D{1}[5-9A-HJ-NP-U]{1}[1-9A-HJ-NP-Za-km-z]{32}$")
(def doge-coach (verily "must" (match doge-regex)))
```

### And putting it back together

```clojure
(def final-user-coach
  (manner
    has-keys-coach
    (manners user-coach
             (at (really "must be a" string?) :password)
             (at doge-coach :doge)
             (at address-coach :address))))
```

## Was that fun?

On a scale from 1 to 10, it was probably a 2. If that's the case I was
successful since normally writing validation code is a 1.

If you want to use *manners* I suggest you give the [project's README][project] a gander.
There is also codox generated [API documentation][api-docs] available.

---

That's it.
Now, go forth and teach your data some manners.

[Clojure]: http://clojure.org/
[api-docs]: http://www.ryanmcg.com/manners/api/
[project]: http://www.ryanmcg.com/manners/
[Vlad]: https://github.com/logaan/vlad
[manner]: http://www.ryanmcg.com/manners/api/manners.victorian.html#var-manner
[DRY]: http://en.wikipedia.org/wiki/Don't_repeat_yourself
