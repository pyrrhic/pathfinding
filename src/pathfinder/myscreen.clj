(ns pathfinder.myscreen
  (:import [com.badlogic.gdx Screen Gdx InputProcessor Input Input$Keys Input$Buttons]
           [com.badlogic.gdx.graphics Color Texture GL20]
           [com.badlogic.gdx.graphics.g2d SpriteBatch])
  (:require [pathfinder.assets :as assets]
            [pathfinder.grid :as grid]
            [pathfinder.astar :as astar]))

(def game {})

(defn testing []
  (astar/calc-path (grid/get-tile 0 0 (:tile-grid game))
                   (grid/get-tile 3 3 (:tile-grid game))
                   (:tile-grid game)))

(defn add-barrier [game x y]
  (assoc-in (assoc-in game [:tile-grid x y :passable] false)
            [:tile-grid x y :texture] (:barrier (:tex-cache game))))

(defn input-processor []
  (reify InputProcessor
    (touchDown [this x y pointer button] false) ;if (button == Input.Buttons.LEFT) {
    (keyDown [this keycode] 
      (alter-var-root (var game) #(assoc-in % [:inputs (keyword (Input$Keys/toString keycode))] true))
      true)
    (keyUp [this keycode] 
      (alter-var-root (var game) #(assoc-in % [:inputs (keyword (Input$Keys/toString keycode))] false))
      true)
    (keyTyped [this character] false)
    (touchUp [this x y pointer button] 
      (alter-var-root (var game) #(assoc-in % [:inputs :mouse-x] x))
      (alter-var-root (var game) #(assoc-in % [:inputs :mouse-y] y))
      false)
    (touchDragged [this x y pointer] false)
    (mouseMoved [this x y] false)
    (scrolled [this amount] false)))

(defn clear-screen []
  (doto (Gdx/gl)
    (.glClearColor 1 1 1 1)
    (.glClear GL20/GL_COLOR_BUFFER_BIT)))

(defn get-state-from-input [inputs]
  (cond 
    (:Q inputs) :place-start
    (:W inputs) :place-end
    (:E inputs) :place-barrier
    :else nil))

(defn set-place-state [game]
  (assoc game :place-state 
          (if (nil? (get-state-from-input (:inputs game))) 
            (:place-state game) 
            (get-state-from-input (:inputs game)))))

(defn draw-everything [game]
  (.begin (:batch game))
  (grid/draw-grid (:tile-grid game) (:batch game))
  (doall 
    (map (fn [tile] (.draw (:batch game) (:path (:tex-cache game)) (float (:x tile)) (float (:y tile)))) (testing)))
  (.end (:batch game))
  game)

(defn clear-inputs [game]
  (assoc game :inputs {}))

(defn game-loop [game delta]
  (clear-screen)
  (->> (set-place-state game)
    (draw-everything)
    (clear-inputs)))

(defn screen []
  (reify Screen
    (show [this]
      (.setInputProcessor Gdx/input (input-processor))
      (def game (let [pre (assoc {}
                                 :batch (SpriteBatch.)
                                 :tex-cache (assets/init-tex-cache)
                                 :place-state :place-none
                                 :inputs {})] 
                  (assoc pre
                    :tile-grid (let [g (grid/create-grid 10 12 (:tex-cache pre))
                                     b (assoc-in g [2 1 :passable] false)
                                     bb (assoc-in b [1 2 :passable] false)
                                     bt (assoc-in bb [2 1 :texture] (:barrier (:tex-cache pre)))
                                     bbt (assoc-in bt [1 2 :texture] (:barrier (:tex-cache pre)))]
                                 bbt)))))
    
    (render [this delta]
      (alter-var-root (var game) #(game-loop % delta)))
    
    (dispose[this])
    (hide [this])
    (pause [this])
    (resize [this w h])
    (resume [this])))