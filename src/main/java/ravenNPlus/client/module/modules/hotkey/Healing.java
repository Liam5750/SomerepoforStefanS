package ravenNPlus.client.module.modules.hotkey;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import ravenNPlus.client.module.setting.impl.DescriptionSetting;
import net.minecraft.item.*;

public class Healing extends Module {

    private final TickSetting preferSlot;
    private final SliderSetting hotbarSlotPreference, itemMode;
    private final DescriptionSetting modeDesc;

    public Healing() {
        super("Healing", ModuleCategory.hotkey, "");
        this.addSetting(preferSlot = new TickSetting("Prefer a slot", false));
        this.addSetting(hotbarSlotPreference = new SliderSetting("Prefer wich slot", 8, 1, 9, 1));
        this.addSetting(itemMode = new SliderSetting("Value:", 1, 1, 4, 1));
        this.addSetting(modeDesc = new DescriptionSetting("Mode: SOUP"));
    }

    public void guiUpdate() {
        modeDesc.setDesc(Utils.md + HealingItems.values()[(int) itemMode.getValue() - 1]);
    }

    @Override
    public void onEnable() {
        if (!this.inGame())
            return;

        if (preferSlot.isToggled()) {
            int preferedSlot = (int) hotbarSlotPreference.getValue() - 1;

            if (HealingItems.values()[(int) itemMode.getValue() - 1] == HealingItems.SOUP && isSoup(preferedSlot)) {
                this.player().inventory.currentItem = preferedSlot;
                this.disable();
                return;
            } else if (HealingItems.values()[(int) itemMode.getValue() - 1] == HealingItems.GAPPLE && isGapple(preferedSlot)) {
                this.player().inventory.currentItem = preferedSlot;
                this.disable();
                return;
            } else if (HealingItems.values()[(int) itemMode.getValue() - 1] == HealingItems.FOOD && isFood(preferedSlot)) {
                this.player().inventory.currentItem = preferedSlot;
                this.disable();
                return;
            } else if (HealingItems.values()[(int) itemMode.getValue() - 1] == HealingItems.ALL && (isGapple(preferedSlot) || isFood(preferedSlot) || isSoup(preferedSlot))) {
                this.player().inventory.currentItem = preferedSlot;
                this.disable();
                return;
            }

        }

        for (int slot = 0; slot <= 8; slot++) {
            if (HealingItems.values()[(int) itemMode.getValue() - 1] == HealingItems.SOUP && isSoup(slot)) {
                this.player().inventory.currentItem = slot;
                this.disable();
                return;
            } else if (HealingItems.values()[(int) itemMode.getValue() - 1] == HealingItems.GAPPLE && isGapple(slot)) {
                this.player().inventory.currentItem = slot;
                this.disable();
                return;
            } else if (HealingItems.values()[(int) itemMode.getValue() - 1] == HealingItems.FOOD && isFood(slot)) {
                this.player().inventory.currentItem = slot;
                this.disable();
                return;
            } else if (HealingItems.values()[(int) itemMode.getValue() - 1] == HealingItems.ALL && (isGapple(slot) || isFood(slot) || isSoup(slot))) {
                this.player().inventory.currentItem = slot;
                this.disable();
                return;
            }
        }
        this.disable();
    }

    public static boolean checkSlot(int slot) {
        ItemStack itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot);

        return itemInSlot != null && itemInSlot.getDisplayName().equalsIgnoreCase("ladder");
    }

    public enum HealingItems {
        SOUP, GAPPLE,
        FOOD, ALL
    }

    public boolean isSoup(int slot) {
        ItemStack itemInSlot = this.player().inventory.getStackInSlot(slot);
        if (itemInSlot == null)
            return false;
        return itemInSlot.getItem() instanceof ItemSoup;
    }

    public boolean isGapple(int slot) {
        ItemStack itemInSlot = this.player().inventory.getStackInSlot(slot);
        if (itemInSlot == null)
            return false;

        return itemInSlot.getItem() instanceof ItemAppleGold;
    }

    public boolean isHead(int slot) {
        ItemStack itemInSlot = this.player().inventory.getStackInSlot(slot);
        if (itemInSlot == null)
            return false;

        return itemInSlot.getItem() instanceof Item;
    }

    public boolean isFood(int slot) {
        ItemStack itemInSlot = this.player().inventory.getStackInSlot(slot);
        if (itemInSlot == null)
            return false;

        return itemInSlot.getItem() instanceof ItemFood;
    }

}