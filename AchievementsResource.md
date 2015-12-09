
---

# Allgemeines #

---



### URL Template: ###
  * "http://localhost:8080/ugnm-service/resources/achievements/"

### MIME Typ: ###
  * "application/json"
_JSON MIME Typ_




---

## Details ##

---



## Liste aller Achievements (AchievementsResource): ##

---


### Operation: ###
  * GET

### Ein-/Ausgabeform: ###

```
{Achievements: [<ACH_RES_1>,<ACH_RES_2,...,<ACH_RES_n>]}
```

### Struktur zwischen Ressourcen: ###
  * "http://localhost:8080/ugnm-service/resources/achievements/{id}"
_Ausgehend von der Liste aller Achievements, ist es möglich auf das einzelne Achievement zuzugreifen._

### Zugriffskontrolle auf Operationen: ###
  * ---

### HTTP Status Codes (Fehlerfall): ###
  * 500: INERNAL\_SERVER\_ERROR
_Der Webserver stieß auf eine unerwartete Bedingung, die ihn davon abhielt, die Anforderung zum Zugriff auf die angeforderte URL durch den Client zu erfüllen._


### HTTP Status Codes (Erfolgsfall): ###

  * 200: OK
_Die Anfage konnte bearbeitet werden._


---


## Erstellen eines Achievements(AchievementsResource): ##

---



### Operation  : ###
  * PUT

### Ein-/Ausgabeform: ###

Eingabe:


```
{'id':<ID>,'description':<DESCRIPTION>,'name':<NAME>,'url':<URL>}
```


### Struktur zwischen Ressourcen: ###
  * ---

### Zugriffskontrolle auf Operationen: ###
  * CREATE (Admin)
_Ein Achievement kann nur von einem Admin erstellt werden._

### HTTP Status Codes (Fehlerfall): ###

  * 401: Unauthorized
_Die Anfrage kann nicht ohne gültige (Admin-)Authentifizierung durchgeführt werden. Wie die Authentifizierung durchgeführt werden soll, wird im „WWW-Authenticate“-Header-Feld der Antwort übermittelt._

  * 409: Conflict
_Das Achievement existiert bereits, der Server kann die anfrage nicht bearbeiten._

### HTTP Status Codes (Erfolgsfall): ###

  * 201: Created
_Der User wurde erstellt._


---
