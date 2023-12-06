GRANT CREATE ON DATABASE bookingticket TO postgre;
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS Bookings CASCADE;
DROP TABLE IF EXISTS EventSection CASCADE;
DROP TABLE IF EXISTS PlannerEvent CASCADE;
DROP TABLE IF EXISTS Events CASCADE;
DROP TABLE IF EXISTS Sections CASCADE;
DROP TABLE IF EXISTS Venues CASCADE;
DROP TABLE IF EXISTS Users CASCADE;
DROP TYPE IF EXISTS UserAccess CASCADE;

-- CREATE TABLE refresh_token (
--    id uuid NOT NULL UNIQUE,
--    token_id VARCHAR(36) NOT NULL,
--    username VARCHAR(255) NOT NULL,
--    PRIMARY KEY (id)
-- );
--
-- CREATE INDEX refresh_token_username
--     ON refresh_token (username);

CREATE TYPE UserAccess AS ENUM ('customer', 'planner', 'admin');
CREATE TABLE IF NOT EXISTS Users (
    email VARCHAR(80) NOT NULL,
    token VARCHAR(255) NOT NULL,
    type UserAccess NOT NULL,
    password VARCHAR(80) NOT NULL,
    PRIMARY KEY (email)
);

CREATE TABLE IF NOT EXISTS Venues (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(80) NOT NULL,
    address VARCHAR(80) NOT NULL,
    version INT
);

CREATE TABLE IF NOT EXISTS Sections (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    venueID UUID NOT NULL,
    type VARCHAR(80) NOT NULL,
	capacity INT NOT NULL,
    CONSTRAINT fk_sVenue FOREIGN KEY(venueID) REFERENCES Venues(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Events (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(80) NOT NULL,
    venueID UUID NOT NULL,
    startDateTime TIMESTAMPTZ NOT NULL,
    endDateTime TIMESTAMPTZ NOT NULL,
    artist VARCHAR(80) NOT NULL,
    description VARCHAR(80) NOT NULL,
    version INT,
    CONSTRAINT fk_eVenue FOREIGN KEY(venueID) REFERENCES Venues(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS PlannerEvent (
    eventID UUID NOT NULL,
    plannerEmail VARCHAR(80) NOT NULL,
    PRIMARY KEY (eventID, plannerEmail),
    CONSTRAINT fk_peEvent FOREIGN KEY(eventID) REFERENCES Events(id) ON DELETE CASCADE,
    CONSTRAINT fk_pePlanner FOREIGN KEY(plannerEmail) REFERENCES Users(email) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS EventSection (
    eventID UUID NOT NULL,
    sectionID UUID NOT NULL,
    price DECIMAL NOT NULL,
    available INT NOT NULL,
    PRIMARY KEY (eventID, sectionID),
    CONSTRAINT fk_seEvent FOREIGN KEY(eventID) REFERENCES Events(id) ON DELETE CASCADE,
    CONSTRAINT fk_seSection FOREIGN KEY(sectionID) REFERENCES Sections(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Bookings (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customerEmail VARCHAR(80) NOT NULL,
    eventID UUID NOT NULL,
    sectionID UUID NOT NULL,
    numTickets INTEGER NOT NULL,
    CONSTRAINT fk_bEvent FOREIGN KEY(eventID) REFERENCES Events(id) ON DELETE CASCADE,
    CONSTRAINT fk_bSection FOREIGN KEY(sectionID) REFERENCES Sections(id) ON DELETE CASCADE,
    CONSTRAINT fk_bCustomer FOREIGN KEY(customerEmail) REFERENCES Users(email) ON DELETE CASCADE
);
    ticketNum INTEGER NOT NULL,
    seatFrom INTEGER,
    seatTo INTEGER
);

CREATE TABLE IF NOT EXISTS public.refresh_token
(
    id uuid NOT NULL UNIQUE,
    token_id VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    CONSTRAINT refresh_token_pkey PRIMARY KEY (id),
    CONSTRAINT fk_username FOREIGN KEY (username)
    REFERENCES public.users (email) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
    );
