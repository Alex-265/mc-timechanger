package at.alex.timechanger.command.impl;

import at.alex.timechanger.CommonClass;
import at.alex.timechanger.command.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class TimeClientCommand extends Command {
    @Override
    public <T> void register(CommandDispatcher<T> dispatcher) {
        LiteralArgumentBuilder<T> command = LiteralArgumentBuilder.literal("ctime");

        command.then(LiteralArgumentBuilder.<T>literal("query")
                .executes(_ -> {
                    if (CommonClass.CONFIG.timeEnabled)
                        sendMessageToPlayer(Component.translatable("commands.timeweatherchanger.time.query", CommonClass.CONFIG.time));
                    else
                        sendMessageToPlayer(Component.translatable("commands.timeweatherchanger.time.query.off"));
                    return 1;
                })
        );
        command.then(LiteralArgumentBuilder.<T>literal("add")
                .then((ArgumentBuilder<T, ?>) Commands.argument("time", IntegerArgumentType.integer(0, 24000)).executes(context -> {
                    int time = IntegerArgumentType.getInteger(context, "time") + CommonClass.CONFIG.time;
                    CommonClass.CONFIG.time = time % 240001;

                    sendMessageToPlayer(Component.translatable("commands.timeweatherchanger.time.set", CommonClass.CONFIG.time));
                    CommonClass.CONFIG.save();
                    return 1;
                }))
        );
        command.then(LiteralArgumentBuilder.<T>literal("set")
                .then((ArgumentBuilder<T, ?>) Commands.literal("day").executes(context -> {
                    setTimeAndEnable(1000);
                    sendMessageToPlayer(Component.translatable("commands.timeweatherchanger.time.set", 1000));
                    return 1;
                })).then((ArgumentBuilder<T, ?>) Commands.literal("midnight").executes(context -> {
                    setTimeAndEnable(18000);
                    sendMessageToPlayer(Component.translatable("commands.timeweatherchanger.time.set", 18000));
                    return 1;
                })).then((ArgumentBuilder<T, ?>) Commands.literal("night").executes(context -> {
                    setTimeAndEnable(13000);
                    sendMessageToPlayer(Component.translatable("commands.timeweatherchanger.time.set", 1300));
                    return 1;
                })).then((ArgumentBuilder<T, ?>) Commands.literal("noon").executes(context -> {
                    setTimeAndEnable(6000);
                    sendMessageToPlayer(Component.translatable("commands.timeweatherchanger.time.set", 6000));
                    return 1;
                })).then((ArgumentBuilder<T, ?>) Commands.argument("time", IntegerArgumentType.integer(0, 24000)).executes(context -> {
                    setTimeAndEnable(IntegerArgumentType.getInteger(context, "time"));
                    sendMessageToPlayer(Component.translatable("commands.timeweatherchanger.time.set", 6000));
                    return 1;
                }))

        );
        command.then(LiteralArgumentBuilder.<T>literal("off").executes(context -> {
            CommonClass.CONFIG.timeEnabled = false;
            CommonClass.CONFIG.save();
            sendMessageToPlayer(Component.translatable("commands.timeweatherchanger.time.off"));
            return 1;
        }));
        dispatcher.register(command);

    }

    private void setTimeAndEnable(int time) {
        time = time % 24001;
        CommonClass.CONFIG.timeEnabled = true;
        CommonClass.CONFIG.time = time;
        CommonClass.CONFIG.save();
    }
}
