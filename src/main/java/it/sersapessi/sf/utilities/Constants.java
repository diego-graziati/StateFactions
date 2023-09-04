package it.sersapessi.sf.utilities;

public final class Constants {
    public static final class Utility{
        public static final String PARSING_CHARSET = "§&";
        public static final String PLUGIN_NAME = "StateFactions";

        public static final class DateFormats{
            public static final String AGF = "AGF";
            public static final String EGF = "EGF";
            public static final String IDF = "IDF";
            public static final String JDF = "JDF";
        }
    }
    public static final class ChatStyling{
        public static final class Colors{
            public static final String BLACK = "§0";
            public static final String DARK_BLUE = "§1";
            public static final String DARK_GREEN = "§2";
            public static final String DARK_AQUA = "§3";
            public static final String DARK_RED = "§4";
            public static final String DARK_PURPLE = "§5";
            public static final String GOLD = "§6";
            public static final String GRAY = "§7";
            public static final String DARK_GRAY = "§8";
            public static final String BLUE = "§9";
            public static final String GREEN = "§A";
            public static final String AQUA = "§B";
            public static final String RED = "§C";
            public static final String LIGHT_PURPLE = "§D";
            public static final String YELLOW = "§E";
            public static final String WHITE = "§F";
        }
        public static final class Formatting{
            public static final String STRIKETHROUGH = "§M";
            public static final String UNDERLINE = "§N";
            public static final String BOLD = "§L";
            public static final String RANDOM = "§K";
            public static final String ITALIC = "§O";
            public static final String RESET = "§R";
        }
        public static final class Templates{
            public static final String STATE_TITLE = "---";
        }
    }
    public static final class QueryMap{
        public static final String GET_STATES = "get_states";
        public static final String GET_CITIZENS = "get_citizens";
        public static final String GET_CITIZENSHIPS_REQUESTS = "get_citizenships_requests";
        public static final String GET_STATE_NUM_CLAIMS = "get_state_num_claims";
        public static final String GET_REGISTERED_PLAYERS = "get_registered_players";
        public static final String GET_CLAIMS = "get_claims";
        public static final String SAVE_PLAYERS = "save_players";
        public static final String SAVE_CITIZENS = "save_citizens";
        public static final String SAVE_CIT_REQUESTS = "save_cit_requests";
        public static final String DELETE_ALL_CIT_REQUESTS = "delete_all_cit_requests";
        public static final String SAVE_CLAIMS = "save_claims";
        public static final String SAVE_STATES = "save_states";
    }
    public static final class DB_Tables{
        public static final class SF_Person{
            public static final String PERSON_ID = "PersonId";
            public static final String PERSON_NAME = "PersonName";
            public static final String PERSON_CREDENTIALS = "PersonCredentials";
            public static final String REGISTRATION_DATE = "RegistrationDate";
        }
        public static final class SF_State{
            public static final String STATE_NAME = "StateName";
            public static final String STATE_ID = "StateId";
            public static final String STATE_FOUNDER = "StateFounder";
            public static final String STATE_CREATION_DATE = "StateCreationDate";
        }
        public static final class SF_State_Space{
            public static final String STATE_ID = "StateId";
            public static final String BLOCK_X1 = "BlockX1";
            public static final String BLOCK_Z1 = "BlockZ1";
            public static final String BLOCK_X2 = "BlockX2";
            public static final String BLOCK_Z2 = "BlockZ2";
            public static final String CLAIM_DATE = "ClaimDate";
        }
        public static final class SF_Citizenship {
            public static final String PERSON_ID = "PersonId";
            public static final String STATE_ID = "StateId";
            public static final String IS_STATE_OWNER = "IsStateOwner";
            public static final String IS_CLAIM_RESPONSIBLE = "IsClaimResponsible";
            public static final String STATE_JOIN_DATE = "StateJoinDate";
        }
        public static final class SF_Citizenship_Request{
            public static final String PERSON_ID = "PersonId";
            public static final String STATE_ID = "StateId";
            public static final String REQUEST_DATE = "RequestDate";
        }
        public static final class Temp_Table{
            public static final String NUM_STATE_CLAIMS = "NumStateClaims";
            public static final String NUM_STATE_CITIZENS = "NumStateCitizens";
        }
    }
    public static final class CommandsArgs{
        public static final String STATE = "state";
        public static final String CREATE = "create";
        public static final String REGISTER = "register";
        public static final String LOGIN = "login";
        public static final String FACTION = "faction";
        public static final String CLAIM = "claim";
        public static final String CLAIM_RESP = "claim-resp";
        public static final String CLAIM_RESP_REMOVE = "claim-resp-remove";
        public static final String CLAIM_RESP_RESIGN = "claim-resp-resign";
        public static final String STATE_OWNER = "state-owner";
        public static final String STATE_OWNER_RESIGN = "state-owner-resign";
        public static final String CIT_REQUEST = "cit-request";
        public static final String CIT_REQUEST_ACCEPT = "cit-request-accept";
        public static final String CIT_REQUEST_DENY = "cit-request-deny";
        public static final String CIT_KICK = "cit-kick";
        public static final String INFO = "info";
        public static final class SHORT{
            public static final String CIT_REQUEST_SHORT = "cit-req";
            public static final String CIT_REQUEST_ACCEPT_SHORT = "cit-req-accept";
            public static final String CIT_REQUEST_DENY_SHORT = "cit-req-deny";
            public static final String CLAIM_RESP_REMOVE_SHORT = "cl-resp-rem";
            public static final String CLAIM_RESP_RESIGN_SHORT = "cl-resp-res";
            public static final String STATE_OWNER_RESIGN_SHORT = "st-owner-res";
        }

