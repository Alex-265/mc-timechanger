package at.alex.timechanger.mixin;

import at.alex.timechanger.CommonClass;
import at.alex.timechanger.config.data.WeatherState;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class LevelMixin {
    @Inject(method = "getRainLevel", cancellable = true, at=@At("HEAD"))
    private void getRainLevel(float partialTicks, CallbackInfoReturnable<Float> cir) {
        if(CommonClass.CONFIG.weatherEnabled) {
            cir.setReturnValue(CommonClass.CONFIG.weather == WeatherState.CLEAR ? 0 : 1F);
        }
    }

    @Inject(method = "getThunderLevel", cancellable = true, at=@At("HEAD"))
    private void getThunderLevel(float partialTicks, CallbackInfoReturnable<Float> cir) {
        if(CommonClass.CONFIG.weatherEnabled) {
            cir.setReturnValue(CommonClass.CONFIG.weather == WeatherState.THUNDER ? 1F : 0);
        }
    }
}
