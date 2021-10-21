# Scraping-Demo

## ScrapingAPI

### Paket: de.kunz.scraping.api

A set of EJBs implementing a REST-API to control scrapping. 

### Paket: de.kunz.scraping.views

A set of POJOs wrapping entity beans to simplfy the REST-API. 

### Paket: de.kunz.scraping.conf

CORS-configuration and path information.

## ScrapingEJB

### Overwiew

ScrapingEJB's EJBs architecture ist inspired by the pipe and filter architecture style. The system is mainly comprised by the following subsystems: 

* Sourcing
* Mapping
* Identification
* Reduction 
* Synchronization 

Sourcing implementes the communication with a variety of web resources providing information on insurance brokers and their business relations. Each insurance broker found is represented by an instance of a respective entitiy bean (Broker).  

Once created by the sourcing subsystem instances of Broker are asynchronously passed to the mapping subsytem, which performs datasource-specific cleaning and transformation operations.  

In practice, information on a particular insurance broker is scattered across a wide range of datasoruce. As pointed out above, each datatsource creates instances of Broker individually, which in turn might lead to a situation, in which 

* instances of broker representing the same physical entity must be matched,
* information must be beaggregated
* and conflicts must be resolved. 

These steps are mainly conducted by the reduction subsytem, whose implementation relies on the identification subsytem deciding whether to instances of Broker correspond to the same physical entity. 

Once reduction is completed, each insurance broker is represented by exactly on instance of Broker. Those instances are asynchronously passed to the synchronization subsystem whose task is to update the underlying database accordingly. 

In the following I would like to provide a more detailed overview over the most important packages.

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

Webresourcen, welche Informationen über Vermittler bereitstellen, werden eine Implementierung des Interfaces IDatasource repräsentiert. 