        public static final class SHORTEST{
            public static final String CLAIM_RESP_SHORTEST = "cresp";
            public static final String CLAIM_RESP_REMOVE_SHORTEST = "crrem";
            public static final String CLAIM_RESP_RESIGN_SHORTEST = "crres";
            public static final String STATE_OWNER_SHORTEST = "so";
            public static final String STATE_OWNER_RESIGN_SHORTEST = "sores";
            public static final String CIT_REQUEST_SHORTEST = "cr";
            public static final String CIT_REQUEST_ACCEPT_SHORTEST = "cra";
            public static final String CIT_REQUEST_DENY_SHORTEST = "crd";
        }
    }
    public static final class Configs{
        public static final String LANG = "lang";
        public static final String DBMS = "supported-DBMS";
        public static final String DBMS_USER = "DBMS-user";
        public static final String DBMS_PWD = "DBMS-pwd";
        public static final String DB_PATH = "path-DB";
        public static final String DATE_FORMAT = "date-format";
        public static final String CLAIM_TAX = "claim-tax";
        public static final String CLAIM_PROTECTION = "claim-protection";
        public static final String DISABLE_EXPLOSIONS = "disable-explosions";

        public static class Fallback{
            public static final int CLAIM_TAX = 10000;
            public static final String DATE_FORMAT = Utility.DateFormats.IDF;
        }
    }

    public static final class Languages{
        public static final String EN_US = "en-US";
        public static final String IT_IT = "it-IT";
        public static final String FALLBACK = EN_US;
    }

    public static final class Databases{
        public static final String MYSQL= "mysql";
    }
    public static final class SQL{
        public static final String CREATE_DB_IF_NOTEXISTS = "CREATE DATABASE IF NOT EXISTS ";
        public static final String USE = "USE ";
        public static final String ALLOW_MULTIQUERIES_TRUE = "allowMultiQueries=true";
    }

    public static final class Localization{
        public static final class Str{
            public static final class Log{
                public static final String LANG_LOGGER_CONFIG_STARTUP_OK = "string.log.lang-logger-config-startup.ok";
                public static final String STARTUP_PROCESS_SUCCESSFUL = "string.log.startup-successful";
                public static final String LOGGEDIN_PLAYERS_LIST_INIT= "string.log.loggedin-players-list-init";
                public static final String ONLINE_PLAYERS_LIST_INIT = "string.log.online-players-list-init";

                public static final class Db{
                    public static final class Startup{
                        public static final String SQL_EXCEPTION = "string.log.db.startup.sql-exception";
                        public static final String IO_EXCEPTION = "string.log.db.startup.io-exception";
                        public static final String SUCCESS = "string.log.db.startup.ok";
                    }

