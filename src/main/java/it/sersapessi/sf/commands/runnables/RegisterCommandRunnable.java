package it.sersapessi.sf.commands.runnables;

import it.sersapessi.sf.StateFactions;
import it.sersapessi.sf.utilities.Constants;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RegisterCommandRunnable extends BukkitRunnable {

    private CommandSender sender;
    private ArrayList<String> args;

    public RegisterCommandRunnable(@NotNull CommandSender sender, @NotNull ArrayList<String> args){
        this.sender=sender;
        this.args=args;
    }

    @Override
    public void run() {
        if(args.size()==3){
            String pwd= args.get(1);
            String pwdConf= args.get(2);
            if(pwd.isBlank() || pwd.contains(" ")){
                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+ StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PWD_CONTAINS_BLANK_SPACES));
            }else if(pwd.length()>10){
                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PWD_TOO_LONG));
            }else{
                if(pwd.equals(pwdConf)){
                    if(StateFactions.peopleHandler.checkIfPlayerIsRegistered(sender.getName())){
                        sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.ALREADY_REGISTERED));
                    }else{
                        StateFactions.peopleHandler.registerPerson(sender.getName(),pwd);
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
}
