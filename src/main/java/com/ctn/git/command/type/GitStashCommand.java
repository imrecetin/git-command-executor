package com.ctn.git.command.type;

import com.ctn.git.model.GitModel;

public class GitStashCommand extends GitCommand {

    private String stashName;
    
    public GitStashCommand(GitModel gitModel,ProcessBuilder processBuilder,String stashName) {
        super(gitModel,processBuilder);
        this.stashName=stashName;
    }

    public GitStashCommand(GitModel gitModel,ProcessBuilder processBuilder) {
        super(gitModel,processBuilder);
    }
    
    @Override
    public String getExecuteCommand() {return gitModel.stashCommandText(this.stashName);}
    
}
