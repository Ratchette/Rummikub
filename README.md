CIS 4150's Project 1 - A distributed model for the game of checkers.
This document will also serve as our team's User Manual

Authors (Group 2)
==================
Rafael Aquino de Carvalho
Ben Douek
Danielle Fudger
Jennifer Winer



Git URL:
=========
https://github.com/Ratchette/Checkers
***NOTE:*** This repository is currently private as an extra preventative measure against academic dishonesty. It will be made public as soon as the other teams require our source code. 

How to Compile and Run the Code
================================
1) Using a terminal, navigate into the directory Checkers

2) Modify the files client.policy and server.policy so in both files, their first line reads:
    grant codeBase "file:<FULL_PATH_TO_CURRENT_DIRECTORY>/" {
    eg. grant codeBase "file:/home/jennifer/University/CIS4150/project1/Checkers/" {

3) Enter the command:  bash run_script.sh
    NOTE: For your convenience the above command both compliles the code and starts the server

4) In a different terminal enter the following command to start the server:
    java -cp $PWD/checkers.jar:$PWD -Djava.security.policy=client.policy checkers.CheckersClient




Features
=========
- A Graphical User interface
- The ability to choose between multiple game types
- The start of a distrubuted computation model using RMI
- A JUnit testing suite
- A list of all of our test cases alongside the number of bugs that each case uncovered



Future Features
================
- Better GUI support for macs
- A distributed computation model featuring an RMI server and clients
- More communication protocols for the setup of the game
- Implement Design patterns
- Implement the ability for clients to obeserver the game instead of playing
- Add more checkers variants to our library
- Implement an artifically intelligent checkers playing client



Known Bugs
===========
At present there are no known bugs
Please see testcases.csv for more information

