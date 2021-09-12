(ns reaper.config
  )

;;; Copy this file to config.cljs and edit to your taste.

;;; Should be regexes and/or predicates so can check for specific pages.
;;; Some of these are more risky than others

(def config
  {:browser "Brave Browser"
   :trash-sites
   ["twitter.com"
    "reddit.com"
    "imgur.com"
    "redd.it"
    "facebook.com"
    "linkedin.com"
    "nextdoor.com"
    "quora.com"
    "zoom.us"
    "news.ycombinator.com"
    "boingboing.net"
    "news.ycombinator.com"
    "duckduckgo.com"
    "localhost"
    "chrome://newtab"
    ]
   })
