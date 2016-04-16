(ns pathfinder.assets
(:import [com.badlogic.gdx Gdx Files]
         [com.badlogic.gdx.graphics Texture]))

(defn init-tex-cache []
  {:tile (Texture. "tile.png")
   :path (Texture. "path.png")
   :barrier (Texture. "barrier.png")
   :start (Texture. "start.png")
   :goal (Texture. "stop.png")})