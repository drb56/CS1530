#Test and Branching/Integration Plan

##Test Plan:
1.	Test that each chess piece can move.
2.	Test that the system can recognize when a move is legal according to USCF rules.
3.	Test that the system can recognize when a move is illegal according to USCF rules.
4.	Test that when the save button is pressed, it saves the current game in progress.
5.	Test that when the load button is pressed, it loads a game from a PGN save-game list.
6.	Test that when the exit button is pressed, it exits the program.
7.	Test that when the undo button is pressed (from Edit -> undo), it correctly undoes a move.
8.	Test that the user color is black when the user chooses black.
9.	Test that the user color is white when the user chooses white.
10.	Test that the "game timer" correctly counts down the time since the game was started.
11. Test that when the "game timer" has elapsed, the current game is forfeited.
12.	Test that the “per turn timer” is correctly set at the beginning of each turn.
13.	Test that when the "per turn timer" has elapsed, the current move is forfeited.
14.	Test that the "player turn" notification correctly notifies the player of their turn.
15. Test that a player can not make more than one move at a time.
16. Test that the title of the game is "Laboon Chess".


For our project we are going to use a method called TDD, or Test Driven Development. For TDD we will write our tests before we write the actual code for the project. This will allow us to ensure that all tests will pass, and that the code is testable code.


##Branching/Integration Plan:
During each sprint our group will work in two teams. Teams will change from sprint to sprint, and equally divide up the work between the two groups. Once a team has finished their work and is ready to push to Git master, the other group will first review their work to check for errors. Finalizing the work, the reviewing team will "+1" their Git work to show that it's progress is to be considered complete. This ensures we have multiple sets of eyes on the code so we have a better opportunity to catch any errors.

Branching names: feature/"name of feature"
Who can integrate branches into master: Anyone, as long as the other group accepts the branch.

