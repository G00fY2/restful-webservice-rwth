SELECT u.EMail, r.FKURL, r.Zeit
FROM rates r, user u
WHERE r.FKEMail = u.EMail AND EMail='hans.peter@rwth-aachen.de'
ORDER BY r.FKURL ASC, r.Zeit DESC;


SELECT p.URL, AVG(r.rate) avg_rates, sum(r.rate=1),sum(r.rate=0)
FROM picture p, rates r
WHERE r.FKURL= p.URL AND URL='http//:www.rwth-aachen.de/FakeDetect/Pics/BildA.jpeg';


SELECT u.EMail, u.EP, u.Achievments
FROM user u, collect c
WHERE u.EMail = c.FKMail
ORDER BY u.Achievments DESC, u.EP DESC LIMIT 0,10;