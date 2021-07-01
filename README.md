# Masters of Reinaissance: ingswAM2021-DeSalvo-Dubini-Aulenti
## Brief introduction
This project consists in a JAVA implementation of the board game "Masters of renaissance". As required, the server implements the MVC pattern with a single virtual view,controller and model for each game that the server handles (Multigame FA). On the other side of the connection, the client implements a view (cli/gui) and a light version of the model (LiteModel). <br>
For a better showcase of the program we implemented a "CHEAT" command accessible during your turn through the specific button in Gui, and through typing "Cheat" in Cli. In particular, the cheat command gives the player 2 faithpoints and puts 25 of each Resource inside his vault. <br>
Both the server and client are contained in a single jar. Instructions on how to run them or on how to run the game offline (Solo FA) will follow later on in the read.me


## Functionalities
- Complete rules
- CLI
- GUI
- Socket
- 2 FA: offline mode + multi-game

## Libraries and plugins used
- IntelliJ
- Java Swing
- JUnit
- Gson
- Gson extras
- Maven assembly

## System Requiremets
### WSL (Ubuntu)
- Suggested System settings:
  - Font: DejaVu Sans Mono for Powerline
  - Font Dimension: 19
  - Wrap text output on resize: unchecked  (Properties -> Layout -> Screen Buffer Size)

Windows cmd not supported (cannot show the ANSI colours)

 ###  SWING
  - Tested and supported resolution: 1920x1080

## How to execute the Jar
There's only one launcher that can start both the server and the clent.
The parameters aren't case sensitive and can be inserted without a specific order.

### Server
#### Starts the server after reading the default server-congifuration file
- Required parameters: -Server 
- Default Port: 50623

>java -jar AM8-MastersLauncher.jar -Server


#### Starts the server with a custom port number
- Required parameters: -Server, -Port n 

>java -jar AM8-MastersLauncher.jar -Server -Port 1234

### Client
#### Starts the client after reading the default client-congifuration file
- Required parameters: -Client 
- Default Port: 50623
- Default ip-address: 127.0.0.1
- Default view: CLI

>java -jar AM8-MastersLauncher.jar -Client

#### Starts the client with custom parameters
- Required parameters: -Client, -Port n, -Server ip, -View 'CLI/GUI'

>java -jar AM8-MastersLauncher.jar -Client -Port 1234 -Server 8.8.8.8 -View Gui

#### Starts the client in Offline-Mode
- Required parameters: -Client, --Solo, -View 'CLI/GUI'

>java -jar AM8-MastersLauncher.jar -Client --Solo -View cli
