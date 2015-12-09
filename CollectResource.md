
---

# Allgemeines #

---

### URL Template: ###
  * "http://localhost:8080/ugnm-service/resources/users/{email}/collect/{achievementId}"

### MIME Typ: ###
  * "application/json"
_JSON MIME Typ_



---

## Details ##

---


## Auslesen eines bestimmten Collects (CollectResource): ##

---


### Operation: ###
  * GET
_Gibt ein Collect mit allen Details aus._


### Ein-/Ausgabeform: ###

Ausgabe:
```
{'achievementId': <ACHIEVEMENTID>, 'userEmail': <USEREMAIL>}
```



### Zugriffskontrolle auf Operation: ###
  * ---

### HTTP Status Codes (Fehlerfall): ###

  * 404: Not Found
_Das angeforderte Collect wurde nicht gefunden. Das Rating kann nicht gefunden werden (Falsch geschrieben, existiert nicht (mehr) in der Datenbank, etc.)._

### HTTP Status Codes (Erfolgsfall): ###

  * 200: OK
_Das gesuchte Collect wurde gefunden und wird ausgegeben._


---


## Löschen eines Collects(CollectResource): ##

---

### Operation: ###
  * DELETE
### Struktur zwischen Ressourcen: ###
  * ---
### Zugriffskontrolle auf Operationen: ###
  * DELETE (Admin)
_Nur einem Admin ist es möglich ein Collect zu löschen._
### HTTP Status Codes (Fehlerfall): ###

  * 404: Not Found
_Die angeforderte Ressource wurde nicht gefunden. Das Collect kann nicht gefunden werden (Falsch geschrieben, existiert nicht (mehr) in der Datenbank, etc.)._

  * 401: Unauthorized
_Die Anfrage kann nicht ohne gültige Authentifizierung durchgeführt werden. Wie die Authentifizierung durchgeführt werden soll, wird im „WWW-Authenticate“-Header-Feld der Antwort übermittelt._

### HTTP Status Codes (Erfolgsfall): ###

  * 200: OK
_Das Collect wurde erfolgreich gelöscht._