SELECT EMail, FKURL, Zeit FROM rates, User WHERE FKEMail = EMail ORDER BY Zeit DESC, FKURL DESC;

SELECT URL, AVG(rate) durchschnitt, sum(rate=1) summe1, sum(rate=0) summe0 FROM Picture, rates WHERE FKURL= URL AND URL='http//:www.rwth-aachen.de/FakeDetect/Pics/BildA.jpeg' GROUP BY URL;

SELECT EMail, EP, Anzahl FROM User, collect WHERE EMail = FKMail ORDER BY Anzahl DESC, EP DESC LIMIT 0,10;



