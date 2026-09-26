package at.alex.timechanger.command.impl;

import at.alex.timechanger.CommonClass;
import at.alex.timechanger.command.Command;
import at.alex.timechanger.config.data.WeatherState;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.network.chat.Component;

public class WeatherClientCommand extends Command {
    public <T> void register(CommandDispatcher<T> dispatcher) {
        LiteralArgumentBuilder<T> command = LiteralArgumentBuilder.literal("cweather");

        command.then(LiteralArgumentBuilder.<T>literal("off")
                .executes(context -> {
                    CommonClass.CONFIG.weatherEnabled = false;
                    CommonClass.CONFIG.save();
                    sendMessageToPlayer(Component.translatable("commands.timeweatherchanger.set.clear"));
                    return 1;
                })
        );
        command.then(LiteralArgumentBuilder.<T>literal("clear")
                .executes(context -> {
                    CommonClass.CONFIG.weatherEnabled = true;
                    CommonClass.CONFIG.weather = WeatherState.CLEAR;
                    CommonClass.CONFIG.save();
                    sendMessageToPlayer(Component.translatable("commands.weather.set.clear"));
                    return 1;
                })
        );
        command.then(LiteralArgumentBuilder.<T>literal("rain")
                .executes(context -> {
                    CommonClass.CONFIG.weatherEnabled = true;
                    CommonClass.CONFIG.weather = WeatherState.RAIN;
                    CommonClass.CONFIG.save();
                    sendMessageToPlayer(Component.translatable("commands.weather.set.rain"));
                    return 1;
                })
        );
        command.then(LiteralArgumentBuilder.<T>literal("thunder")
                .executes(context -> {
                    CommonClass.CONFIG.weatherEnabled = true;
                    CommonClass.CONFIG.weather = WeatherState.THUNDER;
                    CommonClass.CONFIG.save();
                    sendMessageToPlayer(Component.translatable("commands.weather.set.thunder"));
                    return 1;
                })
        );
        dispatcher.register(command);

    }
}
