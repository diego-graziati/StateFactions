package it.sersapessi.sf.utilities.datahandlers;

import it.sersapessi.sf.StateFactions;
import it.sersapessi.sf.utilities.Constants;
import it.sersapessi.sf.utilities.exceptions.TooManyClaimsException;
import it.sersapessi.sf.utilities.models.ClaimRegion;
import it.sersapessi.sf.utilities.models.ClaimSector;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.units.qual.C;

import java.util.Arrays;
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

        StateFactions.logger.log(new LogRecord(Level.INFO,"Player's position: "+sector));

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

                StateFactions.statesHandler.incrementClaims(stateName);

                sender.sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.CLAIM_CREATED));
            }else{
                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.AREA_ALREADY_CLAIMED_SINGLE_STATE)+claimOwner);
            }
        }else{ //multiregion claim

            int stateId = StateFactions.statesHandler.getStateId(stateName);

            ClaimSector claimInstance = region.getSector1();

            do{

                int originalX1=claimInstance.getX1();

                do{
                    if(getClaimOwner(claimInstance).isBlank()){
                        claims.put(new ClaimSector(claimInstance),stateId);

                        StateFactions.statesHandler.incrementClaims(stateName);
                    }

                    claimInstance.setX1(claimInstance.getX1()+1);
                    claimInstance.setX2(claimInstance.getX2()+1);
                }while(claimInstance.getX2()<=region.getSector2().getX2());

                claimInstance.setX1(originalX1);
                claimInstance.setX2(originalX1+1);

                claimInstance.setZ1(claimInstance.getZ1()+1);
                claimInstance.setZ2(claimInstance.getZ2()+1);

            }while(claimInstance.getZ2()<=region.getSector2().getZ2());
        }
    }

    public ClaimRegion getClaimingRegion( int x1, int z1, int x2, int z2){

        int xMax = Math.max(x1, x2);
        int xMin = Math.min(x1, x2);

        int zMax = Math.max(z1, z2);
        int zMin = Math.min(z1, z2);

        if((xMax-xMin)*(zMax-zMin)>maxClaims){
            throw new TooManyClaimsException();
        }else{
            return new ClaimRegion(new ClaimSector(xMin,zMin,xMin+1,zMin+1),new ClaimSector(xMax-1,zMax-1,xMax,zMax));
        }
    }

    public HashMap<ClaimSector, Integer> getClaims() {
        return claims;
    }
}
