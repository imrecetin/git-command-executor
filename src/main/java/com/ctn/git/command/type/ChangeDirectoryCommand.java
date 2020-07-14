package com.ctn.git.command.type;

public class ChangeDirectoryCommand extends CmdCommand {

    private String directory;
    
    public ChangeDirectoryCommand(String directory,ProcessBuilder processBuilder){
        super(processBuilder);
        this.directory=directory;
    }

    @Override
    public String getExecuteCommand() {
        return "cd "+directory;
    }
}
