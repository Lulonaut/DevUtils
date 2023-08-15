package de.lulonaut.devutils;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.List;

public class ConfigCommand extends CommandBase {
    private final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public String getCommandName() {
        return "autojoin";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/autojoin <set/toggle>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }

    public void printUsage() {
        mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Usage: /autojoin <set/toggle>"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return Arrays.asList("set", "toggle");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (mc.isSingleplayer()) {
            mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "This command can't be used in Singleplayer"));
        } else {
            if (args.length == 1) {
                if (args[0].equals("set")) {
                    String host = mc.getCurrentServerData().serverIP;
                    Config.setServer(host);
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "[DevUtils] " + EnumChatFormatting.BLUE + "Autojoin server is now set to: " + EnumChatFormatting.GOLD + host));
                    return;
                } else if (args[0].equals("toggle")) {
                    boolean currentState = Config.getToggle();
                    Config.setToggle(!currentState);
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "[DevUtils] " + EnumChatFormatting.BLUE + "Autojoin is now " + (currentState ? EnumChatFormatting.RED + "disabled" : EnumChatFormatting.GOLD + "enabled")));
                    return;
                }
            }
            printUsage();
        }
    }
}