package at.alex.timechanger.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public abstract class Command {
    public abstract <T> void register(CommandDispatcher<T> dispatcher);

    protected static void sendMessageToPlayer(Component message) {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player==null) return;

        mc.player.displayClientMessage(message, false);
    }
}
