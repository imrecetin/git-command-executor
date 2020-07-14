package com.ctn.git.command.type;

import com.ctn.git.model.GitModel;

public class GitPullCommand extends GitCommand {
    
    public GitPullCommand(GitModel gitModel,ProcessBuilder processBuilder) {
        super(gitModel,processBuilder);
    }
    
    @Override
    public String getExecuteCommand() {
        return gitModel.pullCommandText();
    }
}
