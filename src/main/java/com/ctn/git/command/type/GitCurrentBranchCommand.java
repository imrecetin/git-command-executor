package com.ctn.git.command.type;


import com.ctn.git.model.GitModel;

public class GitCurrentBranchCommand extends GitCommand {

    public GitCurrentBranchCommand(GitModel gitModel, ProcessBuilder processBuilder) {
        super(gitModel, processBuilder);
    }

    @Override
    public String getExecuteCommand() {
        return gitModel.gitCurrentBranchCommandText();
    }
}
