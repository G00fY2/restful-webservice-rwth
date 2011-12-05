Hinweise zum Testen
===================

1) Entfernen von @Scope Annotations

Bitte entfernt aus allen Ressource Klassen im Paket de.rwth.dbis.ugnm.resource die @Scope Annotations. Der JUnit Testrunner ben�tigt speziellen Zugriff auf die Ressourcen. Dieser Zugriff wird ohne Entfernung der Annotations zu Fehlern f�hren, die Eure Tests nicht sauber durchlaufen lassen.

2) Im Ordner src/test findet Ihr ein dokumentiertes Beispiel einer JUnit Testklasse speziell f�r Jersey Ressourcen. Die Klasse enth�lt bereits einige wenige Testcases, die Euch als Orientierungshilfe dienen sollen. Bitte seht diese Testcases nicht als nicht vollst�ndig an! Bitte macht Euch Gedanken, welche Bedingungen man abtesten kann und schreibt entsprechende Testroutinen mit anschliessenden Asserts zum Vergleich von Soll- und Ist-Situation. Bitte ordnet alle Testklassen in das gleiche Paket ein wie die angegebene Beispielklasse.

3) Ausf�hrung der Tests

Zur Ausf�hrung der Tests nutzt Ihr wiederum den Support von Maven:

	mvn clean test

W�hrend der Ausf�hrung der Tests bekommt Ihr auf der Kommandozeile Feedback zum Testablauf (z.B. wieviele Tests sauber durchgelaufen sind (passed), wieviele aufgrund von nicht zutreffenden Asserts fehlgeschlagen sind (failed) und wieviele durch einen unerwarteten Fehler abgebrochen werden mussten (error). Nach der Ausf�hrung findet Ihr im Ordner /target/surefire-reports entsprechende vom JUnit Testrunner erzeugte Testprotokolle, die mehr Informationen zu den Gr�nden fehlgeschlagener oder abgebrochener Tests hergeben. Bitte nutzt diese Reports f�r die Entwicklung Eurer Tests.


