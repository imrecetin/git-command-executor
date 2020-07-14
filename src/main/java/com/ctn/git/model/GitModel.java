package com.ctn.git.model;

import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GitModel implements Cloneable{
    
    private static final Logger LOGGER=Logger.getLogger(GitModel.class.getName());
    private static final String GIT_BASE_FOLDER_NAME="\\.git";
    final static String GIT_HEADS_DIRECTORY="\\.git\\refs\\heads";
    final static String GIT_HEADS_FOLDER_NAME="heads\\";
    final static String GIT_ORIGIN_FOLDER_NAME="origin";
    final static String GIT_DEFAULT_REMOTE_NAME="origin ";
    final static String GIT_BRANCH_PULL_COMMAND="git pull ";
    final static String GIT_BRANCH_PUSH_COMMAND="git push ";
    final static String GIT_BRANCH_STASH_COMMAND="git stash ";
    final static String GIT_BRANCH_STASH_COMMAND_WITH_MESSAGE="git stash save ";
    final static String GIT_BRANCH_CHECKOUT_COMMAND="git checkout ";
    final static String GIT_CURRENT_BRANCH_NAME="git rev-parse --abbrev-ref HEAD";
    final static String CMD_COMMAND_APPENDER=" && ";
    static String CHANGE_PROJECT_DIRECTORY_COMMAND;

    private String projectDirectory;
    private String currentBranchName;
    private List<String> allLocalBranches;
    
    final static FilenameFilter fileNameFilter = (dir, name) -> dir.isFile() && !name.contains(GIT_ORIGIN_FOLDER_NAME);
    final static Function<Path,String> branchNameMapperByHeadsFolderPath=(path)->{
        String fileName=path.toString();
        int indexOfHeadsKeyword=fileName.indexOf(GIT_HEADS_FOLDER_NAME);
        fileName=fileName.substring(indexOfHeadsKeyword+GIT_HEADS_FOLDER_NAME.length());
        return fileName.replace("\\","/");
    };
    
    private GitModel(String projectDirectory,String currentBranchName,List<String> allLocalBranches ){
        this.projectDirectory=projectDirectory;
        this.currentBranchName=currentBranchName;
        this.allLocalBranches=allLocalBranches;
        this.CHANGE_PROJECT_DIRECTORY_COMMAND="cd "+projectDirectory;
        checkGitRepository();
    }
    public GitModel() {}
    
    public GitModel(String projectDirectory,String currentBranchName) {
        this.CHANGE_PROJECT_DIRECTORY_COMMAND="cd "+projectDirectory;
        this.projectDirectory = projectDirectory;
        this.currentBranchName = currentBranchName;
        checkGitRepository();
        populateAllBranches();
    }

    public GitModel(String projectDirectory) {
        this.CHANGE_PROJECT_DIRECTORY_COMMAND="cd "+projectDirectory;
        this.projectDirectory = projectDirectory;
        checkGitRepository();
        populateAllBranches();
    }

    private void checkGitRepository() {
        Path projectPath = Paths.get(projectDirectory + GIT_BASE_FOLDER_NAME);
        if (Files.notExists(projectPath)){
            throw new RuntimeException("Give a valid git repository project");
        }
    }

    private void populateAllBranches() {
        final String gitHeadsFullPathByProjectPath=projectDirectory+GIT_HEADS_DIRECTORY;
        try (Stream<Path> paths = Files.walk(Paths.get(gitHeadsFullPathByProjectPath))) {
            allLocalBranches = paths.filter(path -> fileNameFilter.accept(path.toFile(), path.toString())).map(branchNameMapperByHeadsFolderPath).collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"Path walks got exception");
            LOGGER.log(Level.SEVERE,e.getMessage());
        }
    }
    
    public String checkoutCommandText(String branchName){
        return changeDirectoryCommandText() + GIT_BRANCH_CHECKOUT_COMMAND+branchName;
    }

    public String checkoutCommandText(){
        return checkoutCommandText(getCurrentBranchName());
    }

    public String pullCommandText(){
        return changeDirectoryCommandText() + GIT_BRANCH_PULL_COMMAND;
    }

    public String pushCommandText(){
        return changeDirectoryCommandText()  + GIT_BRANCH_PUSH_COMMAND;
    }

    public String stashCommandText(){
        return changeDirectoryCommandText() + GIT_BRANCH_STASH_COMMAND;
    }

    public String stashCommandText(String stashName){
        return changeDirectoryCommandText() + GIT_BRANCH_STASH_COMMAND_WITH_MESSAGE + stashName;
    }

    public String gitCurrentBranchCommandText(){
        return changeDirectoryCommandText() + GIT_CURRENT_BRANCH_NAME;
    }

    private String changeDirectoryCommandText() {
        return CHANGE_PROJECT_DIRECTORY_COMMAND + CMD_COMMAND_APPENDER;
    }

    public String getProjectDirectory() {
        return projectDirectory;
    }

    public void setProjectDirectory(String projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    public String getCurrentBranchName() {
        return currentBranchName;
    }

    public void setCurrentBranchName(String currentBranchName) {
        this.currentBranchName = currentBranchName;
    }

    public List<String> getAllLocalBranches() {
        return allLocalBranches;
    }

    public void setAllLocalBranches(List<String> allLocalBranches) {
        this.allLocalBranches = allLocalBranches;
    }

    @Override
    public GitModel clone() throws CloneNotSupportedException {
        return new GitModel(this.projectDirectory,this.currentBranchName,this.allLocalBranches);
    }
}
