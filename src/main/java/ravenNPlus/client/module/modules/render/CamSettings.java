package ravenNPlus.client.module.modules.render;

import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class CamSettings extends Module {

    public static TickSetting block, crosshair, bosshealth, armor, health, food, air, hotbar, expirience, text, healTHomount, chat, playerList, debug, costum;
    public static SliderSetting angle, scaleY, scaleX, scaleZ, red, green, blue;
    boolean aa = true;

    public CamSettings() {
        super("CameraSettings", ModuleCategory.render, "Modifies your camera");
        this.addSetting(block = new TickSetting("Block Overlay", false));
        this.addSetting(crosshair = new TickSetting("Crosshair", false));
        this.addSetting(bosshealth = new TickSetting("Boss Health", false));
        this.addSetting(armor = new TickSetting("Armor", false));
        this.addSetting(health = new TickSetting("Health", false));
        this.addSetting(food = new TickSetting("Food", false));
        this.addSetting(air = new TickSetting("Air", false));
        this.addSetting(hotbar = new TickSetting("HotBar", false));
        this.addSetting(expirience = new TickSetting("Experience", false));
        this.addSetting(text = new TickSetting("Text", false));
        this.addSetting(healTHomount = new TickSetting("Heal Amount", false));
        this.addSetting(chat = new TickSetting("Chat", false));
        this.addSetting(playerList = new TickSetting("PlayerList", false));
        this.addSetting(debug = new TickSetting("Debug", false));
        this.addSetting(costum = new TickSetting("Costum", false));
        this.addSetting(angle = new SliderSetting("Costum Angle", 0, -50, 50, 1));
        this.addSetting(scaleX = new SliderSetting("Costum Scale X", 1, -2, 2, 0.1));
        this.addSetting(scaleY = new SliderSetting("Costum Scale Y", 1, -2, 2, 0.1));
        this.addSetting(scaleZ = new SliderSetting("Costum Scale Z", 1, -2, 2, 0.1));
        this.addSetting(red = new SliderSetting("Costum Color Red", 1, 0, 255, 1));
        this.addSetting(green = new SliderSetting("Costum Color Green", 1, 0, 255, 1));
        this.addSetting(blue = new SliderSetting("Costum Color Blue", 1, 0, 255, 1));
    }

    @SubscribeEvent
    public void p(TickEvent.RenderTickEvent e) {
        if (mc.currentScreen instanceof GuiMainMenu && aa) {
            if (this.isEnabled())
                this.disable();

            angle.resetToDefaults();
            scaleX.resetToDefaults();
            scaleY.resetToDefaults();
            scaleZ.resetToDefaults();
            costum.resetToDefaults();
        }

        aa = false;
    }


    @SubscribeEvent
    public void s(RenderBlockOverlayEvent e) {
        if (!this.inGame()) return;
        e.setCanceled(block.isToggled());
    }

    @SubscribeEvent
    public void r(RenderGameOverlayEvent e) {
        if (!this.inGame()) return;

        if (e.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            e.setCanceled(crosshair.isToggled());
        }

        if (e.type == RenderGameOverlayEvent.ElementType.BOSSHEALTH) {
            e.setCanceled(bosshealth.isToggled());
        }

        if (e.type == RenderGameOverlayEvent.ElementType.ARMOR) {
            e.setCanceled(armor.isToggled());
        }

        if (e.type == RenderGameOverlayEvent.ElementType.HEALTH) {
            e.setCanceled(health.isToggled());
        }

        if (e.type == RenderGameOverlayEvent.ElementType.FOOD) {
            e.setCanceled(food.isToggled());
        }

        if (e.type == RenderGameOverlayEvent.ElementType.AIR) {
            e.setCanceled(air.isToggled());
        }

        if (e.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
            e.setCanceled(hotbar.isToggled());
        }

        if (e.type == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            e.setCanceled(expirience.isToggled());
        }

        if (e.type == RenderGameOverlayEvent.ElementType.TEXT) {
            e.setCanceled(text.isToggled());
        }

        if (e.type == RenderGameOverlayEvent.ElementType.HEALTHMOUNT) {
            e.setCanceled(healTHomount.isToggled());
        }

        if (e.type == RenderGameOverlayEvent.ElementType.CHAT) {
            e.setCanceled(chat.isToggled());
        }

        if (e.type == RenderGameOverlayEvent.ElementType.PLAYER_LIST) {
            e.setCanceled(playerList.isToggled());
        }

        if (e.type == RenderGameOverlayEvent.ElementType.CHAT) {
            e.setCanceled(chat.isToggled());
        }

        if (e.type == RenderGameOverlayEvent.ElementType.DEBUG) {
            e.setCanceled(debug.isToggled());
        }

        if (e.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS && costum.isToggled()) {
            GlStateManager.rotate(angle.getValueToFloat(), 1, 1, 1);
        }

        if (e.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS && costum.isToggled()) {
            GlStateManager.scale(scaleX.getValueToFloat(), scaleY.getValueToFloat(), scaleZ.getValueToFloat());
        }

        if (e.type != RenderGameOverlayEvent.ElementType.ALL && costum.isToggled()) {
            GL11.glClearColor(255, 255, 255, 255);
            GL11.glColor4f(red.getValueToFloat(), green.getValueToFloat(), blue.getValueToFloat(), 255);
        }

    }

}