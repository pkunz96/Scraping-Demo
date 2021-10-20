# Scraping-Demo

## ScrapingAPI

### Paket: de.kunz.scraping.api

Eine Menge von EJBs, welche eine REST-API zur Steuerung des Scrapings implementieren.

### Paket: de.kunz.scraping.views

Die in de.kunz.scraping.data.entity definierten Entity-Klassen enthalten Referenzen, wobei beim Serialisieren referenzierte Objekte nicht miteinbezogen werden sollen. Stattdessen sollen lediglich die IDs der refrenzierten Objekte eingefügt werden. Die im Paket definierten Wrapper ersetzen Referenzen durch IDs und ermöglichen damit die Serialisierung im Sinne der Problemstellung. 

### Paket: de.kunz.scraping.conf

CORS-Konfiguraton und Pfadinformationen.

