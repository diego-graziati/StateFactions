package it.sersapessi.sf.commands;

import it.sersapessi.sf.StateFactions;
import it.sersapessi.sf.utilities.Constants;
import it.sersapessi.sf.utilities.models.ClaimRegion;
import it.sersapessi.sf.utilities.models.ClaimSector;
import it.sersapessi.sf.utilities.models.PluginPlayer;
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
            if(sender instanceof Player){

                Player p = (Player) sender;

                if(StateFactions.loggedInPlayers.contains(sender.getName())){
                    if(!StateFactions.db.checkIfStateExists(stateName)){
                        StateFactions.db.createState(stateName,sender.getName());
                        sender.sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.STATE_CREATED)+stateName);

                        StateFactions.db.addCitizenshipRequest(stateName, p.getName());
                        StateFactions.db.acceptCitizenshipRequest(stateName,p.getName());

                        StateFactions.db.setStateOwner(p.getName(),stateName);
                    }else{
                        sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.STATE_ALREADY_EXISTS));
                    }
                }else{
                    sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_LOGGEDIN));
                }
            }else{
                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.YOU_MUST_BE_A_PLAYER));
            }
        }else if(args.get(2).equalsIgnoreCase(Constants.CommandsArgs.CLAIM)){

            if(args.size()==3){
                if(StateFactions.db.checkIfStateExists(stateName)){
                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){

                            if(StateFactions.db.checkIfPersonIsClaimResponsible(stateName,p.getName())){
                                double px=p.getLocation().x();
                                double pz=p.getLocation().z();

                                int x1= (int)px;
                                int z1= (int)pz;

                                int x2;
                                int z2;
                                if(px>=x1){
                                    x2=x1+1;
                                }else{
                                    x2=x1-1;
                                }

                                if(pz>=z1){
                                    z2=z1+1;
                                }else{
                                    z2=z1-1;
                                }

                                ClaimRegion region = new ClaimRegion(new ClaimSector(x1,z1,x2,z2));

                                StateFactions.db.createClaim(sender,stateName,region);
                                sender.sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.CLAIM_CREATED));
                            }else{
                                p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_A_CLAIM_RESPONSIBLE)+"\""+stateName+"\"");
                            }
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
                if(StateFactions.db.checkIfStateExists(stateName)){
                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){
                            if(StateFactions.db.checkIfPersonIsClaimResponsible(stateName,p.getName())){
                                try{
                                    int sec1x1=Integer.parseInt(args.get(3));
                                    int sec1z1=Integer.parseInt(args.get(4));
                                    int sec2x2=Integer.parseInt(args.get(5));
                                    int sec2z2=Integer.parseInt(args.get(6));

                                    StateFactions.logger.log(new LogRecord(Level.INFO, "Claim coordinates: sec1x1->"+sec1x1
                                            +"\tsec1z1->"+sec1z1+"\tsec2x2->"+sec2x2+"\tsec2z2->"+sec2z2));

                                    if(sec1x1==sec2x2 && sec1z1==sec2z2){
                                        sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.YOU_MUST_CLAIM_ONE_BLOCK));
                                    }else{
                                        ClaimRegion region;

                                        if(sec1x1<sec2x2 && sec1z1<sec2z2){
                                            region = new ClaimRegion(new ClaimSector(sec1x1,sec1z1,sec1x1+1,sec1z1+1),
                                                    new ClaimSector(sec2x2-1,sec2z2-1,sec2x2,sec2z2));
                                        }else if(sec1x1>sec2x2 && sec1z1<=sec2z2){
                                            region = new ClaimRegion(new ClaimSector(sec1x1,sec1z1,sec1x1-1,sec1z1+1),
                                                    new ClaimSector(sec2x2+1,sec2z2-1,sec2x2,sec2z2));
                                        }else if(sec1x1<sec2x2){
                                            region = new ClaimRegion(new ClaimSector(sec1x1,sec1z1,sec1x1+1,sec1z1-1),
                                                    new ClaimSector(sec2x2-1,sec2z2+1,sec2x2,sec2z2));
                                        }else{
                                            region = new ClaimRegion(new ClaimSector(sec1x1,sec1z1,sec1x1-1,sec1z1-1),
                                                    new ClaimSector(sec2x2+1,sec2z2+1,sec2x2,sec2z2));
                                        }

                                        StateFactions.db.createClaim(sender,stateName,region);
                                        sender.sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.CLAIM_CREATED));
                                    }
                                }catch(NumberFormatException e){
                                    sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.YOU_MUST_INSERT_COORDS_CORRECTLY));
                                }
                            }else{
                                p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_A_CLAIM_RESPONSIBLE)+"\""+stateName+"\"");
                            }
                        }else{
                            p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_LOGGEDIN));
                        }

                    }else{ //The server is sending the command
                        sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.YOU_MUST_BE_A_PLAYER));
                    }
                }else{
                    sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.STATE_DOESNT_EXISTS));
                }
            }else{
                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_ENOUGH_ARGS));
            }
        }else if(args.get(2).equalsIgnoreCase(Constants.CommandsArgs.CIT_REQUEST) || args.get(2).equalsIgnoreCase(Constants.CommandsArgs.SHORT.CIT_REQUEST_SHORT) || args.get(2).equalsIgnoreCase(Constants.CommandsArgs.SHORTEST.CIT_REQUEST_SHORTEST)){
            if(args.size()==3){

                if(StateFactions.db.checkIfStateExists(stateName)){
                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){

                            if(!StateFactions.db.checkIfPersonIsCitizen(stateName,p.getName())){

                                if(!StateFactions.db.checkIfCitRequestAlreadyExists(stateName,p.getName())){

                                    StateFactions.db.addCitizenshipRequest(stateName,p.getName());

                                    p.sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.CIT_REQ_SENT)+"\""+stateName+"\"");
                                }else{
                                    p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.CIT_REQ_ALREADY_SENT)+"\""+stateName+"\"");
                                }

                            }else{
                                p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.ALREADY_A_CITIZEN)+"\""+stateName+"\"");
                            }

                        }else{
                            p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_LOGGEDIN));
                        }

                    }else{//The server is sending the command
                        sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.YOU_MUST_BE_A_PLAYER));
                    }
                }else{
                    sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.STATE_DOESNT_EXISTS));
                }

            }else{
                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_ENOUGH_ARGS));
            }
        }else if(args.get(2).equalsIgnoreCase(Constants.CommandsArgs.CIT_REQUEST_ACCEPT) || args.get(2).equalsIgnoreCase(Constants.CommandsArgs.SHORT.CIT_REQUEST_ACCEPT_SHORT) || args.get(2).equalsIgnoreCase(Constants.CommandsArgs.SHORTEST.CIT_REQUEST_ACCEPT_SHORTEST)){
            if(args.size()==4){

                //First, I check the person trying to accept the citizenship request
                if(StateFactions.db.checkIfStateExists(stateName)){
                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){

                            if(StateFactions.db.checkIfPersonIsCitizen(stateName,p.getName())){

                                String personName = args.get(3);

                                //Then I check the person asking for the citizenship
                                if(StateFactions.db.checkIfPersonExists(personName)){

                                    if(!StateFactions.db.checkIfPersonIsCitizen(stateName,personName)){

                                        if(StateFactions.db.checkIfCitRequestAlreadyExists(stateName,personName)){

                                            StateFactions.db.acceptCitizenshipRequest(stateName,personName);

                                            p.sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.CIT_REQ_ACCEPTED)+"\""+personName+"\"");

                                            PluginPlayer newCit = StateFactions.getPlayer(personName);

                                            //If the person that got accepted is online, they will receive a message telling them that they got accepted
                                            if(newCit!=null){
                                                newCit.getBukkitPlayer().sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.NEW_CIT)+"\""+stateName+"\"");
                                            }
                                        }else{
                                            p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PERSON_HAS_NO_CIT_REQ_SENT));
                                        }

                                    }else{
                                        p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PERSON_ALREADY_A_CITIZEN));
                                    }

                                }else{
                                    p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PERSON_DOESNT_EXISTS)+"\""+personName+"\"");
                                }

                            }else{
                                p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_A_CITIZEN)+"\""+stateName+"\"");
                            }

                        }else{
                            p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_LOGGEDIN));
                        }

                    }else{//The server is sending the command
                        sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.YOU_MUST_BE_A_PLAYER));
                    }
                }else{
                    sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.STATE_DOESNT_EXISTS));
                }

            }else{
                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_ENOUGH_ARGS));
            }
        }else if(args.get(2).equalsIgnoreCase(Constants.CommandsArgs.CIT_REQUEST_DENY) || args.get(2).equalsIgnoreCase(Constants.CommandsArgs.SHORT.CIT_REQUEST_DENY_SHORT) || args.get(2).equalsIgnoreCase(Constants.CommandsArgs.SHORTEST.CIT_REQUEST_DENY_SHORTEST)){
            if(args.size()==4){

                //First, I check the person trying to deny the citizenship request
                if(StateFactions.db.checkIfStateExists(stateName)){
                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){

                            if(StateFactions.db.checkIfPersonIsCitizen(stateName,p.getName())){

                                String personName = args.get(3);

                                //Then I check the person asking for the citizenship
                                if(StateFactions.db.checkIfPersonExists(personName)){

                                    if(!StateFactions.db.checkIfPersonIsCitizen(stateName,personName)){

                                        if(StateFactions.db.checkIfCitRequestAlreadyExists(stateName,personName)){

                                            StateFactions.db.refuseCitizenshipRequest(stateName,personName);

                                            p.sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.CIT_REQ_DENIED)+"\""+personName+"\"");

                                            PluginPlayer newCit = StateFactions.getPlayer(personName);

                                            //If the person who got denied is online, they will receive a message telling them that they're denied the citizenship to the state
                                            if(newCit!=null){
                                                newCit.getBukkitPlayer().sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.CIT_DENIED)+"\""+stateName+"\"");
                                            }
                                        }else{
                                            p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PERSON_HAS_NO_CIT_REQ_SENT));
                                        }

                                    }else{
                                        p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PERSON_ALREADY_A_CITIZEN));
                                    }

                                }else{
                                    p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PERSON_DOESNT_EXISTS)+"\""+personName+"\"");
                                }

                            }else{
                                p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_A_CITIZEN)+"\""+stateName+"\"");
                            }

                        }else{
                            p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_LOGGEDIN));
                        }

                    }else{//The server is sending the command
                        sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.YOU_MUST_BE_A_PLAYER));
                    }
                }else{
                    sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.STATE_DOESNT_EXISTS));
                }

            }else{
                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_ENOUGH_ARGS));
            }
        }else if(args.get(2).equalsIgnoreCase(Constants.CommandsArgs.CIT_KICK)){

            if(args.size()==4){
                //First, I check the person trying to kick the citizen
                if(StateFactions.db.checkIfStateExists(stateName)){
                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){

                            if(StateFactions.db.checkIfPersonIsStateOwner(stateName,p.getName())){

                                String personName = args.get(3);

                                //Then I check the citizen
                                if(StateFactions.db.checkIfPersonIsCitizen(stateName, personName)){

                                    if(!StateFactions.db.checkIfPersonIsStateOwner(stateName,personName)){


                                            StateFactions.db.removeCitizen(stateName,personName);

                                            p.sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.CIT_KICKED)+"\""+personName+"\"");

                                            PluginPlayer kickedCit = StateFactions.getPlayer(personName);

                                            //If the person who got kicked is online, they will receive a message telling them that they've been kicked out of the state
                                            if(kickedCit!=null){
                                                kickedCit.getBukkitPlayer().sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.BEING_KICKED)+"\""+stateName+"\"");
                                            }

                                    }else{
                                        p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PERSON_IS_STATE_OWNER));
                                    }

                                }else{
                                    p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PERSON_NOT_A_CITIZEN)+"\""+stateName+"\"");
                                }

                            }else{
                                p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PERSON_NOT_STATE_OWNER)+"\""+stateName+"\"");
                            }

                        }else{
                            p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_LOGGEDIN));
                        }

                    }else{//The server is sending the command
                        sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.YOU_MUST_BE_A_PLAYER));
                    }
                }else{
                    sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.STATE_DOESNT_EXISTS));
                }
            }else{
                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_ENOUGH_ARGS));
            }
        }else if(args.get(2).equalsIgnoreCase(Constants.CommandsArgs.INFO)){

        }else if(args.get(2).equalsIgnoreCase(Constants.CommandsArgs.CLAIM_RESP)||args.get(2).equalsIgnoreCase(Constants.CommandsArgs.SHORTEST.CLAIM_RESP_SHORTEST)){

            if(args.size()==4){
                //First, I check the person trying to kick the citizen
                if(StateFactions.db.checkIfStateExists(stateName)){
                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){

                            if(StateFactions.db.checkIfPersonIsStateOwner(stateName,p.getName())){

                                String personName = args.get(3);

                                //Then I check the citizen
                                if(StateFactions.db.checkIfPersonIsCitizen(stateName,personName)){

                                    if(!StateFactions.db.checkIfPersonIsClaimResponsible(stateName,personName)){


                                        StateFactions.db.setStateClaimResponsible(stateName,personName);

                                        p.sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.CIT_PROMOTED_CLAIM_RESPONSIBLE)+"\""+personName+"\"");

                                        PluginPlayer newClaimResp = StateFactions.getPlayer(personName);

                                        //If the person who got kicked is online, they will receive a message telling them that they've been kicked out of the state
                                        if(newClaimResp!=null){
                                            newClaimResp.getBukkitPlayer().sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.PROMOTED_CLAIM_RESPONSIBLE)+"\""+stateName+"\"");
                                        }

                                    }else{
                                        p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.ALREADY_CLAIM_RESPONSIBLE));
                                    }

                                }else{
                                    p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PERSON_NOT_A_CITIZEN)+"\""+stateName+"\"");
                                }

                            }else{
                                p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PERSON_NOT_STATE_OWNER)+"\""+stateName+"\"");
                            }

                        }else{
                            p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_LOGGEDIN));
                        }

                    }else{//The server is sending the command
                        sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.YOU_MUST_BE_A_PLAYER));
                    }
                }else{
                    sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.STATE_DOESNT_EXISTS));
                }
            }else{
                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_ENOUGH_ARGS));
            }
        }else if(args.get(2).equalsIgnoreCase(Constants.CommandsArgs.STATE_OWNER)||args.get(2).equalsIgnoreCase(Constants.CommandsArgs.SHORTEST.STATE_OWNER_SHORTEST)){

            if(args.size()==4){
                //First, I check the person trying to kick the citizen
                if(StateFactions.db.checkIfStateExists(stateName)){
                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){

                            if(StateFactions.db.checkIfPersonIsStateOwner(stateName,p.getName())){

                                String personName = args.get(3);

                                //Then I check the citizen
                                if(StateFactions.db.checkIfPersonIsCitizen(stateName,personName)){

                                    if(!StateFactions.db.checkIfPersonIsStateOwner(stateName,personName)){


                                        StateFactions.db.setStateOwner(stateName,personName);

                                        p.sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.CIT_PROMOTED_STATE_OWNER)+"\""+personName+"\"");

                                        PluginPlayer newStateOwner = StateFactions.getPlayer(personName);

                                        //If the person who got kicked is online, they will receive a message telling them that they've been kicked out of the state
                                        if(newStateOwner!=null){
                                            newStateOwner.getBukkitPlayer().sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.PROMOTED_STATE_OWNER)+"\""+stateName+"\"");
                                        }

                                    }else{
                                        p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PERSON_ALREADY_STATE_OWNER));
                                    }

                                }else{
                                    p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PERSON_NOT_A_CITIZEN)+"\""+stateName+"\"");
                                }

                            }else{
                                p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PERSON_NOT_STATE_OWNER)+"\""+stateName+"\"");
                            }

                        }else{
                            p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_LOGGEDIN));
                        }

                    }else{//The server is sending the command
                        sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.YOU_MUST_BE_A_PLAYER));
                    }
                }else{
                    sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.STATE_DOESNT_EXISTS));
                }
            }else{
                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_ENOUGH_ARGS));
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
            }else if(pwd.length()>10){
                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PWD_TOO_LONG));
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
