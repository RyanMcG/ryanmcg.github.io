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
My goal is to get ***you*** to use manners.
I hope I can do that by example.
Please excuse my French.

<script src="https://gist.github.com/RyanMcG/9029987.js"></script>

I suggest you give the [project's README][project] a gander if you are interested.
I also have some [API documentation][api-docs] available.

Now, go forth and avow that your data will, at times, have bad manners.

[Clojure]: http://clojure.org/
[api-docs]: http://www.ryanmcg.com/incise/
[project]: https://github.com/RyanMcG/manners
[comparisons]: https://github.com/RyanMcG/manners#comparisons
[others]: http://www.clojure-toolbox.com/
[funjs]: http://funjs.herokuapp.com/
