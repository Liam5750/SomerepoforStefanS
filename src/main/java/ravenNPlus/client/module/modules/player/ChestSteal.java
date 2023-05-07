package ravenNPlus.client.module.modules.player;

import ravenNPlus.client.utils.CoolDown;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.DoubleSliderSetting;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ChestSteal extends Module {

    private final DoubleSliderSetting firstDelay, delay, closeDelay;
    private final TickSetting autoClose, ignoreTrash;
    private boolean inChest;
    private final CoolDown delayTimer = new CoolDown(0), closeTimer = new CoolDown(0);
    private ArrayList<Slot> sortedSlots;

    public ChestSteal() {
        super("ChestSteal", ModuleCategory.player, "Automatically Steals items of Chests");
        this.addSetting(firstDelay = new DoubleSliderSetting("Open delay", 250, 450, 0, 1000, 1));
        this.addSetting(delay = new DoubleSliderSetting("Delay", 150, 250, 0, 1000, 1));
        this.addSetting(ignoreTrash = new TickSetting("Ignore Trash", true));
        this.addSetting(autoClose = new TickSetting("Auto Close", false));
        this.addSetting(closeDelay = new DoubleSliderSetting("Close delay", 150, 250, 0, 1000, 1));
    }

    @SubscribeEvent
    public void openChest(TickEvent.RenderTickEvent e) {
        if (!this.inGame())
            return;

        if (mc.currentScreen instanceof GuiChest) {
            if (!inChest) {
                ContainerChest chest = (ContainerChest) this.player().openContainer;
                delayTimer.setCooldown((long) ThreadLocalRandom.current().nextDouble(firstDelay.getInputMin(), firstDelay.getInputMax() + 0.01));
                delayTimer.start();
                generatePath(chest);
                inChest = true;
            }

            if (inChest && !sortedSlots.isEmpty()) {
                if (delayTimer.hasFinished()) {

                    if (ignoreTrash.isToggled()) {
                        if (sortedSlots.toString().startsWith("Egg")) return;
                        if (sortedSlots.toString().startsWith("Raw")) return;
                        if (sortedSlots.toString().contains("Flint")) return;
                        if (sortedSlots.toString().contains("Wooden")) return;
                        if (sortedSlots.toString().equals("Bow")) return;
                        if (sortedSlots.toString().equals("Snow")) return;
                    }

                    mc.playerController.windowClick(this.player().openContainer.windowId, sortedSlots.get(0).s, 0, 1, this.player());
                    delayTimer.setCooldown((long) ThreadLocalRandom.current().nextDouble(delay.getInputMin(), delay.getInputMax() + 0.01));
                    delayTimer.start();
                    sortedSlots.remove(0);
                }
            }

            if (sortedSlots.isEmpty() && autoClose.isToggled()) {
                if (closeTimer.firstFinish()) {
                    this.player().closeScreen();
                    inChest = false;
                } else {
                    closeTimer.setCooldown((long) ThreadLocalRandom.current().nextDouble(closeDelay.getInputMin(), closeDelay.getInputMax() + 0.01));
                    closeTimer.start();
                }
            }
        } else {
            inChest = false;
        }

    }

    public void generatePath(ContainerChest chest) {
        ArrayList<Slot> slots = new ArrayList<Slot>();
        for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); i++) {
            if (chest.getInventory().get(i) != null) {
                slots.add(new Slot(i));
            }
        }

        Slot[] ss = sort(slots.toArray(new Slot[slots.size()]));
        ArrayList<Slot> newSlots = new ArrayList<Slot>();
        Collections.addAll(newSlots, ss);

        this.sortedSlots = newSlots;
    }

    public static Slot[] sort(Slot[] in) {
        if (in == null || in.length == 0) {
            return in;
        }

        Slot[] out = new Slot[in.length];
        Slot current = in[ThreadLocalRandom.current().nextInt(0, in.length)];
        for (int i = 0; i < in.length; i++) {
            if (i == in.length - 1) {
                out[in.length - 1] = Arrays.stream(in).filter(p -> !p.visited).findAny().orElseGet(null);
                break;
            }

            final Slot finalCurrent = current;
            out[i] = finalCurrent;
            finalCurrent.visit();
            current = Arrays.stream(in).filter(p -> !p.visited).min(Comparator.comparingDouble(p -> p.getDistance(finalCurrent))).get();
        }

        return out;
    }

    public static class Slot {

        final int x, y, s;
        boolean visited;

        public Slot(int s) {
            this.x = (s + 1) % 10;
            this.y = s / 9;
            this.s = s;
        }

        public double getDistance(Slot s) {
            return Math.abs(this.x - s.x) + Math.abs(this.y - s.y);
        }

        public void visit() {
            visited = true;
        }
    }

}