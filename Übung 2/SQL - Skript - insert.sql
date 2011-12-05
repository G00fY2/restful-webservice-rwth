INSERT INTO User
  VALUES 
    ('sven.hausburg@rwth-aachen.de','SvenHausburg', 'abc123','Sven Hausburg', DEFAULT),

    ('thomas.wirth@rwth-aachen.de','ThomasWirth', 'def456','Thomas Wirth', DEFAULT),

    ('test.1@rwth-aachen.de','Test1','ghi789','Test 1', DEFAULT);




INSERT INTO Medium 
  VALUES 
    ('www.medium1.de','1','Test Medium 1'),

    ('www.medium2.de','0','Test Medium 2'),

    ('www.medium3.de','1','Test Medium 3');









INSERT INTO Achievement
  VALUES 
    ('001','1000 Punkte','Achievement1','/achievements/001'),

    ('002','2000 Punkte','Achievement2','/achievements/002'),

    ('003','3000 Punkte','Achievement3','/achievements/003');






INSERT INTO Rates
  VALUES 
    ('1001','2011-10-30 16:17:12','1','www.medium1.de','sven.hausburg@rwth-aachen.de'),

    ('1002','2011-10-30 16:17:13','0','www.medium2.de','thomas.wirth@rwth-aachen.de'),

    ('1003','2011-10-30 16:17:14','1','www.medium3.de','test.1@rwth-aachen.de'),

    ('1004','2011-10-30 16:17:15','0','www.medium1.de','thomas.wirth@rwth-aachen.de'),

    ('1005','2011-10-30 16:17:16','1','www.medium2.de','test.1@rwth-aachen.de'),

    ('1006','2011-10-30 16:17:17','0','www.medium3.de','sven.hausburg@rwth-aachen.de');






INSERT INTO Collect
  VALUES 
    ('2001','sven.hausburg@rwth-aachen.de','001'),

    ('2002','thomas.wirth@rwth-aachen.de','002'),

    ('2003','test.1@rwth-aachen.de','003');


