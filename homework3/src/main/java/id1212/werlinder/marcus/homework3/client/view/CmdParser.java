package id1212.werlinder.marcus.homework3.client.view;

import sun.plugin.dom.exception.InvalidStateException;

import java.util.Arrays;

public class CmdParser {
    private static final String SPACE = " ";
    private Commands cmd;
    private String argument;
    private String[] userArgs;

    /**
     * We create an instance the line the user inputted
     * so we can operate on it
     */
    CmdParser(String userLine){
        parseCmd(userLine);
        extractArgs(userLine);
    }

    /**
     * We return the command
     */
    Commands getCmd(){
        return cmd;
    }

    private void extractArgs(String enteredLine) {
        if(enteredLine == null){
            userArgs = null;
            return;
        }

        userArgs = enteredLine.split(SPACE);
        //We don't want the command in this string array
        userArgs = Arrays.copyOfRange(userArgs, 1, userArgs.length);
    }

    private String noExtraSpace(String text){
        if(text == null) {
            return text;
        }
        String plusSign = "+";
        return text.trim().replaceAll(SPACE + plusSign, SPACE);
    }

    private void parseCmd(String userLine){
        int cmdNameIndex = 0;
        try{
            String[] splittedUserLine = noExtraSpace(userLine).split(SPACE);
            cmd = Commands.valueOf(splittedUserLine[cmdNameIndex].toUpperCase());
            if(splittedUserLine.length > 1){
                argument = splittedUserLine[cmdNameIndex + 1].toUpperCase();
            }
        } catch (Throwable failedToReadCmd){
            cmd = Commands.BAD_COMMAND;
        }
    }

    public String getArgument() {
        if(argument == null) throw new IndexOutOfBoundsException("Put in an argument as well plox dox plz");

        return argument;
    }

    public String getArgument(int i) {
        if (userArgs == null) return null;
        if (userArgs.length <= i || userArgs.length == 0) return null;

        return userArgs[i];
    }
}
