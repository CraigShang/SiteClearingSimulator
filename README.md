# SiteClearingSimulator

This simulator is for the training of bull dozer operator to simulate the scenario of a rectangle site that requires clearing. 

## Author

Peizheng Shang

## Getting tarted

start the simulation by running the class StartSimulation.java as java application.

### Prerequisites

JDK or JRE 7 or above

## Design and classes

This simulator consists of three classes: MapReader, BullDozerSimulator and StartSimulation. MapReader is a utility class to read and validate site map from a text file. BullDozerSimulator is the entity class that receive the commands from user, execute the command and keep track of the command history and cost incurred. Finally StartSimulation is for the interaction with user. It displays the UI information, reads and validates the user's command and displays the simulation results. 

## Validation and assumptions

1. The simulator will automatically read the text file sitemap.txt from \testData. This file can only contains lines of strings with same length and only consist of four chars: 'o','r','t','T'. If the text file fails the validation an error message will be given and the simulation process will not start.

2. The command given by user can be 'r' for right turn, 'l' for left turn, 'q' for quit and 'a' for advance. If user choose the advance command, a number as step count should be provided after a space of the 'a' command, for example, 'a 5'. Any other format of commands will fail the validation and user will be asked to re-input command.

3. The outside of map check will only happen after the user chose the advance command. Since the bull dozer's initial location is outside of the site map, user can turn right or left on the initial location without triggering the ending of simulation.