package at.alex.timechanger.config.data;

import at.alex.timechanger.Constants;
import at.alex.timechanger.platform.Services;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {
    public boolean timeEnabled;
    public boolean weatherEnabled;
    public int time = 0;
    public WeatherState weather;

    private Path path = Services.PLATFORM.getConfigFilePath();
    private URL defaultConfigPath = this.getClass().getResource("/defaultConfig.properties");

    public Config() {
        this.load();
    }

    public void load() {
        try {
            if (Files.notExists(path)) {
                createDefault();
            }

            Properties props = new Properties();
            try (InputStream in = Files.newInputStream(path)) {
                props.load(in);
            }

            timeEnabled = Boolean.parseBoolean(props.getProperty("timeEnabled", "false"));
            weatherEnabled = Boolean.parseBoolean(props.getProperty("weatherEnabled", "false"));
            time = Integer.parseInt(props.getProperty("time", "1000"));
            weather = WeatherState.valueOf(
                    props.getProperty("weather", "CLEAR").toUpperCase()
            );

        } catch (Exception e) {
            Constants.LOG.error("Failed to load config.", e);
        }
    }

    public void save() {
        try {
            Properties props = new Properties();

            props.setProperty("timeEnabled", String.valueOf(timeEnabled));
            props.setProperty("weatherEnabled", String.valueOf(weatherEnabled));
            props.setProperty("time", String.valueOf(time));
            props.setProperty("weather", weather.name());

            try (OutputStream out = Files.newOutputStream(path)) {
                props.store(out, "Time & Weather Changer Configuration");
            }

        } catch (IOException e) {
            Constants.LOG.error("Failed to save config.", e);
        }
    }

    private void createDefault() throws IOException {
        Files.createDirectories(path.getParent());

        try (
                InputStream in = defaultConfigPath.openStream();
                OutputStream out = Files.newOutputStream(path)
        ) {
            in.transferTo(out);
        }
    }

}
