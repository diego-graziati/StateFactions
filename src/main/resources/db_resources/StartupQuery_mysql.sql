CREATE TABLE IF NOT EXISTS sf_person(
	PersonId INT NOT NULL,
    PersonName VARCHAR(17) NOT NULL,
    PersonCredentials VARCHAR(10) NOT NULL,
    RegistrationDate DATETIME DEFAULT CURRENT_TIMESTAMP(),

    unique(PersonName),
    primary key(PersonId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sf_state (
    StateId int NOT NULL,
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
	FactionId int NOT NULL,
    FactionName TEXT NOT NULL,
    FactionFounder int NOT NULL,
    FactionCreationDate DATETIME DEFAULT CURRENT_TIMESTAMP(),

    primary key(FactionId),
    FOREIGN KEY(FactionFounder) REFERENCES sf_person(PersonId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sf_citizenship(
	StateId INT NOT NULL,
    PersonId INT NOT NULL,
    IsStateOwner BOOLEAN DEFAULT false,
    IsClaimResponsible BOOLEAN DEFAULT false,
    StateJoinDate DATETIME DEFAULT CURRENT_TIMESTAMP(),

    primary key(StateId,PersonId),

    FOREIGN KEY(StateId) REFERENCES sf_state(StateId),
    FOREIGN KEY(PersonId) REFERENCES sf_person(PersonId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sf_membership(
	PersonId INT NOT NULL,
    FactionId INT NOT NULL,
    MembershipDate DATETIME DEFAULT CURRENT_TIMESTAMP(),

    primary key(PersonId,FactionId),

    FOREIGN KEY(PersonId) REFERENCES sf_person(PersonId),
    FOREIGN KEY(FactionId) REFERENCES sf_faction(FactionId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sf_state_space(
	StateId int NOT NULL,
    BlockX1 int NOT NULL,
    BlockZ1 int NOT NULL,
    BlockX2 int NOT NULL,
    BlockZ2 int NOT NULL,
    ClaimDate DATETIME DEFAULT CURRENT_TIMESTAMP(),

    primary key(BlockX1,BlockX2,BlockZ1,BlockZ2),

    FOREIGN KEY(StateId) REFERENCES sf_state(StateId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sf_citizenship_requests(
	StateId int NOT NULL,
    PersonId int NOT NULL,
    RequestDate DATETIME DEFAULT CURRENT_TIMESTAMP(),

    primary key(StateId,PersonId),

    FOREIGN KEY(StateId) REFERENCES sf_state(StateId),
    FOREIGN KEY(PersonId) REFERENCES sf_person(PersonId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sf_membership_requests(
	FactionId int NOT NULL,
    PersonId int NOT NULL,
    RequestDate DATETIME DEFAULT CURRENT_TIMESTAMP(),

	primary key(FactionId,PersonId),

    FOREIGN KEY(FactionId) REFERENCES sf_faction(FactionId),
    FOREIGN KEY(PersonId) REFERENCES sf_person(PersonId)
) ENGINE=InnoDB;