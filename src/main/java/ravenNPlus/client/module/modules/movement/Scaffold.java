package ravenNPlus.client.module.modules.movement;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.utils.Timer;
import ravenNPlus.client.utils.InvUtils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.*;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Scaffold extends Module {

    public static TickSetting head, camera, showBlockAmount, noSprint, onlyOnGround, onDisableSprint, lookDown, switchItem, swing;
    public static SliderSetting blockShowMode, delay, offset;
    public static ModeSetting mode;
    public static DoubleSliderSetting pitchRange;
    public static DescriptionSetting desc;
    protected static boolean forward;
    static BlockData data = null;

    public Scaffold() {
        super("Scaffold", ModuleCategory.movement, "Places blocks under you (faster then Telly Bridge)");
        this.addSetting(mode = new ModeSetting("Mode", Scaffold.modes.BetterMode));
        this.addSetting(delay = new SliderSetting("Delay", 17, 0, 50, 1));
        this.addSetting(head = new TickSetting("Rotate Head", true));
        this.addSetting(camera = new TickSetting("Rotate Camera", false));
        this.addSetting(offset = new SliderSetting("Camera Offset", 15, 5, 180, 1));
        this.addSetting(lookDown = new TickSetting("Only when looking down", true));
        this.addSetting(pitchRange = new DoubleSliderSetting("Pitch min range:", 70, 85, 0, 90, 1));
        this.addSetting(switchItem = new TickSetting("Switch Item", true));
        this.addSetting(noSprint = new TickSetting("No Sprint", true));
        this.addSetting(onlyOnGround = new TickSetting("Only On Ground", true));
        this.addSetting(onDisableSprint = new TickSetting("Sprint On Disable", true));
        this.addSetting(showBlockAmount = new TickSetting("Show amount of blocks", true));
        this.addSetting(blockShowMode = new SliderSetting("Block display info", 2, 1, 2, 1));
        this.addSetting(swing = new TickSetting("Swing", false));
    }

    int lastItem;
    float lastPitch;
    float yaw, pitch = 96;

    @Override
    public void onEnable() {
        lastItem = InvUtils.getCurrentPlayerSlot();
        lastPitch = this.player().rotationPitch;

        if (switchItem.isToggled()) {
            for (int i = 0; i < 9; i++) {
                net.minecraft.item.ItemStack itemStack = this.player().inventory.getStackInSlot(i);
                if (itemStack != null && itemStack.getItem() instanceof net.minecraft.item.ItemBlock) {
                    boolean b1 = itemStack.getItem() instanceof net.minecraft.item.ItemAnvilBlock;
                    boolean b2 = itemStack.getDisplayName().equals("Sand");
                    boolean b3 = itemStack.getDisplayName().equals("Red Sand");
                    boolean b4 = itemStack.getDisplayName().equals("Anvil");
                    boolean b5 = itemStack.getDisplayName().endsWith("Slab");
                    boolean b6 = itemStack.getDisplayName().startsWith("Lilly");
                    boolean b7 = itemStack.getDisplayName().contains("Sapling");

                    if (b1 || b2 || b3 || b4 || b5 || b6 || b7) return;

                    InvUtils.setCurrentPlayerSlot(i);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        if (switchItem.isToggled())
            InvUtils.setCurrentPlayerSlot(lastItem);

        if (camera.isToggled())
            this.player().rotationPitch = lastPitch;

        this.player().setSprinting(onDisableSprint.isToggled());
    }

    @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
    public void p(net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent pre) {
        if (!this.inGame()) return;
        if (!InvUtils.isPlayerHoldingBlock()) return;

        forward = Utils.Player.isMovingForward() && !mc.gameSettings.keyBindBack.isKeyDown();

        if (mode.getMode() == Scaffold.modes.BetterMode) {

            if (lookDown.isToggled())
                if (this.player().rotationPitch < pitchRange.getInputMin() || this.player().rotationPitch > pitchRange.getInputMax())
                    return;

            BlockData data = null;
            double posY, yDif;

            for (posY = this.player().posY - 1.0D; posY > 0.0D; posY--) {
                BlockData newData = getBlockData(new BlockPos(this.player().posX, posY, this.player().posZ));
                if (newData != null) {
                    yDif = this.player().posY - posY;
                    if (yDif <= 3.0D) {
                        data = newData;
                        break;
                    }
                }
            }

            if (data == null) {
                return;
            }

            if (noSprint.isToggled() && (this.player().isSprinting() || mc.gameSettings.keyBindSprint.isKeyDown() || mc.gameSettings.keyBindSprint.isPressed()))
                this.player().setSprinting(false);

            if (data.pos.equals(new BlockPos(0, -1, 0))) {
                this.player().motionX = 0;
                this.player().motionZ = 0;
            }

            if ((this.player().getHeldItem() != null) && (this.player().getHeldItem().getItem() instanceof net.minecraft.item.ItemBlock)) {
                if (Timer.hasTimeElapsed((long) delay.getValue() * 5, true)) {
                    if (forward) {
                        if (onlyOnGround.isToggled())
                            if (!this.player().onGround) return;

                        if (lookDown.isToggled())
                            if (this.player().rotationPitch < pitchRange.getInputMin() || this.player().rotationPitch > pitchRange.getInputMax())
                                return;

                        if (this.player().getHeldItem() != null && this.player().getHeldItem().getDisplayName().equals("Gravel"))
                            return;

                        if (this.player().getHeldItem() != null && this.player().getHeldItem().getDisplayName().endsWith("Sand"))
                            return;

                        if (this.player().getHeldItem() != null && this.player().getHeldItem().getDisplayName().endsWith("Powder"))
                            return;

                        if (Utils.Player.isPlayerInContainer()) return;
                        if (Utils.Player.isInLiquid()) return;
                        if (!forward) return;

                        mc.playerController.onPlayerRightClick(this.player(), mc.theWorld, this.player().getHeldItem(), data.pos, data.face, new net.minecraft.util.Vec3(data.pos.getX(), data.pos.getY(), data.pos.getZ()));
                        this.sendPacketPlayer(new net.minecraft.network.play.client.C0APacketAnimation());

                        if (swing.isToggled() && forward) {
                            if (Timer.hasTimeElapsed(500L, true))
                                Utils.Player.swing();
                        }
                    }
                }
            }
        }

        if (mode.getMode() == Scaffold.modes.BuggyMode) {

            if (lookDown.isToggled())
                if (this.player().rotationPitch < pitchRange.getInputMin() || this.player().rotationPitch > pitchRange.getInputMax())
                    return;

            BlockPos i = new BlockPos(this.player().posX, this.player().getEntityBoundingBox().minY, this.player().posZ);

            //                                             -2 ?
            if (valid(i.add(0, -1, 0)))
                Utils.Player.place(i.add(0, -1, 0), EnumFacing.UP, true);
            else if (valid(i.add(-1, -1, 0)))
                Utils.Player.place(i.add(0, -1, 0), EnumFacing.EAST, true);
            else if (valid(i.add(1, -1, 0)))
                Utils.Player.place(i.add(0, -1, -1), EnumFacing.WEST, true);
            else if (valid(i.add(0, -1, -1)))
                Utils.Player.place(i.add(0, -1, 0), EnumFacing.SOUTH, true);
            else if (valid(i.add(0, -1, 1)))
                Utils.Player.place(i.add(0, -1, 0), EnumFacing.NORTH, true);
            else if (valid(i.add(1, -1, 1))) {
                if (valid(i.add(0, -1, 1)))
                    Utils.Player.place(i.add(0, -1, 1), EnumFacing.NORTH, true);
                Utils.Player.place(i.add(1, -1, 1), EnumFacing.EAST, true);
            } else if (valid(i.add(-1, -1, 1))) {
                if (valid(i.add(-1, -1, 0)))
                    Utils.Player.place(i.add(0, -1, 1), EnumFacing.WEST, true);
                Utils.Player.place(i.add(-1, -1, 1), EnumFacing.SOUTH, true);
            } else if (valid(i.add(-1, -1, -1))) {
                if (valid(i.add(0, -1, -1)))
                    Utils.Player.place(i.add(0, -1, 1), EnumFacing.SOUTH, true);
                Utils.Player.place(i.add(-1, -1, 1), EnumFacing.WEST, true);
            } else if (valid(i.add(1, -1, -1))) {
                if (valid(i.add(1, -1, 0)))
                    Utils.Player.place(i.add(1, -1, 0), EnumFacing.EAST, true);
                Utils.Player.place(i.add(1, -1, -1), EnumFacing.NORTH, true);
            }
        }

        if (head.isToggled()) {
            if (onlyOnGround.isToggled())
                if (!this.player().onGround) return;
            if (!forward) return;
            this.player().rotationYawHead = yaw;
        }

        if (camera.isToggled()) {
            if (onlyOnGround.isToggled())
                if (!this.player().onGround) return;
            if (!forward) return;
            this.player().rotationPitch = (float) (pitch - offset.getValue());
        }
    }

    boolean valid(BlockPos l) {
        net.minecraft.block.Block i = mc.theWorld.getBlockState(l).getBlock();
        return !(i instanceof net.minecraft.block.BlockLiquid) && i.getMaterial() != net.minecraft.block.material.Material.air || i.getMaterial()
                != net.minecraft.block.material.Material.sand || i.getMaterial() != net.minecraft.block.material.Material.anvil;
    }

    public Scaffold.BlockData getBlockData(BlockPos pos) {
        if (mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        } else if (mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        } else if (mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        } else if (mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        } else if (mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        } else {
            return null;
        }
    }

    @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
    public void s(net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent post) {
        if (!this.inGame()) return;
        if (!InvUtils.isPlayerHoldingBlock()) return;

        if (mode.getMode() == Scaffold.modes.BetterMode) {
            if (onlyOnGround.isToggled())
                if (!this.player().onGround) return;
            if (!forward) return;

            if (lookDown.isToggled())
                if (this.player().rotationPitch < pitchRange.getInputMin() || this.player().rotationPitch > pitchRange.getInputMax())
                    return;

            double posY, yDif;
            for (posY = this.player().posY - 1.0D; posY > 0.0D; posY--) {
                BlockData newData = getBlockData(new BlockPos(this.player().posX, posY, this.player().posZ));
                if (newData != null) {
                    yDif = this.player().posY - posY;
                    if (yDif <= 3.0D) {
                        data = newData;
                        break;
                    }
                }
            }

            if (data == null) {
                return;
            }

            if (java.util.Objects.equals(data.pos, new BlockPos(0, -1, 0))) {
                this.player().motionX = 0;
                this.player().motionZ = 0;
            }

            if (data.face == EnumFacing.UP) {
                yaw = 90;
            } else if (data.face == EnumFacing.NORTH) {
                yaw = 360;
            } else if (data.face == EnumFacing.EAST) {
                yaw = 90;
            } else if (data.face == EnumFacing.SOUTH) {
                yaw = 180;
            } else if (data.face == EnumFacing.WEST) {
                yaw = 270;
            } else {
                yaw = 90;
            }

            ravenNPlus.client.utils.event.EventPreMotion.setYaw(yaw);
            ravenNPlus.client.utils.event.EventPreMotion.setPitch(pitch);

            if (head.isToggled()) {
                if (onlyOnGround.isToggled())
                    if (!this.player().onGround) return;
                if (!forward) return;
                this.player().rotationYawHead = yaw;
            }

            if (camera.isToggled()) {
                if (onlyOnGround.isToggled())
                    if (!this.player().onGround) return;
                if (!forward) return;
                this.player().rotationPitch = (float) (pitch - offset.getValue());
            }
        }
    }

    @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
    public void r(net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent e) {
        if (!this.inGame()) return;
        if (!InvUtils.isPlayerHoldingBlock()) return;
        if (!showBlockAmount.isToggled()) return;

        if (mode.getMode() == Scaffold.modes.BetterMode) {
            if (e.phase == net.minecraftforge.fml.common.gameevent.TickEvent.Phase.END) {
                if (mc.currentScreen == null) {
                    if (onlyOnGround.isToggled())
                        if (!this.player().onGround) return;
                    if (!forward) return;

                    if (lookDown.isToggled())
                        if (this.player().rotationPitch < pitchRange.getInputMin() || this.player().rotationPitch > pitchRange.getInputMax())
                            return;

                    net.minecraft.client.gui.ScaledResolution res = new net.minecraft.client.gui.ScaledResolution(mc);

                    int totalBlocks = 0;
                    if (ravenNPlus.client.module.modules.player.SafeWalk.BlockAmountInfo.values()[(int) blockShowMode.getValue() - 1]
                            == ravenNPlus.client.module.modules.player.SafeWalk.BlockAmountInfo.BLOCKS_IN_CURRENT_STACK) {
                        totalBlocks = InvUtils.getBlockAmountInCurrentStack(this.player().inventory.currentItem);
                    } else {
                        for (int slot = 0; slot < 36; slot++) {
                            if (totalBlocks != 0)
                                totalBlocks += InvUtils.getBlockAmountInCurrentStack(slot);
                        }
                    }

                    if (totalBlocks <= 0) {
                        return;
                    }

                    int rgb;
                    if (totalBlocks < 3)
                        rgb = new java.awt.Color(238, 0, 0).getRGB();
                    else if (totalBlocks < 6)
                        rgb = new java.awt.Color(215, 25, 0).getRGB();
                    else if (totalBlocks < 9)
                        rgb = new java.awt.Color(203, 37, 0).getRGB();
                    else if (totalBlocks < 12)
                        rgb = new java.awt.Color(192, 49, 0).getRGB();
                    else if (totalBlocks < 15)
                        rgb = new java.awt.Color(181, 61, 0).getRGB();
                    else if (totalBlocks < 18)
                        rgb = new java.awt.Color(170, 74, 0).getRGB();
                    else if (totalBlocks < 21)
                        rgb = new java.awt.Color(158, 86, 0).getRGB();
                    else if (totalBlocks < 24)
                        rgb = new java.awt.Color(147, 98, 0).getRGB();
                    else if (totalBlocks < 27)
                        rgb = new java.awt.Color(136, 110, 0).getRGB();
                    else if (totalBlocks < 30)
                        rgb = new java.awt.Color(124, 122, 0).getRGB();
                    else if (totalBlocks < 33)
                        rgb = new java.awt.Color(113, 134, 0).getRGB();
                    else if (totalBlocks < 36)
                        rgb = new java.awt.Color(102, 146, 0).getRGB();
                    else if (totalBlocks < 39)
                        rgb = new java.awt.Color(90, 158, 0).getRGB();
                    else if (totalBlocks < 42)
                        rgb = new java.awt.Color(79, 170, 0).getRGB();
                    else if (totalBlocks < 45)
                        rgb = new java.awt.Color(68, 182, 0).getRGB();
                    else if (totalBlocks < 48)
                        rgb = new java.awt.Color(56, 194, 0).getRGB();
                    else if (totalBlocks < 51)
                        rgb = new java.awt.Color(45, 207, 0).getRGB();
                    else if (totalBlocks < 54)
                        rgb = new java.awt.Color(34, 219, 0).getRGB();
                    else if (totalBlocks < 57)
                        rgb = new java.awt.Color(23, 231, 0).getRGB();
                    else if (totalBlocks < 60)
                        rgb = new java.awt.Color(11, 243, 0).getRGB();
                    else if (totalBlocks < 65)
                        rgb = new java.awt.Color(0, 255, 0).getRGB();
                    else rgb = new java.awt.Color(0, 255, 0).getRGB();

                    String t;
                    if (totalBlocks == 1) {
                        t = totalBlocks + " block";
                    } else {
                        t = totalBlocks + " blocks";
                    }

                    int x = res.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth(t) / 2, y;

                    if (ravenNPlus.client.main.Client.debugger) {
                        y = res.getScaledHeight() / 2 + 17 + mc.fontRendererObj.FONT_HEIGHT;
                    } else {
                        y = res.getScaledHeight() / 2 + 15;
                    }

                    mc.fontRendererObj.drawString(t, (float) x, (float) y, rgb, false);
                }
            }
        }
    }

    protected static class BlockData {
        public final BlockPos pos;
        public final EnumFacing face;

        BlockData(BlockPos pos, EnumFacing face) {
            this.pos = pos;
            this.face = face;
        }
    }

    public enum modes {
        BetterMode, BuggyMode
    }

}