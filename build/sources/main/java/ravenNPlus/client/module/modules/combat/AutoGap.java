package ravenNPlus.client.module.modules.combat;

import ravenNPlus.client.utils.Timer;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.utils.RenderUtils;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraft.util.BlockPos;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;

public class AutoGap extends Module {

    public static SliderSetting startHealth, packetAmount, time;
    public static TickSetting showAmount, legitMode;
    protected boolean sendingPacket = false, switchedItem = false;
    static boolean thisMustBeOnToRunShowAmount = false;
    protected int lastSlot = 0;

    public AutoGap() {
        super("AutoGap", ModuleCategory.combat, "Automatically eats Gapples when your on %s Hearts");
        this.addSetting(startHealth = new SliderSetting("Health", 8D, 1D, 10D, 1D));
        this.addSetting(time = new SliderSetting("Delay", 10, 10, 100D, 1D));
        this.addSetting(packetAmount = new SliderSetting("Packets", 1D, 1D, 50D, 1D));
        this.addSetting(legitMode = new TickSetting("Legit", true));
        this.addSetting(showAmount = new TickSetting("Show Amount", false));
    }

    @SubscribeEvent
    public void s(TickEvent.PlayerTickEvent e) throws LWJGLException {
        if (!this.inGame()) return;

        int gAppleSlot = findGoldenApple();
        if (gAppleSlot != -1) {
            if (this.player().getHealth() < startHealth.getValue() * 2D) {

                if (this.player().inventory.currentItem != gAppleSlot)
                    this.lastSlot = this.player().inventory.currentItem;

                this.player().onGround = true;
                this.player().inventory.currentItem = gAppleSlot;

                if (!legitMode.isToggled()) {
                    this.player().sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos
                            (this.player()), 255, this.player().getHeldItem(), 0, 0, 0));
                } else {
                    while (Timer.hasTimeElapsed((long) time.getValue() * 5, true)) {
                        Mouse.isButtonDown(1);
                    }
                }

                if (this.isEating())
                    if (this.sendingPacket && !legitMode.isToggled()) {
                        for (int i = 0; i < packetAmount.getValue(); i++)
                            this.player().sendQueue.addToSendQueue(new C03PacketPlayer(true));
                        this.sendingPacket = false;
                    } else {
                        this.sendingPacket = true;
                    }

            } else {
                if (this.switchedItem) {
                    this.switchedItem = false;
                }
            }
        }

        thisMustBeOnToRunShowAmount = showAmount.isToggled();
    }

    @SubscribeEvent
    public void r(TickEvent.RenderTickEvent e) {
        if (!this.inGame()) return;

        int gAppleSlot = findGoldenApple();
        if (thisMustBeOnToRunShowAmount && showAmount.isToggled()) {
            if (gAppleSlot != -1) {
                int apples = this.player().inventory.getStackInSlot(gAppleSlot).stackSize;
                Gui.drawRect(RenderUtils.scWidth / 2 + 9, RenderUtils.scHight / 2 - 1,
                        RenderUtils.scWidth / 2 + 10 + RenderUtils.font.getStringWidth(apples + ""),
                        RenderUtils.scHight / 2 + RenderUtils.font.FONT_HEIGHT - 1, Integer.MIN_VALUE);
                RenderUtils.font.drawString(apples + "", RenderUtils.scWidth / 2 + 10, RenderUtils.scHight / 2, -1);
            }
        }
    }

    protected int findGoldenApple() {
        for (int i = 0; i < 9; i++) {
            if (this.player().inventoryContainer.getSlot(36 + i).getHasStack()
                    && this.player().inventoryContainer.getSlot(36 + i).getStack().getItem() instanceof ItemAppleGold)
                return i;

        }

        return -1;
    }

}