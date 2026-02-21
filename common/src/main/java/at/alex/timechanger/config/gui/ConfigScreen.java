package at.alex.timechanger.config.gui;

import at.alex.timechanger.CommonClass;
import at.alex.timechanger.Constants;
import at.alex.timechanger.config.data.WeatherState;
import at.alex.timechanger.utils.TimeNameUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ConfigScreen extends Screen {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/background.png");
    private int sizeX = 248;
    private int sizeY = 156;
    private int posTop;
    private int posLeft;

    public ConfigScreen(Component title) {
        super(title);
    }

    public ConfigScreen() {
        super(Component.empty());
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

        addTitle(y, Component.literal("Set Time & Weather"));
        y += 8 + 10;
        this.addRenderableWidget(
                Checkbox.builder(Component.literal("Set Time"), font)
                        .pos(posLeft + 20, y)
                        .selected(CommonClass.CONFIG.timeEnabled)
                        .onValueChange(((checkbox, b) -> {
                                    CommonClass.CONFIG.timeEnabled = b;
                                })
                        ).build()
        );
        y += 20 + 2;
        AbstractWidget timeSliderWidget = timeSlider.createButton(null, posLeft + 40, y, sizeX - 60);
        this.addRenderableWidget(timeSliderWidget);
        y += 20 + 10;
        this.addRenderableWidget(
                Checkbox.builder(Component.literal("Set Weather"), font)
                        .pos(posLeft + 20, y)
                        .selected(CommonClass.CONFIG.weatherEnabled)
                        .onValueChange(((checkbox, b) -> {
                                    CommonClass.CONFIG.weatherEnabled = b;
                                })
                        ).build()
        );
        y += 20 + 2;
        this.addRenderableWidget(
                CycleButton.builder(component -> Component.literal((component.toString())))
                        .withInitialValue(CommonClass.CONFIG.weather)
                        .displayOnlyValue()
                        .withValues((Object[]) WeatherState.values())
                        .create(posLeft + 40, y, sizeX - 60, 20, Component.empty(), ((cycleButton, o) -> CommonClass.CONFIG.weather = WeatherState.valueOf(o.toString())))
        );
    }

    private void addTitle(int y, Component title) {
        this.addRenderableWidget(new StringWidget(posLeft + (sizeX - font.width(title.getVisualOrderText())) / 2, y, font.width(title.getVisualOrderText()), 8, title, font));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, posLeft, posTop, 0, 0, sizeX, sizeY, 248, 256);
        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (Minecraft.getInstance().level == null) {
            renderPanorama(guiGraphics, partialTick);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        CommonClass.CONFIG.save();
        super.onClose();
    }
}
