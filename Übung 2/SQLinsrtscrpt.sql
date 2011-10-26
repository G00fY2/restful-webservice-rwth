INSERT INTO User
  VALUES 
    ('1234abc','AbCd12',NULL,'Max','Mustermann','max.mustermann@rwth-aachen.de'),

    ('5678def','DeFg34','300','Petra','Mustermann','petra.mustermann@rwth-aachen.de'),

    ('9123ghi','HiJk56','300','Hans','Peter','hans.peter@rwth-aachen.de'
);







INSERT INTO Picture 
  VALUES 
    ('Bild A schön','1','http//:www.rwth-aachen.de/FakeDetect/Pics/BildA.jpeg'),

    ('Bild B nicht schön','0','http//:www.rwth-aachen.de/FakeDetect/Pics/BildB.jpeg'),

    ('Bild C gut','1','http//:www.rwth-aachen.de/FakeDetect/Pics/BildC.jpeg'
);








INSERT INTO Achievement
  VALUES 
    ('http//:www.rwth-aachen.de/FakeDetect/AchievPics/BildAA.jpeg','Herzlichen Glückwunsch! Sieh haben die 1000 Punktemarke überschritten!','1000 Punkte Marke','1234'),

    ('http//:www.rwth-aachen.de/FakeDetect/AchievPics/BildAB.jpeg','Herzlichen Glückwunsch! Sieh haben die 3000 Punktemarke überschritten!','3000 Punkte Marke','5678'),

    ('http//:www.rwth-aachen.de/FakeDetect/AchievPics/BildAC.jpeg','Herzlichen Glückwunsch! Sieh haben die 6000 Punktemarke überschritten!','6000 Punkte Marke','9123'
);








INSERT INTO rates
  VALUES 
    ('1234','21:23:12','1','http//:www.rwth-aachen.de/FakeDetect/Pics/BildA.jpeg','max.mustermann@rwth-aachen.de'),

    ('2345','11:22:12','0','http//:www.rwth-aachen.de/FakeDetect/Pics/BildB.jpeg','petra.mustermann@rwth-aachen.de'),

    ('3456','10:15:12','0','http//:www.rwth-aachen.de/FakeDetect/Pics/BildC.jpeg','hans.peter@rwth-aachen.de'),
    
    ('4567','05:56:12','1','http//:www.rwth-aachen.de/FakeDetect/Pics/BildA.jpeg','petra.mustermann@rwth-aachen.de'),
    
    ('5678','19:15:12','1','http//:www.rwth-aachen.de/FakeDetect/Pics/BildC.jpeg','petra.mustermann@rwth-aachen.de'),

    ('6789','13:45:10','0','http//:www.rwth-aachen.de/FakeDetect/Pics/BildB.jpeg','max.mustermann@rwth-aachen.de'),
   
    ('7891','00:45:10','1','http//:www.rwth-aachen.de/FakeDetect/Pics/BildC.jpeg','max.mustermann@rwth-aachen.de'),

    ('8912','17:34:12','0','http//:www.rwth-aachen.de/FakeDetect/Pics/BildA.jpeg','hans.peter@rwth-aachen.de'),

    ('9123','12:34:12','1','http//:www.rwth-aachen.de/FakeDetect/Pics/BildB.jpeg','hans.peter@rwth-aachen.de'
);





INSERT INTO collect
  VALUES 
    ('1234','1','max.mustermann@rwth-aachen.de','1234'),

    ('2345','2','hans.peter@rwth-aachen.de','5678'),

    ('3456','3','petra.mustermann@rwth-aachen.de','9123'
);


