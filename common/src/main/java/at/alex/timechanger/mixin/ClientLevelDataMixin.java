package at.alex.timechanger.mixin;

import at.alex.timechanger.CommonClass;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.ClientLevelData.class)
public class ClientLevelDataMixin {
    @Inject(method = "getGameTime", at=@At(value = "HEAD"), cancellable = true)
    public void getGameTime(CallbackInfoReturnable<Long> cir) {
        if(CommonClass.CONFIG.timeEnabled) {
            cir.setReturnValue((long) CommonClass.CONFIG.time);
        }
    }
}
