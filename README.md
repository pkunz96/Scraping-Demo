# Scraping-Demo

## ScrapingAPI

### Paket: de.kunz.scraping.api

Eine Menge von EJBs, welche eine REST-API zur Steuerung des Scrapings implementieren.

### Paket: de.kunz.scraping.views

Die in de.kunz.scraping.data.entity definierten Entity-Beans enthalten Referenzen, wobei beim Serialisieren referenzierte Objekte nicht miteinbezogen werden sollen. Stattdessen sollen lediglich die IDs der refrenzierten Objekte eingefügt werden. Die im Paket definierten Wrapper ersetzen Referenzen durch IDs und ermöglichen damit diese Arte der Serialisierung.

### Paket: de.kunz.scraping.conf

CORS-Konfiguraton und Pfadinformationen.

## ScrapingEJB

### Paket: de.kunz.scraping.conf

Die Konfiguration der Anwendung wird durch eine Menge von hierarchisch organisierten Klassen repräsentiert. Es wird genau eine Instanz (Singleton) erstellt und an eine XML-Datei mittels JAXB gebunden. Bei Änderungen wird die XML-Datei automatisch aktualisiert. 

### Paket: de.kunz.scraping.data.entity

Entity-Beans

### Paket: de.kunz.scraping.data.access

EJBs, die Datenbankabfragen kapseln. 

### Paket: de.kunz.scraping.data.querying

Definiert eine generische API für die Abfrage von Objekten, deren Klasse das Interface Queryable implementiert. Eine Quelle dieser Objekte muss das Interface  IDatasource implementieren. 

Zur Durchführung einer Abfrage muss eine Instanz von IQuery mit Hilfe einer Instanz von IQueryBuilder aufgebaut werden. Zunächst werden die Datasources spezifziert, die abgefragt werden sollen. Anschließend werden Prädikate logisch verknüpft, um auszudrücken, welche Objekte zur Ergebnismenge gehören. 

>final String constraintStr = zipCodeStr + "@" + zipCodeCountryCodeStr;  
>IQueryBuilder<Broker> queryBuilder =   
>  IQueryBuilder.getInstance(Broker.class).addDatasource(datasource).startPredicate(LogicalConnective.OR).addConstraint(new ZipCode(), constraintStr, Relation.EUQAL).closePredicate();  
>IQuery<Broker> brokerQuery = queryBuilder.getQuery();  

