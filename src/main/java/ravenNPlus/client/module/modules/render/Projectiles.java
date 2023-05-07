package ravenNPlus.client.module.modules.render;

import ravenNPlus.client.module.Module;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Projectiles extends Module {

    public Projectiles() {
        super("Projectiles", ModuleCategory.render, "Not working (Fix soon)");
    }

    @SubscribeEvent
    public void a(TickEvent.RenderTickEvent ev) {
        if(!this.inGame()) return;
    }

}