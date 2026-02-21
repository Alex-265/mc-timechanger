package at.alex.timechanger;

import at.alex.timechanger.config.data.Config;


public class CommonClass {
    public static Config CONFIG = new Config();

    public static void init() {
        CONFIG.load();
    }
}
