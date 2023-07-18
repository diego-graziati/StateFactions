package it.sersapessi.sf.commands.runnables;

import it.sersapessi.sf.StateFactions;
import it.sersapessi.sf.utilities.Constants;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LoginRunnable extends BukkitRunnable {

    private CommandSender sender;
    private ArrayList<String> args;

    public LoginRunnable(@NotNull CommandSender sender, @NotNull ArrayList<String> args){
        this.sender=sender;
        this.args=args;
    }

    @Override
    public void run() {
        if(args.size()==2){
            String pwd = args.get(1);

            StateFactions.logger.log(new LogRecord(Level.INFO,"Password: "+pwd));

            if(StateFactions.peopleHandler.checkIfPlayerIsRegistered(sender.getName())){
                if(StateFactions.peopleHandler.checkPwd(sender.getName(),pwd)){
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
}
