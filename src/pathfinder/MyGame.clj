(ns pathfinder.MyGame
  (:import (com.badlogic.gdx Game Screen Gdx))
  (:require [pathfinder.myscreen :as myscreen]))

(gen-class
 :name pathfinder.MyGame
 :extends com.badlogic.gdx.Game)
 
(defn -create [^Game this]
  (.setScreen this (myscreen/screen)))