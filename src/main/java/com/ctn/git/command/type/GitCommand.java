package com.ctn.git.command.type;


import com.ctn.git.model.GitModel;

public abstract class GitCommand extends CmdCommand {
    
    public final GitModel gitModel;
    public GitCommand(GitModel gitModel,ProcessBuilder processBuilder){
        super(processBuilder);
        this.gitModel=gitModel;
    }

    @Override
    public String toString() {
        return "CmdCommand{"+
                " Command Name= "+ this.getClass().toString() +
                " command= " + getExecuteCommand() +
                " branch Name= "+gitModel.getCurrentBranchName()+
                '}';
    }
    
}
