package com.ctn.git.command;

import com.ctn.git.command.type.CmdCommand;
import com.ctn.git.command.type.CmdCommandResult;

public class CmdCommandExecutor {
    
    private CmdCommandHistory history;
    private CmdCommandHistory executedHistory;
    private CmdCommandHistory<CmdCommandResult> executedCommandResults;
    
    public CmdCommandExecutor(){
        this.history=new CmdCommandHistory();
        this.executedHistory=new CmdCommandHistory();
        this.executedCommandResults= new CmdCommandHistory();
    }
    
    public void addCommand(CmdCommand cmdCommand){
        history.push(cmdCommand);
    }
    
    public void executeAllCommands(){
        while (!history.isEmpty()){
            CmdCommand command = (CmdCommand)history.pop();
            executedHistory.push(command);
            executedCommandResults.push(command.execute());
        }
    }

    public CmdCommandHistory getHistory() {
        return history;
    }
    public CmdCommandHistory getExecutedHistory() {
        return executedHistory;
    }
    public CmdCommandHistory getExecutedCommandResults() {
        return executedCommandResults;
    }
    public void setHistory(CmdCommandHistory history) {
        this.history = history;
    }
}
