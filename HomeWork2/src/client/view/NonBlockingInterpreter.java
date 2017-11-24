package client.view;

import client.controller.Controller;
import client.net.Listener;

import java.io.IOException;
import java.util.Scanner;

public class NonBlockingInterpreter implements Runnable{
    private static final String PROMPT = "> ";
    private boolean receivingCmds = false;
    private final Scanner console = new Scanner(System.in);
    private  final ThreadSafeStdOut outMgr = new ThreadSafeStdOut();
    private Controller contr;
    private Console OwnConsole = new Console();

    /**
     * We start the interpreter that will read in commands
     * Structure
     * User command --> NonBlocking decrypts --> send to controller --> controller calls appropriate serverConnection function
     *
     */
    public void start(){
        if(receivingCmds){
            return;
        }
        receivingCmds = true;
        contr = new Controller();
        //We had our own listener to the controller, so that we can send back errors or msg from other part of the program
        contr.addServConnectionListener(OwnConsole);
        new Thread(this).start();
    }

    /**
     * Interprets commands and does something with them
     */
    @Override
    public void run() {
        while (receivingCmds) {
            try {
                CmdParser cmdPars = new CmdParser(readNextLine());

                //We want to make sure we have established a connection before we go and try anything else
                if(!contr.isConnected() && cmdPars.getCmd() != Commands.CON){
                    throw new IllegalStateException("You need to connect to the server before you do requests");
                }

                switch (cmdPars.getCmd()) {
                    case QUIT:
                        receivingCmds = false;
                        contr.disconnect();
                        break;
                    case CON:
                            contr.connect("127.0.0.1", 8080);
                        break;
                    case START:
                        contr.startGame();
                        break;
                    case GUESS:
                        try {
                            contr.guess(cmdPars.getArgument());
                        } catch (IndexOutOfBoundsException e) {
                            OwnConsole.errorMsg("You tried a bad GUESS command", e);
                        }
                        break;
                    default:
                        outMgr.println("This is a bad command");
                        break;
                }
            } catch (Exception e) {
                if (receivingCmds) OwnConsole.errorMsg(e.getMessage(), e);
            }
        }
    }

    private String readNextLine(){
        outMgr.print(PROMPT);
        return console.nextLine();
    }

    //This will allow other parts send back errors
    private class Console implements Listener {
        private final ThreadSafeStdOut outMsg = new ThreadSafeStdOut();

        @Override
        public void print(String msg){
            outMsg.println(msg);
            outMsg.print(PROMPT);
        }

        @Override
        public void errorMsg(String error, Exception e) {
            outMsg.print("Error: ");
            outMsg.println(error);
        }

        @Override
        public void disconnect() {
            print("You successfully disconnected");
        }
    }
}
