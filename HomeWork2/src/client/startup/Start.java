package client.startup;

import client.view.NonBlockingInterpreter;

public class Start {
    public static void main (String[] args){
        new NonBlockingInterpreter().start();
    }
}
