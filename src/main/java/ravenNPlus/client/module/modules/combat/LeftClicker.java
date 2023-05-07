package ravenNPlus.client.module.modules.combat;

import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.modules.player.RightClicker;
import ravenNPlus.client.module.setting.impl.*;
import ravenNPlus.client.utils.InvUtils;
import ravenNPlus.client.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LeftClicker extends Module {

    public static SliderSetting jitterLeft, hitSelectTick;
    public static TickSetting weaponOnly;
    public static TickSetting breakBlocks;
    public static DoubleSliderSetting leftCPS;
    public static TickSetting inventoryFill, hitSelect;
    public static ModeSetting clickStyle, clickTimings;
    private long lastClick, leftHold;
    private long leftDownTime, leftUpTime, leftk, leftl;
    private double leftm;
    private boolean leftn;
    public static boolean autoClickerEnabled;
    private boolean leftDown;
    private boolean breakHeld;
    private Random rand = null;
    private Method playerMouseInput;
    private boolean hitSelected;
    private EntityLivingBase target;

    public LeftClicker() {
        super("Left Clicker", ModuleCategory.combat, "Automatically Left clicks | best with delay remover");
        this.addSetting(leftCPS = new DoubleSliderSetting("Left CPS", 9, 13, 1, 60, 1));
        this.addSetting(jitterLeft = new SliderSetting("Jitter left", 0.0D, 0.0D, 3.0D, 0.1D));
        this.addSetting(hitSelect = new TickSetting("HitSelect", false));
        this.addSetting(hitSelectTick = new SliderSetting("HitSelect HurtTick", 7, 1, 10, 1));
        this.addSetting(inventoryFill = new TickSetting("Inventory fill", false));
        this.addSetting(weaponOnly = new TickSetting("Weapon only", false));
        this.addSetting(breakBlocks = new TickSetting("Break blocks", false));
        this.addSetting(clickTimings = new ModeSetting("Click event", RightClicker.ClickEvent.Render));
        this.addSetting(clickStyle = new ModeSetting("Click Style", RightClicker.ClickStyle.Raven));

        try {
            this.playerMouseInput = ReflectionHelper.findMethod(
                    GuiScreen.class,
                    null,
                    new String[]{ "func_73864_a", "mouseClicked" },
                    Integer.TYPE,
                    Integer.TYPE,
                    Integer.TYPE
            );
        } catch (Exception ex) { ex.printStackTrace(); }

        if (this.playerMouseInput != null) { this.playerMouseInput.setAccessible(true); }

        autoClickerEnabled = false;
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent e) {
        target = e.entityLiving;
    }

    @Override
    public void onEnable() {
        if (this.playerMouseInput == null) {
            this.disable();
        }

        boolean allowedClick = false;
        this.rand = new Random();
        autoClickerEnabled = true;
    }

    @Override
    public void onDisable() {
        this.leftDownTime = 0L;
        this.leftUpTime = 0L;
        autoClickerEnabled = false;
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent ev) {
        if(ev.phase == TickEvent.Phase.END)
            return;

        if(!Utils.Client.currentScreenMinecraft() &&
                !(Minecraft.getMinecraft().currentScreen instanceof GuiInventory) // to make it work in survival inventory
                && !(Minecraft.getMinecraft().currentScreen instanceof GuiChest) // to make it work in chests
        )
            return;

        if (!Mouse.isButtonDown(0)) {
            hitSelected = false;
        }

        if(clickTimings.getMode() != RightClicker.ClickEvent.Render)
            return;

        //Utils.Player.sendMessageToSelf(" " + (hitSelected) + " " + (mc.thePlayer.hurtTime != 0 && mc.thePlayer.hurtTime > hitSelectTick.getInput()));
        if(hitSelect.isToggled()) {
            if(hitSelected || (mc.thePlayer.hurtTime != 0 && mc.thePlayer.hurtTime > hitSelectTick.getValue())) {
                hitSelected = true;
            } else {
                return;
            }
        }

        if(clickStyle.getMode() == RightClicker.ClickStyle.Raven) {
            ravenClick();
        } else if (clickStyle.getMode() == RightClicker.ClickStyle.SKid) {
            skidClick(ev, null);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent ev) {
        if(!Utils.Client.currentScreenMinecraft() && !(Minecraft.getMinecraft().currentScreen instanceof GuiInventory)
                && !(Minecraft.getMinecraft().currentScreen instanceof GuiChest) // to make it work in chests
        )
            return;

        if(clickTimings.getMode() != RightClicker.ClickEvent.Tick)
            return;

        if(clickStyle.getMode() == RightClicker.ClickStyle.Raven) {
            ravenClick();
        }
        else if (clickStyle.getMode() == RightClicker.ClickStyle.SKid) {
            skidClick(null, ev);
        }
    }

    private void skidClick(TickEvent.RenderTickEvent er, TickEvent.PlayerTickEvent e) {
        if (!this.inGame())
            return;

        double speedLeft1 = 1.0 / io.netty.util.internal.ThreadLocalRandom.current().nextDouble(leftCPS.getInputMin() - 0.2D, leftCPS.getInputMax());
        double leftHoldLength = speedLeft1 / io.netty.util.internal.ThreadLocalRandom.current().nextDouble(leftCPS.getInputMin() - 0.02D, leftCPS.getInputMax());
        //If none of the buttons are allowed to click, what is the point in generating clicktimes anyway?
        //if (!leftActive && !rightActive) {
        // return;
        //}
        Mouse.poll();
        if(mc.currentScreen != null || !this.inFocus()) {
            doInventoryClick();
            return;
        }

        if (Mouse.isButtonDown(0)) {
            if(breakBlock()) return;
            if (weaponOnly.isToggled() && !InvUtils.isPlayerHoldingWeapon()) {
                return;
            }
            if (jitterLeft.getValue() > 0.0D) {
                double a = jitterLeft.getValue() * 0.45D;
                EntityPlayerSP entityPlayer;
                if (this.rand.nextBoolean()) {
                    entityPlayer = mc.thePlayer;
                    entityPlayer.rotationYaw = (float)((double)entityPlayer.rotationYaw + (double)this.rand.nextFloat() * a);
                } else {
                    entityPlayer = mc.thePlayer;
                    entityPlayer.rotationYaw = (float)((double)entityPlayer.rotationYaw - (double)this.rand.nextFloat() * a);
                }

                if (this.rand.nextBoolean()) {
                    entityPlayer = mc.thePlayer;
                    entityPlayer.rotationPitch = (float)((double)entityPlayer.rotationPitch + (double)this.rand.nextFloat() * a * 0.45D);
                } else {
                    entityPlayer = mc.thePlayer;
                    entityPlayer.rotationPitch = (float)((double)entityPlayer.rotationPitch - (double)this.rand.nextFloat() * a * 0.45D);
                }
            }

            double speedLeft = 1.0 / ThreadLocalRandom.current().nextDouble(leftCPS.getInputMin() - 0.2, leftCPS.getInputMax());
            if (System.currentTimeMillis() - lastClick > speedLeft * 1000) {
                lastClick = System.currentTimeMillis();
                if (leftHold < lastClick) {
                    leftHold = lastClick;
                }
                int key = mc.gameSettings.keyBindAttack.getKeyCode();
                KeyBinding.setKeyBindState(key, true);
                KeyBinding.onTick(key);
                Utils.Client.setMouseButtonState(0, true);
            } else if (System.currentTimeMillis() - leftHold > leftHoldLength * 1000) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
                Utils.Client.setMouseButtonState(0, false);
            }
        }
    }

    private void ravenClick() {

        if(mc.currentScreen != null || !this.inFocus()) {
            doInventoryClick();
            return;
        }

        Mouse.poll();
        if(!Mouse.isButtonDown(0) && !leftDown) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
            Utils.Client.setMouseButtonState(0, false);
        }
        if (Mouse.isButtonDown(0) || leftDown) {
            if (weaponOnly.isToggled() && !InvUtils.isPlayerHoldingWeapon()) {
                return;
            }
            this.leftClickExecute(mc.gameSettings.keyBindAttack.getKeyCode());
        }
    }

    public void leftClickExecute(int key) {

        if(breakBlock()) return;

        if (jitterLeft.getValue() > 0.0D) {
            double a = jitterLeft.getValue() * 0.45D;
            EntityPlayerSP entityPlayer;
            if (this.rand.nextBoolean()) {
                entityPlayer = mc.thePlayer;
                entityPlayer.rotationYaw = (float)((double)entityPlayer.rotationYaw + (double)this.rand.nextFloat() * a);
            } else {
                entityPlayer = mc.thePlayer;
                entityPlayer.rotationYaw = (float)((double)entityPlayer.rotationYaw - (double)this.rand.nextFloat() * a);
            }

            if (this.rand.nextBoolean()) {
                entityPlayer = mc.thePlayer;
                entityPlayer.rotationPitch = (float)((double)entityPlayer.rotationPitch + (double)this.rand.nextFloat() * a * 0.45D);
            } else {
                entityPlayer = mc.thePlayer;
                entityPlayer.rotationPitch = (float)((double)entityPlayer.rotationPitch - (double)this.rand.nextFloat() * a * 0.45D);
            }
        }

        if (this.leftUpTime > 0L && this.leftDownTime > 0L) {
            if (System.currentTimeMillis() > this.leftUpTime && leftDown) {
                KeyBinding.setKeyBindState(key, true);
                KeyBinding.onTick(key);
                this.genLeftTimings();
                Utils.Client.setMouseButtonState(0, true);
                leftDown = false;
            } else if (System.currentTimeMillis() > this.leftDownTime) {
                KeyBinding.setKeyBindState(key, false);
                leftDown = true;
                Utils.Client.setMouseButtonState(0, false);
            }
        } else {
            this.genLeftTimings();
        }
    }

    public void genLeftTimings() {
        double clickSpeed = Utils.Client.ranModuleVal(leftCPS, this.rand) + 0.4D * this.rand.nextDouble();
        long delay = (int)Math.round(1000.0D / clickSpeed);
        if (System.currentTimeMillis() > this.leftk) {
            if (!this.leftn && this.rand.nextInt(100) >= 85) {
                this.leftn = true;
                this.leftm = 1.1D + this.rand.nextDouble() * 0.15D;
            } else {
                this.leftn = false;
            }

            this.leftk = System.currentTimeMillis() + 500L + (long)this.rand.nextInt(1500);
        }

        if (this.leftn) {
            delay = (long)((double)delay * this.leftm);
        }

        if (System.currentTimeMillis() > this.leftl) {
            if (this.rand.nextInt(100) >= 80) {
                delay += 50L + (long)this.rand.nextInt(100);
            }

            this.leftl = System.currentTimeMillis() + 500L + (long)this.rand.nextInt(1500);
        }

        this.leftUpTime = System.currentTimeMillis() + delay;
        this.leftDownTime = System.currentTimeMillis() + delay / 2L - (long)this.rand.nextInt(10);
    }

    private void inInvClick(GuiScreen guiScreen) {
        int mouseInGUIPosX = Mouse.getX() * guiScreen.width / mc.displayWidth;
        int mouseInGUIPosY = guiScreen.height - Mouse.getY() * guiScreen.height / mc.displayHeight - 1;

        try {
            this.playerMouseInput.invoke(guiScreen, mouseInGUIPosX, mouseInGUIPosY, 0);
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
    }

    public boolean breakBlock() {
        if (breakBlocks.isToggled() && mc.objectMouseOver != null) {
            BlockPos p = mc.objectMouseOver.getBlockPos();

            if (p != null) {
                Block bl = mc.theWorld.getBlockState(p).getBlock();
                if (bl != Blocks.air && !(bl instanceof BlockLiquid)) {
                    if (!breakHeld) {
                        int e = mc.gameSettings.keyBindAttack.getKeyCode();
                        KeyBinding.setKeyBindState(e, true);
                        KeyBinding.onTick(e);
                        breakHeld = true;
                    }
                    return true;
                }
                if(breakHeld) {
                    breakHeld = false;
                }
            }
        }

        return false;
    }

    public void doInventoryClick() {
        if (inventoryFill.isToggled() && (mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChest)) {
            if (!Mouse.isButtonDown(0) || !Keyboard.isKeyDown(54) && !Keyboard.isKeyDown(42)) {
                this.leftDownTime = 0L;
                this.leftUpTime = 0L;
            } else if (this.leftDownTime != 0L && this.leftUpTime != 0L) {
                if (System.currentTimeMillis() > this.leftUpTime) {
                    this.genLeftTimings();
                    this.inInvClick(mc.currentScreen);
                }
            } else {
                this.genLeftTimings();
            }
        }
    }

    public enum ClickStyle { Raven,  SKid }
    public enum ClickEvent { Tick, Render }

}