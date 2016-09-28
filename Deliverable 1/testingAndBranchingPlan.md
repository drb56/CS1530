#Test and Branching/Integration Plan

##Test Plan:
1.	Test that each chess piece can only move in its own way according to USCF rules
2.	Test that each chess piece can move.
3.	Test that when the save button is pressed, it saves the game.
4.	Test that when the load button is pressed, it loads a game.
5.	Test that when the exit button is pressed, it exits the game.
6.	Test that when the undo button is pressed (from Edit -> undo), it correctly undoes a move.
7.	Test that the system can recognize when a move is legal.
8.	Test that the system can recognize when a move is illegal.
9.	Test that the user color is black when the user chooses black.
10.	Test that the user color is white when the user chooses white.
11.	Test that the timer correctly counts down.
12.	Test that the “per turn timer” is correctly set at the beginning of each turn.
13.	Test that when the timer hits ‘0’, the current move is forfeit.
14.	Test that the player turn notification correctly notifies the player of their turn.
15. Test that a player can not make more than one move at a time.
16. Test that the title of the game is "Laboon Chess".


For our project we’re going to use a method called TDD, or Test Driven Development. For TDD we will write our tests before we write the actual code for the project. This will allow us to ensure that all tests will pass, and that the code is testable code.


##Branching/Integration Plan:
During each sprint our group will work in two teams, which will change from sprint to sprint, and equally divide up the work between the two groups. Once a team has finished their work and is ready to push to master, the other group will first review their work to check for errors.

We’re doing it this way to make sure we have multiple sets of eyes on the code so we catch any errors in our code. 

Branching names: feature/"name of feature"
Who can integrate branches into master: Anyone, as long as the other group accepts the branch.

