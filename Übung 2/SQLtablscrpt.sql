CREATE TABLE User( 
  Passwort varchar(10) NOT NULL, 
  Benutzername varchar(30) NOT NULL, 
  EP int(11) NOT NULL DEFAULT '0',
  Vorname varchar(30) NOT NULL,
  Nachname varchar(30) NOT NULL,
  EMail varchar(50) NOT NULL,
  
  CONSTRAINT User_PK PRIMARY KEY (EMail, Benutzername)
); 




CREATE TABLE Picture( 
  Beschreibung varchar(100) NOT NULL, 
  Value int(1) NOT NULL, 
  URL varchar(100) NOT NULL, 
  
  CONSTRAINT Picture_PK PRIMARY KEY (URL), 
  CONSTRAINT Picture_Value_C CHECK (Vaulue >=0 AND Value <=1)
); 




CREATE TABLE Achievement(
  Bild varchar(100) NOT NULL, 
  Beschreibung varchar(100) NOT NULL, 
  Name varchar(40) NOT NULL, 
  Identifier int(11) NOT NULL, 
  
  CONSTRAINT Achievement_PK PRIMARY KEY (Identifier)
); 




CREATE TABLE rates( 
  RatesID int(11) AUTO_INCREMENT,
  Zeit datetime NOT NULL,
  Rate int(1) NOT NULL, 
  FKURL varchar(100) NOT NULL, 
  FKEMail varchar(50) NOT NULL,

  CONSTRAINT rates_PK PRIMARY KEY (RatesID),
  CONSTRAINT M_FK FOREIGN KEY (FKEMail) REFERENCES User (EMail),
  CONSTRAINT U_FK FOREIGN KEY (FKURL) REFERENCES Picture (URL)
);



CREATE TABLE collect( 
  FKMail varchar(50) NOT NULL,
  FKIdentifier int(11) NOT NULL, 

  CONSTRAINT collect_PK PRIMARY KEY (FKMail, FKIdentifier),
  CONSTRAINT M2_FK FOREIGN KEY (FKMail) REFERENCES User (EMail),
  CONSTRAINT I_FK FOREIGN KEY (FKIdentifier) REFERENCES Achievement(Identifier)
);



