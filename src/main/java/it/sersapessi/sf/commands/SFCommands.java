package it.sersapessi.sf.commands;

import it.sersapessi.sf.StateFactions;
import it.sersapessi.sf.utilities.Constants;
import it.sersapessi.sf.utilities.models.ClaimRegion;
import it.sersapessi.sf.utilities.models.ClaimSector;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * <code>SFCommands</code> handles all the commands that can be found inside the SF Plugin.
 * */
public class SFCommands implements CommandExecutor {

    /**
     * It's inside <code>onCommand</code> that the command parsing happens.
     * */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {

        StateFactions.logger.log(new LogRecord(Level.INFO,"Args length: "+args.length));//TODO: rimuovere

        if(checkIllegalChars(args)){
            sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+Constants.Localization.Str.Command.Error.ILLEGAL_CHAR);
        }else{
            ArrayList<String> actualArgs = parseCommandArgs(args);

            StateFactions.logger.log(new LogRecord(Level.INFO,"Actual args: "+actualArgs.size()+" \""+actualArgs+"\""));//TODO: rimuovere
            if(actualArgs.size()>=2){
                if(actualArgs.get(0).equalsIgnoreCase(Constants.CommandsArgs.STATE)){
                    parseStateCommands(sender, actualArgs);
                }else if(actualArgs.get(0).equalsIgnoreCase(Constants.CommandsArgs.REGISTER)){
                    executeRegister(sender, actualArgs);
                }else if(actualArgs.get(0).equalsIgnoreCase(Constants.CommandsArgs.LOGIN)){
                    executeLogIn(sender, actualArgs);
                }else if(actualArgs.get(0).equalsIgnoreCase(Constants.CommandsArgs.FACTION)){

                }

            }
        }

        return true;
    }

    private void parseStateCommands(@NotNull CommandSender sender, @NotNull ArrayList<String> args){
        String stateName= args.get(1);
        if(args.get(2).equalsIgnoreCase(Constants.CommandsArgs.CREATE) && args.size()==3){
            if(StateFactions.loggedInPlayers.contains(sender.getName())){
                if(!StateFactions.db.checkIfStateExists(stateName)){
                    StateFactions.db.createState(stateName,sender.getName());
                    sender.sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.STATE_CREATED)+stateName);
                }else{
                    sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.STATE_ALREADY_EXISTS));
                }
            }else{
                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_LOGGEDIN));
            }
        }else if(args.get(2).equalsIgnoreCase(Constants.CommandsArgs.CLAIM)){ //TODO: implements second claim possibility

            if(args.size()==3){
                if(StateFactions.db.checkIfStateExists(stateName)){
                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){
                            ClaimRegion region = new ClaimRegion(new ClaimSector(p.getLocation().getBlockX(),p.getLocation().getBlockZ(),p.getLocation().getBlockX()+1,p.getLocation().getBlockZ()+1));

                            StateFactions.db.createClaim(sender,stateName,region);
                        }else{
                            p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_LOGGEDIN));
                        }

                    }else{ //The server is sending the command
                        sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NO_COORDS_SPECIFIED));
                    }
                }else{
                    sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.STATE_DOESNT_EXISTS));
                }
            } else if (args.size()==7) {
                
            }
        }

    }

    //This method is executed when the "/sf register" command is inputted.
    private void executeRegister(@NotNull CommandSender sender, @NotNull ArrayList<String> args) {
        if(args.size()==3){
            String pwd= args.get(1);
            String pwdConf= args.get(2);
            if(pwd.isBlank() || pwd.contains(" ")){
                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PWD_CONTAINS_BLANK_SPACES));
            }else{
                if(pwd.equals(pwdConf)){
                    if(StateFactions.db.checkIfPersonExists(sender.getName())){
                        sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.ALREADY_REGISTERED));
                    }else{
                        StateFactions.db.registerNewPerson(sender.getName(),pwd);
                        sender.sendPlainMessage(StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.PLAYER_REGISTERED));

                        StateFactions.loggedInPlayers.add(sender.getName());
                        sender.sendPlainMessage(StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.PLAYER_LOGGEDIN));
                    }
                }else{
                    sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PWD_CONFIRMPWD_NOT_EQUAL));
                }
            }
        }else{
            if(args.size()>3){
                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.TOO_MANY_WORDS)+": "+args.size());
            }else{
                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_ENOUGH_ARGS)+": "+args.size());
            }
        }
    }

    //This method is executed when the "/sf login" command is inputted.
    private void executeLogIn(@NotNull CommandSender sender, @NotNull ArrayList<String> args){
        if(args.size()==2){
            String pwd = args.get(1);

            StateFactions.logger.log(new LogRecord(Level.INFO,"Password: "+pwd));

            if(StateFactions.db.checkIfPersonExists(sender.getName())){
                if(StateFactions.db.checkPwd(sender.getName(),pwd)){
                    if(StateFactions.loggedInPlayers.contains(sender.getName())){
                        sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.ALREADY_LOGGEDIN));
                    }else{
                        StateFactions.loggedInPlayers.add(sender.getName());
                        sender.sendPlainMessage(StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.PLAYER_LOGGEDIN));
                    }
                }else{
                    sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.WRONG_PWD));
                }
            }else{
                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_REGISTERED));
            }
        }else{
            sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.TOO_MANY_WORDS)+": "+args.size());
        }
    }

    private boolean checkIllegalChars(String[] args){
        for(String arg : args){
            if(arg.contains(Constants.Utility.PARSING_CHARSET)){
                return true;
            }
        }

        return false;
    }

    //This method parses again all command args to allow names with spaces, possible by using "".
    private ArrayList<String> parseCommandArgs(String[] args){
        ArrayList<String> actualArgs = new ArrayList<>();
        StringBuilder args_builder = new StringBuilder();

        for(int i=0; i<args.length-1;i++){
            args_builder.append(args[i]).append(" ");
        }
        args_builder.append(args[args.length-1]);

        String[] temp;
        if(args_builder.toString().contains("\"")){
            String tempStr = args_builder.toString();
            tempStr=tempStr.replaceAll("\"","\""+Constants.Utility.PARSING_CHARSET);

            temp = tempStr.split("\"");

            for(int i=0;i<temp.length;i++){
                if(temp[i].contains(Constants.Utility.PARSING_CHARSET)){
                    temp[i]=temp[i].replaceAll(Constants.Utility.PARSING_CHARSET,"").trim();
                    if(!temp[i].isBlank()){
                        actualArgs.add(temp[i]);
                    }
                }else{
                    String[] arrayTemp=temp[i].split(" ");

                    for(String temp_item : arrayTemp){
                        actualArgs.add(temp_item.trim());
                    }
                }
            }
        }else{
            temp = args_builder.toString().split(" ");

            for(String temp_item : temp){
                actualArgs.add(temp_item.trim());
            }
        }

        return actualArgs;
    }
}
