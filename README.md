# Scraping-Demo

## ScrapingAPI

### Paket: de.kunz.scraping.api

A set of EJBs implementing a REST-API to control scrapping. 

### Paket: de.kunz.scraping.views

A set of POJOs wrapping entity beans to simplify the REST-API. 

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

Defines an API to read and modify subsystem-specific configuration. At moment the API is implemented based on XML and JAXB. At startup the configuration file is deserialized an translated into an object tree. Any changes to objects in that tree are immediately reflected in the configuration file to ensure persistence. 

### Paket: de.kunz.scraping.data.entity

A set of entity beans that are used as data transfer objects as well. 

### Paket: de.kunz.scraping.data.access

A set of EJBs encapsulating database access. 

### Paket: de.kunz.scraping.data.querying

Defines and impelements a generic API for querying objects implementing the interface IQueryable. Objects are either generated or retrieved by an instance of IDatasource. 

In order to start querying, the client has to get an instance of IQueryBuilder<T> where T is the type ob objects to be queried. A datasource is supposed to return only objects of a type T whiche meet those criteria expressed in terms of predicates and constraints on attribues. 
  
A predicate is collection of constraints and nested predicates linked by a single logical connective. An object of type T is to be returned if and only if the overall predicate specified for the query as a whole evaluates to true.
  
Formally, predicates implement the interface IPredicate<T> whereas attribute implement IAttribute<T>. Please note that the implementation of IAttribute<T> depends on the type T, as this implementation is reponsible for checking whether a given constraint is met by a given instance of T. 

Example: 
  
>final String constraintStr = zipCodeStr + "@" + zipCodeCountryCodeStr;  
>IQueryBuilder<Broker> queryBuilder =   
>  IQueryBuilder.getInstance(Broker.class).addDatasource(datasource).startPredicate(LogicalConnective.OR).addConstraint(new ZipCode(), constraintStr, Relation.EUQAL).closePredicate();  
>IQuery<Broker> brokerQuery = queryBuilder.getQuery();  

Web resources able to provide information on insurance brokers are represented as instances of IDatasource.
  
