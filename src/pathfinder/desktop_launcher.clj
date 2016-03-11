(ns pathfinder.desktop-launchers
  (:import [com.badlogic.gdx.backends.lwjgl LwjglApplication]))

(defn app []
  (LwjglApplication. (pathfinder.MyGame. ) "hello" 800 600))

(app)
