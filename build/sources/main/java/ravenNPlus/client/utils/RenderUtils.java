package ravenNPlus.client.utils;

import jdk.nashorn.internal.ir.annotations.Ignore;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import ravenNPlus.client.module.modules.combat.NewAntiBot;
import ravenNPlus.client.utils.notifications.Notification;
import ravenNPlus.client.utils.notifications.Manager;
import ravenNPlus.client.utils.notifications.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.opengl.GL11;
import java.lang.reflect.Method;
import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import static java.lang.Math.*;
import static org.lwjgl.Sys.getTime;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL13.GL_SAMPLE_ALPHA_TO_COVERAGE;
import static ravenNPlus.client.utils.RoundedUtils.color;

public class RenderUtils {

    // icons -------------------------------------------------------------------------------------------------------------
    public static ResourceLocation icon = new ResourceLocation("ravenNPlus/icon.png");
    public static ResourceLocation icon_old = new ResourceLocation("ravenNPlus/old_icon.png");
    public static ResourceLocation confirm = new ResourceLocation("ravenNPlus/icon/true.png");
    public static ResourceLocation decline = new ResourceLocation("ravenNPlus/icon/false.png");
    public static ResourceLocation info = new ResourceLocation("ravenNPlus/icon/info.png");
    public static ResourceLocation lock = new ResourceLocation("ravenNPlus/icon/lock.png");
    public static ResourceLocation unlock = new ResourceLocation("ravenNPlus/icon/unlock.png");
    public static ResourceLocation menu = new ResourceLocation("ravenNPlus/icon/menu.png");
    public static ResourceLocation setting = new ResourceLocation("ravenNPlus/icon/settings.png");
    public static ResourceLocation speech = new ResourceLocation("ravenNPlus/icon/speech.png");
    public static ResourceLocation stringShadow = new ResourceLocation("ravenNPlus/shadow2.png");
    public static ResourceLocation circleShadow = new ResourceLocation("ravenNPlus/shadow1.png");
    public static ResourceLocation checkSliderBackground = new ResourceLocation("ravenNPlus/icon/modules/toggleback.png");
    public static ResourceLocation checkSliderForeground = new ResourceLocation("ravenNPlus/icon/modules/togglefront.png");
    public static Minecraft mc = Minecraft.getMinecraft();
    public static FontRenderer font = mc.fontRendererObj;
    public static ScaledResolution sc = new ScaledResolution(mc);
    public static int scWidth = sc.getScaledWidth();
    public static int scHight = sc.getScaledHeight();
    public static int off = 0;

    public static void push() {
        GL11.glPushMatrix();
    }

    public static void pop() {
        GL11.glPopMatrix();
    }

    public static void enable(final int glMode) {
        GL11.glEnable(glMode);
    }

    public static void disable(final int glMode) {
        GL11.glDisable(glMode);
    }

    public static void start() {
        enable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        disable(GL11.GL_TEXTURE_2D);
        disable(GL11.GL_CULL_FACE);
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
    }

    public static void stop() {
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        enable(GL11.GL_CULL_FACE);
        enable(GL11.GL_TEXTURE_2D);
        disable(GL11.GL_BLEND);
        color(Color.white.getRGB());
    }

    public static void lineWidth(final float width) {
        GL11.glLineWidth(width);
    }

    public static void vertex(final double x, final double y) {
        GL11.glVertex2d(x, y);
    }

    public static void translate(final double x, final double y) {
        GL11.glTranslated(x, y, 0);
    }

    public static void scale(final double x, final double y) {
        GL11.glScaled(x, y, 1);
    }

    public static void rotate(final double x, final double y, final double z, final double angle) {
        GL11.glRotated(angle, x, y, z);
    }

    public static void begin(final int glMode) {
        GL11.glBegin(glMode);
    }

    public static void end() {
        GL11.glEnd();
    }

