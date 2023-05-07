package ravenNPlus.client.utils;

public class SoundUtil {

    public static String ogg = ".ogg", wav = ".wav", mp3 = ".mp3", m4a  = ".m4a", opus = ".opus";
    public static String chestOpen = "random.chestopen";
    public static String chestClose = "random.chestclosed";
    public static String endermenPortal = "mob.endermen.portal";
    public static String tntExplosion = "random.explode";
    public static String click0 = "random.click";
    public static String click1 = "gui.button.press";
    public static String bowhit  = "random.bowhit";
    public static String playerDie = "game.player.die";
    public static String playerHurt = "game.player.hurt";
    public static String musicCat = "records.cat";

    public static void play(String name, float volume, float pitch) {
        if(!Utils.Player.isPlayerInGame()) return;

        net.minecraft.entity.player.EntityPlayer player = net.minecraft.client.Minecraft.getMinecraft().thePlayer;

        switch (name) {
            case "endermen_portal":
                player.playSound(endermenPortal, volume, pitch);
                break;
            case "random.explode":
                player.playSound(tntExplosion, volume, pitch);
                break;
            case "random.click":
                player.playSound(click0, volume, pitch);
                break;
            case "gui.button.press":
                player.playSound(click1, volume, pitch);
                break;
            case "random.bowhit":
                player.playSound(bowhit, volume, pitch);
                break;
            case "game.player.hurt":
                player.playSound(playerHurt, volume, pitch);
                break;
            case "game.player.die":
                player.playSound(playerDie, volume, pitch);
                break;
            case "records.cat":
                player.playSound(musicCat, volume, pitch);
                break;
            case "random.chestopen":
                player.playSound(chestOpen, volume, pitch);
                break;
            case "random.chestclosed":
                player.playSound(chestClose, volume, pitch);
                break;
        }
    }

}