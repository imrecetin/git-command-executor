package com.ctn.git.command.type;

import com.ctn.git.command.type.GitCommand;
import com.ctn.git.model.GitModel;

public class GitCheckoutCommand extends GitCommand {

    public GitCheckoutCommand(GitModel gitModel,ProcessBuilder processBuilder) {
        super(gitModel,processBuilder);
    }

    @Override
    public String getExecuteCommand() {
        return gitModel.checkoutCommandText(gitModel.getCurrentBranchName());
    }
}
