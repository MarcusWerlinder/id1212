package client.view;

/**
 * To make sure that multiple clients print on the same time
 * We ensure that only one at a time is printed
 */
public class ThreadSafeStdOut {
    /**
     * I want to print out safely :)
     */
    synchronized void print(String text) {
        System.out.print(text);
    }
    /**
     * Same as above but if I want a linebreak as well :)
     */
    synchronized void println(String text){
        System.out.println(text);
    }
}
