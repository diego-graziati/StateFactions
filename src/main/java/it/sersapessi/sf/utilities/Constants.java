package it.sersapessi.sf.utilities;

public final class Constants {
    public static final class Utility{
        public static final String PARSING_CHARSET = "§&";
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
    }
    public static final class QueryMap{
        public static final String INSERT_NEW_STATE = "insert_new_state";
        public static final String GET_PERSON_ID = "get_person_id";
        public static final String CHECK_IF_PERSON_EXISTS = "check_if_person_exists";
        public static final String INSERT_NEW_PERSON = "insert_new_person";
        public static final String GET_PERSON_CREDENTIALS = "get_person_credentials";
        public static final String CHECK_IF_STATE_EXISTS = "check_if_state_exists";
        public static final String CREATE_CLAIM = "create_claim";
        public static final String GET_CLAIM_OWNER = "get_claim_owner";
        public static final String GET_STATE_NAME_BY_ID = "get_state_name_by_id";
        public static final String GET_STATE_ID_BY_NAME = "get_state_id_by_name";
    }
    public static final class DB_Tables{
        public static final class SF_Person{
            public static final String PERSON_ID = "PersonId";
            public static final String PERSON_NAME = "PersonName";
            public static final String PERSON_CREDENTIALS = "PersonCredentials";
        }
        public static final class SF_State{
            public static final String STATE_NAME = "StateName";
            public static final String STATE_ID = "StateId";
        }
        public static final class SF_State_Space{
            public static final String STATE_ID = "StateId";
            public static final String BLOCK_X = "BlockX";
            public static final String BLOCK_Z = "BlockZ";
        }
    }
    public static final class CommandsArgs{
        public static final String STATE = "state";
        public static final String CREATE = "create";
        public static final String REGISTER = "register";
        public static final String LOGIN = "login";
        public static final String FACTION = "faction";
        public static final String CLAIM = "claim";
    }
    public static final class Configs{
        public static final String LANG = "lang";
        public static final String DBMS = "supported-DBMS";
        public static final String DBMS_USER = "DBMS-user";
        public static final String DBMS_PWD = "DBMS-pwd";
        public static final String DB_PATH = "path-DB";
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
                    public static final String PWD_CONFIRMPWD_NOT_EQUAL= "string.command.error.pwd-confirmpwd-not-equal";
                    public static final String ALREADY_REGISTERED= "string.command.error.already-registered";
                    public static final String TOO_MANY_WORDS= "string.command.error.too-many-words";
                    public static final String WRONG_PWD= "string.command.error.wrong-pwd";
                    public static final String ALREADY_LOGGEDIN= "string.command.error.already-loggedin";
                    public static final String NOT_ENOUGH_ARGS= "string.command.error.not-enough-args";
                    public static final String NOT_REGISTERED = "string.command.error.not-registered";
                    public static final String NOT_LOGGEDIN = "string.command.error.not-loggedin";
                    public static final String NO_COORDS_SPECIFIED = "string.command.error.no-coords-specified";
                    public static final String STATE_ALREADY_EXISTS = "string.command.error.state-already-exists";
                    public static final String STATE_DOESNT_EXISTS = "string.command.error.state-doesnt-exists";
                    public static final String ILLEGAL_CHAR = "string.command.error.illegal-char";
                    public static final String AREA_ALREADY_CLAIMED_SINGLE_STATE= "string.command.error.area-already-claimed.single-state";
                }
                public static final class Success{
                    public static final String PLAYER_REGISTERED = "string.command.success.player-registered";
                    public static final String PLAYER_LOGGEDIN = "string.command.success.player-loggedin";
                    public static final String STATE_CREATED = "string.command.success.state-created";
                    public static final String CLAIM_CREATED= "string.command.success.claim-created";
                }
            }
            public static final class Title{
                public static final String EXITING_STATE = "string.title.exiting-state";
            }
        }
    }

    public static final class Resources{
        public static final String DB_RESOURCES_FOLDER = "/db_resources";

        public static final String DB_STARTUP_MYSQL = DB_RESOURCES_FOLDER+"/StartupQuery_mysql.sql";
        public static final class MYSQL{
            public static final String DB_MYSQL_FOLDER = DB_RESOURCES_FOLDER+"/mysql";
            public static final String DB_CHECK_IF_PERSON_EXISTS = DB_MYSQL_FOLDER+"/CheckIfPersonExists_query_sql.sql";
            public static final String DB_INSERT_NEW_PERSON = DB_MYSQL_FOLDER+"/InsertNewPerson_query_sql.sql";
            public static final String DB_GET_PERSON_CREDENTIALS = DB_MYSQL_FOLDER+"/GetPersonCredentials_query_sql.sql";
            public static final String DB_GET_PERSON_ID = DB_MYSQL_FOLDER+"/GetPersonId_query_sql.sql";
            public static final String DB_INSERT_NEW_STATE = DB_MYSQL_FOLDER+"/InsertNewState_query_sql.sql";
            public static final String DB_CHECK_IF_STATE_EXISTS = DB_MYSQL_FOLDER+"/CheckIfStateExists_query_sql.sql";
            public static final String DB_CREATE_CLAIM = DB_MYSQL_FOLDER+"/CreateClaim_query_sql.sql";
            public static final String DB_GET_CLAIM_OWNER = DB_MYSQL_FOLDER+"/GetClaimOwner_query_sql.sql";
            public static final String DB_GET_STATE_NAME_BY_ID = DB_MYSQL_FOLDER+"/GetStateNameById_query_sql.sql";
            public static final String DB_GET_STATE_ID_BY_NAME = DB_MYSQL_FOLDER+"/GetStateIdByName_query_sql.sql";
        }
    }
}
