use ugnm1112g4;
drop table Achievement;
drop table Collect;
drop table Medium;
drop table Rates;
drop table User;

CREATE TABLE User( 
  email varchar(100) NOT NULL, 
  username varchar(100) NOT NULL, 
  password varchar(100) NOT NULL,
  name varchar(100) NOT NULL,
  ep int(11) NOT NULL DEFAULT '0',
  
  CONSTRAINT User_PK PRIMARY KEY (email),
); 




CREATE TABLE Medium( 
  id int(11) AUTO_INCREMENT NOT NULL,
  url varchar(100) NOT NULL, 
  value int(1) NOT NULL, 
  description varchar(100) NOT NULL, 
  tag varchar(100) NOT NULL,
  
  CONSTRAINT Medium_PK PRIMARY KEY (id), 
  CONSTRAINT Medium_value_CHECK CHECK (vaulue >=0 AND calue <=1)
); 




CREATE TABLE Achievement( 
  id int(11) AUTO_INCREMENT NOT NULL,
  description varchar(100) NOT NULL, 
  name varchar(40) NOT NULL, 
  url varchar(100) NOT NULL, 
  
  CONSTRAINT Achievement_PK PRIMARY KEY (id)
); 




CREATE TABLE Rates( 
  id int(11) AUTO_INCREMENT,
  time timestamp NOT NULL,
  rate int(1) NOT NULL, 
  mediumId varchar(100) NOT NULL, 
  userEmail varchar(50) NOT NULL,

  CONSTRAINT Rates_PK PRIMARY KEY (id),
  CONSTRAINT Rates_FK1 FOREIGN KEY (mediumId) REFERENCES Medium (id),
  CONSTRAINT Rates_FK2 FOREIGN KEY (userEmail) REFERENCES User (email)
);



CREATE TABLE Collect( 
  id int(11) AUTO_INCREMENT,
  userEmail varchar(50) NOT NULL,
  achievementId int(11) NOT NULL, 

  CONSTRAINT Collect_PK PRIMARY KEY (id),
  CONSTRAINT Collect_FK1 FOREIGN KEY (userEmail) REFERENCES User (email),
  CONSTRAINT Collect_FK2 FOREIGN KEY (achievementId) REFERENCES Achievement(id)
);



