INSERT INTO User
  VALUES 
    ('1234abc','AbCd12', DEFAULT, DEFAULT,'Max','Mustermann','max.mustermann@rwth-aachen.de'),

    ('5678def','DeFg34', '300', DEFAULT,'Petra','Mustermann','petra.mustermann@rwth-aachen.de'),

    ('9123ghi','HiJk56', '300','2','Hans','Peter','hans.peter@rwth-aachen.de'
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
    (NULL,'2011-10-30 16:17:12','1','http//:www.rwth-aachen.de/FakeDetect/Pics/BildA.jpeg','max.mustermann@rwth-aachen.de'),

    (NULL,'2011-10-30 16:18:09','0','http//:www.rwth-aachen.de/FakeDetect/Pics/BildB.jpeg','petra.mustermann@rwth-aachen.de'),

    (NULL,'2011-10-30 16:22:56','0','http//:www.rwth-aachen.de/FakeDetect/Pics/BildC.jpeg','hans.peter@rwth-aachen.de'),
    
    (NULL,'2011-10-30 16:25:32','1','http//:www.rwth-aachen.de/FakeDetect/Pics/BildA.jpeg','petra.mustermann@rwth-aachen.de'),
    
    (NULL,'2011-10-30 16:27:33','1','http//:www.rwth-aachen.de/FakeDetect/Pics/BildC.jpeg','petra.mustermann@rwth-aachen.de'),

    (NULL,'2011-10-30 16:38:12','0','http//:www.rwth-aachen.de/FakeDetect/Pics/BildB.jpeg','max.mustermann@rwth-aachen.de'),
   
    (NULL,'2011-10-30 16:39:45','1','http//:www.rwth-aachen.de/FakeDetect/Pics/BildC.jpeg','max.mustermann@rwth-aachen.de'),

    (NULL,'2011-10-30 16:43:54','0','http//:www.rwth-aachen.de/FakeDetect/Pics/BildA.jpeg','hans.peter@rwth-aachen.de'),

    (NULL,'2011-10-30 16:54:11','1','http//:www.rwth-aachen.de/FakeDetect/Pics/BildB.jpeg','hans.peter@rwth-aachen.de'
);





INSERT INTO collect
  VALUES 
    ('max.mustermann@rwth-aachen.de','1234'),

    ('hans.peter@rwth-aachen.de','5678'),

    ('petra.mustermann@rwth-aachen.de','1234'
);


