package at.alex.timechanger;

import at.alex.timechanger.config.gui.ConfigScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class TimeChangerClient implements ClientModInitializer {
    private static KeyMapping keyBinding;

    @Override
    public void onInitializeClient() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.timechanger.openconfig",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                new KeyMapping.Category(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID,"name"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.consumeClick()) {
                Minecraft.getInstance().setScreen(new ConfigScreen(Component.empty()));
            }
        });
    }
}
