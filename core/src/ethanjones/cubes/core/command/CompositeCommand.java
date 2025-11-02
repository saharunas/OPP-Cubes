package ethanjones.cubes.core.command;

import java.util.ArrayList;
import java.util.List;

public class CompositeCommand implements Command {
    private final List<Command> subCommands = new ArrayList<>();

    public void add(Command cmd) {
        subCommands.add(cmd);
    }

    @Override
    public void execute() {
        for (Command c : subCommands) c.execute();
    }

    @Override
    public void undo() {
        // Undo in reverse order
        for (int i = subCommands.size() - 1; i >= 0; i--) {
            subCommands.get(i).undo();
        }
    }
}