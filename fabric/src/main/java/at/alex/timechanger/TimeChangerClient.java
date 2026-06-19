package at.alex.timechanger;

import at.alex.timechanger.config.gui.ConfigScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class TimeChangerClient implements ClientModInitializer {
    private static KeyMapping keyBinding;

    @Override
    public void onInitializeClient() {
        keyBinding = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.timechanger.openconfig",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                new KeyMapping.Category(Identifier.fromNamespaceAndPath(Constants.MOD_ID,"name"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.consumeClick()) {
                Minecraft.getInstance().setScreenAndShow(new ConfigScreen(Component.empty()));
            }
        });
    }
}
