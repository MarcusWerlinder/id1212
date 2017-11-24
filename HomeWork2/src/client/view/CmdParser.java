package client.view;

import sun.plugin.dom.exception.InvalidStateException;

public class CmdParser {
    private static final String SPACE = " ";
    private Commands cmd;
    private String argument;
    private final String userLine;

    /**
     * We create an instance the line the user inputted
     * so we can operate on it
     */
    CmdParser(String userLine){
        this.userLine = userLine;
        parseCmd(userLine);
    }

    /**
     * We return the command
     */
    Commands getCmd(){
        return cmd;
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

    public String getArgument() throws InvalidStateException {
        if(argument == null) throw new InvalidStateException("Put in an argument as well plox dox plz");

        return argument;
    }
}
