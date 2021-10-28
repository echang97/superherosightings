DROP DATABASE IF EXISTS superherosightings;

CREATE DATABASE superherosightings;

USE superherosightings;

-- Tables here
CREATE TABLE Superpower (
	id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(25) NOT NULL,
    description VARCHAR(150) NULL
);

CREATE TABLE Hero (
	id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(25) NOT NULL,
    description VARCHAR(150) NULL,
    superpowerId INT NOT NULL,
    CONSTRAINT FOREIGN KEY fk_superpower (superpowerId)
		REFERENCES Superpower(id)
);

CREATE TABLE `Organization` (
	id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(25) NOT NULL,
    description VARCHAR(150) NULL,
    address VARCHAR(50) NOT NULL,
    phone CHAR(10) NULL,
    email VARCHAR(50) NULL
);

CREATE TABLE HeroOrganization (
	heroId INT,
    organizationId INT,
    CONSTRAINT PRIMARY KEY pk_heroorg (heroId, organizationId),
    CONSTRAINT FOREIGN KEY fk_heroorg_hero (heroId)
		REFERENCES Hero(id),
	CONSTRAINT FOREIGN KEY fk_heroorg_org (organizationId)
		REFERENCES Organization(id)
);

CREATE TABLE Location (
	id INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(25),
    description VARCHAR(150),
    longitude DECIMAL(9,6), -- -180 to 180
    latitude DECIMAL(8,6) -- -90 to 90
);

CREATE TABLE Sighting (
	id INT PRIMARY KEY AUTO_INCREMENT,
    date DATE NOT NULL,
    locationId INT,
    CONSTRAINT FOREIGN KEY fk_location (locationId)
		REFERENCES Location(id)
);

CREATE TABLE HeroSighting (
	heroId INT,
    sightingId INT,
    CONSTRAINT PRIMARY KEY pk_herosighting (heroId, sightingId),
    CONSTRAINT FOREIGN KEY fk_herosighting_hero (heroId)
		REFERENCES Hero (id),
	CONSTRAINT FOREIGN KEY fk_herosighting_sighting (sightingId)
		REFERENCES Sighting(id)
);
