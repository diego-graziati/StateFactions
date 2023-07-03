package it.sersapessi.sf.utilities;

import it.sersapessi.sf.StateFactions;
import it.sersapessi.sf.utilities.models.ClaimRegion;
import it.sersapessi.sf.utilities.models.ClaimSector;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * <code>Database</code> handles all the operations that needs to be done towards the plugin's db.
 * */
public class Database {

    private Connection db;
    private String dbName;


    private HashMap<String, String> queryMap;
    /**
     * The <code>Database</code>'s constructor establishes a connection with the db and tries to execute a first
     * startup query.
     *
     * @param dbms this is the string that identifies the dbms used. It is specified inside the configs' file.
     * @param path this is the path to the db used.
     * @param user the user of the db, if any is required to establish the connection.
     * @param pwd the password necessary to establish the connection to the db.
     *
     * @throws SQLException this exception can be thrown, in this case, if the Connection fails to be established, if the user or pwd are incorrect
     *                      or if the startup query execution fails.
     * @throws IOException this exception can be thrown if reading the startup sql file needed fails for any possible reason.
     * */
    public Database(@NotNull String dbms, @NotNull String path, @NotNull String user, @NotNull String pwd) throws SQLException, IOException {
        db = DriverManager.getConnection("jdbc:"+dbms+"://"+path+"?"+Constants.SQL.ALLOW_MULTIQUERIES_TRUE,user,pwd);

        getDBName(path);
        String startupSQL = getStartupSQL(dbms);
        PreparedStatement startupStatement;

        queryMap = new HashMap<>();

        switch (dbms){
            case Constants.Databases.MYSQL:

                startupStatement=db.prepareStatement(startupSQL);
                startupStatement.execute();

                StateFactions.logger.log(new LogRecord(Level.INFO,"QueryMap: "+((queryMap==null)?"null":"Not null")));

                queryMap.put(Constants.QueryMap.CHECK_IF_PERSON_EXISTS,getSQL(Constants.Resources.MYSQL.DB_CHECK_IF_PERSON_EXISTS));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test1"));
                queryMap.put(Constants.QueryMap.INSERT_NEW_PERSON,getSQL(Constants.Resources.MYSQL.DB_INSERT_NEW_PERSON));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test2"));
                queryMap.put(Constants.QueryMap.GET_PERSON_CREDENTIALS,getSQL(Constants.Resources.MYSQL.DB_GET_PERSON_CREDENTIALS));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test3"));
                queryMap.put(Constants.QueryMap.GET_PERSON_ID,getSQL(Constants.Resources.MYSQL.DB_GET_PERSON_ID));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test4"));
                queryMap.put(Constants.QueryMap.INSERT_NEW_STATE,getSQL(Constants.Resources.MYSQL.DB_INSERT_NEW_STATE));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test5"));
                queryMap.put(Constants.QueryMap.CHECK_IF_STATE_EXISTS,getSQL(Constants.Resources.MYSQL.DB_CHECK_IF_STATE_EXISTS));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test6"));
                queryMap.put(Constants.QueryMap.CREATE_CLAIM,getSQL(Constants.Resources.MYSQL.DB_CREATE_CLAIM));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test7"));
                queryMap.put(Constants.QueryMap.GET_CLAIM_OWNER,getSQL(Constants.Resources.MYSQL.DB_GET_CLAIM_OWNER));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test8"));
                queryMap.put(Constants.QueryMap.GET_STATE_NAME_BY_ID,getSQL(Constants.Resources.MYSQL.DB_GET_STATE_NAME_BY_ID));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test9"));
                queryMap.put(Constants.QueryMap.GET_STATE_ID_BY_NAME,getSQL(Constants.Resources.MYSQL.DB_GET_STATE_ID_BY_NAME));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test10"));

                break;
        }
    }

