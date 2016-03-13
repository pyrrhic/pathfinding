(ns pathfinder.myscreen
  (:import [com.badlogic.gdx Screen Gdx]
           [com.badlogic.gdx.graphics Color Texture GL20]
           [com.badlogic.gdx.graphics.g2d SpriteBatch])
  (:require [pathfinder.assets :as assets]
            [pathfinder.grid :as grid]
            [pathfinder.astar :as astar]
            [pathfinder.input :as input]))

(declare ^SpriteBatch batch)
(declare tile-grid)
(declare move-path)

(defn testing []
  (astar/calc-path (grid/get-tile 0 0 tile-grid)
                   (grid/get-tile 3 3 tile-grid)
                   tile-grid))

(defn screen []
  (reify Screen
    (show [this]
      (.setInputProcessor Gdx/input (input/input-processor))
      (def batch (SpriteBatch.))
      (assets/init-tex-cache)
      (def tile-grid (let [g (grid/create-grid 10 12)
                           b (assoc-in g [2 1 :passable] false)
                           bb (assoc-in b [1 2 :passable] false)
                           bt (assoc-in bb [2 1 :texture] (:barrier assets/tex-cache))
                           bbt (assoc-in bt [1 2 :texture] (:barrier assets/tex-cache))]
                       bbt))
      (def move-path (testing)))
    
    (render [this delta]
      (doto (Gdx/gl)
        (.glClearColor 1 1 1 1)
        (.glClear GL20/GL_COLOR_BUFFER_BIT))
      (.begin batch)
      (grid/draw-grid tile-grid batch)
      (doall 
        (map (fn [tile] (.draw batch (:path assets/tex-cache) (float (:x tile)) (float (:y tile)))) (testing)))
      (.end batch)
      (input/))
    
    (dispose[this])
    (hide [this])
    (pause [this])
    (resize [this w h])
    (resume [this])))

;(defn screen []
;  (proxy [Screen] []
;    (show []
;      (def batch (SpriteBatch.))
;      (assets/init-tex-cache)
;      (def tile-grid (let [g (grid/create-grid 10 12)
;                           b (assoc-in g [2 1 :passable] false)
;                           bb (assoc-in b [1 2 :passable] false)]
;                       bb))
;      (def move-path (testing)))
;    
;    (render [delta]
;      (doto (Gdx/gl)
;        (.glClearColor 1 1 1 1)
;        (.glClear GL20/GL_COLOR_BUFFER_BIT))
;      (.begin batch)
;      (grid/draw-grid tile-grid batch)
;      (let [pa (map (fn [tile] (if (not (:passable tile)) 
;                                 (assoc tile :texture (:barrier assets/tex-cache))
;                                 tile)) (testing))]
;        (doall 
;          (map (fn [tile] (.draw batch (:path assets/tex-cache) (float (:x tile)) (float (:y tile)))) pa)))
;      (.end batch))
;    
;    (dispose[])
;    (hide [])
;    (pause [])
;    (resize [w h])
;    (resume [])))