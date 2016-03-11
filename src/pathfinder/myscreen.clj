(ns pathfinder.myscreen
  (:import [com.badlogic.gdx Screen]
           [com.badlogic.gdx.graphics Color Texture]
           [com.badlogic.gdx.graphics.g2d SpriteBatch])
  (:require [pathfinder.assets :as assets]
            [pathfinder.grid :as grid]
            [pathfinder.astar :as astar]))

(declare ^SpriteBatch batch)
(declare tile-grid)

(defn testing []
  (astar/calc-path (grid/get-tile 0 0 tile-grid)
                   (grid/get-tile 3 3 tile-grid)
                   tile-grid))

(defn screen []
  (proxy [Screen] []
    (show []
      (def batch (SpriteBatch.))
      (assets/init-tex-cache)
      (def tile-grid (grid/create-grid 10 12)))
    
    (render [delta]
      (.begin batch)
      (grid/draw-grid tile-grid batch)
      (.end batch))
    
    (dispose[])
    (hide [])
    (pause [])
    (resize [w h])
    (resume [])))

