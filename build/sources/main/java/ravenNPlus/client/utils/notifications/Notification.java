package ravenNPlus.client.utils.notifications;

import ravenNPlus.client.clickgui.RavenNPlus.ClickGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import java.awt.Color;

public class Notification {

    private final Type type;
    private final String title;
    private final String messsage;
    private long start;
    private final long fadedIn;
    private final long fadeOut;
    private final long end;

    public Notification(Type type, String title, String messsage, int length) {
        this.type = type;
        this.title = title;
        this.messsage = messsage;

        fadedIn = 200L * length;
        fadeOut = fadedIn + 500L * length;
        end = fadeOut + fadedIn;
    }

    public void show() {
        start = System.currentTimeMillis();
    }

    public boolean isShown() {
        return getTime() <= end;
    }

    private long getTime() {
        return System.currentTimeMillis() - start;
    }

    public void render() {

        double offset;
        int width = 45;   //120
        int height = 30;
        long time = getTime();

        if (time < fadedIn) {
            offset = Math.tanh(time / (double) (fadedIn) * 3.0) * width;
        } else if (time > fadeOut) {
            offset = (Math.tanh(3.0 - (time - fadeOut) / (double) (end - fadeOut) * 3.0) * width);
        } else {
            offset = width;
        }

        Color color = new Color(0, 0, 0, 220);
        Color color1;

        if (type == Type.INFO) {
            color1 = new Color(0, 26, 169);
        } else if (type == Type.WARNING) {
            color1 = new Color(204, 193, 0);
        } else if (type == Type.OTHER) {
            color1 = new Color(125, 40, 255);
        } else {
            color1 = new Color(204, 0, 18);
            int i = Math.max(0, Math.min(255, (int) (Math.sin(time / 100.0) * 255.0 / 2 + 127.5)));
            color = new Color(i, 0, 0, 220);
        }

        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        int messageWidth = fontRenderer.getStringWidth(messsage);
        int titleWidth = fontRenderer.getStringWidth(title);
        int scaledWidth = scaledResolution.getScaledWidth();
        int scaledHeight = scaledResolution.getScaledHeight();

        offset += Math.floor(Math.max(titleWidth, messageWidth) - (1F));

        int m = 0;
        if (Minecraft.getMinecraft().currentScreen instanceof ClickGui) {
            //m = 0;

            drawRect(scaledWidth - offset, scaledHeight - 22 - m - height, scaledWidth, scaledHeight - 22 - m, color.getRGB());
            drawRect(scaledWidth - offset, scaledHeight - 22 - m - height, scaledWidth - offset + 4, scaledHeight - 22 - m, color1.getRGB());
            fontRenderer.drawString(title, (int) (scaledWidth - offset + 8), scaledHeight - 22 - m - height, -1);
            drawRect((int) (scaledWidth - offset + 8), scaledHeight - 39 - m, (int) (scaledWidth - offset + 8) + titleWidth, scaledHeight - 39 + 1, new Color(-1).getRGB()); //underline
            fontRenderer.drawString(messsage, (int) (scaledWidth - offset + 8), scaledHeight - 33 - m, -1);

        } else {
            //m = scaledHeight-45;

            drawRect(scaledWidth - offset, scaledHeight - 5 - m - height, scaledWidth, scaledHeight - 5 - m, color.getRGB());
            drawRect(scaledWidth - offset, scaledHeight - 5 - m - height, scaledWidth - offset + 4, scaledHeight - 5 - m, color1.getRGB());
            fontRenderer.drawString(title, (int) (scaledWidth - offset + 8), scaledHeight - 2 - m - height, -1);
            drawRect((int) (scaledWidth - offset + 8), scaledHeight - 22 - m, (int) (scaledWidth - offset + 8) + titleWidth, scaledHeight - 22 - m + 1, new Color(-1).getRGB()); //underline
            fontRenderer.drawString(messsage, (int) (scaledWidth - offset + 8), scaledHeight - 15 - m, -1);

        }
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(int mode, double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(mode, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

}