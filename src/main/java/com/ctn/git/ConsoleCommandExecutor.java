package com.ctn.git;

import com.ctn.git.command.type.ChangeDirectoryCommand;
import com.ctn.git.command.CmdCommandExecutor;
import com.ctn.git.command.CmdCommandHistory;
import com.ctn.git.command.type.CmdCommandResult;
import com.ctn.git.command.type.Command;
import com.ctn.git.command.type.GitCurrentBranchCommand;
import com.ctn.git.command.type.GitPullAllBranchesCommand;
import com.ctn.git.command.type.GitStashCommand;
import com.ctn.git.model.GitModel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConsoleCommandExecutor {
    private static final Logger LOGGER=Logger.getLogger(ConsoleCommandExecutor.class.getName());
    private static final String GIT_BASE_FOLDER_NAME="\\.git";

    public static void main( String[] args ) throws CloneNotSupportedException {
        
        checkIsGitRepository(args);
        
        final String projectFullPath=args[0];
        final String operationType=args[1];
        Optional<GitOperations> gitOperation = GitOperations.valueBy(operationType);
        if (gitOperation.isPresent() && GitOperations.UPDATE_ALL_BRANCHES.equals(gitOperation.get())){
            updateAllLocalBranches(projectFullPath);
        }else if (gitOperation.isPresent() && GitOperations.STASH_LOCAL_CHANGES.equals(gitOperation.get())){
            if (args.length!=3){
                throw new RuntimeException("Give a stash name");
            }
            final String stashName=args[2];
            stashLocalChanges(projectFullPath,stashName);
        }else if (gitOperation.isPresent() && GitOperations.CURRENT_BRANCH_NAME.equals(gitOperation.get())){
            currentBranchName(projectFullPath);
        }else{
            throw new RuntimeException("Selected Git Operation could not found "+operationType);
        }
    }

    private static void checkIsGitRepository(String[] args) {
        if (args==null || args.length==0)
            throw new RuntimeException("Given at least Git Operation Types "+Stream.of(GitOperations.values()).map(GitOperations::toString).collect(Collectors.joining(" , ")));
        String projectDirectory="";
        if (args.length>=1){
            Path currentRelativePath = Paths.get("");
            projectDirectory=currentRelativePath.toAbsolutePath().toString();
        }
        if (args.length>=2){
            projectDirectory=args[0];
        }
        Path projectPath = Paths.get(projectDirectory + GIT_BASE_FOLDER_NAME);
        if (Files.notExists(projectPath)){
            throw new RuntimeException("Give a valid git repository project");
        }
    }

    private static void currentBranchName(String projectFullPath) {
        GitModel gitModel=new GitModel(projectFullPath);
        final ProcessBuilder processBuilder =new ProcessBuilder();
        CmdCommandExecutor cmdExecutor=new CmdCommandExecutor();
        cmdExecutor.addCommand(new GitCurrentBranchCommand(gitModel,processBuilder));
        cmdExecutor.executeAllCommands();
        final CmdCommandHistory executedCommandResultsHistory = cmdExecutor.getExecutedCommandResults();
        LOGGER.log(Level.INFO,executedCommandResultsHistory.toString());
    }

    private static void updateAllLocalBranches(String projectFullPath) throws CloneNotSupportedException {
        final ProcessBuilder processBuilder =new ProcessBuilder();
        GitModel gitModel=new GitModel(projectFullPath);
        
        CmdCommandExecutor cmdExecutor=new CmdCommandExecutor();
        cmdExecutor.addCommand(new GitCurrentBranchCommand(gitModel,processBuilder));
        cmdExecutor.executeAllCommands();
        CmdCommandHistory<CmdCommandResult> executedCommandResults = cmdExecutor.getExecutedCommandResults();
        CmdCommandResult currentBranchNameCommandResult = executedCommandResults.pop();
        gitModel.setCurrentBranchName(currentBranchNameCommandResult.getOutput());
        
        cmdExecutor=new CmdCommandExecutor();
        cmdExecutor.addCommand(new ChangeDirectoryCommand(projectFullPath,processBuilder));
        cmdExecutor.addCommand(new GitPullAllBranchesCommand(gitModel,processBuilder));
        cmdExecutor.executeAllCommands();
        final CmdCommandHistory executedHistory = cmdExecutor.getExecutedHistory();
        LOGGER.log(Level.INFO,executedHistory.toString());
    }

    private static void stashLocalChanges(String projectFullPath,String stashName){
        GitModel gitModel=new GitModel(projectFullPath);
        final ProcessBuilder processBuilder =new ProcessBuilder();
        CmdCommandExecutor cmdExecutor=new CmdCommandExecutor();
        cmdExecutor.addCommand(new ChangeDirectoryCommand(projectFullPath,processBuilder));
        cmdExecutor.addCommand(new GitStashCommand(gitModel,processBuilder,stashName));
        cmdExecutor.executeAllCommands();
        final CmdCommandHistory executedCommandResultsHistory = cmdExecutor.getExecutedCommandResults();
        LOGGER.log(Level.INFO,executedCommandResultsHistory.toString());
    }
}
