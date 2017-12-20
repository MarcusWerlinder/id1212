package client.startup;

import client.view.NonBlockingInterpreter;

public class MainStart {

    public static void main (String[] args){
        new NonBlockingInterpreter().start();
    }
}
