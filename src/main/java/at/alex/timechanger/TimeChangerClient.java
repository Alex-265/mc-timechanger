package at.alex.timechanger;

//? if fabric {
import at.alex.timechanger.command.Command;
import at.alex.timechanger.config.gui.ConfigScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
//? if >=26.1 {
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
//?} else {
/*import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
*///?}
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
//? if <26.3 {
import org.lwjgl.glfw.GLFW;
//?}

public class TimeChangerClient implements ClientModInitializer {
    private static KeyMapping keyBinding;

    @Override
    public void onInitializeClient() {
        //? if >=26.1 {
        keyBinding = KeyMappingHelper.registerKeyMapping(new KeyMapping(
        //?} else {
        /*keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
        *///?}
                "key.timechanger.openconfig",
                //? if >=26.3 {
                /*InputConstants.Type.KEYBOARD,
                InputConstants.KEY_N,
                *///?} else {
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                //?}
                new KeyMapping.Category(Identifier.fromNamespaceAndPath(Constants.MOD_ID,"name"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.consumeClick()) {
                //? if >=26.2 {
                /*Minecraft.getInstance().setScreenAndShow(new ConfigScreen(Component.empty()));
                *///?} else {
                Minecraft.getInstance().setScreen(new ConfigScreen(Component.empty()));
                //?}
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, buildContext) -> {
            for (Command command : CommonClass.COMMAND_MANAGER.getCommands()) {
                command.register(dispatcher);
            }
        });
    }
}
//?}
