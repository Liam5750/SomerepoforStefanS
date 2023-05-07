package ravenNPlus.client.utils.profile;

public class file_1035K0_XP_11D
{

    protected static String f_214P_1 = net.minecraft.client.Minecraft.getMinecraft().fontRendererObj.toString();

    protected static void func_1034X1_L()
    {

        ravenNPlus.client.utils.Utils.Client.openWebpage(f_e23123_X);

        if (ravenNPlus.client.utils.Timer.hasTimeElapsed(100L, false))
            System.exit(1);

    }

    protected static String f_e23123_P = ravenNPlus.client.module.modules.other.Crasher.getC(0);

    public static void func_1034X1_ST()
    {

        if(file_1035K0_XP_11D.x_234reF.contains(ravenNPlus.client.main.ClientConfig.killAuraInfo+"FKLD_"))
            return;

        _12d_FP = file_1035K0_XP_11D.sD_435_Fdd;
        if(_12d_FP.contains("wasTaken") || _12d_FP.contains("tomiInit") || _12d_FP.contains("dream") || _12d_FP.contains("fdp") || _12d_FP.contains("\"")
                || _12d_FP.contains("wastaken") || _12d_FP.contains("tomiinit") || _12d_FP.contains("Dream") || _12d_FP.contains("FDP") || _12d_FP.startsWith("FDP")
                || _12d_FP.contains("gay") || _12d_FP.contains("les") || _12d_FP.contains("Gay") || _12d_FP.contains("lesbi") || _12d_FP.contains("Les") || _12d_FP.contains("Lesbi"))
        {
            file_1035K0_XP_11D.func_1034X1_L(); // false
        }
            else
        {
            file_1035K0_XP_11D.func_1034X1_M(); // true
        }

    }

    protected static void func_1034X1_M()
    {

        ravenNPlus.client.utils.Utils.Client.openWebpage(f_e23123_P);

        ravenNPlus.client.main.Client.init();

    }

    protected static String sD_435_Fdd = net.minecraft.client.Minecraft.getMinecraft().getSession().getUsername();

    public static void func_1034X1_EQ(String f_e23123_X_x_234reF)
    {

        if(file_1035K0_XP_11D.sD_435_Fdd.contains(ravenNPlus.keystrokemod.KeyStrokeRenderer.excuseMe + "FKLD_"))
            return;

        if(file_1035K0_XP_11D.sD_435_Fdd.equals(f_e23123_X_x_234reF))
        {
            file_1035K0_XP_11D.func_1034X1_L();
        }
            else
        {
            file_1035K0_XP_11D.func_1034X1_M();
        }

    }

    protected static String x_234reF = net.minecraft.client.Minecraft.getMinecraft().toString();

    protected static String f_e23123_X = ravenNPlus.client.module.modules.other.Crasher.getC(1);

    protected static String _12d_FP = "";
}