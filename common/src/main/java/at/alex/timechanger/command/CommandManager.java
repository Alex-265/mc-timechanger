package at.alex.timechanger.command;

import at.alex.timechanger.command.impl.TimeClientCommand;
import at.alex.timechanger.command.impl.WeatherClientCommand;

import java.util.*;

public class CommandManager {
    private Set<Command> commands = new HashSet<>();

    public CommandManager() {
        this.add(new TimeClientCommand());
        this.add(new WeatherClientCommand());
    }

    public void add(Command command) {
        this.commands.add(command);
    }

    public List<Command> getCommands() {
        return commands.stream().toList();
    }
}
