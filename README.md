# A Star Path Finding Test!

Experimenting with Clojure, Libgdx (dat Java interop), and my implementation of the path finding algorithm, A Star.

Super un-polished and un-optimized.
This is just a quick dirty hack to play around and learn some new-to-me tech.

## What it looks like, since you probably won't be downloading this.

![alt text](http://i.imgur.com/NsdfOh9.gif "Oops.")

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
