package ravenNPlus.client.utils;

public class DimensionHelper {

    public static boolean isPlayerInNether() {
        if (!Utils.Player.isPlayerInGame()) return false;
        return (net.minecraft.client.Minecraft.getMinecraft().thePlayer.dimension == DIMENSIONS.NETHER.getDimensionID());
    }

    public static boolean isPlayerInEnd() {
        if (!Utils.Player.isPlayerInGame()) return false;
        return (net.minecraft.client.Minecraft.getMinecraft().thePlayer.dimension == DIMENSIONS.END.getDimensionID());
    }

    public static boolean isPlayerInOverworld() {
        if (!Utils.Player.isPlayerInGame()) return false;
        return (net.minecraft.client.Minecraft.getMinecraft().thePlayer.dimension == DIMENSIONS.OVERWORLD.getDimensionID());
    }

    enum DIMENSIONS {
        NETHER(-1), OVERWORLD(0), END(1);

        private final int dimensionID;

        DIMENSIONS(int n) {
            this.dimensionID = n;
        }

        public int getDimensionID() {
            return dimensionID;
        }
    }

}