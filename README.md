# Android Connect 4 Game
Android connect 4 game based on minimax algorithm, as the player is considered the minimizing palyer and the program is the maximizing player, the difficulty of the game can be adjusted by controlling the depth of minimax recursion (more depth means more intelligence but also needs more time) also to prevent the game from blocking in program's turn the minimax was implemented in a worker thread so all the time-consuming stuff would be done in the back ground without affecting the game's main thread

The board of the game is 6x7 not 7x6 so it's not really standard game board, but who cares, the standard board is solved any way (one of the players can force winning)

Hope you get use of the code and enjoy the game :D
