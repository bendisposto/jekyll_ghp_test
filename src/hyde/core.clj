(ns hyde.core
  (:require [clojure.data.json :as json])
  (:gen-class))


(def data (json/read-str
           (slurp "https://api.meetup.com/rheinjug/events")
           :key-fn keyword))


(defn extract-render-data [{beschreibung :description
                            titel :name
                            {ort :name} :venue
                            datum :time
                            eintrag :updated
                            link :link}]
  (format "---
layout: post
date: %1$tY-%1$tm-%1$td 14:55:05 +0100
datum: %2$td.%2$tm.%2$tY 18:30 Uhr
title: \"%3$s\"
categories: rheinjug event
link: %4$s
ort: %5$s
---
%6$s"

          (java.util.Date. eintrag)
          (java.util.Date. datum)
          titel
          link
          ort
          beschreibung))


(defn -main
  [& args]
  (doseq [{id :id :as event} data]
    (spit (str "_posts/" id "-meetup.md")
          (extract-render-data event)))
  )