    public boolean checkIfPersonExists(@NotNull String personName) {
        StateFactions.logger.log(new LogRecord(Level.INFO,"checkIfPersonExists test 1"));
        try{
            PreparedStatement ps = db.prepareStatement(queryMap.get(Constants.QueryMap.CHECK_IF_PERSON_EXISTS));
            StateFactions.logger.log(new LogRecord(Level.INFO,"checkIfPersonExists test 2. PreparedStatement ps: "+((ps==null)?"null":"Not null")));
            assert ps != null;
            ps.setString(1,personName);
            StateFactions.logger.log(new LogRecord(Level.INFO,"checkIfPersonExists test 3"));
            try{
                ResultSet set = ps.executeQuery();
                StateFactions.logger.log(new LogRecord(Level.INFO,"checkIfPersonExists test 4"));

                set.next();

                try {
                    String user=set.getString(Constants.DB_Tables.SF_Person.PERSON_NAME);

                    StateFactions.logger.log(new LogRecord(Level.INFO,StateFactions.translationManager.getString(Constants.Localization.Str.Log.Db.SUCCESSFUL_CHECK_IF_PERSON_EXISTS)));
                    return personName.equals(user);
                }catch (SQLException exception){
                    return false;
                }
            }catch(SQLException exception){
                return false;
            }
        }catch (SQLException exception){
            StateFactions.logger.log(new LogRecord(Level.SEVERE,StateFactions.translationManager.getString(Constants.Localization.Str.Log.Db.FAILED_TO_CHECK_IF_PERSON_EXISTS)));
            throw new RuntimeException(exception.getMessage());
        }
    }

    public void registerNewPerson(@NotNull String personName, String pwd) {
        try{
            PreparedStatement ps = db.prepareStatement(queryMap.get(Constants.QueryMap.INSERT_NEW_PERSON));
            ps.setString(1,personName);
            ps.setString(2,pwd);

            ps.execute();

            StateFactions.logger.log(new LogRecord(Level.INFO,StateFactions.translationManager.getString(Constants.Localization.Str.Log.Db.SUCCESSFUL_PERSON_REGISTRATION)));
        }catch(SQLException exception){
            StateFactions.logger.log(new LogRecord(Level.SEVERE,StateFactions.translationManager.getString(Constants.Localization.Str.Log.Db.FAILED_TO_REGISTER_PERSON)));
            throw new RuntimeException(exception.getMessage());
        }
    }

    public boolean checkPwd(@NotNull String personName, @NotNull String pwd){
        try{
            PreparedStatement ps = db.prepareStatement(queryMap.get(Constants.QueryMap.GET_PERSON_CREDENTIALS));
            StateFactions.logger.log(new LogRecord(Level.INFO,"Person Name: "+personName+"\tPwd: "+pwd));
            StateFactions.logger.log(new LogRecord(Level.INFO, queryMap.get(Constants.QueryMap.GET_PERSON_CREDENTIALS)));
            ps.setString(1,personName);

            try {
                ResultSet set = ps.executeQuery();

                set.next();

                try{
                    String pCredentials = set.getString(Constants.DB_Tables.SF_Person.PERSON_CREDENTIALS);

                    StateFactions.logger.log(new LogRecord(Level.INFO,StateFactions.translationManager.getString(Constants.Localization.Str.Log.Db.SUCCESSFUL_PWD_CHECK)));
                    return pwd.equals(pCredentials);
                }catch(SQLException exception){
                    exception.printStackTrace();
                    StateFactions.logger.log(new LogRecord(Level.SEVERE,StateFactions.translationManager.getString(Constants.Localization.Str.Log.Db.FAILED_PWD_CHECK)));
                    throw new RuntimeException(exception.getMessage());
                }
            }catch(SQLException exception){
                exception.printStackTrace();
                StateFactions.logger.log(new LogRecord(Level.INFO,"No "+personName+" found. Set: "));
                return false;
            }
        }catch(SQLException exception){
            StateFactions.logger.log(new LogRecord(Level.SEVERE,StateFactions.translationManager.getString(Constants.Localization.Str.Log.Db.FAILED_PWD_CHECK)));
            throw new RuntimeException(exception.getMessage());
        }
    }

    public boolean checkIfStateExists(@NotNull String stateName) {
        try{
            PreparedStatement ps = db.prepareStatement(queryMap.get(Constants.QueryMap.CHECK_IF_STATE_EXISTS));
            assert ps != null;
            ps.setString(1,stateName);
            try{
                ResultSet set = ps.executeQuery();

                set.next();

                try {
                    String state=set.getString(Constants.DB_Tables.SF_State.STATE_NAME);

                    StateFactions.logger.log(new LogRecord(Level.INFO,StateFactions.translationManager.getString(Constants.Localization.Str.Log.Db.SUCCESSFUL_CHECK_IF_STATE_EXISTS)));
                    return stateName.equals(state);
                }catch (SQLException exception){
                    return false;
                }
            }catch(SQLException exception){
                return false;
            }
        }catch (SQLException exception){
            StateFactions.logger.log(new LogRecord(Level.SEVERE,StateFactions.translationManager.getString(Constants.Localization.Str.Log.Db.FAILED_TO_CHECK_IF_PERSON_EXISTS)));
            throw new RuntimeException(exception.getMessage());
        }
    }

