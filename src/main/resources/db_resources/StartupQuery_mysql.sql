CREATE TABLE IF NOT EXISTS sf_person(
	PersonId INT AUTO_INCREMENT,
    PersonName VARCHAR(17) NOT NULL,
    PersonCredentials VARCHAR(10) NOT NULL,
    RegistrationDate DATETIME DEFAULT CURRENT_TIMESTAMP(),

    unique(PersonName),
    primary key(PersonId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sf_state (
    StateId int AUTO_INCREMENT,
    StateName VARCHAR(100) NOT NULL,
    StateFounder int NOT NULL,
    StateCreationDate DATETIME DEFAULT CURRENT_TIMESTAMP(),
    StateColor VARCHAR(7),

    unique(StateColor),
    unique(StateName),
    primary key(StateId),
    FOREIGN KEY(StateFounder) REFERENCES sf_person(PersonId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sf_faction(
	FactionId int AUTO_INCREMENT,
    FactionName TEXT NOT NULL,
    FactionFounder int NOT NULL,
    FactionCreationDate DATETIME DEFAULT CURRENT_TIMESTAMP(),

    primary key(FactionId),
    FOREIGN KEY(FactionFounder) REFERENCES sf_person(PersonId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sf_citizenship(
	StateId INT,
    PersonId INT,
    IsStateOwner BOOLEAN DEFAULT false,
    IsClaimResponsible BOOLEAN DEFAULT false,
    StateJoinDate DATETIME DEFAULT CURRENT_TIMESTAMP(),

    primary key(StateId,PersonId),

    FOREIGN KEY(StateId) REFERENCES sf_state(StateId),
    FOREIGN KEY(PersonId) REFERENCES sf_person(PersonId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sf_membership(
	PersonId INT,
    FactionId INT,
    MembershipDate DATETIME DEFAULT CURRENT_TIMESTAMP(),

    primary key(PersonId,FactionId),

    FOREIGN KEY(PersonId) REFERENCES sf_person(PersonId),
    FOREIGN KEY(FactionId) REFERENCES sf_faction(FactionId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sf_state_space(
	StateId int NOT NULL,
    BlockX1 int,
    BlockZ1 int,
    BlockX2 int,
    BlockZ2 int,
    ClaimDate DATETIME DEFAULT CURRENT_TIMESTAMP(),

    UNIQUE(BlockX1,BlockX2,BlockZ1,BlockZ2),

    FOREIGN KEY(StateId) REFERENCES sf_state(StateId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sf_citizenship_requests(
	StateId int,
    PersonId int,
    RequestDate DATETIME DEFAULT CURRENT_TIMESTAMP(),

    primary key(StateId,PersonId),

    FOREIGN KEY(StateId) REFERENCES sf_state(StateId),
    FOREIGN KEY(PersonId) REFERENCES sf_person(PersonId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sf_membership_requests(
	FactionId int,
    PersonId int,
    RequestDate DATETIME DEFAULT CURRENT_TIMESTAMP(),

	primary key(FactionId,PersonId),

    FOREIGN KEY(FactionId) REFERENCES sf_faction(FactionId),
    FOREIGN KEY(PersonId) REFERENCES sf_person(PersonId)
) ENGINE=InnoDB;