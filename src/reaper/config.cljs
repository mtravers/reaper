(ns reaper.config
  )

;;; TODO this should be edn, not clojure!
;;; Should be regexes and/or predicates so can check for specific pages
;;; Some of these are more risky than others

(def config
  {:browser "Brave Browser"
   :trash-sites
   ["twitter.com"
    "mastodon.social"
    "reddit.com"
    "imgur.com"
    "redd.it"
    "facebook.com"
    "linkedin.com"
    "nextdoor.com"
    "zoom.us"
    "news.ycombinator.com"
    "boingboing.net"
    "news.ycombinator.com"
    "lawyersgunsmoneyblog.com"
    "digbysblog.net"
    "eschatonblog.com"
    "orthosphere.wordpress.com"
    "duckduckgo.com"
    "disqus.com"
    "nytimes.com"
    ;; Nah, these are work projects, don't want to close them randomly
    #_ "localhost"
    "hyperphor.com"
    "chrome://newtab"
    ]
   })
