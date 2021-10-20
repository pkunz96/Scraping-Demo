# Scraping-Demo

## ScrapingAPI

### Paket: de.kunz.scraping.api

Eine Menge von EJBs, welche eine REST-API zur Steuerung des Scrapings implementieren.

### Paket: de.kunz.scraping.views

Problemstellung: Die in de.kunz.scraping.data.entity definierten Entity-Klassen enthalten Referenzen. Beim Serialisieren würden referenzierte Objekte ebenfalls serialisiert. Stattdessen sollen beim Serialisieren lediglich die IDs referenzierter Objekte eingefügt werden. 

Lösung: Die im Paket definierten Wrapper ersetzen Referenzen durch IDs und ermöglichen damit die Serialisierung im Sinne der Problemstellung. 

### Paket: de.kunz.scraping.conf

CORS-Konfiguraton und Pfadinformationen.