    public void createState(@NotNull String stateName, @NotNull String stateFounder){
        try{
            PreparedStatement ps = db.prepareStatement(queryMap.get(Constants.QueryMap.GET_PERSON_ID));
            StateFactions.logger.log(new LogRecord(Level.INFO,"State Founder: "+stateFounder));
            StateFactions.logger.log(new LogRecord(Level.INFO, queryMap.get(Constants.QueryMap.GET_PERSON_CREDENTIALS)));
            ps.setString(1,stateFounder);

            ResultSet set = ps.executeQuery();

            set.next();

            int founderId = set.getInt(Constants.DB_Tables.SF_Person.PERSON_ID);

            try{
                ps = db.prepareStatement(queryMap.get(Constants.QueryMap.INSERT_NEW_STATE));
                StateFactions.logger.log(new LogRecord(Level.INFO,"State Name: "+stateName));
                StateFactions.logger.log(new LogRecord(Level.INFO, queryMap.get(Constants.QueryMap.INSERT_NEW_STATE)));
                ps.setString(1,stateName);
                ps.setInt(2,founderId);
                ps.setInt(3,founderId);

                ps.execute();
                StateFactions.logger.log(new LogRecord(Level.INFO,StateFactions.translationManager.getString(Constants.Localization.Str.Log.Db.SUCCESSFUL_STATE_CREATION)));
            }catch (SQLException e){
                StateFactions.logger.log(new LogRecord(Level.SEVERE,StateFactions.translationManager.getString(Constants.Localization.Str.Log.Db.FAILED_STATE_CREATION)));
                throw new RuntimeException(e.getMessage());
            }
        }catch (SQLException e) {
            StateFactions.logger.log(new LogRecord(Level.SEVERE,StateFactions.translationManager.getString(Constants.Localization.Str.Log.Db.FAILED_ID_CHECK)));
            throw new RuntimeException(e.getMessage());
        }
    }

