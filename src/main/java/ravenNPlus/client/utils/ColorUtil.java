package ravenNPlus.client.utils;

import java.awt.Color;

import static org.lwjgl.opengl.GL11.glColor4f;

public class ColorUtil {

    // --------------- Colors --------------- \\
    public static int color_int_min = Integer.MIN_VALUE;
    public static int color_int_max = Integer.MAX_VALUE;
    public static int color_bow = 0x6f825432;
    public static int color_sword = 0x6f169c9d;
    public static int color_gold = 0x6ff9801d;
    public static int color_block = 0x6ff38caa;
    public static int color_esp_green = 0x6f00ff00;
    public static int color_bindComponent = Color.HSBtoRGB((float)(System.currentTimeMillis() % 3750L) / 3750.0f, 0.8f, 0.8f);
    public static Color color_categoryComponent_3 = new Color(92, 92, 92, 255);
    public static int color_descComponent1 = (new Color(226, 83, 47)).getRGB();
    public static int color_modeComponent1 = (new Color(30, 144, 255)).getRGB();
    public static int color_moduleComponent1 = new Color(0, 85, 255).getRGB();
    public static int color_moduleComponent2 = new Color(154, 2, 255).getRGB();
    public static int color_moduleComponent3 = new Color(175, 143, 233).getRGB();
    public static int color_client = color_moduleComponent2;
    public static int color_full = 0xffffffff;
    public static int color_white1 = -1;
    public static int color_white2 = Color.white.getRGB();
    public static int color_black1 = 1;
    public static int color_black2 = Color.black.getRGB();
    public static int color_button_rgb1 = new Color(104,103,104).getRGB();
    public static int color_button_rgb2 = new Color(209, 102, 102).getRGB();
    public static int color_button_rgb3 = new Color(31, 30, 31).getRGB();
    public static int color_moduleComponent4 = -12302777;
    public static int color_moduleComponent5 = -12829381;
    public static int color_rangeSliderComponent1 = 0xff1d1d1F;
    public static int color_sliderComponent1 = Color.getHSBColor((float) (System.currentTimeMillis() % 11000L) / 11000.0f, 0.75f, 0.9f).getRGB();
    public static int color_tickComponent1 = (new Color(40, 255, 0)).getRGB();
    public static int color_tickComponent2 = (new Color(169, 169, 169)).getRGB();

    public static int color_commandPrompt1 = 0xff2D3742;
    public static int color_commandPrompt2 = new Color(79, 104, 158).getRGB();
    public static int color_commandPrompt3 = new Color(51, 51, 51, 210).getRGB();
    public static int color_commandPromptTitle = 0xff088000;
    public static int color_onlyAlpha = 0xff000000;
    public static int color_red = 0xff0000;
    public static int color_green = 0x00ff00;
    public static int color_blue = 0x0000ff;

    public static void setColor_client(int color) {
        ColorUtil.color_client = color;
    }

    public static void setColor_client(Color color) {
        ColorUtil.color_client = color.getRGB();
    }

    public static void color_clear(boolean stringMode) {
        if (stringMode)
            net.minecraft.client.Minecraft.getMinecraft().fontRendererObj.drawString("", 0, 0, ColorUtil.color_full);

        if (!stringMode)
            net.minecraft.client.renderer.GlStateManager.clearColor(255f, 255f, 255f, 255f);
    }

    public static void setColor(int color) {
        float a = (color >> 24 & 0xFF) / 255.0F;
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        glColor4f(r, g, b, a);
    }

    public static Color rainbowEffect(int delay) {
        float hue = (float) (System.nanoTime() + (long) delay) / 2.0E10F % 1.0F;
        Color color = new Color((int) Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0F, 1.0F)), 16));
        return new Color((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F,
                (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
    }

    public static Color color_blend(Color color1, Color color2, double ration) {
        float r = (float) ration;
        float ir = 1.0F - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        return new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir, rgb1[2] * r + rgb2[2] * ir);
    }

}