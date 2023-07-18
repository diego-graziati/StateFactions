package it.sersapessi.sf.commands;

import it.sersapessi.sf.StateFactions;
import it.sersapessi.sf.commands.runnables.LoginRunnable;
import it.sersapessi.sf.commands.runnables.RegisterRunnable;
import it.sersapessi.sf.commands.runnables.StateRunnable;
import it.sersapessi.sf.utilities.Constants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
        new StateRunnable(sender, args).runTask(StateFactions.pluginInstance);
    }

    //This method is executed when the "/sf register" command is inputted.
    private void executeRegister(@NotNull CommandSender sender, @NotNull ArrayList<String> args) {
        new RegisterRunnable(sender,args).runTask(StateFactions.pluginInstance);
    }

    //This method is executed when the "/sf login" command is inputted.
    private void executeLogIn(@NotNull CommandSender sender, @NotNull ArrayList<String> args){
        new LoginRunnable(sender,args).runTask(StateFactions.pluginInstance);
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
            tempStr=tempStr.replaceAll(" \"","\""+Constants.Utility.PARSING_CHARSET);
            tempStr=tempStr.replaceAll("\" ",Constants.Utility.PARSING_CHARSET+"\"");

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