    public static void stopDrawing() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void startDrawing() {
        GL11.glEnable(GL_BLEND);
        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        try {
            Method m = ReflectionHelper.findMethod(
                    EntityRenderer.class,
                    Minecraft.getMinecraft().entityRenderer,
                    new String[]{
                            "func_78479_a",
                            "setupCameraTransform"
                    },
                    float.class, int.class
            );

            m.setAccessible(true);
            m.invoke(Minecraft.getMinecraft().entityRenderer, Utils.Client.getTimer().renderPartialTicks, 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Color blend(Color color, Color color1, double d0) {
        float f = (float) d0;
        float f1 = 1.0F - f;
        float[] afloat = new float[3];
        float[] afloat1 = new float[3];

        color.getColorComponents(afloat);
        color1.getColorComponents(afloat1);

        return new Color(afloat[0] * f + afloat1[0] * f1, afloat[1] * f + afloat1[1] * f1, afloat[2] * f + afloat1[2] * f1);
    }

    public static void drawLine(final double firstX, final double firstY, final double secondX, final double secondY, final double lineWidth, final Color color) {
        start();
        if (color != null)
            color(color.getRGB());
        lineWidth((float) lineWidth);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        begin(GL11.GL_LINES);

        {
            vertex(firstX, firstY);
            vertex(secondX, secondY);
        }

        end();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        stop();
    }

    public static void drawRect(double d0, double d1, double d2, double d3, int i) {
        double d4;

        if (d0 < d2) {
            d4 = d0;
            d0 = d2;
            d2 = d4;
        }

        if (d1 < d3) {
            d4 = d1;
            d1 = d3;
            d3 = d4;
        }

        float f = (float) (i >> 24 & 255) / 255.0F;
        float f1 = (float) (i >> 16 & 255) / 255.0F;
        float f2 = (float) (i >> 8 & 255) / 255.0F;
        float f3 = (float) (i & 255) / 255.0F;
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wR = tess.getWorldRenderer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f1, f2, f3, f);
        wR.begin(7, DefaultVertexFormats.POSITION);
        wR.pos(d0, d3, 0.0D).endVertex();
        wR.pos(d2, d3, 0.0D).endVertex();
        wR.pos(d2, d1, 0.0D).endVertex();
        wR.pos(d0, d1, 0.0D).endVertex();
        tess.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawBorderedRect(float f, float f1, float f2, float f3, float f4, int i, int j) {
        drawRect(f, f1, f2, f3, j);
        float f5 = (float) (i >> 24 & 255) / 255.0F;
        float f6 = (float) (i >> 16 & 255) / 255.0F;
        float f7 = (float) (i >> 8 & 255) / 255.0F;
        float f8 = (float) (i & 255) / 255.0F;

        GL11.glEnable(GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        glPushMatrix();
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glLineWidth(f4);
        GL11.glBegin(1);
        GL11.glVertex2d(f, f1);
        GL11.glVertex2d(f, f3);
        GL11.glVertex2d(f2, f3);
        GL11.glVertex2d(f2, f1);
        GL11.glVertex2d(f, f1);
        GL11.glVertex2d(f2, f1);
        GL11.glVertex2d(f, f3);
        GL11.glVertex2d(f2, f3);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    public static void drawFilledBox(final AxisAlignedBB axis) {
        final Tessellator tess = Tessellator.getInstance();
        final WorldRenderer wR = tess.getWorldRenderer();

        wR.begin(7, DefaultVertexFormats.POSITION);
        wR.pos(axis.minX, axis.minY, axis.minZ).endVertex();
        wR.pos(axis.minX, axis.maxY, axis.minZ).endVertex();
        wR.pos(axis.maxX, axis.minY, axis.minZ).endVertex();
        wR.pos(axis.maxX, axis.maxY, axis.minZ).endVertex();
        wR.pos(axis.maxX, axis.minY, axis.maxZ).endVertex();
        wR.pos(axis.maxX, axis.maxY, axis.maxZ).endVertex();
        wR.pos(axis.minX, axis.minY, axis.maxZ).endVertex();
        wR.pos(axis.minX, axis.maxY, axis.maxZ).endVertex();
        wR.pos(axis.maxX, axis.maxY, axis.minZ).endVertex();
        wR.pos(axis.maxX, axis.minY, axis.minZ).endVertex();
        wR.pos(axis.minX, axis.maxY, axis.minZ).endVertex();
        wR.pos(axis.minX, axis.minY, axis.minZ).endVertex();
        wR.pos(axis.minX, axis.maxY, axis.maxZ).endVertex();
        wR.pos(axis.minX, axis.minY, axis.maxZ).endVertex();
        wR.pos(axis.maxX, axis.maxY, axis.maxZ).endVertex();
        wR.pos(axis.maxX, axis.minY, axis.maxZ).endVertex();
        wR.pos(axis.minX, axis.maxY, axis.minZ).endVertex();
        wR.pos(axis.maxX, axis.maxY, axis.minZ).endVertex();
        wR.pos(axis.maxX, axis.maxY, axis.maxZ).endVertex();
        wR.pos(axis.minX, axis.maxY, axis.maxZ).endVertex();
        wR.pos(axis.minX, axis.maxY, axis.minZ).endVertex();
        wR.pos(axis.minX, axis.maxY, axis.maxZ).endVertex();
        wR.pos(axis.maxX, axis.maxY, axis.maxZ).endVertex();
        wR.pos(axis.maxX, axis.maxY, axis.minZ).endVertex();
        wR.pos(axis.minX, axis.minY, axis.minZ).endVertex();
        wR.pos(axis.maxX, axis.minY, axis.minZ).endVertex();
        wR.pos(axis.maxX, axis.minY, axis.maxZ).endVertex();
        wR.pos(axis.minX, axis.minY, axis.maxZ).endVertex();
        wR.pos(axis.minX, axis.minY, axis.minZ).endVertex();
        wR.pos(axis.minX, axis.minY, axis.maxZ).endVertex();
        wR.pos(axis.maxX, axis.minY, axis.maxZ).endVertex();
        wR.pos(axis.maxX, axis.minY, axis.minZ).endVertex();
        wR.pos(axis.minX, axis.minY, axis.minZ).endVertex();
        wR.pos(axis.minX, axis.maxY, axis.minZ).endVertex();
        wR.pos(axis.minX, axis.minY, axis.maxZ).endVertex();
        wR.pos(axis.minX, axis.maxY, axis.maxZ).endVertex();
        wR.pos(axis.maxX, axis.minY, axis.maxZ).endVertex();
        wR.pos(axis.maxX, axis.maxY, axis.maxZ).endVertex();
        wR.pos(axis.maxX, axis.minY, axis.minZ).endVertex();
        wR.pos(axis.maxX, axis.maxY, axis.minZ).endVertex();
        wR.pos(axis.minX, axis.maxY, axis.maxZ).endVertex();
        wR.pos(axis.minX, axis.minY, axis.maxZ).endVertex();
        wR.pos(axis.minX, axis.maxY, axis.minZ).endVertex();
        wR.pos(axis.minX, axis.minY, axis.minZ).endVertex();
        wR.pos(axis.maxX, axis.maxY, axis.minZ).endVertex();
        wR.pos(axis.maxX, axis.minY, axis.minZ).endVertex();
        wR.pos(axis.maxX, axis.maxY, axis.maxZ).endVertex();
        wR.pos(axis.maxX, axis.minY, axis.maxZ).endVertex();
        tess.draw();
    }

    public static void drawEasy(boolean pre) {
        if (pre) {
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0F, -1100000.0F);
        } else {
            GL11.glDisable(32823);
            GL11.glPolygonOffset(1.0F, 1100000.0F);
        }
    }

    public static int drawStringStringInteger = 0;
    public static int drawStringHUDInteger = 0;

    public static void drawStringHUD_Disable(double range, boolean sortBots, int x) {
        if (!Utils.Player.isPlayerInGame()) return;

        List<Entity> targets = (List<Entity>) mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
        targets = targets.stream().filter(entity -> /*entity.getDistanceToEntity(mc.thePlayer) < range &&*/ entity != mc.thePlayer && !entity.isDead && ((EntityLivingBase) entity).getHealth() > 0).collect(Collectors.toList());
        targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase) entity).getDistanceToEntity(mc.thePlayer)));
        if (targets.isEmpty()) return;
        EntityLivingBase target = (EntityLivingBase) targets.get(0);

        if (sortBots && NewAntiBot.isBot(target))
            return;

        if (mc.thePlayer.getDistanceToEntity(target) < range) {
            drawStringHUDInteger = x;
            drawStringStringInteger = 0;
        }
    }

