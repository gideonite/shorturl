# shorturl

A crappy little url shortener web application, a quick exercise in web dev.  I
enjoyed using Compojure for this.

### TODO:

* Do something better than keeping data in memory.
* More robust infrastructure for users, i.e. sessions.
* click data visualization
* realtime click data visualization

And a whole lot of other interesting stuff too: http://gist.io/7268187

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

A page should open in the nearest web browser, but if it doesn't nagivate over
to `localhost:3000`.
