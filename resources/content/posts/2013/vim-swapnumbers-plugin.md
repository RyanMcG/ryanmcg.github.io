{:title "Swap numbers plugin for VIM"
 :date "2013-12-13"
 :category :code
 :tags [:code :vim]}

# Swap numbers plugin for VIM

During an impromptu lightning talk session at [NationJS][] someone informed me I had been using my keyboard wrong. The *right*
way is to swap the number keys at the top of the keyboard with their shifted
counterpart. I loved the idea so much that I tried to implement it in Vim (as
opposed to making the swap system wide). A misunderstanding of `noremap` was all
that was necessary to fail.

Fast forward a couple of months with a proper understanding of `noremap` and here it
is, [vim-swapnumbers][].

This plugin will swap numbers in insert mode with there shifted counterparts
(e.g. `1` with `!` and `3` with `#`). I figured this swap would not be ideal in
other modes where counts are used for motion (e.g. `4j` still moves down 4 lines
in normal mode).

This is the first project I am licensing under the same license as Vim. Vim's
license directly mentions [KCC][], a children's charity in Uganda. Consider
supporting them! They now accept BTC for your donating pleasure:

    13UUaGK8ZDLxjY7RYu2bKEabqjww2KDyxD

Don't trust a random bitcoin address? That's probably good practice. See [`:h
license`](http://www.gnu.org/licenses/vim-license.txt) or go to
http://iccf-holland.org/bitcoin.html

[KCC]: http://iccf-holland.org/kcc.html
[NationJS]: http://nationjs.com/
[vim-swapnumbers]: https://github.com/RyanMcG/vim-swapnumbers
