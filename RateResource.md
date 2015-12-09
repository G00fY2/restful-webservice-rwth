
---

# Allgemeines #

---

### URL Template: ###
  * "http://localhost:8080/ugnm-service/resources/users/{email}/rates/{id}"

### MIME Typ: ###
  * "application/json"
_JSON MIME Typ_



---

## Details ##

---


## Auslesen eines bestimmten Ratings (RateResource): ##

---


### Operation: ###
  * GET
_Gibt ein Rating mit allen Details aus._


### Ein-/Ausgabeform: ###

Ausgabe:
```
{'url': <URL>, (( time: <ZEIT 00:00:00> )) ?, 'rate': <RATE>, 'mediumURL': <MEDIUMURL>, 'userEmail': <USEREMAIL>}
```



### Zugriffskontrolle auf Operation: ###
  * ---

### HTTP Status Codes (Fehlerfall): ###

  * 404: Not Found
_Das angeforderte Rating wurde nicht gefunden. Das Rating kann nicht gefunden werden (Falsch geschrieben, existiert nicht (mehr) in der Datenbank, etc.)._

### HTTP Status Codes (Erfolgsfall): ###

  * 200: OK
_Das gesuchte Rating wurde gefunden und wird ausgegeben._


---


## Löschen eines Ratings (RateResource): ##

---

### Operation: ###
  * DELETE
### Struktur zwischen Ressourcen: ###
  * ---
### Zugriffskontrolle auf Operationen: ###
  * DELETE (Admin)
_Nur einem Admin ist es möglich ein Rating zu löschen._
### HTTP Status Codes (Fehlerfall): ###

  * 404: Not Found
_Die angeforderte Ressource wurde nicht gefunden. Das Rating kann nicht gefunden werden (Falsch geschrieben, existiert nicht (mehr) in der Datenbank, etc.)._

  * 401: Unauthorized
_Die Anfrage kann nicht ohne gültige Authentifizierung durchgeführt werden. Wie die Authentifizierung durchgeführt werden soll, wird im „WWW-Authenticate“-Header-Feld der Antwort übermittelt._

### HTTP Status Codes (Erfolgsfall): ###

  * 200: OK
_Das Rating wurde erfolgreich gelöscht._