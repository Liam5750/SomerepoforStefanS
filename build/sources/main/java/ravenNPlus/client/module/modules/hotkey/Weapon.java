package ravenNPlus.client.module.modules.hotkey;

import ravenNPlus.client.utils.InvUtils;
import ravenNPlus.client.module.Module;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.ai.attributes.AttributeModifier;

public class Weapon extends Module {

    public Weapon() {
        super("Weapon", ModuleCategory.hotkey, "");
    }

    @Override
    public void onEnable() {
        if (!this.inGame())
            return;

        int index = -1;
        double damage = -1;

        for (int slot = 0; slot <= 8; slot++) {
            ItemStack itemInSlot = this.player().inventory.getStackInSlot(slot);
            if (itemInSlot == null)
                continue;
            for (AttributeModifier mooommHelp : itemInSlot.getAttributeModifiers().values()) {
                if (mooommHelp.getAmount() > damage) {
                    damage = mooommHelp.getAmount();
                    index = slot;
                }
            }


        }
        if (index > -1 && damage > -1) {
            if (this.player().inventory.currentItem != index) {
                InvUtils.hotkeyToSlot(index);
            }
        }
        this.disable();
    }

}