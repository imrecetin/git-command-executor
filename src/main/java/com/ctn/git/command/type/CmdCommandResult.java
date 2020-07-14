package com.ctn.git.command.type;

public class CmdCommandResult extends Command{
    
    private String output;
    private String executedCommand;
    private String resultMessage;
    
    public CmdCommandResult(String output,String executedCommand,String resultMessage) {
        this.output=output;
        this.executedCommand=executedCommand;
        this.resultMessage=resultMessage;
    }

    @Override
    public String toString() {
        return "CmdCommandResult { " +
                " output= '" + output + '\'' +
                " executedCommand= '" + executedCommand + '\'' +
                " resultMessage= '" + resultMessage + '\'' +
                " } ";
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getExecutedCommand() {
        return executedCommand;
    }

    public void setExecutedCommand(String executedCommand) {
        this.executedCommand = executedCommand;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

}
