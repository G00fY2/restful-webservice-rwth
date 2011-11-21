SELECT u.EMail, r.FKURL, r.Zeit
FROM rates r, User u
WHERE r.FKEMail = u.EMail AND EMail='hans.peter@rwth-aachen.de'
ORDER BY r.FKURL ASC, r.Zeit DESC;


SELECT URL, AVG(rate) durchschnitt, sum(rate=1) summe1, sum(rate=0) summe0
FROM Medium, rates
WHERE FKURL= URL AND URL='http//:www.rwth-aachen.de/FakeDetect/Pics/BildA.jpeg'
GROUP BY URL;


SELECT u.EMail, u.EP
FROM User u
ORDER BY u.EP DESC LIMIT 0,10;