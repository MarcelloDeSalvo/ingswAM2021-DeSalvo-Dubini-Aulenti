package it.polimi.ingsw.network;

import it.polimi.ingsw.network.client.ClientReceiver;
import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.gui.Gui;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import java.util.stream.Collectors;


public class ClientMain {
    private String hostName;
    private int portNumber;

    private ClientView view;
    private String viewMode;

    private ClientSender clientSender;
    private ClientReceiver clientReceiver;

    private final List<String> myParam = new ArrayList<>(Arrays.asList("-SERVER", "-PORT", "-VIEW", "--SOLO"));

    public ClientMain(){ }

    public static void main(String[] args){

        ClientMain clientMain = new ClientMain();

        clientMain.commandLineParametersCheck(args);

        System.out.println("Hostname: " + clientMain.getHostName());
        System.out.println("Port: " + clientMain.getPortNumber());
        System.out.println("View: " + clientMain.getMode());

        clientMain.connect();
    }

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

                    if (!( options.get(s).get(0).toUpperCase().equals("CLI") || options.get(s).get(0).toUpperCase().equals("GUI"))){
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
                    break;

                default:
                    System.err.println("Params error");
                    System.exit(1);
            }

        }
    }


    public void connect(){
        try {

            Socket echoSocket = new Socket(hostName, portNumber);
            view = viewSelector();

            clientReceiver = new ClientReceiver(echoSocket, view);
            clientSender = new ClientSender(echoSocket, view);

            view.setSender(clientSender);


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
            e.printStackTrace();
        }

    }

    ClientView viewSelector() throws FileNotFoundException {
        switch (this.viewMode.toUpperCase()){
            case "CLI":
                Cli cli = new Cli();
                System.out.println('\n'+"|°-___--_[ CLI MODE ]_--___-°|"+'\n');
                return cli;

            case "GUI":
                Gui gui = new Gui();
                return gui;

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
