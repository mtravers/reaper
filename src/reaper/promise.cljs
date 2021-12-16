(.then (.resolve js/Promise 42) #(prn :foo %))

;;;

(def applescript (js/require "applescript"))

;;; This doesn't work
(defn exec
  [script]
  (let [promise js/Promise]
    (.execString applescript script (fn [err res]
                                      (prn :xxx err res)
                                      (when err
                                        (throw (ex-info "Applescript error" {:err err})))
                                      (.resolve promise res)))
    (.all js/Promise (list promise))
    ;; TODO get the value out of promise
    promise))

;;; This works but seems insanely complicated. Surely there's a simpler way
(defn exec
  [script]
  (let [atom (atom nil)
        promise
        (js/Promise. (fn [resolve err]
                       (.execString applescript script (fn [err res]
                                                         (when err
                                                           (throw (ex-info "Applescript error" {:err err})))
                                                         (resolve res)))))]
    ;; Wait for promise to be resolved; this is the heart of things.
    (.all js/Promise (list promise))
    (.then promise #(reset! atom %))
    atom
    ))


;;; Variant
(defn exec
  [script]
  (let [atom (atom nil)
        promise
        (js/Promise. (fn [resolve err]
                       (.execString applescript script (fn [err res]
                                                         (when err
                                                           (throw (ex-info "Applescript error" {:err err})))
                                                         (reset! atom res)
                                                         (resolve res)))))]
    ;; Wait for promise to be resolved; this is the heart of things.
    (.all js/Promise (list promise))
    ;; @atom does not work, and I don't understand why, promise should be resolved and atom value set at this point
    ;; Suspect that wait doesn't work. Argh, it ddoesn't wait, it just makes another promise! Fuck me. 
    atom
    ))

OK, there is an await keyword in js https://www.geeksforgeeks.org/how-to-return-the-response-from-an-asynchronous-call-in-javascript/?ref=rp

Seems like the cljs equivalent requires core.async http://www.patternweld.com/code/2018/12/11/async-and-await-in-clojurescript.html
  or maybe https://blog.jeaye.com/2017/09/30/clojurescript-promesa/
