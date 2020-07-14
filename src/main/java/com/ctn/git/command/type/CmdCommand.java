package com.ctn.git.command.type;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public abstract class CmdCommand extends Command{
    protected ProcessBuilder processBuilder;
    public CmdCommand(ProcessBuilder processBuilder){
        this.processBuilder=processBuilder;
    }

    public abstract String  getExecuteCommand();
    
    public CmdCommandResult execute(){
        String resultMessage;
        try {
            processBuilder.command("cmd.exe", "/c", getExecuteCommand());
            final Process process = processBuilder.inheritIO().start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                resultMessage=String.format("%s Execution success",getExecuteCommand());
                reader.close();
            }else{
                resultMessage=String.format("%s Execution failed",getExecuteCommand());
                reader.close();
            }
            return new CmdCommandResult(output.toString(),getExecuteCommand(),resultMessage);
        } catch (Exception e) {
            resultMessage=String.format("%s Execution got exception",getExecuteCommand());
            return new CmdCommandResult(e.getMessage(),getExecuteCommand(),resultMessage);
        }
    }

    @Override
    public String toString() {
        return "CmdCommand{"+
                " Command Name= "+ this.getClass().toString() +
                " command= " + getExecuteCommand() +
                '}';
    }
}
