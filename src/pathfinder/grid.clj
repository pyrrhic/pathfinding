(ns pathfinder.grid)

(defn- create-tile
  [x y texture]
  {:grid-x x 
   :grid-y y 
   :x (* x 32) 
   :y (* y 32)
   :passable true
   :parent nil
   :move-cost 1
   :g nil
   :f nil
   :texture texture})

(defn- create-row [row-num num-of-columns tex-cache]
	(loop [indx 0
	       data []]
	     (if (= (count data) num-of-columns)
	       data
	       (recur
	         (inc indx)
	         (conj data (create-tile row-num indx (:tile tex-cache)))))))

(defn create-grid [num-rows num-cols tex-cache]
  (vec (map #(create-row % num-cols tex-cache) (range 0 num-rows))))

(defn get-num-rows
  [grid]
  (count grid))

(defn get-num-cols
  [grid]
  (count (first grid)))

(defn get-tile 
  [x y grid]
  (if (and 
        (< x (get-num-rows grid))
        (>= x 0)
        (< y (get-num-cols grid))
        (>= y 0))
    (nth (nth grid x) y)
    nil))

(defn get-north-neighbor
  [x y grid]
  (get-tile x (inc y) grid))

(defn get-south-neighbor
  [x y grid]
  (get-tile x (dec y) grid))

(defn get-east-neighbor
  [x y grid]
  (get-tile (inc x) y grid))

(defn get-west-neighbor
  [x y grid]
  (get-tile (dec x) y grid))

(defn get-neighbors
 [x y grid]
 (filter #(not (nil? %)) 
   (conj
     [] 
     (get-north-neighbor x y grid)
     (get-east-neighbor x y grid)
     (get-south-neighbor x y grid)
     (get-west-neighbor x y grid))))

(defn draw-grid [grid batch]
	(loop [g grid]
	 (if (= (count g) 0)
	   nil
	   (do 
	     (doall (map 
	             (fn [tile]
	              (let [{x :x, y :y, t :texture} tile]
                 (.draw batch t (float x) (float y))))
	             (first g)))
	     (recur (rest g))))))
    
  