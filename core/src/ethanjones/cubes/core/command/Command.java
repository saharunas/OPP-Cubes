package ethanjones.cubes.core.command;

public interface Command {
    void execute();
    void undo();
}