    static int border;
    public static void drawStringHUD(int x, int y, int range, boolean background, boolean cl, boolean head, boolean sortBots) {
        if (!Utils.Player.isPlayerInGame()) return;

        List<Entity> targets = mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
        targets = targets.stream().filter(entity -> /*entity.getDistanceToEntity(mc.thePlayer) < range &&*/ entity != mc.thePlayer && !entity.isDead && ((EntityLivingBase) entity).getHealth() > 0).collect(Collectors.toList());
        targets.sort(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer)));
        if (targets.isEmpty()) return;
        EntityLivingBase target = (EntityLivingBase) targets.get(0);

        if (sortBots && NewAntiBot.isBot(target))
            return;

        if (Utils.Player.isPlayerInContainer())
            return;

        if (background && !head && drawStringHUDInteger != x + 2 && drawStringHUDInteger > x + 1)
            RoundedUtils.drawSmoothRoundedRect(x - 3, y - 3, drawStringHUDInteger - 10, y + 22, 3, ColorUtil.color_int_min);

        if (background && head && drawStringHUDInteger != x + 2 && drawStringHUDInteger > x + 1)
            RoundedUtils.drawSmoothRoundedRect(x - 22, y - 3, drawStringHUDInteger - 10, y + 22, 3, ColorUtil.color_int_min);

        if (mc.thePlayer.getDistanceToEntity(target) > range) {
            if (background && drawStringHUDInteger != x + 2 && drawStringHUDInteger > x + 1)
                RenderUtils.drawStringHUDInteger--; // --;

            RenderUtils.drawStringStringInteger = 0;
        } else {

            if (sortBots && NewAntiBot.isBot(target))
                return;

            FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
            Utils.HUD.fontRender.drawString("", 0, 0, 0xFFFFFFFF);

            if (drawStringHUDInteger < fr.getStringWidth(target.getName()) + x + 60)
                drawStringHUDInteger++;
            else
                drawStringHUDInteger--;

            if (cl)
                Utils.HUD.fontRender.drawString("", 0, 0, 0xFFFFFFFF);

            if (!background)
                drawStringStringInteger = 110;

            if (drawStringStringInteger < 110)
                drawStringStringInteger++;

            if (drawStringStringInteger == 110) {
                float targetHealth = target.getHealth();
                int heartColor;
                if (targetHealth < 1) {
                    heartColor = new Color(238, 0, 0).getRGB();
                } else if (targetHealth < 2) {
                    heartColor = new Color(215, 25, 0).getRGB();
                } else if (targetHealth < 3) {
                    heartColor = new Color(203, 37, 0).getRGB();
                } else if (targetHealth < 4) {
                    heartColor = new Color(192, 49, 0).getRGB();
                } else if (targetHealth < 5) {
                    heartColor = new Color(181, 61, 0).getRGB();
                } else if (targetHealth < 6) {
                    heartColor = new Color(170, 74, 0).getRGB();
                } else if (targetHealth < 7) {
                    heartColor = new Color(158, 86, 0).getRGB();
                } else if (targetHealth < 8) {
                    heartColor = new Color(147, 98, 0).getRGB();
                } else if (targetHealth < 9) {
                    heartColor = new Color(136, 110, 0).getRGB();
                } else if (targetHealth < 10) {
                    heartColor = new Color(124, 122, 0).getRGB();
                } else if (targetHealth < 11) {
                    heartColor = new Color(113, 134, 0).getRGB();
                } else if (targetHealth < 12) {
                    heartColor = new Color(102, 146, 0).getRGB();
                } else if (targetHealth < 13) {
                    heartColor = new Color(90, 158, 0).getRGB();
                } else if (targetHealth < 14) {
                    heartColor = new Color(79, 170, 0).getRGB();
                } else if (targetHealth < 15) {
                    heartColor = new Color(68, 182, 0).getRGB();
                } else if (targetHealth < 16) {
                    heartColor = new Color(56, 194, 0).getRGB();
                } else if (targetHealth < 17) {
                    heartColor = new Color(24, 219, 0).getRGB();
                } else if (targetHealth < 18) {
                    heartColor = new Color(23, 231, 0).getRGB();
                } else if (targetHealth < 19) {
                    heartColor = new Color(11, 243, 0).getRGB();
                } else if (targetHealth < 20) {
                    heartColor = new Color(0, 255, 0).getRGB();
                } else heartColor = new Color(0, 255, 0).getRGB();

                fr.drawString("Target : ", x, y, 0xFFFFFFFF);
                fr.drawString(target.getName(), x + fr.getStringWidth("Target : ") + 2, y, Color.red.getRGB());

                fr.drawString("Health : ", x + 1, y + 10, 0xFFFFFFFF);    ///            - 1

                DecimalFormat decimalTargetHealth = new DecimalFormat("#.##");
                decimalTargetHealth.setRoundingMode(RoundingMode.CEILING);
                for (Number n : Collections.singletonList(targetHealth)) {
                    Double d = n.doubleValue();

                    fr.drawString("" + decimalTargetHealth.format(d), x + fr.getStringWidth("Health : ") + 2, y + 10, heartColor);
                }

                if (border > drawStringHUDInteger - 10)
                    border = -9999;

                if (background && head)
                    RenderUtils.drawHead(target, x - 20, y - 1, 18, 19);

                if (background && !head)
                    Gui.drawRect((x + ((int) targetHealth * 4)), y + 19, x - 2, y + 21, heartColor);

                if (background && head)
                    Gui.drawRect((x + (int) targetHealth * 4), y + 19, x - 21, y + 21, heartColor);
            }
        }
    }

    public static void drawHead(Entity en, int x, int y, int width, int height) {
        GL11.glColor4f(1F, 1F, 1F, 1F);

        if (NewAntiBot.isBot(en))
            return;

        assert ((EntityPlayerSP) en).hasSkin();
        if (en instanceof EntityPlayer) {
            if (mc.getNetHandler().getPlayerInfo(en.getName()).hasLocationSkin()) {
                ResourceLocation skin = mc.getNetHandler().getPlayerInfo(en.getName()).getLocationSkin();
                if (skin != null || skin.getResourcePath() != null || skin.getResourceDomain() != null) {
                    mc.renderEngine.bindTexture(skin);
                    RenderUtils.drawScaledCustomSizeModalRect(x, y, 8F, 8F, 8, 8, width, height, 64F, 64F);
                }
            }
        }
    }

    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wR = tess.getWorldRenderer();
        wR.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        wR.pos(x, y + height, 0.0D).tex(u * f, (v + (float) vHeight) * f1).endVertex();
        wR.pos(x + width, y + height, 0.0D).tex((u + (float) uWidth) * f, (v + (float) vHeight) * f1).endVertex();
        wR.pos(x + width, y, 0.0D).tex((u + (float) uWidth) * f, v * f1).endVertex();
        wR.pos(x, y, 0.0D).tex(u * f, v * f1).endVertex();
        tess.draw();
    }

    public static void drawCircle(float x, float y, float radius, int start, int end) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        GL11.glColor4d(Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue(), Color.WHITE.getAlpha());

        glEnable(GL_LINE_SMOOTH);
        glLineWidth(2F);
        glBegin(GL_LINE_STRIP);
        for (float i = end; i >= start; i -= (360 / 90.0f))
            glVertex2f((float) (x + (cos(i * PI / 180) * (radius * 1.001F))), (float) (y + (sin(i * PI / 180) * (radius * 1.001F))));
        glEnd();
        glDisable(GL_LINE_SMOOTH);

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawLoadingCircle(float x, float y) {
        for (int i = 0; i < 4; i++) {
            int rot = (int) ((System.nanoTime() / 5000000 * i) % 360);
            drawCircle(x, y, i * 10, rot - 180, rot);
        }
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wR = tess.getWorldRenderer();
        wR.begin(3, DefaultVertexFormats.POSITION);
        wR.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        wR.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        wR.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        wR.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        wR.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tess.draw();
        wR.begin(3, DefaultVertexFormats.POSITION);
        wR.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        wR.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        wR.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        wR.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        wR.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tess.draw();
        wR.begin(1, DefaultVertexFormats.POSITION);
        wR.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        wR.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        wR.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        wR.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        wR.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        wR.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        wR.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        wR.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tess.draw();
    }

    public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wR = tess.getWorldRenderer();
        wR.begin(7, DefaultVertexFormats.POSITION);
        wR.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        wR.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        wR.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        wR.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        wR.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        wR.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        wR.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        wR.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tess.draw();
        wR.begin(7, DefaultVertexFormats.POSITION);
        wR.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        wR.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        wR.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        wR.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        wR.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        wR.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        wR.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        wR.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tess.draw();
        wR.begin(7, DefaultVertexFormats.POSITION);
        wR.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        wR.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        wR.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        wR.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        wR.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        wR.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        wR.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        wR.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tess.draw();
        wR.begin(7, DefaultVertexFormats.POSITION);
        wR.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        wR.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        wR.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        wR.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        wR.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        wR.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        wR.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        wR.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        tess.draw();
        wR.begin(7, DefaultVertexFormats.POSITION);
        wR.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        wR.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        wR.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        wR.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        wR.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        wR.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        wR.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        wR.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tess.draw();
        wR.begin(7, DefaultVertexFormats.POSITION);
        wR.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        wR.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        wR.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        wR.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        wR.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        wR.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        wR.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        wR.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tess.draw();
    }

    public static void notification(Type type, String title, String message, int length) {
        Manager.show(new Notification(type, title, message, length));
    }

    public static void draw2DImage(ResourceLocation image, int x, int y, int width, int height, Color c) {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDepthMask(false);
        try {
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha());
            mc.getTextureManager().bindTexture(image);
            Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
            draw2DImageByStringIsCon = false;
        } catch (Exception e) {
            e.printStackTrace();
            draw2DImageByStringIsCon = true;
        }
        //  mc.getTextureManager().bindTexture(null);
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        checkForError_LoadingIcons();
    }

    static boolean draw2DImageByStringIsCon = false;
    public static void draw2DImageByString(String image, int x, int y, int width, int height, Color c) {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDepthMask(false);
        try {
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha());
            ResourceLocation img = new ResourceLocation(image);
            mc.renderEngine.bindTexture(img);
            Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
            draw2DImageByStringIsCon = false;
        } catch (Exception e) {
            draw2DImageByStringIsCon = true;
            e.printStackTrace();
        }
        //  mc.getTextureManager().bindTexture(null);
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        checkForError_LoadingIcons();
    }

    public static boolean checkForError_LoadingIcons() {
        if (draw2DImageByStringIsCon) {
            font.drawString("Some Icons didn't loaded right", 5, 5, ColorUtil.color_red);
            return true;
        }

        return false;
    }

    protected static void hoverAnimation(int x, int y, int width, int height, int mouseX, int mouseY) {
        if (isHovered(x, y, width, height, mouseX, mouseY)) {
            if (off < 5)
                off++;
        } else {
            if (off > 0)
                off--;
        }
    }

    public static void drawHover2DImageByString(String image, int x, int y, int width, int height, int mouseX, int mouseY, Color c) {
        RenderUtils.hoverAnimation(x, y, width, height, mouseX, mouseY);

        if (off > 0) {
            RenderUtils.draw2DImage(new ResourceLocation(image), x - off, y - off, width + off * 2, height + off * 2, c);
        } else {
            RenderUtils.draw2DImage(new ResourceLocation(image), x, y, width, height, c);
        }
    }

    public static boolean isHoveringBound(float mouseX, float mouseY, float x, float y, float width, float height) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public static boolean isHovered(double x, double y, double width, double height, int mouseX, int mouseY) {
        return mouseX > x && mouseY > y && mouseX < width && mouseY < height;
    }

    public static void renderItemStack(ItemStack is, int xPos, int yPos, boolean dura) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().zLevel = -150.0F;
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(is, xPos, yPos);
        Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, is, xPos, yPos);
        Minecraft.getMinecraft().getRenderItem().zLevel = 0.0F;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5D, 0.5D, 0.5D);
        GlStateManager.disableDepth();
        renderEnchantText(is, xPos, yPos, dura);
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        GlStateManager.popMatrix();
    }

    public static void drawAnimatedRect(String text, int width, int height, int fadedIn, int fadeOut, int end) {
        // width = 45;  | 120
        // height = 30; |  30

        long time = getTime();
        double offset;
        if (time < fadedIn) {
            offset = Math.tanh(time / (double) (fadedIn) * 3.0) * width;
        } else if (time > fadeOut) {
            offset = (Math.tanh(3.0 - (time - fadeOut) / (double) (end - fadeOut) * 3.0) * width);
        } else {
            offset = width;
        }

        drawRect(scHight - offset, scHight - 5 - height, scWidth, scHight - 5, -1);
        drawRect(scWidth - offset, scHight - 5 - height, scWidth - offset + 4, scHight - 5, -1);

        font.drawString(text, (int) (scWidth - offset + 8), scHight - 2 - height, -1);
        int xBegin = (int) (scWidth - offset + 8);
        int yBegin = scHight - 22;
        int xEnd = xBegin + scWidth;
        int yEnd = yBegin + 1;
        drawRect(xBegin, yBegin, xEnd, yEnd, new Color(-1).getRGB());
    }

    public static void renderEnchantText(ItemStack is, int xPos, int yPos, boolean dura) {
        int newYPos = yPos - 24;
        if (is.getEnchantmentTagList() != null && is.getEnchantmentTagList().tagCount() >= 6) {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("god", (float) (xPos * 2), (float) newYPos, 16711680);
        } else {
            if (is.getItem() instanceof ItemArmor) {
                int protection = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, is);
                int projectileProtection = EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, is);
                int blastProtectionLvL = EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, is);
                int fireProtection = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, is);
                int thornsLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, is);
                int unbreakingLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, is);
                int remainingDurability = is.getMaxDamage() - is.getItemDamage();
                if (dura) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(String.valueOf(remainingDurability), (float) (xPos * 2), (float) yPos, 16777215);
                }

                if (protection > 0) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("prot" + protection, (float) (xPos * 2), (float) newYPos, -1);
                    newYPos += 8;
                }

                if (projectileProtection > 0) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("proj" + projectileProtection, (float) (xPos * 2), (float) newYPos, -1);
                    newYPos += 8;
                }

                if (blastProtectionLvL > 0) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("bp" + blastProtectionLvL, (float) (xPos * 2), (float) newYPos, -1);
                    newYPos += 8;
                }

                if (fireProtection > 0) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("frp" + fireProtection, (float) (xPos * 2), (float) newYPos, -1);
                    newYPos += 8;
                }

                if (thornsLvl > 0) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("th" + thornsLvl, (float) (xPos * 2), (float) newYPos, -1);
                    newYPos += 8;
                }

                if (unbreakingLvl > 0) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("unb" + unbreakingLvl, (float) (xPos * 2), (float) newYPos, -1);
                    newYPos += 8;
                }
            }

            if (is.getItem() instanceof ItemBow) {
                int powerLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, is);
                int punchLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, is);
                int flameLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, is);
                int unbreakingLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, is);
                if (powerLvl > 0) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("pow" + powerLvl, (float) (xPos * 2), (float) newYPos, -1);
                    newYPos += 8;
                }

                if (punchLvl > 0) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("pun" + punchLvl, (float) (xPos * 2), (float) newYPos, -1);
                    newYPos += 8;
                }

                if (flameLvl > 0) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("flame" + flameLvl, (float) (xPos * 2), (float) newYPos, -1);
                    newYPos += 8;
                }

                if (unbreakingLvl > 0) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("unb" + unbreakingLvl, (float) (xPos * 2), (float) newYPos, -1);
                    newYPos += 8;
                }
            }

            if (is.getItem() instanceof ItemSword) {
                int sharpnessLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, is);
                int knockbackLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, is);
                int fireAspectLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, is);
                int unbreakingLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, is);
                if (sharpnessLvl > 0) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("sh" + sharpnessLvl, (float) (xPos * 2), (float) newYPos, -1);
                    newYPos += 8;
                }

                if (knockbackLvl > 0) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("kb" + knockbackLvl, (float) (xPos * 2), (float) newYPos, -1);
                    newYPos += 8;
                }

                if (fireAspectLvl > 0) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("fire" + fireAspectLvl, (float) (xPos * 2), (float) newYPos, -1);
                    newYPos += 8;
                }

                if (unbreakingLvl > 0) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("unb" + unbreakingLvl, (float) (xPos * 2), (float) newYPos, -1);
                }
            }

            if (is.getItem() instanceof ItemTool) {
                int unbreakingLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, is);
                int efficiencyLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, is);
                int fortuneLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, is);
                int silkTouchLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, is);
                if (efficiencyLvl > 0) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("eff" + efficiencyLvl, (float) (xPos * 2), (float) newYPos, -1);
                    newYPos += 8;
                }

                if (fortuneLvl > 0) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("fo" + fortuneLvl, (float) (xPos * 2), (float) newYPos, -1);
                    newYPos += 8;
                }

                if (silkTouchLvl > 0) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("silk" + silkTouchLvl, (float) (xPos * 2), (float) newYPos, -1);
                    newYPos += 8;
                }

                if (unbreakingLvl > 0) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("ub" + unbreakingLvl, (float) (xPos * 2), (float) newYPos, -1);
                }
            }

            if (is.getItem() == Items.golden_apple && is.hasEffect()) {
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("god", (float) (xPos * 2), (float) newYPos, -1);
            }
        }
    }

    public static void drawOutlinedEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glColor4f(red, green, blue, 0.20F);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void scissorBox(int x, int y, int xend, int yend) {
        int width = xend - x;
        int height = yend - y;
        int factor = sc.getScaleFactor();
        int bottomY = Minecraft.getMinecraft().currentScreen.height - yend;
        glScissor(x * factor, bottomY * factor, width * factor, height * factor);
    }

    public static int interpolateColor(int rgba1, int rgba2, float percent) {
        int r1 = rgba1 & 0xFF, g1 = rgba1 >> 8 & 0xFF, b1 = rgba1 >> 16 & 0xFF, a1 = rgba1 >> 24 & 0xFF;
        int r2 = rgba2 & 0xFF, g2 = rgba2 >> 8 & 0xFF, b2 = rgba2 >> 16 & 0xFF, a2 = rgba2 >> 24 & 0xFF;

        int r = (int) (r1 < r2 ? r1 + (r2 - r1) * percent : r2 + (r1 - r2) * percent);
        int g = (int) (g1 < g2 ? g1 + (g2 - g1) * percent : g2 + (g1 - g2) * percent);
        int b = (int) (b1 < b2 ? b1 + (b2 - b1) * percent : b2 + (b1 - b2) * percent);
        int a = (int) (a1 < a2 ? a1 + (a2 - a1) * percent : a2 + (a1 - a2) * percent);

        return r | g << 8 | b << 16 | a << 24;
    }

    public static void setupLineSmooth() {
        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_TEXTURE_2D);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_MULTISAMPLE);
        glEnable(GL_SAMPLE_ALPHA_TO_COVERAGE);
        glShadeModel(GL_SMOOTH);
    }

    public static void drawLine(double startX, double startY, double startZ, double endX, double endY, double endZ, float thickness) {
        glPushMatrix();
        setupLineSmooth();
        glLineWidth(thickness);
        glBegin(GL_LINES);
        glVertex3d(startX, startY, startZ);
        glVertex3d(endX, endY, endZ);
        glEnd();
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_LIGHTING);
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_MULTISAMPLE);
        glDisable(GL_SAMPLE_ALPHA_TO_COVERAGE);
        glPopMatrix();
    }

    public static int getRainbow(int speed, int offset) {
        float hue = (System.currentTimeMillis() + offset) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, 1f, 1f).getRGB();
    }

    public static List getEntitiesWithinAABB(AxisAlignedBB axisalignedBB) {
        ArrayList list = new ArrayList();
        int chunkMinX = MathHelper.floor_double(((axisalignedBB.minX - 2.0) / 16.0));
        int chunkMaxX = MathHelper.floor_double(((axisalignedBB.maxX + 2.0) / 16.0));
        int chunkMinZ = MathHelper.floor_double(((axisalignedBB.minZ - 2.0) / 16.0));
        int chunkMaxZ = MathHelper.floor_double(((axisalignedBB.maxZ + 2.0) / 16.0));
        for (int x = chunkMinX; x <= chunkMaxX; ++x) {
            for (int z = chunkMinZ; z <= chunkMaxZ; ++z) {
                if (!mc.theWorld.getChunkProvider().chunkExists(x, z)) {
                    continue;
                }
                mc.theWorld.getChunkFromChunkCoords(x, z).getEntitiesWithinAABBForEntity(mc.thePlayer, axisalignedBB, list, null);
            }
        }
        return list;
    }

    public static void drawCheckSlider(int x, int y, int width, int height, boolean enabled, int offset, Color foregroundColor, Color backGroundColor) {
        if (!enabled) {
            RenderUtils.draw2DImage(checkSliderBackground, x, y - 1, width, height, backGroundColor);
            RenderUtils.draw2DImage(checkSliderForeground, x - offset, y, width - 2, height - 2, foregroundColor);
        } else {
            RenderUtils.draw2DImage(checkSliderBackground, x, y - 1, width, height, backGroundColor);
            RenderUtils.draw2DImage(checkSliderForeground, x + offset, y, width - 2, height - 2, foregroundColor);
        }

        font.drawString("", 0, 0, 0xffffff, false);
    }

    public static void drawCheckSlider(int x, int y, boolean enabled, Color foregroundColor, Color backGroundColor) {
        if (!enabled) {
            RenderUtils.draw2DImage(checkSliderBackground, x, y - 1, 10, 10, backGroundColor);
            RenderUtils.draw2DImage(checkSliderForeground, x - 2, y, 8, 10, foregroundColor);
        } else {
            RenderUtils.draw2DImage(checkSliderBackground, x, y - 1, 10, 10, backGroundColor);
            RenderUtils.draw2DImage(checkSliderForeground, x + 2, y, 8, 10, foregroundColor);
        }

        font.drawString("", 0, 0, 0xffffff, false);
    }

    public static void rect(final double x, final double y, final double width, final double height, final boolean filled, final Color color) {
        start();
        if (color != null)
            color(color.getRGB());
        begin(filled ? GL11.GL_TRIANGLE_FAN : GL11.GL_LINES);

        {
            vertex(x, y);
            vertex(x + width, y);
            vertex(x + width, y + height);
            vertex(x, y + height);
            if (!filled) {
                vertex(x, y);
                vertex(x, y + height);
                vertex(x + width, y);
                vertex(x + width, y + height);
            }
        }
        end();
        stop();
    }

    public static void rect(final double x, final double y, final double width, final double height, final Color color) {
        rect(x, y, width, height, true, color);
    }

    public static void renderFriends(Entity en, int mode, int rgb, boolean damage) {
        if (Utils.FriendUtils.isAFriend(en) && (!NewAntiBot.isBot(en)))
            Utils.HUD.drawBoxAroundEntity(en, mode, 1D, 1D, rgb, damage);
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------
    // Finnaly Working animations thx to KV

    public static class AnimateRects {

        public static CoolDown coolDown;

        public static void drawAnimatedRect_TopBottom(int x, int y, int width, int height, int color) {
            float tPercent = (float) (coolDown.getElapsedTime() / coolDown.getCooldownTime());
            Gui.drawRect(x, y, x + width, y + (int) (height * tPercent), color);
        }

        public static void drawAnimatedRect_LeftRight(int x, int y, int width, int height, int color) {
            float tPercent = (float) (coolDown.getElapsedTime() / coolDown.getCooldownTime());
            Gui.drawRect(x, y, (int) (x + (width * tPercent)), y + height, color);
        }

        public static void drawAnimatedRect_RightLeft(int x, int y, int width, int height, int color) {
            float tPercent = (float) (coolDown.getElapsedTime() / coolDown.getCooldownTime());
            Gui.drawRect(x, y, (int) (x + (width / tPercent)), y + height, color);
        }

        public static void setCooldown(long animationLength) {
            coolDown.setCooldown(animationLength);
        }

        public static void startCooldown() {
            coolDown.start();
        }

    }

    //---------------------------------------------------------------------------------------------------------------------------------------------

}