                    public static final String NO_DB_SPECIFIED = "string.log.db.no-db-specified";
                    public static final String FAILED_TO_CHECK_IF_PERSON_EXISTS= "string.log.db.failed-to-check-if-person-exists";
                    public static final String FAILED_TO_REGISTER_PERSON= "string.log.db.failed-to-register-person";
                    public static final String SUCCESSFUL_CHECK_IF_PERSON_EXISTS= "string.log.db.successful-check-if-person-exists";
                    public static final String SUCCESSFUL_CHECK_IF_STATE_EXISTS = "string.log.db.successful-check-if-state-exists";
                    public static final String SUCCESSFUL_PERSON_REGISTRATION= "string.log.db.successful-person-registration";
                    public static final String SUCCESSFUL_PWD_CHECK= "string.log.db.successful-pwd-check";
                    public static final String SUCCESSFUL_STATE_CREATION = "string.log.db.successful-state-creation";
                    public static final String FAILED_PWD_CHECK= "string.log.db.failed-pwd-check";
                    public static final String FAILED_ID_CHECK = "string.log.db.failed-id-check";
                    public static final String FAILED_STATE_CREATION = "string.log.db.failed-state-creation";
                }
            }
            public static final class Command{
                public static final class Error{
                    public static final String PWD_CONTAINS_BLANK_SPACES = "string.command.error.pwd-contains-blank-spaces";
                    public static final String PWD_TOO_LONG = "string.command.error.pwd-too-long";
                    public static final String PWD_CONFIRMPWD_NOT_EQUAL= "string.command.error.pwd-confirmpwd-not-equal";
                    public static final String ALREADY_REGISTERED= "string.command.error.already-registered";
                    public static final String TOO_MANY_WORDS= "string.command.error.too-many-words";
                    public static final String WRONG_PWD= "string.command.error.wrong-pwd";
                    public static final String YOU_MUST_BE_A_PLAYER = "string.command.error.you-must-be-a-player";
                    public static final String ALREADY_LOGGEDIN= "string.command.error.already-loggedin";
                    public static final String NOT_ENOUGH_ARGS= "string.command.error.not-enough-args";
                    public static final String NOT_REGISTERED = "string.command.error.not-registered";
                    public static final String NOT_LOGGEDIN = "string.command.error.not-loggedin";
                    public static final String NO_COORDS_SPECIFIED = "string.command.error.no-coords-specified";
                    public static final String STATE_ALREADY_EXISTS = "string.command.error.state-already-exists";
                    public static final String STATE_DOESNT_EXISTS = "string.command.error.state-doesnt-exists";
                    public static final String ILLEGAL_CHAR = "string.command.error.illegal-char";
                    public static final String AREA_ALREADY_CLAIMED_SINGLE_STATE= "string.command.error.area-already-claimed.single-state";
                    public static final String YOU_MUST_INSERT_COORDS_CORRECTLY= "string.command.error.you-must-insert-coords-correctly";
                    public static final String YOU_MUST_CLAIM_ONE_BLOCK = "string.command.error.you-must-claim-one-block";
                    public static final String ALREADY_A_CITIZEN = "string.command.error.already-a-citizen";
                    public static final String PERSON_ALREADY_A_CITIZEN = "string.command.error.person-already-a-citizen";
                    public static final String CIT_REQ_ALREADY_SENT = "string.command.error.cit-req-already-sent";
                    public static final String PERSON_DOESNT_EXISTS = "string.command.error.person-doesnt-exists";
                    public static final String NOT_A_CITIZEN = "string.command.error.not-a-citizen";
                    public static final String PERSON_NOT_A_CITIZEN = "string.command.error.person-not-a-citizen";
                    public static final String PERSON_HAS_NO_CIT_REQ_SENT = "string.command.error.person-has-no-cit-req-sent";
                    public static final String PERSON_NOT_STATE_OWNER = "string.command.error.person-not-state-owner";
                    public static final String PERSON_IS_STATE_OWNER = "string.command.error.person-is-state-owner";
                    public static final String NOT_A_CLAIM_RESPONSIBLE = "string.command.error.not-a-claim-responsible";
                    public static final String ALREADY_CLAIM_RESPONSIBLE= "string.command.error.already-claim-responsible";
                    public static final String PERSON_ALREADY_STATE_OWNER = "string.command.error.person-already-state-owner";
                    public static final String PERSON_NOT_A_CLAIM_RESPONSIBLE = "string.command.error.person-not-a-claim-responsible";
                    public static final String TOO_MANY_CLAIMS = "string.command.error.too-many-claims";
                    public static final String YET_TO_BE_IMPLEMENTED = "string.command.error.yet-to-be-implemented";
                }
                public static final class Success{
                    public static final String PLAYER_REGISTERED = "string.command.success.player-registered";
                    public static final String PLAYER_LOGGEDIN = "string.command.success.player-loggedin";
                    public static final String STATE_CREATED = "string.command.success.state-created";
                    public static final String CLAIM_CREATED= "string.command.success.claim-created";
                    public static final String CIT_REQ_SENT= "string.command.success.cit-req-sent";
                    public static final String CIT_REQ_ACCEPTED = "string.command.success.cit-req-accepted";
                    public static final String CIT_REQ_DENIED = "string.command.success.cit-req-denied";
                    public static final String CIT_KICKED = "string.command.success.cit-kicked";
                    public static final String CIT_PROMOTED_CLAIM_RESPONSIBLE = "string.command.success.cit-promoted-claim-responsible";
                    public static final String CIT_PROMOTED_STATE_OWNER = "string.command.success.cit-promoted-state-owner";
                    public static final String NEW_CIT = "string.command.success.new-cit";
                    public static final String CIT_DENIED = "string.command.success.cit-denied";
                    public static final String BEING_KICKED = "string.command.success.being-kicked";
                    public static final String PROMOTED_CLAIM_RESPONSIBLE = "string.command.success.promoted-claim-responsible";
                    public static final String PROMOTED_STATE_OWNER = "string.command.success.promoted-state-owner";
                    public static final String STATE_OWNER_RESIGN = "string.command.success.state-owner-resign";
                    public static final String CLAIM_RESPONSIBLE_RESIGN = "string.command.success.claim-responsible-resign";
                    public static final String REMOVE_CLAIM_RESPONSIBLE = "string.command.success.remove-claim-responsible";
                    public static final String LOST_CLAIM_RESPONSIBLE = "string.command.success.lost-claim-responsible";
                }
            }
            public static final class Title{
                public static final String EXITING_STATE = "string.title.exiting-state";
            }
            public static final class Event{
                public static final class Error{
                    public static final String BLOCK_PLACED_ON_OTHERS_CLAIM = "string.event.error.block-placed-on-others-claim";
                    public static final String BREAKING_BLOCK_ON_OTHERS_CLAIM = "string.event.error.breaking-block-on-others-claim";
                    public static final String BURNING_THINGS_ON_OTHERS_CLAIM= "string.event.error.burning-things-on-others-claim";
                }
            }
            public static final class State{
                public static final class Info{
                    public static final String ID = "string.state.info.id";
                    public static final String FOUNDER = "string.state.info.founder";
                    public static final String CREATION_DATE = "string.state.info.creation-date";
                    public static final String CLAIMS_NUMBER = "string.state.info.claims-number";
                    public static final String CITIZENS_NUMBER = "string.state.info.citizens-number";
                }
            }
        }
    }

