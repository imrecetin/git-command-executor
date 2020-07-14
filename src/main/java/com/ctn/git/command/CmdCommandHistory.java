package com.ctn.git.command;
import com.ctn.git.command.type.Command;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

public class CmdCommandHistory<T extends Command> {
    private Queue<T> commands=new LinkedList<>();
    public void push(T command){commands.add(command);}
    public T pop(){
        return commands.remove();
    }
    public boolean isEmpty(){
        return commands.isEmpty();
    }

    @Override
    public String toString() {
        return "CmdCommandHistory{ "+
                commands.stream().map(T::toString).collect(Collectors.joining(" \n "))
                + " } ";
    }
}
