package client.view;


import client.net.OutputHandler;
import client.controller.Controller;

import java.util.Scanner;

public class NonBlockingInterpreter implements Runnable {
    private static final String PROMPT = "> ";
    private final Scanner console = new Scanner(System.in);
    private boolean receivingCmds = false;
    private Controller contr;
    private final ThreadSafeStdOut outMgr = new ThreadSafeStdOut();

    /**
     * Here we start an interpreter that will read the users input
     * and respond with the right method
     */
    public void start(){
        if(receivingCmds){
            return;
        }
        receivingCmds = true;
        contr = new Controller();
        new Thread(this).start();
    }

    @Override
    public void run(){
        while(receivingCmds) {
            try {
                CmdParser cmdLine = new CmdParser(readNextLine());
                switch (cmdLine.getCmd()) {
                    case QUIT:
                        receivingCmds = false;
                        contr.disconnect();
                        break;
                    case CON:
                        contr.connect("127.0.0.1", 8080, new ConsoleOutput());
                        break;
                    case START:
                        contr.startGame();
                        break;
                    case GUESS:
                        contr.sendGuess(cmdLine.getArgument());
                        break;
                    default:
                        outMgr.println("Not a valid command");
                }
            } catch (Exception e) {
                outMgr.println("Crash and burn");
            }
        }

    }

    private String readNextLine(){
        outMgr.print(PROMPT);
        return console.nextLine();
    }

    public class ConsoleOutput implements OutputHandler {
        @Override
        public void handleMsg(String msg) {
            outMgr.println(msg);
            outMgr.print(PROMPT);
        }
    }
}
