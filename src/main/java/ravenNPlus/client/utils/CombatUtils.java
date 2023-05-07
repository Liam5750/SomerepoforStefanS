package ravenNPlus.client.utils;

import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.client.network.NetworkPlayerInfo;
import java.util.ArrayList;
import java.util.Collection;

public class CombatUtils {

    public static boolean canTarget(Entity entity, boolean team) {
        if (entity != null && entity != Minecraft.getMinecraft().thePlayer) {
            EntityLivingBase entityLivingBase = null;

            if (entity instanceof EntityLivingBase)
                entityLivingBase = (EntityLivingBase) entity;

            boolean isTeam = isTeam(Minecraft.getMinecraft().thePlayer, entity);
            boolean isVisible = (!entity.isInvisible());

            return !(entity instanceof EntityArmorStand) && isVisible && (entity instanceof EntityPlayer && !isTeam && !team || entity instanceof EntityAnimal || entity instanceof EntityMob || entity instanceof EntityLivingBase && entityLivingBase.isEntityAlive());
        } else {
            return false;
        }
    }

    public static boolean isPlayerTwiceInGame() {
        for (NetworkPlayerInfo info : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
            String infoID = info.getGameProfile().getId().toString();

            if (getPlayerList().get(0).getGameProfile().getId().toString().equals(infoID)) {

                for (NetworkPlayerInfo info2 : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
                    String infoID2 = info2.getGameProfile().getId().toString();

                    if (getPlayerList().get(0).getGameProfile().getId().toString().equals(infoID2))
                        return true;

                }
            }
        }

        return false;
    }

    public static boolean isTeam(EntityPlayer player, Entity entity) {
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).getTeam() != null && player.getTeam() != null) {
            Character entity_3 = entity.getDisplayName().getFormattedText().charAt(3);
            Character player_3 = player.getDisplayName().getFormattedText().charAt(3);
            Character entity_2 = entity.getDisplayName().getFormattedText().charAt(2);
            Character player_2 = player.getDisplayName().getFormattedText().charAt(2);
            boolean isTeam = false;
            if (entity_3.equals(player_3) && entity_2.equals(player_2)) {
                isTeam = true;
            } else {
                Character entity_1 = entity.getDisplayName().getFormattedText().charAt(1);
                Character player_1 = player.getDisplayName().getFormattedText().charAt(1);
                Character entity_0 = entity.getDisplayName().getFormattedText().charAt(0);
                Character player_0 = player.getDisplayName().getFormattedText().charAt(0);
                if (entity_1.equals(player_1) && Character.isDigit(0) && entity_0.equals(player_0)) {
                    isTeam = true;
                }
            }

            return isTeam;
        } else {
            return true;
        }
    }

    public static boolean canCrit() {
        return !Utils.Player.isInLiquid() && Minecraft.getMinecraft().thePlayer.onGround && Minecraft.getMinecraft().thePlayer.isSwingInProgress;
    }

    public static float[][] getBipedRotations(ModelBiped bip) {
        float[][] rotations = new float[5][];
        float[] headRotation = {bip.bipedHead.rotateAngleX, bip.bipedHead.rotateAngleY, bip.bipedHead.rotateAngleZ};
        rotations[0] = headRotation;
        float[] rightArmRotation = {bip.bipedRightArm.rotateAngleX, bip.bipedRightArm.rotateAngleY, bip.bipedRightArm.rotateAngleZ};
        rotations[1] = rightArmRotation;
        float[] leftArmRotation = {bip.bipedLeftArm.rotateAngleX, bip.bipedLeftArm.rotateAngleY, bip.bipedLeftArm.rotateAngleZ};
        rotations[2] = leftArmRotation;
        float[] rightLegRotation = {bip.bipedRightLeg.rotateAngleX, bip.bipedRightLeg.rotateAngleY, bip.bipedRightLeg.rotateAngleZ};
        rotations[3] = rightLegRotation;
        float[] leftLegRotation = {bip.bipedLeftLeg.rotateAngleX, bip.bipedLeftLeg.rotateAngleY, bip.bipedLeftLeg.rotateAngleZ};
        rotations[4] = leftLegRotation;
        return rotations;
    }

    public static ArrayList<EntityPlayer> getPlayerList() {
        Collection<NetworkPlayerInfo> playerInfoMap = Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap();
        ArrayList<EntityPlayer> list = new ArrayList<>();
        for (NetworkPlayerInfo networkPlayerInfo : playerInfoMap) {
            list.add(Minecraft.getMinecraft().theWorld.getPlayerEntityByName(networkPlayerInfo.getGameProfile().getName()));
        }
        return list;
    }

    public static boolean isEntityInPlayerRange(Entity en, double range, boolean invis) {
        if (invis && !en.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer))
            return false;

        return Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) < range;

        // mc.thePlayer.getDistanceToEntity(getTarget()) < range.getValue()
    }

    public static boolean inTab(EntityLivingBase en) {
        if (!Minecraft.getMinecraft().isSingleplayer()) {

            for (NetworkPlayerInfo info : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap())
                if (info != null && info.getGameProfile() != null && info.getGameProfile().getName().contains(en.getName()))
                    return true;
        }

        return false;
    }

}