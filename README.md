Rummikub
========

The game of Rummikub

Future Implementation Ideas
============================

Server
-------
- Make the server compute the total time it takes to play a game (using Java's Date structure)
- printUsage: find a way to unify the name of the program in this function and in the ant file (AKA figure out a way to share to have both use the same variable
- run: analyze the exception that was thrown (what and from who) to provide the user with more meaningful input as to the error
- main: Allow one server to host multiple games - keep forking off new threads? - one loop in main that does not terminate automatically at the end of a game
- main: on the previous point, need to redirect each game's output to a different location
	eg. McCaughan's server 

- implement a client-server protocol so that each player knows how many players there are in total in addition to which player number it is.
- print all errors to stderr
- make errors easier to distinguish from everything else

- why do 90% of functions throw an exception?
