package ravenNPlus.client.utils;

//thanks to WMS and L4J [ :3 ]

import net.minecraft.client.gui.Gui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import java.awt.*;
import static org.lwjgl.opengl.GL11.*;
import static ravenNPlus.client.utils.RenderUtils.*;

public class RoundedUtils {

    final static Minecraft mc = Minecraft.getMinecraft();

    public static void enableGL2D() {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(true);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
    }

    public static void disableGL2D() {
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
    }

    //code by SleepyFish with help of L4J

    /**
     * @param x      : X pos
     * @param y      : Y pos
     * @param x1     : X2 pos
     * @param y1     : Y2 pos
     * @param radius : round of edges;
     * @param color  : color;
     */
    public static void drawSmoothRoundedRect(float x, float y, float x1, float y1, float radius, int color) {
        glPushAttrib(1);
        glScaled(0.5D, 0.5D, 0.5D);
        x *= 2.0D;
        y *= 2.0D;
        x1 *= 2.0D;
        y1 *= 2.0D;
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        ColorUtil.setColor(color);
        glEnable(GL_LINE_SMOOTH);
        glBegin(GL_POLYGON);
        int i;

        //left corner
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);

        //right corner
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);

        //left corner
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius);

        //right corner
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius);

        glEnd();
        glBegin(GL_LINE_LOOP);

        //left upper smooth corner
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);

        //right upper smooth corner
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);

        //left down smooth corner
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius);

        //right down smooth corner
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius);

        glEnd();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        glScaled(2.0D, 2.0D, 2.0D);
        glPopAttrib();
        glLineWidth(1);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void drawOutlinedButton(float x, float y, float width, float height, float radius, int color) {
        RoundedUtils.drawRoundedOutline(x, y, width, height, radius, 0.7F, color);
    }

    public static void drawOutlinedButton(float x, float y, float width, float height, int color) {
        RoundedUtils.drawRoundedOutline(x, y, width, height, 5, 0.7F, color);
    }

    public static void drawOutlinedButton(float x, float y, float width, float height, float radius) {
        RoundedUtils.drawRoundedOutline(x, y, width, height, radius, 0.7F, ColorUtil.color_int_min);
    }

    public static void drawOutlinedButton(float x, float y, float width, float height) {
        RoundedUtils.drawRoundedOutline(x, y, width, height, 5, 0.7F, ColorUtil.color_int_min);
    }

    public static void drawOutlinedButton(float x, float y, float radius, int color) {
        RoundedUtils.drawRoundedOutline(x, y, 40, 20, radius, 0.7F, color);
    }

    public static void drawOutlinedButton(float x, float y, float radius) {
        RoundedUtils.drawRoundedOutline(x, y, 40, 20, radius, 0.7F, ColorUtil.color_int_min);
    }

    public static void drawOutlinedButton(float x, float y, int color) {
        RoundedUtils.drawRoundedOutline(x, y, 40, 20, 5, 0.7F, color);
    }

    public static void drawOutlinedButton(float x, float y) {
        RoundedUtils.drawRoundedOutline(x, y, 40, 20, 5, 0.7F, ColorUtil.color_int_min);
    }

    public static void drawRoundedRect(float x, float y, float x1, float y1, float radius, int color) {
        glPushAttrib(0);
        glScaled(0.5D, 0.5D, 0.5D);
        x *= 2.0D;
        y *= 2.0D;
        x1 *= 2.0D;
        y1 *= 2.0D;
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        ColorUtil.setColor(color);
        glEnable(GL_LINE_SMOOTH);
        glBegin(GL_POLYGON);
        int i;
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius);
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius);
        glEnd();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glScaled(2.0D, 2.0D, 2.0D);
        glEnable(GL_BLEND);
        glPopAttrib();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void drawRoundedOutline(float x, float y, float x1, float y1, float radius, float lineWidth, int color) {
        glPushAttrib(0);
        glScaled(0.5D, 0.5D, 0.5D);
        x *= 2.0D;
        y *= 2.0D;
        x1 *= 2.0D;
        y1 *= 2.0D;
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        ColorUtil.setColor(color);
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(lineWidth);
        glBegin(GL_LINE_LOOP);
        int i;
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius);
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius);
        glEnd();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
        glScaled(2.0D, 2.0D, 2.0D);
        glPopAttrib();
        glLineWidth(1);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static int width() {
        return (new ScaledResolution(Minecraft.getMinecraft())).getScaledWidth();
    }

    public static int height() {
        return (new ScaledResolution(Minecraft.getMinecraft())).getScaledHeight();
    }

    public static void drawRect(float x, float y, float x1, float y1) {
        glBegin(7);
        glVertex2f(x, y1);
        glVertex2f(x1, y1);
        glVertex2f(x1, y);
        glVertex2f(x, y);
        glEnd();
    }

    public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int borderColor) {
        enableGL2D();
        glColor(borderColor);
        drawRect(x + width, y, x1 - width, y + width);
        drawRect(x, y, x + width, y1);
        drawRect(x1 - width, y, x1, y1);
        drawRect(x + width, y1 - width, x1 - width, y1);
        disableGL2D();
    }

    public static void drawFilledCircle(double x, double y, double r, int c, int id) {
        float f = (float) (c >> 24 & 255) / 255.0F;
        float f2 = (float) (c >> 16 & 255) / 255.0F;
        float f3 = (float) (c >> 8 & 255) / 255.0F;
        float f4 = (float) (c & 255) / 255.0F;
        glEnable(3042);
        glDisable(3553);
        glColor4f(f2, f3, f4, f);
        glBegin(9);
        int i;
        double x2;
        double y2;
        if (id == 1) {
            glVertex2d(x, y);

            for (i = 0; i <= 90; ++i) {
                x2 = Math.sin((double) i * 3.141526 / 180.0) * r;
                y2 = Math.cos((double) i * 3.141526 / 180.0) * r;
                glVertex2d(x - x2, y - y2);
            }
        } else if (id == 2) {
            glVertex2d(x, y);

            for (i = 90; i <= 180; ++i) {
                x2 = Math.sin((double) i * 3.141526 / 180.0) * r;
                y2 = Math.cos((double) i * 3.141526 / 180.0) * r;
                glVertex2d(x - x2, y - y2);
            }
        } else if (id == 3) {
            glVertex2d(x, y);

            for (i = 270; i <= 360; ++i) {
                x2 = Math.sin((double) i * 3.141526 / 180.0) * r;
                y2 = Math.cos((double) i * 3.141526 / 180.0) * r;
                glVertex2d(x - x2, y - y2);
            }
        } else if (id == 4) {
            glVertex2d(x, y);

            for (i = 180; i <= 270; ++i) {
                x2 = Math.sin((double) i * 3.141526 / 180.0) * r;
                y2 = Math.cos((double) i * 3.141526 / 180.0) * r;
                glVertex2d(x - x2, y - y2);
            }
        } else {
            for (i = 0; i <= 360; ++i) {
                x2 = Math.sin((double) i * 3.141526 / 180.0) * r;
                y2 = Math.cos((double) i * 3.141526 / 180.0) * r;
                glVertex2f((float) (x - x2), (float) (y - y2));
            }
        }

        glEnd();
        glEnable(3553);
        glDisable(3042);
    }

    public static void drawFullCircle(int cx, int cy, double r, int segments, float lineWidth, int part, int c) {
        glScalef(0.5F, 0.5F, 0.5F);
        r *= 2.0;
        cx *= 2;
        cy *= 2;
        float f2 = (float) (c >> 24 & 255) / 255.0F;
        float f3 = (float) (c >> 16 & 255) / 255.0F;
        float f4 = (float) (c >> 8 & 255) / 255.0F;
        float f5 = (float) (c & 255) / 255.0F;
        glEnable(3042);
        glLineWidth(lineWidth);
        glDisable(3553);
        glEnable(2848);
        glBlendFunc(770, 771);
        glColor4f(f3, f4, f5, f2);
        glBegin(3);

        for (int i = segments - part; i <= segments; ++i) {
            double x = Math.sin((double) i * Math.PI / 180.0) * r;
            double y = Math.cos((double) i * Math.PI / 180.0) * r;
            glVertex2d((double) cx + x, (double) cy + y);
        }

        glEnd();
        glDisable(2848);
        glEnable(3553);
        glDisable(3042);
        glScalef(2.0F, 2.0F, 2.0F);
    }

    public static void glColor(int hex) {
        float alpha = (float) (hex >> 24 & 255) / 255.0F;
        float red = (float) (hex >> 16 & 255) / 255.0F;
        float green = (float) (hex >> 8 & 255) / 255.0F;
        float blue = (float) (hex & 255) / 255.0F;
        glColor4f(red, green, blue, alpha);
    }

    public static void drawFullscreenImage(String image) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        glDisable(2929);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        glDisable(3008);
        ResourceLocation texture = new ResourceLocation(image);
        mc.renderEngine.bindTexture(texture);
        Gui.drawModalRectWithCustomSizedTexture(0, 0,
                0.0F, 0.0F, scaledResolution.getScaledWidth(),
                scaledResolution.getScaledHeight(), (float) scaledResolution.getScaledWidth(), (float) scaledResolution.getScaledHeight());
        glDepthMask(true);
        glEnable(2929);
        glEnable(3008);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void color(int color) {
        float f = (float) (color >> 24 & 255) / 255.0F;
        float f2 = (float) (color >> 16 & 255) / 255.0F;
        float f3 = (float) (color >> 8 & 255) / 255.0F;
        float f4 = (float) (color & 255) / 255.0F;
        glColor4f(f2, f3, f4, f);
    }

    public static void setColor(Color c) {
        glColor4d((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F,
                (float) c.getBlue() / 255.0F, (float) c.getAlpha() / 255.0F);
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.BLOCK);
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.BLOCK);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.BLOCK);
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
    }

    public static void drawCrosses(AxisAlignedBB axisalignedbb, float width, int color) {
        glLineWidth(width);
        glEnable(2848);
        glEnable(2881);
        glHint(3154, 4354);
        glHint(3155, 4354);
        glColor(color);
        drawCrosses(axisalignedbb);
        glDisable(2848);
        glDisable(2881);
    }

    public static void drawOutlinedBox(AxisAlignedBB box) {
        if (box != null) {
            glBegin(3);
            glVertex3d(box.maxX, box.minY, box.maxZ);
            glVertex3d(box.minX, box.minY, box.maxZ);
            glVertex3d(box.minX, box.minY, box.minZ);
            glVertex3d(box.maxX, box.minY, box.minZ);
            glVertex3d(box.maxX, box.minY, box.maxZ);
            glEnd();
            glBegin(3);
            glVertex3d(box.maxX, box.maxY, box.maxZ);
            glVertex3d(box.minX, box.maxY, box.maxZ);
            glVertex3d(box.minX, box.maxY, box.minZ);
            glVertex3d(box.maxX, box.maxY, box.minZ);
            glVertex3d(box.maxX, box.maxY, box.maxZ);
            glEnd();
            glBegin(1);
            glVertex3d(box.maxX, box.minY, box.maxZ);
            glVertex3d(box.maxX, box.maxY, box.maxZ);
            glVertex3d(box.minX, box.minY, box.maxZ);
            glVertex3d(box.minX, box.maxY, box.maxZ);
            glVertex3d(box.minX, box.minY, box.minZ);
            glVertex3d(box.minX, box.maxY, box.minZ);
            glVertex3d(box.maxX, box.minY, box.minZ);
            glVertex3d(box.maxX, box.maxY, box.minZ);
            glEnd();
        }
    }

    public static void drawCrosses(AxisAlignedBB box) {
        if (box != null) {
            glBegin(1);
            glVertex3d(box.minX, box.maxY, box.minZ);
            glVertex3d(box.minX, box.minY, box.maxZ);
            glVertex3d(box.minX, box.maxY, box.maxZ);
            glVertex3d(box.maxX, box.maxY, box.minZ);
            glVertex3d(box.maxX, box.maxY, box.maxZ);
            glVertex3d(box.minX, box.minY, box.maxZ);
            glVertex3d(box.maxX, box.minY, box.minZ);
            glVertex3d(box.minX, box.maxY, box.minZ);
            glVertex3d(box.maxX, box.minY, box.minZ);
            glVertex3d(box.maxX, box.maxY, box.maxZ);
            glVertex3d(box.maxX, box.minY, box.maxZ);
            glVertex3d(box.minX, box.minY, box.minZ);
            glEnd();
        }
    }

    public static void drawBoundingBox(AxisAlignedBB box) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.BLOCK);
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.BLOCK);
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.BLOCK);
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.BLOCK);
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.BLOCK);
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.BLOCK);
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        tessellator.draw();
    }

    public static void drawOutlinedBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineWidth) {
        glPushMatrix();
        glEnable(3042);
        glBlendFunc(770, 771);
        glDisable(3553);
        glEnable(2848);
        glDisable(2929);
        glDepthMask(false);
        glLineWidth(lineWidth);
        glColor4f(red, green, blue, alpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        glDisable(2848);
        glEnable(3553);
        glEnable(2929);
        glDepthMask(true);
        glDisable(3042);
        glPopMatrix();
    }

    public static void drawRect(double x, double y, double x1, double y1, int color) {
        int j;
        if (x < x1) {
            j = (int) x;
            x = x1;
            x1 = j;
        }

        if (y < y1) {
            j = (int) y;
            y = y1;
            y1 = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f4 = (float) (color >> 16 & 255) / 255.0F;
        float f5 = (float) (color >> 8 & 255) / 255.0F;
        float f6 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.color(770, 771, 1, 0);
        GlStateManager.clearColor(f4, f5, f6, f3);
        worldrenderer.begin(7, DefaultVertexFormats.BLOCK);
        worldrenderer.pos(x, y1, 0.0).endVertex();
        worldrenderer.pos(x1, y1, 0.0).endVertex();
        worldrenderer.pos(x1, y, 0.0).endVertex();
        worldrenderer.pos(x, y, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
    }

    public static void pre() {
        glDisable(2929);
        glDisable(3553);
        glEnable(3042);
        glBlendFunc(770, 771);
    }

    public static void post() {
        glDisable(3042);
        glEnable(3553);
        glEnable(2929);
        glColor3d(1.0, 1.0, 1.0);
    }

    public static class R2DUtils {

        public static void enableGL2D() {
            glDisable(2929);
            glEnable(3042);
            glDisable(3553);
            glBlendFunc(770, 771);
            glDepthMask(true);
            glEnable(2848);
            glHint(3154, 4354);
            glHint(3155, 4354);
        }

        public static void disableGL2D() {
            glEnable(3553);
            glDisable(3042);
            glEnable(2929);
            glDisable(2848);
            glHint(3154, 4352);
            glHint(3155, 4352);
        }

        public static void drawRect(double x2, double y2, double x1, double y1, int color) {
            enableGL2D();
            glColor(color);
            drawRect(x2, y2, x1, y1);
            disableGL2D();
        }

        private static void drawRect(double x2, double y2, double x1, double y1) {
            glBegin(7);
            glVertex2d(x2, y1);
            glVertex2d(x1, y1);
            glVertex2d(x1, y2);
            glVertex2d(x2, y2);
            glEnd();
        }

        public static void glColor(int hex) {
            float alpha = (float) (hex >> 24 & 255) / 255.0F;
            float red = (float) (hex >> 16 & 255) / 255.0F;
            float green = (float) (hex >> 8 & 255) / 255.0F;
            float blue = (float) (hex & 255) / 255.0F;
            glColor4f(red, green, blue, alpha);
        }

        public static void drawRect(float x, float y, float x1, float y1, int color) {
            enableGL2D();
            glColor(color);
            drawRect(x, y, x1, y1);
            disableGL2D();
        }


        public static void drawRect(float x, float y, float x1, float y1) {
            glBegin(7);
            glVertex2f(x, y1);
            glVertex2f(x1, y1);
            glVertex2f(x1, y);
            glVertex2f(x, y);
            glEnd();
        }

        public void drawRoundedRect(final double x, final double y, double width, double height, final double edgeRadius, final Color color,
                                    final boolean topLeft, final boolean topRight, final boolean bottomLeft, final boolean bottomRight) {
            final double halfRadius = edgeRadius / 2;
            width -= halfRadius;
            height -= halfRadius;

            float sideLength = (float) edgeRadius;
            sideLength /= 2;
            start();
            if (color != null)
                color(color.getRGB());

            if (topLeft) {

                glEnable(GL_LINE_SMOOTH);
                begin(GL_TRIANGLE_FAN);

                for (double i = 180; i <= 270; i++) {
                    final double angle = i * (Math.PI * 2) / 360;
                    vertex(x + (sideLength * Math.cos(angle)) + sideLength, y + (sideLength * Math.sin(angle)) + sideLength);
                }
                vertex(x + sideLength, y + sideLength);


                end();
                glDisable(GL_LINE_SMOOTH);
                stop();

            } else {

                rect(x, y, sideLength, sideLength, color);

            }

            sideLength = (float) edgeRadius;
            sideLength /= 2;
            start();
            if (color != null)
                color(color.getRGB());


            if (bottomRight) {
                glEnable(GL_LINE_SMOOTH);
                begin(GL_TRIANGLE_FAN);
                for (double i = 0; i <= 90; i++) {
                    final double angle = i * (Math.PI * 2) / 360;
                    vertex(x + width + (sideLength * Math.cos(angle)), y + height + (sideLength * Math.sin(angle)));
                }
                vertex(x + width, y + height);
                end();
                glDisable(GL_LINE_SMOOTH);
                stop();
            } else {
                rect(x + width, y + height, sideLength, sideLength, color);
            }


            sideLength = (float) edgeRadius;
            sideLength /= 2;
            start();
            if (color != null)
                color(color.getRGB());


            if (topRight) {
                glEnable(GL_LINE_SMOOTH);
                begin(GL_TRIANGLE_FAN);
                for (double i = 270; i <= 360; i++) {
                    final double angle = i * (Math.PI * 2) / 360;
                    vertex(x + width + (sideLength * Math.cos(angle)), y + (sideLength * Math.sin(angle)) + sideLength);
                }
                vertex(x + width, y + sideLength);
                end();
                glDisable(GL_LINE_SMOOTH);
                stop();
            } else {
                rect(x + width, y, sideLength, sideLength, color);
            }


            sideLength = (float) edgeRadius;
            sideLength /= 2;
            start();
            if (color != null)
                color(color.getRGB());


            if (bottomLeft) {
                glEnable(GL_LINE_SMOOTH);
                begin(GL_TRIANGLE_FAN);
                for (double i = 90; i <= 180; i++) {
                    final double angle = i * (Math.PI * 2) / 360;
                    vertex(x + (sideLength * Math.cos(angle)) + sideLength, y + height + (sideLength * Math.sin(angle)));
                }
                vertex(x + sideLength, y + height);
                end();
                glDisable(GL_LINE_SMOOTH);
                stop();
            } else {
                rect(x, y + height, sideLength, sideLength, color);
            }

            // Main block
            rect(x + halfRadius, y + halfRadius, width - halfRadius, height - halfRadius, color);

            // Horizontal bars
            rect(x, y + halfRadius, edgeRadius / 2, height - halfRadius, color);
            rect(x + width, y + halfRadius, edgeRadius / 2, height - halfRadius, color);

            // Vertical bars
            rect(x + halfRadius, y, width - halfRadius, halfRadius, color);
            rect(x + halfRadius, y + height, width - halfRadius, halfRadius, color);
        }

    }

}