    public ArrayList<String> getClaimOwners(@NotNull ClaimRegion region){
        ArrayList<String> claimOwners=new ArrayList<>();

        try {
            if(region.isSingularMode()){
                PreparedStatement ps = db.prepareStatement(queryMap.get(Constants.QueryMap.GET_CLAIM_OWNER));
                StateFactions.logger.log(new LogRecord(Level.INFO, queryMap.get(Constants.QueryMap.GET_CLAIM_OWNER)));
                ps.setInt(1,region.getSector1().getX1());
                ps.setInt(2,region.getSector1().getZ1());
                ps.setInt(3,region.getSector1().getX2());
                ps.setInt(4,region.getSector1().getZ2());

                ResultSet set = ps.executeQuery();

                StateFactions.logger.log(new LogRecord(Level.INFO,"ResultSet set = "+(set==null?"null":set)));
                if(set!=null && set.next()){

                    int stateId = set.getInt(Constants.DB_Tables.SF_State_Space.STATE_ID);

                    StateFactions.logger.log(new LogRecord(Level.INFO, "StateId: "+stateId));

                    ps = db.prepareStatement(queryMap.get(Constants.QueryMap.GET_STATE_NAME_BY_ID));
                    StateFactions.logger.log(new LogRecord(Level.INFO, queryMap.get(Constants.QueryMap.GET_STATE_NAME_BY_ID)));
                    ps.setInt(1,stateId);
                    set = ps.executeQuery();

                    set.next();

                    claimOwners.add(set.getString(Constants.DB_Tables.SF_State.STATE_NAME));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return claimOwners;
    }

    public void createClaim(CommandSender sender, String stateName, ClaimRegion region) {

        try{
            ArrayList<String> claimOwners = getClaimOwners(region);

            if(claimOwners.isEmpty()){
                PreparedStatement ps = db.prepareStatement(queryMap.get(Constants.QueryMap.GET_STATE_ID_BY_NAME));
                StateFactions.logger.log(new LogRecord(Level.INFO, queryMap.get(Constants.QueryMap.GET_STATE_ID_BY_NAME)));
                ps.setString(1,stateName);

                ResultSet set = ps.executeQuery();

                set.next();

                int stateId=set.getInt(Constants.DB_Tables.SF_State.STATE_ID);

                if(region.isSingularMode()){
                    ps = db.prepareStatement(queryMap.get(Constants.QueryMap.CREATE_CLAIM));
                    StateFactions.logger.log(new LogRecord(Level.INFO, queryMap.get(Constants.QueryMap.CREATE_CLAIM)));
                    ps.setInt(1,region.getSector1().getX1());
                    ps.setInt(2,region.getSector1().getZ1());
                    ps.setInt(3,region.getSector1().getX2());
                    ps.setInt(4,region.getSector1().getZ2());
                    ps.setInt(5,stateId);

                    ps.execute();

                    sender.sendPlainMessage(Constants.ChatStyling.Colors.GREEN+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Success.CLAIM_CREATED));
                }
            }else{
                sender.sendPlainMessage(Constants.ChatStyling.Colors.RED+StateFactions.translationManager.getString(Constants.Localization.Str.Command.Error.AREA_ALREADY_CLAIMED_SINGLE_STATE)+claimOwners.get(0));
            }

        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String getStateNameByPosition(ClaimSector sector){
        String stateClaimOwner="";

        try{
            PreparedStatement ps = db.prepareStatement(queryMap.get(Constants.QueryMap.GET_CLAIM_OWNER));
            StateFactions.logger.log(new LogRecord(Level.INFO, queryMap.get(Constants.QueryMap.GET_CLAIM_OWNER)));
            ps.setInt(1,sector.getX1());
            ps.setInt(2,sector.getZ1());
            ps.setInt(3,sector.getX2());
            ps.setInt(4,sector.getZ2());

            ResultSet set = ps.executeQuery();

            if(set!=null && set.next()){
                int stateId = set.getInt(Constants.DB_Tables.SF_State.STATE_ID);

                ps = db.prepareStatement(queryMap.get(Constants.QueryMap.GET_STATE_NAME_BY_ID));
                StateFactions.logger.log(new LogRecord(Level.INFO, queryMap.get(Constants.QueryMap.GET_STATE_NAME_BY_ID)));
                ps.setInt(1,stateId);

                set = ps.executeQuery();

                set.next();

                stateClaimOwner=set.getString(Constants.DB_Tables.SF_State.STATE_NAME);
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return stateClaimOwner;
    }
    private String getStartupSQL(@NotNull String type) throws IOException {
        StringBuilder startupSQL= new StringBuilder(Constants.SQL.USE)
                                                    .append(dbName)
                                                    .append(";\n\n");


        switch(type){
            case Constants.Databases.MYSQL:
                InputStream inStream = StateFactions.class.getResourceAsStream(Constants.Resources.DB_STARTUP_MYSQL);
                assert inStream != null;
                BufferedReader br = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8));

                String row;
                while((row=br.readLine())!=null){
                    startupSQL.append(row).append("\n");
                }

                br.close();
                inStream.close();
                break;
        }

        StateFactions.logger.log(new LogRecord(Level.INFO,"----\nSQL Startup String:\n"+startupSQL+"\n\n"));
        return startupSQL.toString();
    }

    private void getDBName(@NotNull String path) throws SQLException {
        StringBuilder db=new StringBuilder();

        if(path.contains("/")){
            db.append(path.split("/")[path.split("/").length-1]);
        }else{
            if(path.contains(":")){
                StateFactions.logger.log(new LogRecord(Level.SEVERE,StateFactions.translationManager.getString(Constants.Localization.Str.Log.Db.NO_DB_SPECIFIED)));
                throw new SQLException();
            }else{
                db.append(path);
            }
        }

        dbName=db.toString();
    }

    private String getSQL(@NotNull String path) throws IOException {
        StringBuilder sql = new StringBuilder();
        StateFactions.logger.log(new LogRecord(Level.INFO,"SQL StringBuilder: "+((sql==null)?"null":"Not null")));

        InputStream inStream = StateFactions.class.getResourceAsStream(path);
        StateFactions.logger.log(new LogRecord(Level.INFO,"InputStream inStream: "+((inStream==null)?"null":"Not null")));
        assert inStream != null;
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream,StandardCharsets.UTF_8));

        String row;
        while((row = br.readLine())!=null){
            sql.append(row).append("\n");
        }

        br.close();
        inStream.close();

        StateFactions.logger.log(new LogRecord(Level.INFO,"\n\nSQL:\n"+sql.toString()));
        return sql.toString();
    }
}
