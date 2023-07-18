package it.sersapessi.sf.utilities.models;

import java.sql.Timestamp;

public class Citizen extends Person{

    private boolean isStateOwner;
    private boolean isClaimResponsible;
    private Timestamp joinDate;

    public Citizen(int personId, String name, boolean isStateOwner, boolean isClaimResponsible) {
        super(personId, name);
        this.isStateOwner = isStateOwner;
        this.isClaimResponsible = isClaimResponsible;
        this.joinDate= new Timestamp(System.currentTimeMillis());
    }

    public Citizen(int personId, String name, boolean isStateOwner, boolean isClaimResponsible, Timestamp joinDate) {
        super(personId, name);
        this.isStateOwner = isStateOwner;
        this.isClaimResponsible = isClaimResponsible;
        this.joinDate= joinDate;
    }


    public boolean isStateOwner() {
        return isStateOwner;
    }

    public boolean isClaimResponsible() {
        return isClaimResponsible;
    }

    public Timestamp getJoinDate() {
        return joinDate;
    }

    public void setStateOwner(boolean stateOwner) {
        isStateOwner = stateOwner;
    }

    public void setClaimResponsible(boolean claimResponsible) {
        isClaimResponsible = claimResponsible;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Citizen){
            return ((Person)obj).getName().equals(getName());
        }

        return false;
    }
}
