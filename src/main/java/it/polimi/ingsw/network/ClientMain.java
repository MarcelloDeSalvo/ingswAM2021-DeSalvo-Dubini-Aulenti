package it.polimi.ingsw.network;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.client.ClientReceiver;
import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.cli.Color;
import it.polimi.ingsw.view.gui.Gui;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import java.util.stream.Collectors;


public class ClientMain {
    private String hostName;
    private int portNumber;
    private ClientView view;
    private String viewMode;
    private boolean singlePlayer = false;

    private final List<String> myParam = new ArrayList<>(Arrays.asList("-SERVER", "-PORT", "-VIEW", "--SOLO"));

    /**
     * Creates a new Client Main and tries to read its configuration file and the parameters passed by the user
     * @param args the arguments passed through the command line
     */
    public static void main(String[] args){
        ClientMain clientMain = null;

        if (args.length<=1){
            try {
                //reads the configuration from the file
                String jsonPath = "/ConfigurationFiles/ClientConfig.json";
                Gson gson = new Gson();
                Reader reader = new InputStreamReader(ClientMain.class.getResourceAsStream(jsonPath), StandardCharsets.UTF_8);
                clientMain = gson.fromJson(reader, ClientMain.class);

            }catch (Exception e){
                e.printStackTrace();
                System.out.println("There was an issue with starting the Client.");
                System.exit(1);
            }

        }else{
            clientMain = new ClientMain();
        }

        clientMain.commandLineParametersCheck(args);

        System.out.println("Hostname: " + clientMain.getHostName());
        System.out.println("Port: " + clientMain.getPortNumber());
        System.out.println("View: " + clientMain.getMode());

        if (!clientMain.singlePlayer)
            clientMain.connect();
        else{
            try {
                clientMain.view = clientMain.viewSelector();
            }catch (FileNotFoundException e){
                System.out.println("View Not Found, please restart");
                System.exit(-1);
            }
            clientMain.view.setNickname("Player");
            clientMain.view.setSinglePlayer(true);
            Controller controller = new Controller(clientMain.view, clientMain.view.getNickname());
            clientMain.view.addObserverController(controller);
            while (true){
                if (! clientMain.view.readInput())
                    break;
            }
        }
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
                    hostName = options.get(s).get(0);
                    break;

                case "-PORT":
                    if (options.get(s).size()!=1){
                        System.err.println("Port param error");
                        System.exit(1);
                    }

                    try {
                        portNumber = Integer.parseInt(options.get(s).get(0));
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

                    viewMode = options.get(s).get(0);
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
    }


    /**
     * Creates a new Socket and two threads: one listens to the socket (Receiver) and the other listens to the user inputs (CLI/GUI)
     */
    public void connect(){
        try {

            Socket echoSocket = new Socket(hostName, portNumber);
            view = viewSelector();

            ClientReceiver clientReceiver = new ClientReceiver(echoSocket, view);
            ClientSender clientSender = new ClientSender(echoSocket, view);

            view.addObserverController(clientSender);

            clientSender.start();
            clientReceiver.start();

            clientSender.join();
            clientReceiver.join();

            echoSocket.close();

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);

        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);

        } catch (InterruptedException e) {
            System.err.println("Thread InterruptedException error " + hostName);

        }
    }

    /**
     * Creates a new View based on the user's selection
     * @return a new view
     * @throws FileNotFoundException if the view cannot read his configuration files
     */
    ClientView viewSelector() throws FileNotFoundException {
        switch (this.viewMode.toUpperCase()){
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

    public String getHostName() {
        return hostName;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public String getMode() {
        return viewMode;
    }
}
