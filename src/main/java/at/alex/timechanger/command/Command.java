package at.alex.timechanger.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public abstract class Command {
    public abstract <T> void register(CommandDispatcher<T> dispatcher);

    protected static void sendMessageToPlayer(Component message) {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player==null) return;

        //? if >=26.1 {
        mc.player.sendSystemMessage(message);
        //?} else {
        /*mc.player.displayClientMessage(message, false);
        *///?}
    }
}
