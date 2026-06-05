package at.alex.timechanger.config.gui;

import at.alex.timechanger.CommonClass;
import at.alex.timechanger.Constants;
import at.alex.timechanger.config.data.WeatherState;
import at.alex.timechanger.utils.TimeNameUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.Arrays;

public class ConfigScreen extends Screen {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/background.png");
    public static final int FONT_COLOR = Color.fromHex("#4c4c4c").getColor();
    private int sizeX = 248;
    private int sizeY = 156;
    private int posTop;
    private int posLeft;
    private Screen previousScreen;

    public ConfigScreen(Component title) {
        super(title);
    }

    public ConfigScreen(Screen previousScreen) {
        super(Component.empty());
        this.previousScreen = previousScreen;
    }

    @Override
    protected void init() {
        this.posLeft = (width - sizeX) / 2;
        this.posTop = (height - sizeY) / 2;

        int y = this.posTop + 10;

        OptionInstance<Integer> timeSlider = new OptionInstance<>(
                "timechanger.options.time",
                OptionInstance.noTooltip(),
                ((component, integer) -> Component.literal(TimeNameUtil.getNameOfTime(integer) + " (" + integer + ")")),
                new OptionInstance.IntRange(0, 24000),
                CommonClass.CONFIG.time,
                (integer) -> {
                    CommonClass.CONFIG.time = integer;
                }
        );

        addTitle(y, Component.literal("Set Time & Weather").withoutShadow().withColor(FONT_COLOR));
        y += 8 + 10;

        this.addRenderableWidget(
                Checkbox.builder(Component.literal("Set Time").withoutShadow().withColor(FONT_COLOR), font)
                        .selected(CommonClass.CONFIG.timeEnabled)
                        .onValueChange(((checkbox, b) -> {
                                    CommonClass.CONFIG.timeEnabled = b;
                                })
                        )
                        .pos(posLeft + 20, y)
                        .build()
        );
        y += 20 + 2;
        AbstractWidget timeSliderWidget = timeSlider.createButton(null, posLeft + 40, y, sizeX - 60);
        this.addRenderableWidget(timeSliderWidget);
        y += 20 + 10;
        this.addRenderableWidget(
                Checkbox.builder(Component.literal("Set Weather").withoutShadow().withColor(FONT_COLOR), font)
                        .pos(posLeft + 20, y)
                        .selected(CommonClass.CONFIG.weatherEnabled)
                        .onValueChange(((checkbox, b) -> {
                                    CommonClass.CONFIG.weatherEnabled = b;
                                })
                        ).build()
        );
        y += 20 + 2;
        this.addRenderableWidget(
                CycleButton.builder(component -> Component.literal(component), CommonClass.CONFIG.weather.toString())
                        .displayOnlyValue()
                        .withValues(Arrays.stream(WeatherState.values()).map(String::valueOf).toArray(String[]::new))
                        .create(posLeft + 40, y, sizeX - 60, 20, Component.empty(), ((cycleButton, o) -> CommonClass.CONFIG.weather = WeatherState.valueOf(o.toString())))
        );
    }

    private void addTitle(int y, Component title) {
        this.addRenderableWidget(new StringWidget(
                posLeft + (sizeX - font.width(title.getVisualOrderText())) / 2,
                y,
                font.width(title.getVisualOrderText()),
                8,
                title,
                font));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float delta) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, posLeft, posTop, 0, 0, sizeX, sizeY, 248, 256);
        super.extractRenderState(guiGraphics, mouseX, mouseY, delta);
    }


    @Override
    public void extractPanorama(GuiGraphicsExtractor guiGraphics, float partialTick) {
        if (Minecraft.getInstance().level == null) {
            super.extractPanorama(guiGraphics, partialTick);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return this.previousScreen != null;
    }

    @Override
    public void onClose() {
        CommonClass.CONFIG.save();
        this.minecraft.setScreen(this.previousScreen);
    }
}
