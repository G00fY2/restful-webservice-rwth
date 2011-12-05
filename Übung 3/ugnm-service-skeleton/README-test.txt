Hinweise zum Testen
===================

1) Entfernen von @Scope Annotations

Bitte entfernt aus allen Ressource Klassen im Paket de.rwth.dbis.ugnm.resource die @Scope Annotations. Der JUnit Testrunner benötigt speziellen Zugriff auf die Ressourcen. Dieser Zugriff wird ohne Entfernung der Annotations zu Fehlern führen, die Eure Tests nicht sauber durchlaufen lassen.

2) Im Ordner src/test findet Ihr ein dokumentiertes Beispiel einer JUnit Testklasse speziell für Jersey Ressourcen. Die Klasse enthält bereits einige wenige Testcases, die Euch als Orientierungshilfe dienen sollen. Bitte seht diese Testcases nicht als nicht vollständig an! Bitte macht Euch Gedanken, welche Bedingungen man abtesten kann und schreibt entsprechende Testroutinen mit anschliessenden Asserts zum Vergleich von Soll- und Ist-Situation. Bitte ordnet alle Testklassen in das gleiche Paket ein wie die angegebene Beispielklasse.

3) Ausführung der Tests

Zur Ausführung der Tests nutzt Ihr wiederum den Support von Maven:

	mvn clean test

Während der Ausführung der Tests bekommt Ihr auf der Kommandozeile Feedback zum Testablauf (z.B. wieviele Tests sauber durchgelaufen sind (passed), wieviele aufgrund von nicht zutreffenden Asserts fehlgeschlagen sind (failed) und wieviele durch einen unerwarteten Fehler abgebrochen werden mussten (error). Nach der Ausführung findet Ihr im Ordner /target/surefire-reports entsprechende vom JUnit Testrunner erzeugte Testprotokolle, die mehr Informationen zu den Gründen fehlgeschlagener oder abgebrochener Tests hergeben. Bitte nutzt diese Reports für die Entwicklung Eurer Tests.


