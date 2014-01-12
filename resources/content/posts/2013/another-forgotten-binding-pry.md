{:title "Another forgotten binding.pry"
 :date "2013-08-12"
 :category :code
 :tags [:hooks :code :rails :git :debugger :pry :diff]}

# Another forgotten binding.pry

Tools like [pry][] and [debugger][] are almost indispensable once you have gotten used to them.
Unfortunately, nearly all users will accidently include them in a commit at some point.

If you are a very careful person you might proof read your diffs before
committing. While I generally follow this advice it can be quite easy to miss
something, like a `binding.pry` you were using to debug/test your changes.
You'll probably catch this before you deploy.

If you don't though&hellip;

Well, let's just avoid those potentially devastating timeouts in prod by utilizing a *pre-commit hook*.
For those unaware, [git hooks][] are a mechanism available to git users which executes scripts when "certain important actions occur" with your git repository.
One of these important actions is commiting.
Git provides several hooks around this action, one of which is the aforementioned *pre-commit hook*.

<script src="https://gist.github.com/RyanMcG/5775028.js"></script>

This is just a simple (and suboptimal) shell script that when run at the
root of a Rails project will attempt to use [git's grep][gg] to find instances of
`debugger` or `binding.pry` in your source. If one is found it exits non-zero
which indicates to git that the in-progress commit should be cancelled! Mistake
avoided!

If you have pre-commit hook returning a false positive (returning non-zero when
it should not be) you may always bypass a failing pre-commit hook by adding a
`--no-verify` (or `-n` for lazy users) to your commit command.

## But wait, there's more

You may have noticed that the above script does not simply check for `binding.pry` and `debugger`.
It also checks for diff artifacts (i.e. `<<<<<<<`, `>>>>>>>` and `=======`).
These are just two things you may want in your precommit hook.
Perhaps, you want to avoid committing invocations of `console.log` in JavaScript source too.

## Installation

To install this hook you'll have to do something like the following.

    curl https://gist.github.com/RyanMcG/5775028/raw/rails-pre-commit
    chmod +x rails-pre-commit
    mv rails-pre-commit path/to/your/rails/root/.git/hooks/pre-commit

## Modularity

Complex hooks often develop over time.
When they do, modularity can simplify creating and sharing new part.
While git does not help with this out of the box there are various projects which do.

One such project, aptly named [git-hooks][], allows many scripts to be specifies
for the same action. It uses subdirectories with the same name as the the hook inside of
a folder called `git_hooks` at the root of the repository. Source controlling
the hooks is often desirable so you can share them with the rest of your team.

[pry]: http://pryrepl.org/
[debugger]: http://devdocs.io/javascript/statements/debugger
[git hooks]: http://git-scm.com/book/en/Customizing-Git-Git-Hooks
[git-hooks]: https://github.com/icefox/git-hooks
[gg]: https://www.kernel.org/pub/software/scm/git/docs/git-grep.html
