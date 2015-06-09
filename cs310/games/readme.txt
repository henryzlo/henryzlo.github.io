$cs310/games

This directory contains the game package for cs310, Spring 2009.

Compiling: 
 cd src; javac -d ../classes cs310/*.java for sources in cs310
 or use IDE

Running TestGame: first code example
With java: cd classes; java cs310.TestGame 

The main driver for the games is invoked as follows.
To start with, we use simple line-oriented UI, as follows. Later, in point 6 below, see note about GUI case.

cd classes
   java cs310.PlayGame <game> <player1> <player2>
 
important cases to start:
   java cs310.PlayGame <game> human cs310.Backtrack      (human goes first)
   java cs310.PlayGame <game>  cs310.Backtrack human    (human goes second)

For example,
   java cs310.PlayGame Easy human cs310.Backtrack
or just (since human cs310.Backtrack is the default)
   java cs310.PlayGame Easy

The games known to the package are

CMN, Easy, Fifteen, Game0, GameZero, Nim, Putnam, Sticks, Sticks5, Sticks7, Tictactoe

The provided computerplayer is

cs310.Backtrack // fake backtracking algorithm (you make it work properly in pa4)

You may write your own games and computerplayers by studying the
various APIs.

To see the javadoc, visit

http://www.cs.umb.edu/cs310/games/docs/api/
or in eclipse, find docs/api/index.html, and right-click>Open With>Web Browser

To get started reading the sources:  Only 1. and 2. are needed for hw5.

1. Core Game APIs:
in edu.umb.cs.game:
  Game.java, Move.java,PlayerNumber.java (an enum)

2. Easy Game: first complete game, very stupid
cs310.TestGame.java--main program
in edu.umb.cs.game:Easy.java--implements Game, Move, Iterator<Move>

3. PlayOneGame and Players, who decide on moves
in edu.umb.cs.game:PlayOneGame.java, Player.java, ComputerPlayer.java
cs310.Backtrack.java--not yet intelligent ComputerPlayer--you will fix

4. The business of getting a move out of a human, in edu.umb.cs.gamesui:HumanPlayer.java,
to keep the UI dependencies out of the game package. Note that HumanPlayer extends
the abstract class Player, itself in the game package, so the game package classes
can know about a Player, without being involved with UI directly. HumanPlayer
calls requestMove in ConsoleView to get a new move.

5. Rest of PlayOneGame support: see PlayGame for creating the objects
AbstractPlayOneGame.java, PlayOneGameFactory.java
AbstractFactory, GameFactory.java,PlayerFactory.java

6. If interested, the GUI setup: in package edu.umb.cs.gamesui.
Tictactoe has a GUI, TictactoeGUI.java.  It uses Swing to display a
simple grid of buttons on the screen for tic-tac-toe. To run this:
   java cs310.PlayGame -cg Tictactoe              for human first, Backtrack second
   jave cs310.PlayGame -cg Tictactoe cs310.Backtrack human     for human second

7. Other games to look at
BigTictactoe.java, Tictactoe.java
Nim.java
Game0.java
Fifteen.java
and others

Ant Support
An ant build.xml file is included, which will compile everything in the project with "ant build".
(This wil work on cs.umb UNIX systems, or if you install ant on your PC)
If you are using eclipse, it has an ant view, which allows you to do "ant build" by clicking
a button, once you have loaded the build.xml using the ant-plus button.
"ant run" will run TestGame
"ant javadoc" will rebuild the Javadocs in docs
"ant test" will run several of the game unit tests, test.out is the expected output.


