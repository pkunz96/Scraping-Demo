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

>private IQuery<Broker> buildQuery(IDatasource<Broker> datasource, List<ZipCode> zipCodeList) {
>IQueryBuilder<Broker> queryBuilder = IQueryBuilder.getInstance(Broker.class);
>queryBuilder.addDatasource(datasource).startPredicate(LogicalConnective.OR);
>    	for(de.kunz.scraping.data.entity.ZipCode zipCode: zipCodeList) {
>    		final String zipCodeStr = zipCode.getZipCode();
>    		final String zipCodeCountryCodeStr = zipCode.getCountry() != null ? zipCode.getCountry().getCountryCode() : null;
>   		if(zipCodeStr != null && (zipCodeCountryCodeStr != null)) {
>    			final String constraintStr = zipCodeStr + "@" + zipCodeCountryCodeStr;
>    			queryBuilder.addConstraint(new de.kunz.scraping.sourcing.querying.ZipCode(), constraintStr, Relation.EUQAL);
>   		}    		
>    	}
>    	return queryBuilder.closePredicate().getQuery();  



Dabei müssen zunächst Datasources spezifziert werden. Damit ein Durch Prädikate können Objekte mit be, sowie Constrains innerhalb von Prädikaten spezifziert werden. 



* Instanz von IQueryBuilder erstellen. 
* Query aufbauen 
** 

Hierbei sind zunächst die Datasources zu spezifizieren, schließen können innerhalb von hierar






Eine Menge von PJOs welche Konfigurationsinformationen speichern. Instanzen werden an eine XML-Datei mittels JAXB gebunden und bei Änderungen werden diese in der XML-Datei persistiert. 

