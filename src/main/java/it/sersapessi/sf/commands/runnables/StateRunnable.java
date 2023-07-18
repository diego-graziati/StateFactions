package it.sersapessi.sf.commands.runnables;

import it.sersapessi.sf.StateFactions;
import it.sersapessi.sf.utilities.Constants;
import it.sersapessi.sf.utilities.models.ClaimRegion;
import it.sersapessi.sf.utilities.models.ClaimSector;
import it.sersapessi.sf.utilities.models.PluginPlayer;
import it.sersapessi.sf.utilities.models.State;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class StateRunnable extends BukkitRunnable {

    private CommandSender sender;
    private ArrayList<String> args;

    public StateRunnable(@NotNull CommandSender sender, @NotNull ArrayList<String> args){
        this.sender=sender;
        this.args=args;
    }

    @Override
    public void run() {
        String stateName= args.get(1);
        if(args.get(2).equalsIgnoreCase(Constants.CommandsArgs.CREATE) && args.size()==3){
            if(sender instanceof Player){

                Player p = (Player) sender;

                if(StateFactions.loggedInPlayers.contains(sender.getName())){
                    if(!StateFactions.statesHandler.checkIfStateExists(stateName)){
                        StateFactions.statesHandler.createState(stateName,sender.getName());
                        sender.sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.STATE_CREATED)+stateName);

                        StateFactions.statesHandler.addStateOwner(stateName,p.getName());
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
                if(StateFactions.statesHandler.checkIfStateExists(stateName)){
                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){

                            if(StateFactions.statesHandler.checkIfPersonIsCitizen(stateName,p.getName())){
                                if(StateFactions.statesHandler.checkIfCitIsClaimResponsible(stateName,p.getName())){
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

                                    StateFactions.claimsHandler.createClaims(sender,stateName,region);
                                }else{
                                    p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_A_CLAIM_RESPONSIBLE)+"\""+stateName+"\"");
                                }
                            }else{
                                p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_A_CITIZEN)+"\""+stateName+"\"");
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
                if(StateFactions.statesHandler.checkIfStateExists(stateName)){
                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){
                            if(StateFactions.statesHandler.checkIfPersonIsCitizen(stateName,p.getName())){
                                if(StateFactions.statesHandler.checkIfCitIsClaimResponsible(stateName,p.getName())){
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

                                            int currentNumClaims = (region.getSector2().getX1()-region.getSector1().getX1())*(region.getSector2().getZ1()-region.getSector1().getZ1());

                                            if(currentNumClaims<=StateFactions.claimsHandler.getMaxClaims() && StateFactions.claimsHandler.getMaxClaims()>0){
                                                StateFactions.claimsHandler.createClaims(sender,stateName,region);
                                                sender.sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.CLAIM_CREATED));
                                            }else{
                                                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.TOO_MANY_CLAIMS)+StateFactions.claimsHandler.getMaxClaims());
                                            }
                                        }
                                    }catch(NumberFormatException e){
                                        sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.YOU_MUST_INSERT_COORDS_CORRECTLY));
                                    }
                                }else{
                                    p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_A_CLAIM_RESPONSIBLE)+"\""+stateName+"\"");
                                }
                            }else{
                                p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_A_CITIZEN)+"\""+stateName+"\"");
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

                if(StateFactions.statesHandler.checkIfStateExists(stateName)){
                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){

                            if(!StateFactions.statesHandler.checkIfPersonIsCitizen(stateName, p.getName())){

                                if(!StateFactions.statesHandler.checkIfCitRequestExists(stateName, p.getName())){

                                    StateFactions.statesHandler.addCitRequest(stateName,p.getName());

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
                if(StateFactions.statesHandler.checkIfStateExists(stateName)){
                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){

                            if(StateFactions.statesHandler.checkIfPersonIsCitizen(stateName,p.getName())){

                                String personName = args.get(3);

                                //Then I check the person asking for the citizenship
                                if(StateFactions.peopleHandler.checkIfPlayerIsRegistered(personName)){

                                    if(!StateFactions.statesHandler.checkIfPersonIsCitizen(stateName,personName)){

                                        if(StateFactions.statesHandler.checkIfCitRequestExists(stateName,personName)){

                                            StateFactions.statesHandler.addCitizen(stateName,personName);
                                            StateFactions.statesHandler.removeCitRequest(stateName,personName);

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
                if(StateFactions.statesHandler.checkIfStateExists(stateName)){
                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){

                            if(StateFactions.statesHandler.checkIfPersonIsCitizen(stateName,p.getName())){

                                String personName = args.get(3);

                                //Then I check the person asking for the citizenship
                                if(StateFactions.peopleHandler.checkIfPlayerIsRegistered(personName)){

                                    if(!StateFactions.statesHandler.checkIfPersonIsCitizen(stateName,personName)){

                                        if(StateFactions.statesHandler.checkIfCitRequestExists(stateName,personName)){

                                            StateFactions.statesHandler.removeCitRequest(stateName,personName);

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
                if(StateFactions.statesHandler.checkIfStateExists(stateName)){
                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){

                            if(StateFactions.statesHandler.checkIfPersonIsCitizen(stateName,p.getName())){
                                if(StateFactions.statesHandler.checkIfCitIsStateOwner(stateName,p.getName())){

                                    String personName = args.get(3);

                                    //Then I check the citizen
                                    if(StateFactions.statesHandler.checkIfPersonIsCitizen(stateName, personName)){

                                        if(!StateFactions.statesHandler.checkIfCitIsStateOwner(stateName,personName)){


                                            StateFactions.statesHandler.removeCitizen(stateName,personName);

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
        }else if(args.get(2).equalsIgnoreCase(Constants.CommandsArgs.INFO)){

            if(args.size()==3){
                if(StateFactions.statesHandler.checkIfStateExists(stateName)){

                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){

                            State info = StateFactions.statesHandler.getStateInfo(stateName);

                            p.sendPlainMessage(info.toString());
                        }else{
                            p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_LOGGEDIN));
                        }
                    }else{
                        sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.YOU_MUST_BE_A_PLAYER));
                    }

                }else{
                    sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.STATE_DOESNT_EXISTS));
                }
            }else{
                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.NOT_ENOUGH_ARGS));
            }
        }else if(args.get(2).equalsIgnoreCase(Constants.CommandsArgs.CLAIM_RESP)||args.get(2).equalsIgnoreCase(Constants.CommandsArgs.SHORTEST.CLAIM_RESP_SHORTEST)){

            if(args.size()==4){
                //First, I check the person trying to kick the citizen
                if(StateFactions.statesHandler.checkIfStateExists(stateName)){
                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){

                            if(StateFactions.statesHandler.checkIfPersonIsCitizen(stateName,p.getName())){
                                if(StateFactions.statesHandler.checkIfCitIsStateOwner(stateName,p.getName())){

                                    String personName = args.get(3);

                                    //Then I check the citizen
                                    if(StateFactions.statesHandler.checkIfPersonIsCitizen(stateName,personName)){

                                        if(!StateFactions.statesHandler.checkIfCitIsStateOwner(stateName,personName)){


                                            StateFactions.statesHandler.addClaimResponsible(stateName,personName);

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
        }else if(args.get(2).equalsIgnoreCase(Constants.CommandsArgs.STATE_OWNER)||args.get(2).equalsIgnoreCase(Constants.CommandsArgs.SHORTEST.STATE_OWNER_SHORTEST)){

            if(args.size()==4){
                //First, I check the person trying to promote the citizen
                if(StateFactions.statesHandler.checkIfStateExists(stateName)){
                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){

                            if(StateFactions.statesHandler.checkIfPersonIsCitizen(stateName,p.getName())){
                                if(StateFactions.statesHandler.checkIfCitIsStateOwner(stateName,p.getName())){

                                    String personName = args.get(3);

                                    //Then I check the citizen
                                    if(StateFactions.statesHandler.checkIfPersonIsCitizen(stateName,personName)){

                                        if(!StateFactions.statesHandler.checkIfCitIsStateOwner(stateName,personName)){


                                            StateFactions.statesHandler.addStateOwner(stateName,personName);

                                            p.sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.CIT_PROMOTED_STATE_OWNER)+"\""+personName+"\"");

                                            PluginPlayer newStateOwner = StateFactions.getPlayer(personName);

                                            //If the person who got promoted is online, they will receive a message telling them that they've been promoted to state owner
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
        }else if(args.get(2).equalsIgnoreCase(Constants.CommandsArgs.STATE_OWNER_RESIGN)||args.get(2).equalsIgnoreCase(Constants.CommandsArgs.SHORT.STATE_OWNER_RESIGN_SHORT)||args.get(2).equalsIgnoreCase(Constants.CommandsArgs.SHORTEST.STATE_OWNER_RESIGN_SHORTEST)){

            if(args.size()==3){
                //I check the person trying to resign
                if(StateFactions.statesHandler.checkIfStateExists(stateName)){
                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){

                            if(StateFactions.statesHandler.checkIfPersonIsCitizen(stateName,p.getName())){
                                if(StateFactions.statesHandler.checkIfCitIsStateOwner(stateName,p.getName())){

                                    StateFactions.statesHandler.removeStateOwner(stateName, p.getName());

                                    p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.STATE_OWNER_RESIGN));
                                }else{
                                    p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PERSON_NOT_STATE_OWNER)+"\""+stateName+"\"");
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
        }else if(args.get(2).equalsIgnoreCase(Constants.CommandsArgs.CLAIM_RESP_RESIGN)||args.get(2).equalsIgnoreCase(Constants.CommandsArgs.SHORT.CLAIM_RESP_RESIGN_SHORT)||args.get(2).equalsIgnoreCase(Constants.CommandsArgs.SHORTEST.CLAIM_RESP_RESIGN_SHORTEST)){

            if(args.size()==3){
                //I check the person trying to resign
                if(StateFactions.statesHandler.checkIfStateExists(stateName)){
                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){

                            if(StateFactions.statesHandler.checkIfPersonIsCitizen(stateName,p.getName())){
                                if(StateFactions.statesHandler.checkIfCitIsClaimResponsible(stateName,p.getName())){

                                    StateFactions.statesHandler.removeClaimResponsible(stateName, p.getName());

                                    p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.CLAIM_RESPONSIBLE_RESIGN));
                                }else{
                                    p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PERSON_NOT_STATE_OWNER)+"\""+stateName+"\"");
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
        }else if(args.get(2).equalsIgnoreCase(Constants.CommandsArgs.CLAIM_RESP_REMOVE)||args.get(2).equalsIgnoreCase(Constants.CommandsArgs.SHORT.CLAIM_RESP_REMOVE_SHORT)||args.get(2).equalsIgnoreCase(Constants.CommandsArgs.SHORTEST.CLAIM_RESP_REMOVE_SHORTEST)){
            if(args.size()==4){
                //First, I check the person trying to remove the claim responsible
                if(StateFactions.statesHandler.checkIfStateExists(stateName)){
                    if(sender instanceof Player){

                        Player p = (Player) sender;

                        if(StateFactions.loggedInPlayers.contains(p.getName())){

                            if(StateFactions.statesHandler.checkIfPersonIsCitizen(stateName,p.getName())){
                                if(StateFactions.statesHandler.checkIfCitIsStateOwner(stateName,p.getName())){

                                    String personName = args.get(3);

                                    //Then I check the citizen
                                    if(StateFactions.statesHandler.checkIfPersonIsCitizen(stateName,personName)){

                                        if(StateFactions.statesHandler.checkIfCitIsClaimResponsible(stateName,personName)){


                                            StateFactions.statesHandler.removeClaimResponsible(stateName, personName);

                                            p.sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.REMOVE_CLAIM_RESPONSIBLE)+"\""+personName+"\"");

                                            PluginPlayer newStateOwner = StateFactions.getPlayer(personName);

                                            //If the person who got demoted is online, they will receive a message telling them that they've lost the role of claim-responsible
                                            if(newStateOwner!=null){
                                                newStateOwner.getBukkitPlayer().sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.LOST_CLAIM_RESPONSIBLE)+"\""+stateName+"\"");
                                            }

                                        }else{
                                            p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PERSON_NOT_A_CLAIM_RESPONSIBLE));
                                        }

                                    }else{
                                        p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PERSON_NOT_A_CITIZEN)+"\""+stateName+"\"");
                                    }

                                }else{
                                    p.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.PERSON_NOT_STATE_OWNER)+"\""+stateName+"\"");
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
        }
    }
}
