package ravenNPlus.client.utils;

import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import java.io.IOException;
import org.lwjgl.opengl.GL11;

public class BlurUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final ResourceLocation shader = new ResourceLocation("shaders/post/blur.json");
    public static BlurUtils instance;
    private ShaderGroup shaderGroup;
    private Framebuffer framebuffer;
    private int lastFactor;
    private int lastWidth;
    private int lastHeight;

    private Framebuffer getMainFrameBuffer() {
        return ObfuscationReflectionHelper.getPrivateValue(ShaderGroup.class, shaderGroup, "mainFramebuffer");
    }

    private Framebuffer getFramebuffer() {
        return ObfuscationReflectionHelper.getPrivateValue(ShaderGroup.class, shaderGroup, "getFramebuffer");
    }

    private void setValues(final float strength) {
        this.getFramebuffer().setFramebufferColor(255, 255, 255, strength);
    }

    public void init() {
        try {
            this.shaderGroup = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), shader);
            this.shaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            this.framebuffer = getMainFrameBuffer();
        } catch (final JsonSyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void blur(boolean mode) {
        if (mode) {
            Minecraft.getMinecraft().entityRenderer.loadShader(shader);
        } else {
            Minecraft.getMinecraft().entityRenderer.stopUseShader();
        }
    }

    public static void initFboAndShader() {
        try {
            Framebuffer buffer = new Framebuffer(mc.displayWidth, mc.displayHeight, false);
            buffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);

            ShaderGroup blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), buffer, shader);
            blurShader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public final void blurEria(final double x, final double y, final double areaWidth, final double areaHeight, final float blurStrength) {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);

        final int scaleFactor = scaledResolution.getScaleFactor();
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();

        if (sizeHasChanged(scaleFactor, width, height) || framebuffer == null || shaderGroup == null) {
            init();
        }

        this.lastFactor = scaleFactor;
        this.lastWidth = width;
        this.lastHeight = height;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.scissorBox((int) x, (int) y, (int) areaWidth, (int) areaHeight);
        framebuffer.bindFramebuffer(true);
        shaderGroup.loadShaderGroup(Utils.Client.getTimer().renderPartialTicks);
        setValues(blurStrength);
        mc.getFramebuffer().bindFramebuffer(false);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    private boolean sizeHasChanged(final int scaleFactor, final int width, final int height) {
        return (lastFactor != scaleFactor || lastWidth != width || lastHeight != height);
    }

}