package ravenNPlus.client.module.modules.hotkey;

import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemFishingRod;

public class Trajectories extends Module {

    private final TickSetting preferSlot;
    private final SliderSetting hotbarSlotPreference;

    public Trajectories() {
        super("Trajectories", ModuleCategory.hotkey, "");
        this.addSetting(preferSlot = new TickSetting("Prefer a slot", false));
        this.addSetting(hotbarSlotPreference = new SliderSetting("Prefer wich slot", 5, 1, 9, 1));
    }

    @Override
    public void onEnable() {
        if (!this.inGame())
            return;

        if (preferSlot.isToggled()) {
            int preferedSlot = (int) hotbarSlotPreference.getValue() - 1;

            if (checkSlot(preferedSlot)) {
                this.player().inventory.currentItem = preferedSlot;
                this.disable();
                return;
            }
        }

        for (int slot = 0; slot <= 8; slot++) {
            if (checkSlot(slot)) {
                if (this.player().inventory.currentItem != slot) {
                    this.player().inventory.currentItem = slot;
                } else {
                    return;
                }
                this.disable();
                return;
            }
        }
        this.disable();
    }

    public static boolean checkSlot(int slot) {
        ItemStack itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot);

        return itemInSlot != null && (itemInSlot.getItem() instanceof ItemSnowball || itemInSlot.getItem() instanceof ItemEgg || itemInSlot.getItem() instanceof ItemFishingRod);
    }

}