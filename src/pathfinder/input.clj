(ns pathfinder.input
  (:import [com.badlogic.gdx InputProcessor]))

(def down-keys {})

(defn reset-down-keys [d-keys]
  def down-keys {})

(defn input-processor []
  (reify InputProcessor
    (touchDown [this x y pointer button] false)
    (keyDown [this keycode] 
      (def down-keys (assoc down-keys (keyword keycode) true))
      true)
    (keyUp [this keycode] 
      (def down-keys (assoc down-keys (keyword keycode) true))
      true)
    (keyTyped [this character] false)
    (touchUp [this x y pointer button] false)
    (touchDragged [this x y pointer] false)
    (mouseMoved [this x y] false)
    (scrolled [this amount] false)))
  