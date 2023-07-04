CREATE TABLE IF NOT EXISTS sf_person(
	PersonId INT AUTO_INCREMENT,
    PersonName VARCHAR(17),
    PersonCredentials VARCHAR(10),

    unique(PersonName),
    primary key(PersonId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sf_state (
    StateId int AUTO_INCREMENT,
    StateName VARCHAR(100),
    StateOwner int,
    ClaimResponsible int,
    StateColor VARCHAR(7),

    unique(StateColor),
    unique(StateName),
    primary key(StateId),
    FOREIGN KEY(StateOwner) REFERENCES sf_person(PersonId),
    FOREIGN KEY(ClaimResponsible) REFERENCES sf_person(PersonId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sf_faction(
	FactionId int AUTO_INCREMENT,
    FactionName TEXT,

    primary key(FactionId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sf_citizenship(
	StateId INT,
    PersonId INT,
    primary key(StateId,PersonId),

    FOREIGN KEY(StateId) REFERENCES sf_state(StateId),
    FOREIGN KEY(PersonId) REFERENCES sf_person(PersonId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sf_membership(
	PersonId INT,
    FactionId INT,

    primary key(PersonId,FactionId),

    FOREIGN KEY(PersonId) REFERENCES sf_person(PersonId),
    FOREIGN KEY(FactionId) REFERENCES sf_faction(FactionId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sf_state_space(
	StateId int,
    BlockX1 int,
    BlockZ1 int,
    BlockX2 int,
    BlockZ2 int,

    UNIQUE(BlockX1,BlockX2,BlockZ1,BlockZ2),

    FOREIGN KEY(StateId) REFERENCES sf_state(StateId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sf_citizenship_requests(
	StateId int,
    PersonId int,

    primary key(StateId,PersonId),

    FOREIGN KEY(StateId) REFERENCES sf_state(StateId),
    FOREIGN KEY(PersonId) REFERENCES sf_person(PersonId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sf_membership_requests(
	FactionId int,
    PersonId int,

	primary key(FactionId,PersonId),

    FOREIGN KEY(FactionId) REFERENCES sf_faction(FactionId),
    FOREIGN KEY(PersonId) REFERENCES sf_person(PersonId)
) ENGINE=InnoDB;