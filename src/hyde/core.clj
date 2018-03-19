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
                            link :link
                            id :id}]
  {:ort ort
   :id id
   :link link
   :eintrag (java.util.Date. eintrag)
   :datum (java.util.Date. datum)
   :titel titel
   :beschreibung beschreibung})


(defn render [{:keys [beschreibung eintrag ort link titel datum]}]
  (format "---
layout: post
date: %1$tY-%1$tm-%1$td 14:55:05 +0100
title: \"%3$s\"
categories: rheinjug event
---
# %3$s
Am %2$td.%2$tm.%2$tY 18:30 Uhr
Ort: %5$s
%6$s
Anmeldung: %4$s"

          eintrag
          datum
          titel
          link
          ort
          beschreibung))


(defn -main
  [& args]
  (doseq [{eintrag :eintrag id :id :as event} (map extract-render-data data)]
    (spit (format "%1$tY-%1$tm-%1$td-%2$s.markdown" eintrag id)
          (extract-render-data event))))


