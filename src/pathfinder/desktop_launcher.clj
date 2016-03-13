(ns pathfinder.desktop-launcher
  (:import [com.badlogic.gdx Gdx Application] 
           [com.badlogic.gdx.backends.lwjgl LwjglApplication])
  (:require (pathfinder [myscreen :as myscreen])))

(def game (pathfinder.MyGame. ))

(defn ss []
  (.setScreen game (myscreen/screen)))

(defn rr [a]
  (.postRunnable Gdx/app a))

(defn app []
  (LwjglApplication. game "hello" 800 600))

;(defn app []
;  (LwjglApplication. (pathfinder.MyGame. ) "hello" 800 600))

(app)
