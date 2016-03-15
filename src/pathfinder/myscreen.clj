(ns pathfinder.myscreen
  (:import [com.badlogic.gdx Screen Gdx]
           [com.badlogic.gdx.graphics Color Texture GL20]
           [com.badlogic.gdx.graphics.g2d SpriteBatch]
           [com.badlogic.gdx InputProcessor Input Input$Keys])
  (:require [pathfinder.assets :as assets]
            [pathfinder.grid :as grid]
            [pathfinder.astar :as astar]))

(declare move-path)
(declare game)

(defn testing []
  (astar/calc-path (grid/get-tile 0 0 (:tile-grid game))
                   (grid/get-tile 3 3 (:tile-grid game))
                   (:tile-grid game)))

(defn handle-inputs [inputs]
  (cond 
    (:Q inputs) :place-start
    (:W inputs) :place-end
    (:E inputs) :place-barrier))

(defn input-processor []
  (reify InputProcessor
    (touchDown [this x y pointer button] false)
    (keyDown [this keycode] 
      (def down-keys (assoc (:inputs game) (keyword (Input$Keys/toString keycode)) true))
      true)
    (keyUp [this keycode] 
      (def down-keys (assoc (:inputs game) (keyword (Input$Keys/toString keycode)) false))
      true)
    (keyTyped [this character] false)
    (touchUp [this x y pointer button] false)
    (touchDragged [this x y pointer] false)
    (mouseMoved [this x y] false)
    (scrolled [this amount] false)))

(defn screen []
  (reify Screen
    (show [this]
      (.setInputProcessor Gdx/input (input-processor))
      (def game (let [pre (assoc {} 
                                 :batch (SpriteBatch.)
                                 :tex-cache (assets/init-tex-cache)
                                 :inputs {})] 
                  (assoc pre
                    :tile-grid (let [g (grid/create-grid 10 12 (:tex-cache pre))
                                     b (assoc-in g [2 1 :passable] false)
                                     bb (assoc-in b [1 2 :passable] false)
                                     bt (assoc-in bb [2 1 :texture] (:barrier (:tex-cache pre)))
                                     bbt (assoc-in bt [1 2 :texture] (:barrier (:tex-cache pre)))]
                                 bbt)))))
    
    (render [this delta]
      (doto (Gdx/gl)
        (.glClearColor 1 1 1 1)
        (.glClear GL20/GL_COLOR_BUFFER_BIT))
      (.begin (:batch game))
      (grid/draw-grid (:tile-grid game) (:batch game))
      (doall 
        (map (fn [tile] (.draw (:batch game) (:path (:tex-cache game)) (float (:x tile)) (float (:y tile)))) (testing)))
      (.end (:batch game)))
    
    (dispose[this])
    (hide [this])
    (pause [this])
    (resize [this w h])
    (resume [this])))