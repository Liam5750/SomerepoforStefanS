package ravenNPlus.client.module.modules.combat;

import ravenNPlus.client.utils.InvUtils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

public class AutoWeapon extends Module {

    public static TickSetting onlyWhenHoldingDown;
    public static TickSetting goBackToPrevSlot;
    private boolean onWeapon;
    private int prevSlot;

    public AutoWeapon() {
        super("AutoWeapon", ModuleCategory.combat, "Hotkeys to your weapon when u hit someone");
        this.addSetting(onlyWhenHoldingDown = new TickSetting("Only when holding LMB", true));
        this.addSetting(goBackToPrevSlot = new TickSetting("Revert to old slot", true));
    }

    @SubscribeEvent
    public void datsDaSoundOfDaPolis(TickEvent.RenderTickEvent ev) {
        if (!this.inGame() || mc.currentScreen != null) return;

        if (mc.objectMouseOver == null || mc.objectMouseOver.entityHit == null || (onlyWhenHoldingDown.isToggled() && !Mouse.isButtonDown(0))) {
            if (onWeapon) {
                onWeapon = false;
                if (goBackToPrevSlot.isToggled()) {
                    this.player().inventory.currentItem = prevSlot;
                }
            }
        } else {
            Entity target = mc.objectMouseOver.entityHit;
            if (onlyWhenHoldingDown.isToggled()) {
                if (!Mouse.isButtonDown(0)) return;
            }
            if (!onWeapon) {
                prevSlot = this.player().inventory.currentItem;
                onWeapon = true;

                int maxDamageSlot = InvUtils.getMaxDamageSlot();

                if (maxDamageSlot > 0 && InvUtils.getSlotDamage(maxDamageSlot) > InvUtils.getSlotDamage(this.player().inventory.currentItem)) {
                    this.player().inventory.currentItem = maxDamageSlot;
                }
            }
        }
    }

}