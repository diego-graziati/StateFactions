package it.sersapessi.sf.utilities.models;

import it.sersapessi.sf.StateFactions;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ClaimSector {

    private int x1;
    private int z1;
    private int x2;
    private int z2;
    private Timestamp claimDate;

    public ClaimSector(int x1, int z1, int x2, int z2){
        this.x1=x1;
        this.z1=z1;
        this.x2=x2;
        this.z2=z2;

        claimDate = new Timestamp(System.currentTimeMillis());
    }

    public ClaimSector(int x1, int z1, int x2, int z2, Timestamp claimDate){
        this.x1=x1;
        this.z1=z1;
        this.x2=x2;
        this.z2=z2;

        this.claimDate = claimDate;
    }

    public ClaimSector(ClaimSector sector){
        x1= sector.x1;
        z1= sector.z1;
        x2= sector.x2;
        z2= sector.z2;
        claimDate= sector.claimDate;
    }

    public int getX1(){
        return x1;
    }

    public int getZ1(){
        return z1;
    }

    public int getX2() {
        return x2;
    }

    public int getZ2() {
        return z2;
    }

    public Timestamp getClaimDate() {
        return claimDate;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public void setZ1(int z1) {
        this.z1 = z1;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public void setZ2(int z2) {
        this.z2 = z2;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ClaimSector cs1){
            return cs1.x1==x1 && cs1.x2==x2 && cs1.z1==z1 && cs1.z2==z2;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return x1+z1+x2+z2;
    }

    @Override
    public String toString() {
        return "[("+x1+","+z1+");("+x2+","+z2+"); "+claimDate+"]";
    }
}
