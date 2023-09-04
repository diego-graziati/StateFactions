package it.sersapessi.sf.commands;

import it.sersapessi.sf.StateFactions;
import it.sersapessi.sf.commands.runnables.LoginCommandRunnable;
import it.sersapessi.sf.commands.runnables.RegisterCommandRunnable;
import it.sersapessi.sf.commands.runnables.StateCommandRunnable;
import it.sersapessi.sf.utilities.Constants;
import it.sersapessi.sf.utilities.models.State;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>SFCommands</code> handles all the commands that can be found inside the SF Plugin.
 * */
public class SFCommands implements TabExecutor {

    /**
     * It's inside <code>onCommand</code> that the command parsing happens.
     * */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {

        //TODO: check command length when "Arabia Saudita" is used as state name
        if(checkIllegalChars(args)){
            sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+Constants.Localization.Str.Command.Error.ILLEGAL_CHAR);
        }else{
            ArrayList<String> actualArgs = parseCommandArgs(args);

            if(actualArgs.size()>=2){
                if(actualArgs.get(0).equalsIgnoreCase(Constants.CommandsArgs.STATE)){
                    executeStateCommands(sender, actualArgs);
                }else if(actualArgs.get(0).equalsIgnoreCase(Constants.CommandsArgs.REGISTER)){
                    executeRegister(sender, actualArgs);
                }else if(actualArgs.get(0).equalsIgnoreCase(Constants.CommandsArgs.LOGIN)){
                    executeLogIn(sender, actualArgs);
                }else if(actualArgs.get(0).equalsIgnoreCase(Constants.CommandsArgs.FACTION)){
                    sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.YET_TO_BE_IMPLEMENTED));
                }

            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //TODO: check command length when "Arabia Saudita" is used as state name
        if(StateFactions.loggedInPlayers.contains(sender.getName())){
            if(!checkIllegalChars(args)){
                ArrayList<String> actualArgs = parseCommandArgs(args);

                if(actualArgs.get(0).equalsIgnoreCase(Constants.CommandsArgs.STATE)){
                    return completeStateCommands(sender,actualArgs);
                }else if(actualArgs.get(0).equalsIgnoreCase(Constants.CommandsArgs.REGISTER)){
                }else if(actualArgs.get(0).equalsIgnoreCase(Constants.CommandsArgs.LOGIN)){
                }else if(actualArgs.get(0).equalsIgnoreCase(Constants.CommandsArgs.FACTION)){
                }
            }
        }

        return null;
    }

    //These methods execute actions based on the commands
    private void executeStateCommands(@NotNull CommandSender sender, @NotNull ArrayList<String> args){
        if(args.size()>2){
            new StateCommandRunnable(sender, args).runTask(StateFactions.pluginInstance);
        }else{
            sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_ENOUGH_ARGS)+": "+args.size());
        }
    }

    //This method is executed when the "/sf register" command is inputted.
    private void executeRegister(@NotNull CommandSender sender, @NotNull ArrayList<String> args) {
        new RegisterCommandRunnable(sender,args).runTask(StateFactions.pluginInstance);
    }

    //This method is executed when the "/sf login" command is inputted.
    private void executeLogIn(@NotNull CommandSender sender, @NotNull ArrayList<String> args){
        new LoginCommandRunnable(sender,args).runTask(StateFactions.pluginInstance);
    }

    //These methods return a list of possible command completions
    //This method is executed when someone tries to use the "/sf state" command
    private List<String> completeStateCommands(@NotNull CommandSender sender, @NotNull ArrayList<String> args){

        List<String> returnValues = new ArrayList<>();

        if(args.size()==1){ // ex. "/sf state"
            ArrayList<State> states = StateFactions.statesHandler.getStates();

            for(State state : states){
                returnValues.add(state.getStateName());
            }
        }else if(args.size()==2){ // ex. "/sf state <state-name>"
            //Every player, if logged in, can see a state info tab
            returnValues.add(Constants.CommandsArgs.INFO);

            //Every other possible command arg must be filtered: it needs to be checked whether that player can or cannot execute the given command.
        }

        return returnValues;
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
