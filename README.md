# A Star Path Finding Test!

Experimenting with Clojure, Libgdx (dat Java interop), and my implementation of the path finding algorithm, A Star.

Super un-polished and un-optimized.
This is just a quick dirty hack to play around and learn some new-to-me tech.

And just incase you have no idea what the A Star path finding algorithm is, it's a shortest path algorithm.
Not guaranteed shortest path, since it makes some trade-offs for performance reasons. 
Usually see A Star or a variant of it in video games.

If you want guaranteed shortest path I think dijkstra's algorithm would be a good alternative.

## What it looks like, since you probably won't be downloading this.

![alt text](http://i.imgur.com/NsdfOh9.gif "Oops.")

Legend:
Green - The starting location.
Red - The destination.
Light blue - The path from the start to the destination.
Dark blue - "Walls". They are impassable. In the gif, I'm placing them at runtime.
## Usage

Run desktop_launcher.clj.

Green tile is the start location.
Red tile is the end location.
Light blue is the path.
Light grey is a walkable tile. 
Dark blue are walls.

Don't click on anything other than tiles, or you get to see the app crash.
Also don't place the start or end location on a wall, otherwise the app will crash.

Press Q then click on a tile to set the start location.
Press W then click on a tile to set the end location.
press E then click on a tile to place a wall.
