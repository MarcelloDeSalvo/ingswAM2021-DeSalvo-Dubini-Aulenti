package it.polimi.ingsw.network;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.client.ClientReceiver;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.cli.Color;
import it.polimi.ingsw.view.gui.Gui;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

class ClientInfo{
    private String hostName;
    private int portNumber;
    private String viewMode;

    public String getHostName() { return hostName; }
    public int getPortNumber() { return portNumber; }
    public String getViewMode() { return viewMode; }

    public void setHostName(String hostName) { this.hostName = hostName; }
    public void setPortNumber(int portNumber) { this.portNumber = portNumber; }
    public void setViewMode(String viewMode) { this.viewMode = viewMode; }
}

public class ClientMain {

    private final ClientInfo clientInfo;
    private ClientView view;
    private boolean singlePlayer;

    private final List<String> myParam = new ArrayList<>(Arrays.asList("-SERVER", "-PORT", "-VIEW", "--SOLO"));

    public ClientMain(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
        singlePlayer = false;
    }

    public ClientMain() {
        clientInfo = new ClientInfo();
        singlePlayer = false;
    }

    /**
     * Creates a new Client Main. <br>
     * Tries to read its configuration file or the parameters passed by the user
     * @param args the arguments passed through the command line
     */
    public static void main(String[] args){
        ClientMain clientMain = null;

        if (args.length>1)
            clientMain = new ClientMain();
        else {  //reads the configuration from the file
            try {
                String jsonPath = "/ConfigurationFiles/ClientConfig.json";
                Gson gson = new Gson();
                Reader reader = new InputStreamReader(ClientMain.class.getResourceAsStream(jsonPath), StandardCharsets.UTF_8);
                ClientInfo clientInfo = gson.fromJson(reader, ClientInfo.class);
                clientMain = new ClientMain(clientInfo);

            }catch (Exception e){
                System.err.println("There was an issue with starting the Client: " + e.getMessage());
                System.exit(-1);
            }
        }

        clientMain.commandLineParametersCheck(args);

        try {
            clientMain.view = clientMain.viewSelector();
        }catch (FileNotFoundException | NullPointerException e){
            System.err.println("View Not Found, please restart");
            System.exit(-1);
        }

        if (!clientMain.singlePlayer)
            clientMain.connect();
        else
           clientMain.singlePlayer();
    }

    /**
     * Checks that the parameters are correct
     */
    public void commandLineParametersCheck(String[] args){

        HashMap<String, List<String>> options = new HashMap<>();
        List<String> params = Arrays.asList(args);

        List<String> paramsComm = params.stream().filter(c -> c.charAt(0) == '-').collect(Collectors.toList());

        for (String key: paramsComm) {
            options.put(key.toUpperCase(),new ArrayList<>());
        }


        String key = null;

        for (String s : args) {
            if (s.charAt(0) == '-') {
                if (!(myParam.contains(s.toUpperCase()))){
                    System.err.println("Params Error");
                    System.exit(1);
                }

                key = s;

            }else if (key != null) {
                options.get(key.toUpperCase()).add(s);
            }
        }

        commandLineCases(options);
    }

    /**
     * Executes the operations based on the received parameters
     * @param options are the commands received
     */
    private void commandLineCases(HashMap<String, List<String>> options){
        for (String s: options.keySet()) {
            switch (s){
                case "-SERVER":
                    if (options.get(s).size()!=1){
                        System.err.println("Server param error");
                        System.exit(1);
                    }
                    clientInfo.setHostName(options.get(s).get(0));
                    break;

                case "-PORT":
                    if (options.get(s).size()!=1){
                        System.err.println("Port param error");
                        System.exit(1);
                    }

                    try {
                        clientInfo.setPortNumber(Integer.parseInt(options.get(s).get(0)));
                    }catch (NumberFormatException e){
                        System.err.println("The port is not a string");
                        System.exit(1);
                    }

                    break;

                case "-VIEW":
                    if (options.get(s).size()!=1){
                        System.err.println("View param error");
                        System.exit(1);
                    }

                    if (!( options.get(s).get(0).equalsIgnoreCase("CLI") || options.get(s).get(0).equalsIgnoreCase("GUI"))){
                        System.err.println("Please select 'GUI' or 'CLI'");
                        System.exit(1);
                    }

                    clientInfo.setViewMode(options.get(s).get(0));
                    break;

                case "--SOLO":
                    if (options.get(s).size()!=0){
                        System.err.println("View param error");
                        System.exit(1);
                    }

                    System.out.println("SINGLE PLAYER MODE ON");

                    if (options.containsKey("-PORT") || options.containsKey("-SERVER"))
                        System.out.println("PORT and SERVER will be ignored");

                    singlePlayer = true;
                    break;

                default:
                    System.err.println("Params error");
                    System.exit(1);
            }

        }

        System.out.println("Hostname: " + clientInfo.getHostName());
        System.out.println("Port: " + clientInfo.getPortNumber());
        System.out.println("View: " + clientInfo.getViewMode());
    }

    /**
     * Creates a ClientReceiver that connects the client to the socket and listens to the streams<br>
     *
     */
    public void connect(){
        ClientReceiver clientReceiver = null;

        try {
            clientReceiver = new ClientReceiver(view, clientInfo.getHostName(), clientInfo.getPortNumber());

        }catch (Exception e){
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        view.addObserverController(clientReceiver);
        clientReceiver.start();
    }

    /**
     * Starts the single player mode
     */
    public void singlePlayer(){
        view.setNickname("Player");
        view.setSinglePlayer(true);
        Controller controller = new Controller(view, view.getNickname());
        view.addObserverController(controller);
    }

    /**
     * Creates a new View based on the user's selection
     * @return a new view
     * @throws FileNotFoundException if the view cannot read his configuration files
     */
    ClientView viewSelector() throws FileNotFoundException {
        switch (clientInfo.getViewMode().toUpperCase()){
            case "CLI":
                Cli cli = new Cli();
                System.out.println('\n'+"|°-___--_["+ Color.ANSI_CYAN.escape()+" CLI MODE "+Color.ANSI_RESET.escape()+"]_--___-°|"+'\n');
                return cli;

            case "GUI":
                return new Gui();

            default:
                System.out.println("Invalid view");
                return null;
        }
    }
}
