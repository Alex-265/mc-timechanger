package at.alex.timechanger;

import at.alex.timechanger.command.CommandManager;
import at.alex.timechanger.config.data.Config;


public class CommonClass {
    public static Config CONFIG = new Config();
    public static CommandManager COMMAND_MANAGER = new CommandManager();
    public static void init() {
    }
}