    public static final class Resources{

        public static final class InternalResPaths{
            public static final class Lang{
                public static final String LANG_FOLDER = "/lang";
                public static final String EN_US = LANG_FOLDER+"/en-US.json";
                public static final String IT_IT = LANG_FOLDER+"/it-IT.json";
            }
        }
        public static final class ExportedResPaths{
            public static final class Lang{
                public static final String LANG_FOLDER ="/lang";
            }
        }
        public static final String DB_RESOURCES_FOLDER = "/db_resources";

        public static final String DB_STARTUP_MYSQL = DB_RESOURCES_FOLDER+"/StartupQuery_mysql.sql";
        public static final class MYSQL{
            public static final String DB_MYSQL_FOLDER = DB_RESOURCES_FOLDER+"/mysql";
            public static final String DB_GET_STATES = DB_MYSQL_FOLDER+"/GetStates_query_sql.sql";
            public static final String DB_GET_CITIZENS = DB_MYSQL_FOLDER+"/GetCitizens_query_sql.sql";
            public static final String DB_GET_CITIZENSHIPS_REQUESTS = DB_MYSQL_FOLDER+"/GetCitizenshipsRequests_query_sql.sql";
            public static final String DB_GET_STATE_NUM_CLAIMS = DB_MYSQL_FOLDER+"/GetStateNumClaims_query_sql.sql";
            public static final String DB_GET_REGISTERED_PLAYERS = DB_MYSQL_FOLDER+"/GetRegisteredPlayers_query_sql.sql";
            public static final String DB_GET_CLAIMS = DB_MYSQL_FOLDER+"/GetClaims_query_sql.sql";
            public static final String DB_SAVE_PLAYERS = DB_MYSQL_FOLDER+"/SavePlayers_query_sql.sql";
            public static final String DB_SAVE_CITIZENS = DB_MYSQL_FOLDER+"/SaveCitizens_query_sql.sql";
            public static final String DB_SAVE_CIT_REQUESTS = DB_MYSQL_FOLDER+"/SaveCitRequests_query_sql.sql";
            public static final String DB_DELETE_ALL_CIT_REQUESTS = DB_MYSQL_FOLDER+"/DeleteAllCitRequests_query_sql.sql";
            public static final String DB_SAVE_CLAIMS = DB_MYSQL_FOLDER+"/SaveClaims_query_sql.sql";
            public static final String DB_SAVE_STATES = DB_MYSQL_FOLDER+"/SaveStates_query_sql.sql";
        }
    }
}
