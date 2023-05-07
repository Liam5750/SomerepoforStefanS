package ravenNPlus.client.module.modules.render;

import ravenNPlus.client.module.Module;
import ravenNPlus.client.utils.RenderUtils;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.event.imp.RenderEntityModelEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent.Pre;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderPlayerEvent.Post;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Chams extends Module {

   public static TickSetting renderHandEvent, renderEntityModelEvent, renderFogEvent, trueFalse;

   public Chams() {
      super("Chams", ModuleCategory.render, "See players through walls");
      this.addSetting(renderEntityModelEvent = new TickSetting("Render Entity Model", false));
      this.addSetting(renderHandEvent = new TickSetting("Render Hand", false));
      this.addSetting(renderFogEvent = new TickSetting("Render FOG", false));
      this.addSetting(trueFalse = new TickSetting("Debug False", false));
   }

   @SubscribeEvent
   public void r1(Pre e) {
      if (e.entity != this.player())
         RenderUtils.drawEasy(true);
   }

   @SubscribeEvent
   public void r2(Post e) {
      if (e.entity != this.player())
         RenderUtils.drawEasy(false);
   }

   //--------------------------------------------------------------------------

   @SubscribeEvent
   public void r3(RenderHandEvent e) {
      if (renderHandEvent.isToggled())
         RenderUtils.drawEasy(trueFalse.isToggled());
   }

   @SubscribeEvent
   public void r4(RenderEntityModelEvent e) {
      if (renderEntityModelEvent.isToggled())
         RenderUtils.drawEasy(trueFalse.isToggled());
   }

   @SubscribeEvent
   public void r5(EntityViewRenderEvent.RenderFogEvent e) {
      if (renderFogEvent.isToggled())
         RenderUtils.drawEasy(trueFalse.isToggled());
   }

}