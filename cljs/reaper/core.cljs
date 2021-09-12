(ns reaper.core
  (:require [clojure.string :as str]
            [clojure.pprint :as pprint]
            [reaper.config :as config]
            applescript))

(def applescript (js/require "applescript"))

;;; TODO this cps-style is a pain, but not sure best way to convert to normal flow.
(defn exec-cps
  [cont script]
  (.execString applescript script (fn [err res]
                                    (when err
                                      (throw (ex-info "Applescript error" {:err err})))
                                    (cont res))))

(defn exec-app-cps
  [cont app script]
  (exec-cps cont (str "tell application \"" app "\"\n " script "\n end tell")))

(defn all-urls
  [cont]
  (exec-app-cps #(cont (js->clj %)) (:browser config/config) "get URL of tabs of windows"))

(defn is-trash?
  [{:keys [url]}]
  (some #(str/includes? url %) (:trash-sites config/config)))

(defn brave-fancy
  [cont]
  (exec-app-cps
   (fn [tabs]
     (exec-app-cps
      (fn [[urls names]]
        (cont
         (map (fn [tab url name]
                {:url url
                 :title name
                 :identifier (second (re-matches #"(.*) of application .*" tab))})
              (flatten1 tabs)
              (flatten1 urls)
              (flatten1 names))))
      (:browser config/config) "get [URL, name] of tabs of windows"))
   (:browser config/config)
   "get tabs of windows"))

(defn close-tabs
  [tabs]
  (when-not (empty? tabs)
    (exec-app-cps (fn [_] (close-tabs (rest tabs)))
                  (:browser config/config)
                  (str "close " (:identifier (first tabs))))))

(defn grouped-tabs
  [tabs]
  (->> tabs
       (group-by #(second (re-matches #"https?://(.*?)/.*" (:url %))))
       (sort-by (comp count second))
       reverse))

(defn clean-tabs
  [& do-it]
  (brave-fancy
   (fn [tabs]
     (let [trash (filter is-trash? tabs)]
       ;; TODO prettier output, but this works
       (prn (str (count tabs) " tabs, " (count trash) " trash"))
       (pprint/pprint (grouped-tabs (map #(select-keys % [:url :title]) trash)))
       (when do-it
         (close-tabs trash))))))

;;; TODO dry-run option
(clean-tabs true)
