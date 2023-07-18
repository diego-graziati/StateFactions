package it.sersapessi.sf.utilities;

import it.sersapessi.sf.StateFactions;
import it.sersapessi.sf.utilities.models.*;
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

                queryMap.put(Constants.QueryMap.GET_STATES,getSQL(Constants.Resources.MYSQL.DB_GET_STATES));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test1"));
                queryMap.put(Constants.QueryMap.GET_CITIZENS,getSQL(Constants.Resources.MYSQL.DB_GET_CITIZENS));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test2"));
                queryMap.put(Constants.QueryMap.GET_CITIZENSHIPS_REQUESTS,getSQL(Constants.Resources.MYSQL.DB_GET_CITIZENSHIPS_REQUESTS));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test3"));
                queryMap.put(Constants.QueryMap.GET_STATE_NUM_CLAIMS,getSQL(Constants.Resources.MYSQL.DB_GET_STATE_NUM_CLAIMS));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test4"));
                queryMap.put(Constants.QueryMap.GET_REGISTERED_PLAYERS,getSQL(Constants.Resources.MYSQL.DB_GET_REGISTERED_PLAYERS));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test5"));
                queryMap.put(Constants.QueryMap.GET_CLAIMS,getSQL(Constants.Resources.MYSQL.DB_GET_CLAIMS));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test6"));
                queryMap.put(Constants.QueryMap.SAVE_PLAYERS,getSQL(Constants.Resources.MYSQL.DB_SAVE_PLAYERS));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test7"));
                queryMap.put(Constants.QueryMap.SAVE_CITIZENS,getSQL(Constants.Resources.MYSQL.DB_SAVE_CITIZENS));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test8"));
                queryMap.put(Constants.QueryMap.SAVE_CIT_REQUESTS,getSQL(Constants.Resources.MYSQL.DB_SAVE_CIT_REQUESTS));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test9"));
                queryMap.put(Constants.QueryMap.DELETE_ALL_CIT_REQUESTS,getSQL(Constants.Resources.MYSQL.DB_DELETE_ALL_CIT_REQUESTS));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test10"));
                queryMap.put(Constants.QueryMap.SAVE_CLAIMS,getSQL(Constants.Resources.MYSQL.DB_SAVE_CLAIMS));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test11"));
                queryMap.put(Constants.QueryMap.SAVE_STATES,getSQL(Constants.Resources.MYSQL.DB_SAVE_STATES));
                StateFactions.logger.log(new LogRecord(Level.INFO,"Test12"));

                break;
        }
    }

    public HashMap<Person,String> retrievePlayersData(){
        try{
            HashMap<Person,String> players = new HashMap<>();

            Statement stmt = db.createStatement();
            ResultSet players_set = stmt.executeQuery(queryMap.get(Constants.QueryMap.GET_REGISTERED_PLAYERS));

            if(players_set!=null && players_set.next()){ //Se ci sono giocatori salvati, estraggo le loro informazioni. Altrimenti ignoro.
                //Salvo il primo giocatore.
                players.put(new Person(players_set.getInt(Constants.DB_Tables.SF_Person.PERSON_ID),
                                        players_set.getString(Constants.DB_Tables.SF_Person.PERSON_NAME),
                                        players_set.getTimestamp(Constants.DB_Tables.SF_Person.REGISTRATION_DATE)),
                            players_set.getString(Constants.DB_Tables.SF_Person.PERSON_CREDENTIALS));

                //Salvo tutti gli altri giocatori.
                while(players_set.next()){
                    players.put(new Person(players_set.getInt(Constants.DB_Tables.SF_Person.PERSON_ID),
                                    players_set.getString(Constants.DB_Tables.SF_Person.PERSON_NAME),
                                    players_set.getTimestamp(Constants.DB_Tables.SF_Person.REGISTRATION_DATE)),
                            players_set.getString(Constants.DB_Tables.SF_Person.PERSON_CREDENTIALS));
                }

                players_set.close();
            }

            return players;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //This method retrieves all data saved inside the database and loads them into the respective handlers.
    public ArrayList<State> retrieveStateData() {
        try{
            ArrayList<State> states = new ArrayList<>();

            Statement stateStmt = db.createStatement();
            Statement citStmt = db.createStatement();
            Statement citReqStmt = db.createStatement();
            ResultSet states_set = stateStmt.executeQuery(queryMap.get(Constants.QueryMap.GET_STATES));
            ResultSet citizens_set = citStmt.executeQuery(queryMap.get(Constants.QueryMap.GET_CITIZENS));
            ResultSet citizenships_req_set = citReqStmt.executeQuery(queryMap.get(Constants.QueryMap.GET_CITIZENSHIPS_REQUESTS));

            if(states_set!=null && states_set.next()){ //esiste almeno uno stato

                if(citizens_set!=null && citizens_set.next()){ //esiste almeno un cittadino per uno stato
                    HashMap<Integer, ArrayList<Citizen>> citizens = new HashMap<>();

                    ArrayList<Citizen> tempCitizens = new ArrayList<>();
                    tempCitizens.add(new Citizen(citizens_set.getInt(Constants.DB_Tables.SF_Citizenship.PERSON_ID),
                                    StateFactions.peopleHandler.getPlayerName(citizens_set.getInt(Constants.DB_Tables.SF_Citizenship.PERSON_ID)),
                                    citizens_set.getBoolean(Constants.DB_Tables.SF_Citizenship.IS_STATE_OWNER),
                                    citizens_set.getBoolean(Constants.DB_Tables.SF_Citizenship.IS_CLAIM_RESPONSIBLE),
                                    citizens_set.getTimestamp(Constants.DB_Tables.SF_Citizenship.STATE_JOIN_DATE)));
                    citizens.put(citizens_set.getInt(Constants.DB_Tables.SF_Citizenship.STATE_ID),
                                tempCitizens);

                    while(citizens_set.next()){
                        //Se esiste di già lo StateId vuol dire che esiste di già un cittadino e, pertanto, bisogna semplicemente aggiungerne un altro.
                        if(citizens.containsKey(citizens_set.getInt(Constants.DB_Tables.SF_Citizenship.STATE_ID))){
                            citizens.get(citizens_set.getInt(Constants.DB_Tables.SF_Citizenship.STATE_ID))
                                    .add(new Citizen(citizens_set.getInt(Constants.DB_Tables.SF_Citizenship.PERSON_ID),
                                            StateFactions.peopleHandler.getPlayerName(citizens_set.getInt(Constants.DB_Tables.SF_Citizenship.PERSON_ID)),
                                            citizens_set.getBoolean(Constants.DB_Tables.SF_Citizenship.IS_STATE_OWNER),
                                            citizens_set.getBoolean(Constants.DB_Tables.SF_Citizenship.IS_CLAIM_RESPONSIBLE),
                                            citizens_set.getTimestamp(Constants.DB_Tables.SF_Citizenship.STATE_JOIN_DATE)));
                        }else{ //Se lo stato non esiste, allora vuol dire che non c'è alcun cittadino e bisogna ricreare tutto.
                            tempCitizens = new ArrayList<>();
                            tempCitizens.add(new Citizen(citizens_set.getInt(Constants.DB_Tables.SF_Citizenship.PERSON_ID),
                                    StateFactions.peopleHandler.getPlayerName(citizens_set.getInt(Constants.DB_Tables.SF_Citizenship.PERSON_ID)),
                                    citizens_set.getBoolean(Constants.DB_Tables.SF_Citizenship.IS_STATE_OWNER),
                                    citizens_set.getBoolean(Constants.DB_Tables.SF_Citizenship.IS_CLAIM_RESPONSIBLE),
                                    citizens_set.getTimestamp(Constants.DB_Tables.SF_Citizenship.STATE_JOIN_DATE)));
                            citizens.put(citizens_set.getInt(Constants.DB_Tables.SF_Citizenship.STATE_ID),
                                    tempCitizens);
                        }
                    }

                    //Ottengo i cittadini del primo stato del set stati, il numero dei suoi claim e le informazioni necessarie.
                    if(citizens.containsKey(states_set.getInt(Constants.DB_Tables.SF_State.STATE_ID))){
                        tempCitizens = citizens.get(states_set.getInt(Constants.DB_Tables.SF_State.STATE_ID));
                    }else{
                        tempCitizens = new ArrayList<>();
                    }

                    PreparedStatement pStmt = db.prepareStatement(queryMap.get(Constants.QueryMap.GET_STATE_NUM_CLAIMS));
                    pStmt.setInt(1,states_set.getInt(Constants.DB_Tables.SF_State_Space.STATE_ID));

                    ResultSet stateClaims = pStmt.executeQuery();

                    int numStateClaims=0;
                    if(stateClaims.next()){
                        numStateClaims = stateClaims.getInt(Constants.DB_Tables.Temp_Table.NUM_STATE_CLAIMS);
                    }

                    states.add(new State(states_set.getInt(Constants.DB_Tables.SF_State.STATE_ID),
                            states_set.getString(Constants.DB_Tables.SF_State.STATE_NAME),
                            StateFactions.peopleHandler.getPlayerName(states_set.getInt(Constants.DB_Tables.SF_State.STATE_FOUNDER)),
                            states_set.getTimestamp(Constants.DB_Tables.SF_State.STATE_CREATION_DATE),
                            numStateClaims, tempCitizens));

                    //Ottengo tutti gli altri stati
                    while(states_set.next()){
                        if(citizens.containsKey(states_set.getInt(Constants.DB_Tables.SF_State.STATE_ID))){
                            tempCitizens = citizens.get(states_set.getInt(Constants.DB_Tables.SF_State.STATE_ID));
                        }else{
                            tempCitizens = new ArrayList<>();
                        }

                        pStmt = db.prepareStatement(queryMap.get(Constants.QueryMap.GET_STATE_NUM_CLAIMS));
                        pStmt.setInt(1,states_set.getInt(Constants.DB_Tables.SF_State_Space.STATE_ID));

                        stateClaims = pStmt.executeQuery();

                        numStateClaims=0;
                        if(stateClaims.next()){
                            numStateClaims = stateClaims.getInt(Constants.DB_Tables.Temp_Table.NUM_STATE_CLAIMS);
                        }

                        states.add(new State(states_set.getInt(Constants.DB_Tables.SF_State.STATE_ID),
                                states_set.getString(Constants.DB_Tables.SF_State.STATE_NAME),
                                StateFactions.peopleHandler.getPlayerName(states_set.getInt(Constants.DB_Tables.SF_State.STATE_FOUNDER)),
                                states_set.getTimestamp(Constants.DB_Tables.SF_State.STATE_CREATION_DATE),
                                numStateClaims, tempCitizens));
                    }

                    citizens_set.close();
                }else{ //Non esiste alcun cittadino per nessuno stato
                    ArrayList<Citizen> citizens = new ArrayList<>();

                    PreparedStatement pStmt = db.prepareStatement(queryMap.get(Constants.QueryMap.GET_STATE_NUM_CLAIMS));
                    pStmt.setInt(1,states_set.getInt(Constants.DB_Tables.SF_State_Space.STATE_ID));

                    ResultSet stateClaims = pStmt.executeQuery();

                    int numStateClaims=0;
                    if(stateClaims.next()){
                        numStateClaims = stateClaims.getInt(Constants.DB_Tables.Temp_Table.NUM_STATE_CLAIMS);
                    }

                    states.add(new State(states_set.getInt(Constants.DB_Tables.SF_State.STATE_ID),
                            states_set.getString(Constants.DB_Tables.SF_State.STATE_NAME),
                            StateFactions.peopleHandler.getPlayerName(states_set.getInt(Constants.DB_Tables.SF_State.STATE_FOUNDER)),
                            states_set.getTimestamp(Constants.DB_Tables.SF_State.STATE_CREATION_DATE),
                            numStateClaims, citizens));
                    while(states_set.next()){
                        pStmt = db.prepareStatement(queryMap.get(Constants.QueryMap.GET_STATE_NUM_CLAIMS));
                        pStmt.setInt(1,states_set.getInt(Constants.DB_Tables.SF_State_Space.STATE_ID));

                        stateClaims = pStmt.executeQuery();

                        numStateClaims=0;
                        if(stateClaims.next()){
                            numStateClaims = stateClaims.getInt(Constants.DB_Tables.Temp_Table.NUM_STATE_CLAIMS);
                        }

                        states.add(new State(states_set.getInt(Constants.DB_Tables.SF_State.STATE_ID),
                                states_set.getString(Constants.DB_Tables.SF_State.STATE_NAME),
                                StateFactions.peopleHandler.getPlayerName(states_set.getInt(Constants.DB_Tables.SF_State.STATE_FOUNDER)),
                                states_set.getTimestamp(Constants.DB_Tables.SF_State.STATE_CREATION_DATE),
                                numStateClaims, citizens));
                    }
                }
                states_set.close();
            }

            if(!states.isEmpty()){//Se non ci sono stati tanto vale non andare a riempire le richieste di cittadinanza. È inutile.
                if(citizenships_req_set!=null && citizenships_req_set.next()){//Se ci sono richieste di cittadinanza bene, controllo, altrimenti ignoro.
                    HashMap<Integer,ArrayList<CitizenshipRequest>> citRequests = new HashMap<>();

                    ArrayList<CitizenshipRequest> tempCitRequests = new ArrayList<>();
                    tempCitRequests.add(new CitizenshipRequest(citizenships_req_set.getInt(Constants.DB_Tables.SF_Citizenship_Request.PERSON_ID),
                                        StateFactions.peopleHandler.getPlayerName(citizenships_req_set.getInt(Constants.DB_Tables.SF_Citizenship_Request.PERSON_ID)),
                                        citizenships_req_set.getTimestamp(Constants.DB_Tables.SF_Citizenship_Request.REQUEST_DATE)));
                    citRequests.put(citizenships_req_set.getInt(Constants.DB_Tables.SF_Citizenship_Request.STATE_ID),
                            tempCitRequests);

                    while(citizenships_req_set.next()){
                        if(citRequests.containsKey(citizenships_req_set.getInt(Constants.DB_Tables.SF_Citizenship_Request.STATE_ID))){
                            citRequests.get(citizenships_req_set.getInt(Constants.DB_Tables.SF_Citizenship_Request.STATE_ID))
                                    .add(new CitizenshipRequest(citizenships_req_set.getInt(Constants.DB_Tables.SF_Citizenship_Request.PERSON_ID),
                                                                StateFactions.peopleHandler.getPlayerName(citizenships_req_set.getInt(Constants.DB_Tables.SF_Citizenship_Request.PERSON_ID)),
                                            citizenships_req_set.getTimestamp(Constants.DB_Tables.SF_Citizenship_Request.REQUEST_DATE)));
                        }else{
                            tempCitRequests = new ArrayList<>();
                            tempCitRequests.add(new CitizenshipRequest(citizenships_req_set.getInt(Constants.DB_Tables.SF_Citizenship_Request.PERSON_ID),
                                    StateFactions.peopleHandler.getPlayerName(citizenships_req_set.getInt(Constants.DB_Tables.SF_Citizenship_Request.PERSON_ID)),
                                    citizenships_req_set.getTimestamp(Constants.DB_Tables.SF_Citizenship_Request.REQUEST_DATE)));
                            citRequests.put(citizenships_req_set.getInt(Constants.DB_Tables.SF_Citizenship_Request.STATE_ID),
                                    tempCitRequests);
                        }
                    }

                    //Riempiamo gli stati con le richieste di cittadinanza, se ci sono.
                    for(State state : states){
                        if(citRequests.containsKey(state.getStateId())){
                            tempCitRequests = citRequests.get(state.getStateId());

                            state.setCitRequests(tempCitRequests);
                        }
                    }

                    citizenships_req_set.close();
                }
            }

            return states;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public HashMap<ClaimSector,Integer> retrieveClaimsData(){
        try{
            HashMap<ClaimSector,Integer> claims = new HashMap<>();

            Statement stmt = db.createStatement();
            ResultSet claims_set = stmt.executeQuery(queryMap.get(Constants.QueryMap.GET_CLAIMS));

            //Se ci sono claims salvati procedo. Altrimenti ignoro.
            if(claims_set!=null && claims_set.next()){
                //Salvo il primo claim.
                claims.put(new ClaimSector(claims_set.getInt(Constants.DB_Tables.SF_State_Space.BLOCK_X1),
                                            claims_set.getInt(Constants.DB_Tables.SF_State_Space.BLOCK_Z1),
                                            claims_set.getInt(Constants.DB_Tables.SF_State_Space.BLOCK_X2),
                                            claims_set.getInt(Constants.DB_Tables.SF_State_Space.BLOCK_Z2),
                                            claims_set.getTimestamp(Constants.DB_Tables.SF_State_Space.CLAIM_DATE)),
                            claims_set.getInt(Constants.DB_Tables.SF_State_Space.STATE_ID));

                //Salvo tutti gli altri claim.
                while(claims_set.next()){
                    claims.put(new ClaimSector(claims_set.getInt(Constants.DB_Tables.SF_State_Space.BLOCK_X1),
                                    claims_set.getInt(Constants.DB_Tables.SF_State_Space.BLOCK_Z1),
                                    claims_set.getInt(Constants.DB_Tables.SF_State_Space.BLOCK_X2),
                                    claims_set.getInt(Constants.DB_Tables.SF_State_Space.BLOCK_Z2),
                                    claims_set.getTimestamp(Constants.DB_Tables.SF_State_Space.CLAIM_DATE)),
                            claims_set.getInt(Constants.DB_Tables.SF_State_Space.STATE_ID));
                }

                claims_set.close();
            }

            return claims;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void savePlayersData(HashMap<Person, String> registeredPlayers){
        try{
            int numValues = registeredPlayers.size();

            if(numValues>0){
                StringBuilder querySql = new StringBuilder(queryMap.get(Constants.QueryMap.SAVE_PLAYERS));

                for(int i=0; i<numValues; ){
                    if(i==0){
                        querySql.append("\nVALUES(?,?,?,?)");
                    }else{
                        querySql.append("\n(?,?,?,?)");
                    }

                    i++;
                    if(i==numValues){
                        querySql.append(";");
                    }else{
                        querySql.append(",");
                    }
                }

                Object[] players_obj = registeredPlayers.keySet().toArray();

                PreparedStatement pStmt = db.prepareStatement(querySql.toString());

                for(int i=0, y=1; i<numValues; i++){
                    Person player = (Person) players_obj[i];

                    String pwd = registeredPlayers.get(player);

                    pStmt.setInt(y,player.getPersonId());
                    y++;

                    pStmt.setString(y,player.getName());
                    y++;

                    pStmt.setString(y,pwd);
                    y++;

                    pStmt.setTimestamp(y,player.getJoinDate());
                    y++;
                }

                pStmt.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void saveStatesData(ArrayList<State> states){
        try{
            int numStates = states.size();

            if(numStates>0){
                StringBuilder querySql = new StringBuilder(queryMap.get(Constants.QueryMap.SAVE_STATES));

                for(int i=0; i<numStates; ){

                    if(i==0){
                        querySql.append("\nVALUES(?,?,?,?)");
                    }else{
                        querySql.append("\n(?,?,?,?)");
                    }

                    i++;
                    if(i==numStates){
                        querySql.append(";");
                    }else{
                        querySql.append(",");
                    }
                }

                PreparedStatement pStmt = db.prepareStatement(querySql.toString());

                for(int i=0, y=1; i<numStates; i++){

                    pStmt.setInt(y,states.get(i).getStateId());
                    y++;

                    pStmt.setString(y,states.get(i).getStateName());
                    y++;

                    pStmt.setInt(y,StateFactions.peopleHandler.getPlayerId(states.get(i).getStateFounder()));
                    y++;

                    pStmt.setTimestamp(y,states.get(i).getCreationDate());
                    y++;
                }

                pStmt.execute();

                querySql = new StringBuilder(queryMap.get(Constants.QueryMap.SAVE_CITIZENS));

                for(int i=0; i<numStates; ){
                    State state = states.get(i);

                    for(int y=0; y<state.getNumCitizens(); y++){

                        if(i==0){
                            querySql.append("\nVALUES(?,?,?,?,?)");
                        }else{
                            querySql.append("\n(?,?,?,?,?)");
                        }

                        if(i<numStates-1 && y<state.getNumCitizens()-1){
                            querySql.append(",");
                        }
                    }

                    i++;
                    if(i==numStates){
                        querySql.append(";");
                    }
                }

                if(!querySql.toString().equals(queryMap.get(Constants.QueryMap.SAVE_CITIZENS)) && !querySql.toString().equals(queryMap.get(Constants.QueryMap.SAVE_CITIZENS)+";")){


                    pStmt = db.prepareStatement(querySql.toString());

                    for(int i=0, y=1; i<numStates; i++){

                        State state = states.get(i);

                        for(int k=0; k<state.getNumCitizens(); k++){
                            pStmt.setInt(y,states.get(i).getStateId());
                            y++;

                            pStmt.setInt(y,StateFactions.peopleHandler.getPlayerId(states.get(i).getCitizens().get(k).getName()));
                            y++;

                            pStmt.setBoolean(y,states.get(i).getCitizens().get(k).isStateOwner());
                            y++;

                            pStmt.setBoolean(y,states.get(i).getCitizens().get(k).isClaimResponsible());
                            y++;

                            pStmt.setTimestamp(y,states.get(i).getCitizens().get(k).getJoinDate());
                            y++;
                        }
                    }

                    pStmt.execute();
                }

                querySql = new StringBuilder(queryMap.get(Constants.QueryMap.DELETE_ALL_CIT_REQUESTS));

                Statement stmt = db.createStatement();
                stmt.execute(querySql.toString());

                querySql = new StringBuilder(queryMap.get(Constants.QueryMap.SAVE_CIT_REQUESTS));

                for(int i=0; i<numStates; ){
                    State state = states.get(i);

                    for(int y=0; y<state.getCitRequests().size(); y++){

                        if(i==0){
                            querySql.append("\nVALUES(?,?,?)");
                        }else{
                            querySql.append("\n(?,?,?)");
                        }

                        if(i<numStates-1 && y<state.getCitRequests().size()-1){
                            querySql.append(",");
                        }
                    }

                    i++;
                    if(i==numStates){
                        querySql.append(";");
                    }
                }

                if(!querySql.toString().equals(queryMap.get(Constants.QueryMap.SAVE_CIT_REQUESTS)) && !querySql.toString().equals(queryMap.get(Constants.QueryMap.SAVE_CIT_REQUESTS)+";")){

                    pStmt = db.prepareStatement(querySql.toString());

                    for(int i=0, y=1; i<numStates; i++){

                        State state = states.get(i);

                        for(int k=0; k<state.getCitRequests().size(); k++){
                            pStmt.setInt(y,states.get(i).getStateId());
                            y++;

                            pStmt.setInt(y,StateFactions.peopleHandler.getPlayerId(states.get(i).getCitRequests().get(k).getPersonName()));
                            y++;

                            pStmt.setTimestamp(y,states.get(i).getCitRequests().get(k).getRequestDate());
                            y++;
                        }
                    }

                    pStmt.execute();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void saveClaimsData(HashMap<ClaimSector, Integer> claims){
        try{
            int numClaims = claims.size();

            if(numClaims>0){
                StringBuilder querySql = new StringBuilder(queryMap.get(Constants.QueryMap.SAVE_CLAIMS));

                for(int i=0; i<numClaims; ){
                    if(i==0){
                        querySql.append("\nVALUES(?,?,?,?,?,?)");
                    }else{
                        querySql.append("\n(?,?,?,?,?,?)");
                    }

                    i++;
                    if(i==numClaims){
                        querySql.append(";");
                    }else{
                        querySql.append(",");
                    }
                }

                Object[] claims_obj = claims.keySet().toArray();

                PreparedStatement pStmt = db.prepareStatement(querySql.toString());

                for(int i=0, y=1; i<claims_obj.length; i++){
                    ClaimSector claim = (ClaimSector) claims_obj[i];

                    pStmt.setInt(y,claims.get(claim));
                    y++;

                    pStmt.setInt(y,claim.getX1());
                    y++;

                    pStmt.setInt(y,claim.getZ1());
                    y++;

                    pStmt.setInt(y,claim.getX2());
                    y++;

                    pStmt.setInt(y,claim.getZ2());
                    y++;

                    pStmt.setTimestamp(y,claim.getClaimDate());
                    y++;
                }

                pStmt.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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

        InputStream inStream = StateFactions.class.getResourceAsStream(path);
        assert inStream != null;
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream,StandardCharsets.UTF_8));

        String row;
        while((row = br.readLine())!=null){
            sql.append(row).append("\n");
        }

        br.close();
        inStream.close();

        return sql.toString();
    }
}
