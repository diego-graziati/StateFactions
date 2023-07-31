package it.sersapessi.sf.utilities.models;

public class ClaimRegion {

    private ClaimSector sector1;    //In a multiregion claim, sector 1 is always the minor sector.
    private ClaimSector sector2;    //In a multiregion claim, sector 2 is always the bigger sector.

    private boolean isSingularMode; //It means if the claim is on a singular block/sector, or if it is on a much larger area.

    public ClaimRegion(ClaimSector sector1){
        this(sector1,null);
    }

    public ClaimRegion(ClaimSector sector1, ClaimSector sector2){

        this.sector1=sector1;
        this.sector2=sector2;

        if(sector2==null){
            isSingularMode=true;
        }else{
            isSingularMode=false;
        }
    }

    public ClaimSector getSector1(){
        return sector1;
    }

    public ClaimSector getSector2(){
        return sector2;
    }

    public boolean isSingularMode(){
        return isSingularMode;
    }

    @Override
    public String toString() {
        return "{ "+ getSector1()+","+getSector2()+"}";
    }
}
