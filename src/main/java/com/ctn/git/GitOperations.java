package com.ctn.git;

import java.util.Optional;

public enum GitOperations {
    UPDATE_ALL_BRANCHES("UAB"),
    STASH_LOCAL_CHANGES("SLC"),
    CURRENT_BRANCH_NAME("CBN");
    String jvmArg;
    
    GitOperations(String jvmArg){
        this.jvmArg=jvmArg;
    }
    public String jvmArg(){
        return this.jvmArg;
    }
    
    public static Optional<GitOperations> valueBy(String jvmArg){
        GitOperations[] gitOperations = GitOperations.values();
        for (GitOperations gitOperation:gitOperations){
            if (gitOperation.jvmArg().equals(jvmArg)){
                return Optional.ofNullable(gitOperation);
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "Name : "+name()+" JvmArg : "+jvmArg;
    }
}
