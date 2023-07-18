package it.sersapessi.sf.utilities.datahandlers;

import it.sersapessi.sf.StateFactions;
import it.sersapessi.sf.utilities.Constants;
import it.sersapessi.sf.utilities.models.ClaimRegion;
import it.sersapessi.sf.utilities.models.ClaimSector;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ClaimsHandler {

    //ClaimSector= all the state claims, Integer= stateId
    private HashMap<ClaimSector,Integer> claims;
    private int maxClaims;

    public ClaimsHandler(HashMap<ClaimSector,Integer> claims, int maxClaims){
        this.claims=claims;
        this.maxClaims=maxClaims;
    }

    public String getClaimOwner(ClaimSector sector){
        String claimOwner="";

        Integer stateId=claims.get(sector);

        if(stateId!=null){
            claimOwner = StateFactions.statesHandler.getStateName(stateId);
        }

        return claimOwner;
    }

    public int getMaxClaims() {
        return maxClaims;
    }

    public void createClaims(CommandSender sender, String stateName, ClaimRegion region){

        if(region.isSingularMode()){    //singleregion claim
            String claimOwner = getClaimOwner(region.getSector1());

            if(claimOwner.isBlank()) {

                int stateId = StateFactions.statesHandler.getStateId(stateName);

                claims.put(new ClaimSector(region.getSector1()),stateId);

                sender.sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.CLAIM_CREATED));
            }else{
                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.AREA_ALREADY_CLAIMED_SINGLE_STATE)+claimOwner);
            }
        }else{ //multiregion claim

            int stateId = StateFactions.statesHandler.getStateId(stateName);

            if(region.getSector1().getX1()<region.getSector2().getX2() && region.getSector1().getZ1()<region.getSector2().getZ2()){
                //case:1

                ClaimSector claimSector = new ClaimSector(region.getSector1().getX1(),region.getSector1().getZ1(),region.getSector1().getX2(),region.getSector1().getZ2());

                while(claimSector.getZ2()!=region.getSector2().getZ2()+1){

                    int originalX1=claimSector.getX1();

                    while(claimSector.getX2()!=region.getSector2().getX2()+1){

                        if(getClaimOwner(claimSector).isBlank()){
                            claims.put(new ClaimSector(claimSector),stateId);
                            StateFactions.statesHandler.incrementClaims(stateName);
                        }

                        int x1=claimSector.getX1();
                        int x2=claimSector.getX2();
                        claimSector.setX1(++x1);
                        claimSector.setX2(++x2);
                    }

                    claimSector.setX1(originalX1);
                    claimSector.setX2(++originalX1);

                    int z1=claimSector.getZ1();
                    int z2=claimSector.getZ2();
                    claimSector.setZ1(++z1);
                    claimSector.setZ2(++z2);
                }
            }else if(region.getSector1().getX1()<region.getSector2().getX2() && region.getSector1().getZ1()>region.getSector2().getZ2()){
                //case:2

                ClaimSector claimSector = new ClaimSector(region.getSector1().getX1(),region.getSector1().getZ1(),region.getSector1().getX2(),region.getSector1().getZ2());

                while(claimSector.getZ2()!=region.getSector2().getZ2()-1){

                    int originalX1=claimSector.getX1();

                    while(claimSector.getX2()!=region.getSector2().getX2()+1){

                        if(getClaimOwner(claimSector).isBlank()){
                            claims.put(new ClaimSector(claimSector),stateId);
                            StateFactions.statesHandler.incrementClaims(stateName);
                        }

                        int x1=claimSector.getX1();
                        int x2=claimSector.getX2();
                        claimSector.setX1(++x1);
                        claimSector.setX2(++x2);
                    }

                    claimSector.setX1(originalX1);
                    claimSector.setX2(++originalX1);

                    int z1=claimSector.getZ1();
                    int z2=claimSector.getZ2();
                    claimSector.setZ1(--z1);
                    claimSector.setZ2(--z2);
                }
            }else if(region.getSector1().getX1()>region.getSector2().getX2() && region.getSector1().getZ1()<region.getSector2().getZ2()){
                //case:3

                ClaimSector claimSector = new ClaimSector(region.getSector1().getX1(),region.getSector1().getZ1(),region.getSector1().getX2(),region.getSector1().getZ2());

                while(claimSector.getZ2()!=region.getSector2().getZ2()+1){

                    int originalX1=claimSector.getX1();

                    while(claimSector.getX2()!=region.getSector2().getX2()-1){

                        if(getClaimOwner(claimSector).isBlank()){
                            claims.put(new ClaimSector(claimSector),stateId);
                            StateFactions.statesHandler.incrementClaims(stateName);
                        }

                        int x1=claimSector.getX1();
                        int x2=claimSector.getX2();
                        claimSector.setX1(--x1);
                        claimSector.setX2(--x2);
                    }

                    claimSector.setX1(originalX1);
                    claimSector.setX2(--originalX1);

                    int z1=claimSector.getZ1();
                    int z2=claimSector.getZ2();
                    claimSector.setZ1(++z1);
                    claimSector.setZ2(++z2);
                }
            }else{
                //Case:4

                ClaimSector claimSector = new ClaimSector(region.getSector1().getX1(),region.getSector1().getZ1(),region.getSector1().getX2(),region.getSector1().getZ2());

                while(claimSector.getZ2()!=region.getSector2().getZ2()-1){

                    int originalX1=claimSector.getX1();

                    while(claimSector.getX2()!=region.getSector2().getX2()-1){

                        if(getClaimOwner(claimSector).isBlank()){
                            claims.put(new ClaimSector(claimSector),stateId);
                            StateFactions.statesHandler.incrementClaims(stateName);
                        }

                        int x1=claimSector.getX1();
                        int x2=claimSector.getX2();
                        claimSector.setX1(--x1);
                        claimSector.setX2(--x2);
                    }

                    claimSector.setX1(originalX1);
                    claimSector.setX2(--originalX1);

                    int z1=claimSector.getZ1();
                    int z2=claimSector.getZ2();
                    claimSector.setZ1(--z1);
                    claimSector.setZ2(--z2);
                }
            }
        }
    }

    public HashMap<ClaimSector, Integer> getClaims() {
        return claims;
    }
}
