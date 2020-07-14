package com.ctn.git.command.type;

import com.ctn.git.model.GitModel;
import sun.plugin.cache.CacheUpdateHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GitPullAllBranchesCommand extends GitCommand {

    //Insertion order
    private LinkedList<CmdCommand> pullCommands;
    private LinkedList<CmdCommandResult> pullCommandResults;
    
    public GitPullAllBranchesCommand(GitModel gitModel,ProcessBuilder processBuilder) throws CloneNotSupportedException {
        super(gitModel,processBuilder);
        final List<String> allLocalBranches = gitModel.getAllLocalBranches();
        generateCommands(allLocalBranches);
        pullCommandResults=new LinkedList<>();
    }

    private void generateCommands(List<String> allLocalBranches) throws CloneNotSupportedException {
        if (allLocalBranches==null || allLocalBranches.isEmpty())
            return;
        pullCommands=new LinkedList<>();
        GitStashCommand stashCommand=new GitStashCommand(gitModel.clone(),processBuilder,"created stash by JVM");
        pullCommands.add(stashCommand);
        String previousCurrentBranchName=gitModel.getCurrentBranchName();
        for(String branchName:allLocalBranches){
            gitModel.setCurrentBranchName(branchName);
            GitCheckoutCommand checkoutCommand=new GitCheckoutCommand(gitModel.clone(),processBuilder);
            GitPullCommand pullCommand=new GitPullCommand(gitModel.clone(),processBuilder);
            pullCommands.add(checkoutCommand);
            pullCommands.add(pullCommand);
        }
        gitModel.setCurrentBranchName(previousCurrentBranchName);
        GitCheckoutCommand reverseCheckoutCommand=new GitCheckoutCommand(gitModel,processBuilder);
        pullCommands.add(reverseCheckoutCommand);
    }

    @Override
    public String getExecuteCommand() {
        return null;
    }

    @Override
    public GitCommandResult execute() {
        if(pullCommands==null || pullCommands.isEmpty())
            return new GitCommandResult("Empty Pull Commands",null,"Empty Pull Commands");
        for(CmdCommand cmdCommand:pullCommands){
            CmdCommandResult commandResult = cmdCommand.execute();
            pullCommandResults.add(commandResult);
        }
        return new GitCommandResult(pullCommandResults.stream().map(CmdCommandResult::getOutput).collect(Collectors.joining(" \n ")),
                pullCommandResults.stream().map(CmdCommandResult::getExecutedCommand).collect(Collectors.joining(" \n ")),
                pullCommandResults.stream().map(CmdCommandResult::getResultMessage).collect(Collectors.joining(" \n ")));
    }

    @Override
    public String toString() {
        return "GitPullAllBranchesCommand{ " +
                " pullCommands= " + pullCommands.stream().map(CmdCommand::toString).collect(Collectors.joining(" \n ")) +
                " } ";
    }
}
