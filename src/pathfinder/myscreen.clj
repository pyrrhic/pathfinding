(ns pathfinder.myscreen
  (:import [com.badlogic.gdx Screen Gdx InputProcessor Input Input$Keys Input$Buttons]
           [com.badlogic.gdx.graphics Color Texture GL20]
           [com.badlogic.gdx.graphics.g2d SpriteBatch])
  (:require [pathfinder.assets :as assets]
            [pathfinder.grid :as grid]
            [pathfinder.astar :as astar]))

(def game {})

(def start [0 0])
(def goal [4 4])

(defn calculate-path []
  (astar/calc-path (grid/get-tile (nth start 0) (nth start 1) (:tile-grid game))
                   (grid/get-tile (nth goal 0) (nth goal 1) (:tile-grid game))
                   (:tile-grid game)))

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
      (alter-var-root (var game) #(assoc-in % [:inputs :mouse-y] (- 600 y)))
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
  "updates the :place-state in the game map with either :place-start, :place-end, or :place-barrier"
  (assoc game :place-state 
          (if (nil? (get-state-from-input (:inputs game))) 
            (:place-state game) 
            (get-state-from-input (:inputs game)))))

(defn draw-everything [game]
  (.begin (:batch game))
  (grid/draw-grid (:tile-grid game) (:batch game))
  (doall 
    (map (fn [tile] (.draw (:batch game) (:path (:tex-cache game)) (float (:x tile)) (float (:y tile)))) (calculate-path)))
  (.draw (:batch game) 
    (:start (:tex-cache game)) 
    (float (* 32 (nth start 0))) 
    (float (* 32 (nth start 1))))
  (.draw (:batch game) 
    (:goal (:tex-cache game)) 
    (float (* 32 (nth goal 0))) 
    (float (* 32 (nth goal 1))))
  (.end (:batch game))
  game)

(defn clear-inputs [game]
  (assoc game :inputs {}))

(defn pixel->tile [n]
  (quot n 32))

(defn add-barrier [game x y]
  (assoc-in (assoc-in game [:tile-grid x y :passable] false)
            [:tile-grid x y :texture] (:barrier (:tex-cache game))))

(defn add-tile [game x y]
  (assoc-in (assoc-in game [:tile-grid x y :passable] true)
            [:tile-grid x y :texture] (:tile (:tex-cache game))))

(defn add-start [game x y]
  "replaces old start texture with a tile texture, updates the start var, and adds new start texture"
  (let [g-no-start (add-tile game (nth start 0) (nth start 1))]
    (alter-var-root (var start) (fn [s] [x y]))
    (assoc-in (assoc-in g-no-start [:tile-grid x y :passable] true)
            [:tile-grid x y :texture] (:start (:tex-cache g-no-start)))))

(defn add-goal [game x y]
  "replaces old goal texture with a tile texture, updates the goal var, and adds new goal texture"
  (let [g-no-goal (add-tile game (nth goal 0) (nth goal 1))]
    (alter-var-root (var goal) (fn [g] [x y]))
    (assoc-in (assoc-in g-no-goal [:tile-grid x y :passable] true)
              [:tile-grid x y :texture] (:goal (:tex-cache g-no-goal)))))

(defn process-mouse-click [game]
  (if (nil? (get-in game [:inputs :mouse-x]))
    game
    (let [x (get-in game [:inputs :mouse-x])
          y (get-in game [:inputs :mouse-y])]
      (case (:place-state game)
        :place-barrier (add-barrier game (pixel->tile x) (pixel->tile y))
        :place-start (add-start game (pixel->tile x) (pixel->tile y))
        :place-end (add-goal game (pixel->tile x) (pixel->tile y))
        :place-none game))))

(defn game-loop [game delta]
  (clear-screen) 
  (-> (set-place-state game)
    (process-mouse-click)
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
                    :tile-grid (grid/create-grid 20 15 (:tex-cache pre))))))
    
    (render [this delta]
      (if (empty? game) ;so the app does not explode with a load this file into the repl. it'll explode because (show) is never called, and it sets up the game map.
        ""
        (alter-var-root (var game) #(game-loop % delta))))
    
    (dispose[this])
    (hide [this])
    (pause [this])
    (resize [this w h])
    (resume [this])))