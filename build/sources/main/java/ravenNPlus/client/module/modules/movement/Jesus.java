package ravenNPlus.client.module.modules.movement;

import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.ModeSetting;
import ravenNPlus.client.module.setting.impl.DescriptionSetting;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Jesus extends Module {

    public static DescriptionSetting desc;
    public static ModeSetting mode;

    public Jesus() {
        super("Jesus", ModuleCategory.movement, "Walk over Wotah, like Jesus did");
        this.addSetting(mode = new ModeSetting("Mode", Jesus.modes.Dolphin));
    }

    @SubscribeEvent
    public void r(PlayerEvent e) {
        if (!this.inGame()) return;

        if (mode.getMode() == Jesus.modes.NPC_Vulcan) {
            if (this.inWater() && this.player().isInsideOfMaterial(Material.air)) {
                this.player().motionY = 0.08;
            }

        } else if (mode.getMode() == Jesus.modes.Jump) {
            if (this.inWater() && this.player().onGround) {
                this.player().motionY = 1;
            }

        } else if (mode.getMode() == Jesus.modes.AAC) {
            if (!this.player().onGround && this.inWater() || this.player().isInWater()) {
                if (!this.player().isSprinting()) {
                    this.player().motionX *= 0.99999;
                    this.player().motionY *= 0.0;
                    this.player().motionZ *= 0.99999;

                    if (this.player().isCollidedHorizontally) {
                        this.player().motionY = ((this.player().posY - (this.player().posY - 1) / 8f));
                    }

                } else {
                    this.player().motionX *= 0.99999;
                    this.player().motionY *= 0.0;
                    this.player().motionZ *= 0.99999;

                    if (this.player().isCollidedHorizontally) {
                        this.player().motionY = ((this.player().posY - (this.player().posY - 1) / 8f));
                    }
                }

                if (this.player().fallDistance >= 4) {
                    this.player().motionY = -0.004;
                } else if (this.player().isInWater()) this.player().motionY = 0.09;
            }

            if (this.player().hurtTime != 0) {
                this.player().onGround = false;
            }

        } else if (mode.getMode() == Jesus.modes.Matrix) {
            if (this.player().isInWater()) {
                mc.gameSettings.keyBindJump.isPressed();
                if (this.player().isCollidedHorizontally) {
                    this.player().motionY = +0.09;
                    return;
                }

                this.player().motionY = 0.0;
                this.player().motionX *= 0.15;
                this.player().motionZ *= 0.15;
            }

        } else if (mode.getMode() == Jesus.modes.Spartan) {
            if (this.player().isCollidedHorizontally) {
                this.player().motionY += 0.15;
                return;
            }

            this.player().motionY = 0.0;
            this.player().onGround = true;
            this.player().motionX *= 0.085;
            this.player().motionZ *= 0.085;
        } else if (mode.getMode() == Jesus.modes.Dolphin) {
            if (this.player().isInWater()) {
                this.player().motionY += 0.03999999910593033;
            }

        } else if (mode.getMode() == Jesus.modes.Twilight) {
            if (this.player().isInWater()) {
                this.player().motionX *= 0.01;
                this.player().motionZ *= 0.01;
                ravenNPlus.client.utils.Utils.Player.bop(0.7);
            }

        } else if (mode.getMode() == Jesus.modes.Frog) {
            if (this.player().isInWater()) {
                this.player().onGround = true;
                this.player().setJumping(true);
            }
        }

    }

    public enum modes {
        NPC_Vulcan, Jump, AAC,
        Matrix, Spartan, Dolphin,
        Twilight, Frog;
    }

}