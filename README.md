# Masters of Reinaissance: ingswAM2021-DeSalvo-Dubini-Aulenti
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
