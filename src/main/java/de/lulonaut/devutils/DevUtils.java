package de.lulonaut.devutils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Mod(modid = "devutils", useMetadata = true)
public class DevUtils {

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.config = new Configuration(event.getSuggestedConfigurationFile());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new ConfigCommand());


        File logsFolder = new File(Minecraft.getMinecraft().mcDataDir, "logs");
        if (logsFolder.exists()) {
            File[] logFiles = logsFolder.listFiles();
            if (logFiles != null) {
                for (File file : logFiles) {
                    //Delete all but the latest log
                    if (file.getName().endsWith("log.gz")) {
                        file.delete();
                    }
                }
            }
        }
        File crashReportsFolder = new File(Minecraft.getMinecraft().mcDataDir, "crash-reports");
        if (crashReportsFolder.exists()) {
            File[] reports = crashReportsFolder.listFiles();
            if (reports != null) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String formattedDate = dateFormat.format(new Date());
                for (File report : reports) {
                    //Delete all reports that are not from today
                    if (!report.getName().startsWith("crash-" + formattedDate)) {
                        report.delete();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu) {
            FMLClientHandler.instance().connectToServer(new GuiMultiplayer(Minecraft.getMinecraft().currentScreen), new ServerData("server", Config.getServer(), false));
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }
}
