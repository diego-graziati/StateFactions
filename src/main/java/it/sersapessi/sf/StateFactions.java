package it.sersapessi.sf;

import it.sersapessi.sf.commands.SFCommands;
import it.sersapessi.sf.eventhandlers.NonPlayerEvents;
import it.sersapessi.sf.eventhandlers.PlayerEvents;
import it.sersapessi.sf.utilities.Constants;
import it.sersapessi.sf.utilities.Database;
import it.sersapessi.sf.utilities.ExportedFilesRoutine;
import it.sersapessi.sf.utilities.TranslationManager;
import it.sersapessi.sf.utilities.models.PluginPlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * <code>StateFactions</code> is the first class loaded when the plugin actually starts to run. It's its "main" class.
 *
 * */
public final class StateFactions extends JavaPlugin {

    public static FileConfiguration config;
    public static TranslationManager translationManager;
    public static Database db;
    public static ArrayList<String> loggedInPlayers;
    private static ArrayList<PluginPlayer> onlinePlayers;

    public static PluginLogger logger;

    /**
     * @see JavaPlugin
     * */
    @Override
    public void onEnable() {
        // Plugin startup logic
        startupProcess();
        SFCommands commands = new SFCommands();
        Objects.requireNonNull(getCommand("sf")).setExecutor(commands);

        getServer().getPluginManager().registerEvents(new PlayerEvents(),this);
        getServer().getPluginManager().registerEvents(new NonPlayerEvents(),this);
    }

    /**
     * @see JavaPlugin
     * */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * <code>startupProcess</code> instantiates and initializes all the required processes and resources so that the plugin
     * can actually work properly.
     * */
    public void startupProcess(){
        logger = new PluginLogger(this);

        saveDefaultConfig();
        config = getConfig();

        //File export
        try {
            ExportedFilesRoutine.exportFiles();
        } catch (Exception e) {
            logger.log(new LogRecord(Level.INFO,"Files export procedure failed"));
            throw new RuntimeException(e);
        }

        logger.log(new LogRecord(Level.INFO,"Files exported"));

        //Lang selection
        String selectedLang;
        try {
            //It gets the selected language
            selectedLang=config.getString(Constants.Configs.LANG);
            if(selectedLang==null){
                selectedLang= Constants.Languages.FALLBACK;
            }
            translationManager = new TranslationManager(selectedLang);
        } catch (FileNotFoundException e) {
            //Fallback to EN_US to check if the language selected isn't supported.
            selectedLang=Constants.Languages.FALLBACK;
            try {
                translationManager = new TranslationManager(selectedLang);
            } catch (FileNotFoundException ex) {
                //Return an exception since the files have been corrupted.
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logger.log(new LogRecord(Level.INFO, translationManager.getString(Constants.Localization.Str.Log.LANG_LOGGER_CONFIG_STARTUP_OK)));

        //Database setups
        try {
            db = new Database(
                    Objects.requireNonNull(config.getString(Constants.Configs.DBMS)),
                    Objects.requireNonNull(config.getString(Constants.Configs.DB_PATH)),
                    Objects.requireNonNull((config.getString(Constants.Configs.DBMS_USER)!=null?config.getString(Constants.Configs.DBMS_USER):"")),
                    Objects.requireNonNull((config.getString(Constants.Configs.DBMS_PWD)!=null?config.getString(Constants.Configs.DBMS_PWD):"")));
            logger.log(new LogRecord(Level.INFO, translationManager.getString(Constants.Localization.Str.Log.Db.Startup.SUCCESS)));
        } catch (SQLException e) {
            logger.log(new LogRecord(Level.SEVERE,translationManager.getString(Constants.Localization.Str.Log.Db.Startup.SQL_EXCEPTION)));
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.log(new LogRecord(Level.SEVERE,translationManager.getString(Constants.Localization.Str.Log.Db.Startup.IO_EXCEPTION)));
            throw new RuntimeException(e);
        }

        logger.log(new LogRecord(Level.INFO,translationManager.getString(Constants.Localization.Str.Log.STARTUP_PROCESS_SUCCESSFUL)));

        //Logged In Players ArrayList<String> init
        loggedInPlayers = new ArrayList<>();

        logger.log(new LogRecord(Level.INFO,translationManager.getString(Constants.Localization.Str.Log.LOGGEDIN_PLAYERS_LIST_INIT)));

        //Online Players ArrayList<PluginPlayer> init
        onlinePlayers = new ArrayList<>();

        logger.log(new LogRecord(Level.INFO,translationManager.getString(Constants.Localization.Str.Log.ONLINE_PLAYERS_LIST_INIT)));
    }

    public static void addOnlinePlayer(PluginPlayer player){
        onlinePlayers.add(player);
    }

    public static PluginPlayer getPlayer(String playerName){
        for(PluginPlayer player : onlinePlayers){
            if(Objects.requireNonNull(player.getBukkitPlayer().getPlayer()).getName().equals(playerName)){
                return player;
            }
        }

        return null;
    }

    public static void removePlayer(String playerName){
        onlinePlayers.removeIf(player -> Objects.requireNonNull(player.getBukkitPlayer().getPlayer()).getName().equals(playerName));
    }
}
