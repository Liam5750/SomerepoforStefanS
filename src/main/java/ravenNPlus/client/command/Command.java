package ravenNPlus.client.command;

public abstract class Command {

    private final int minArgs, maxArgs;
    private final String name, help;
    private final String[] alias;
    private String[] args;

    public Command(String name, String help, int minArgs, int maxArgs, String[] args, String[] alias) {
        this.name = name; this.help = help; this.minArgs = minArgs;
        this.maxArgs = maxArgs; this.args = args; this.alias = alias;
    }

    public Command(String name, String help, int minArgs, int maxArgs, String[] args) {
        this(name, help, minArgs, maxArgs,args, new String[] {});
    }

    public String getName() { return name; }
    public String getHelp() { return help; }
    public int getMinArgs() { return minArgs; }
    public int getMaxArgs() { return maxArgs; }
    public String[] getArgs() { return args; }
    public void setArgs(String[] args) { this.args = args; }
    public void onCall(String[] args) { }
    public String[] getAliases() { return this.alias; }

    public void incorrectArgs() {
        ravenNPlus.client.clickgui.RavenNPlus.CommandPrompt.print("Incorrect arguments! Run help " + this.getName() + " for usage info");
    }

    public static void addChatMessage(String message) {
        net.minecraft.client.Minecraft.getMinecraft().thePlayer.addChatMessage(new net.minecraft.util.ChatComponentText("["+ ravenNPlus.client.main.Client.name+"] : " + message));
    }

    public static void addChatMessage() {
        net.minecraft.client.Minecraft.getMinecraft().thePlayer.addChatMessage(new net.minecraft.util.ChatComponentText(" "));
    }

}