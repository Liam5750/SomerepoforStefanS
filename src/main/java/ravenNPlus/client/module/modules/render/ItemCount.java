package ravenNPlus.client.module.modules.render;

import ravenNPlus.client.main.Client;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import ravenNPlus.client.module.setting.impl.DescriptionSetting;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.awt.Color;

public class ItemCount extends Module {

    public static TickSetting gap, water, egg, snow, arrow;
    public static TickSetting shadow;
    public static SliderSetting red, green, blue;
    public static DescriptionSetting desc;
    private int gapCount = 0, waterCount = 0, eggCount = 0;
    private int arrowCount = 0, snowCount = 0, rgb_c = 0;

    public ItemCount() {
        super("ItemCount", ModuleCategory.render, "This is Shitty");
        this.addSetting(red = new SliderSetting("Red", 220.0D, 0.0D, 255.0D, 1.0D));
        this.addSetting(green = new SliderSetting("Green", 255.0D, 0.0D, 255.0D, 1.0D));
        this.addSetting(blue = new SliderSetting("Blue", 255.0D, 0.0D, 255.0D, 1.0D));
        this.addSetting(shadow = new TickSetting("Shadow", x));
        this.addSetting(desc = new DescriptionSetting("Mode :"));
        this.addSetting(gap = new TickSetting("Gap", x));
        this.addSetting(water = new TickSetting("Water", o));
        this.addSetting(egg = new TickSetting("Egg", o));
        this.addSetting(snow = new TickSetting("Snow", o));
        this.addSetting(arrow = new TickSetting("Arrow", o));
    }

    public void guiUpdate() {
        this.rgb_c = (new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue())).getRGB();
    }

    @SubscribeEvent
    public void s(TickEvent.PlayerTickEvent post) {

        for (int i = 9; i < this.player().inventoryContainer.getInventory().size(); i++) {
            if (!this.player().inventoryContainer.getSlot(i).getHasStack() || gapCount != 0 || waterCount != 0 || eggCount != 0 || arrowCount != 0 || snowCount != 0)
                continue;

            ItemStack iS = this.player().inventoryContainer.getInventory().get(i);

            if (gap.isToggled()) {
                if (iS.getItem() == Items.golden_apple)
                    gapCount++;
            }

            if (water.isToggled()) {
                if (iS.getItem() == Items.water_bucket)
                    waterCount++;
            }

            if (egg.isToggled()) {
                if (iS.getItem() == Items.egg)
                    eggCount++;
            }

            if (arrow.isToggled()) {
                if (iS.getItem() == Items.arrow)
                    arrowCount++;
            }

            if (snow.isToggled()) {
                if (iS.getItem() == Items.snowball)
                    snowCount++;
            }
        }
    }

    @SubscribeEvent
    public void r(TickEvent.RenderTickEvent e) {
        if (!this.inGame()) return;
        if (e.phase == TickEvent.Phase.END) {
            if (mc.currentScreen == null) {

                String t = "gaps: " + gapCount;
                String n = "water: " + waterCount;
                String j = "eggs: " + eggCount;
                String k = "snow: " + snowCount;
                String l = "arrows: " + arrowCount;

                int offset = 1;
                int x1 = (int) this.screenWidth() / 2 - mc.fontRendererObj.getStringWidth(t) / 2, y1;
                int x2 = (int) this.screenWidth() / 2 - mc.fontRendererObj.getStringWidth(n) / 2, y2;
                int x3 = (int) this.screenWidth() / 2 - mc.fontRendererObj.getStringWidth(j) / 2, y3;
                int x4 = (int) this.screenWidth() / 2 - mc.fontRendererObj.getStringWidth(k) / 2, y4;
                int x5 = (int) this.screenWidth() / 2 - mc.fontRendererObj.getStringWidth(l) / 2, y5;

                if (Client.debugger) {
                    y1 = (int) this.screenHeight() / 2 + 17 + mc.fontRendererObj.FONT_HEIGHT;
                    y2 = (int) this.screenHeight() / 2 + 17 + mc.fontRendererObj.FONT_HEIGHT;
                    y3 = (int) this.screenHeight() / 2 + 17 + mc.fontRendererObj.FONT_HEIGHT;
                    y4 = (int) this.screenHeight() / 2 + 17 + mc.fontRendererObj.FONT_HEIGHT;
                    y5 = (int) this.screenHeight() / 2 + 17 + mc.fontRendererObj.FONT_HEIGHT;
                } else {
                    y1 = (int) this.screenHeight() / 2 + 15;
                    y2 = (int) this.screenHeight() / 2 + 15;
                    y3 = (int) this.screenHeight() / 2 + 15;
                    y4 = (int) this.screenHeight() / 2 + 15;
                    y5 = (int) this.screenHeight() / 2 + 15;
                }

                if (gap.isToggled() && gapCount > 0 && this.isEnabled()) {
                    mc.fontRendererObj.drawString(t, (float) x1, (float) y1 + offset * 7, rgb_c, shadow.isToggled());
                    offset++;
                }

                if (water.isToggled() && waterCount > 0 && this.isEnabled()) {
                    mc.fontRendererObj.drawString(n, (float) x2, (float) y2 + offset * 7, rgb_c, shadow.isToggled());
                    offset++;
                }

                if (egg.isToggled() && eggCount > 0 && this.isEnabled()) {
                    mc.fontRendererObj.drawString(j, (float) x3, (float) y3 + offset * 7, rgb_c, shadow.isToggled());
                    offset++;
                }

                if (snow.isToggled() && snowCount > 0 && this.isEnabled()) {
                    mc.fontRendererObj.drawString(k, (float) x4, (float) y4 + offset * 7, rgb_c, shadow.isToggled());
                    offset++;
                }

                if (arrow.isToggled() && arrowCount > 0 && this.isEnabled()) {
                    mc.fontRendererObj.drawString(l, (float) x5, (float) y5 + offset * 7, rgb_c, shadow.isToggled());
                    offset++;
                }

            }
        }
    }

}