(ns pathfinder.assets
(:import [com.badlogic.gdx Gdx Files]
         [com.badlogic.gdx.graphics Texture]))

(declare tex-cache)

(defn init-tex-cache []
  (def tex-cache
    {:tile (Texture. "tile.png")
     :path (Texture. "path.png")
     :barrier (Texture. "barrier.png")}))