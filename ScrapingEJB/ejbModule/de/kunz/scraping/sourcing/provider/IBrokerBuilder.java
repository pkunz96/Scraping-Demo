package de.kunz.scraping.sourcing.provider;

import java.util.*;

import de.kunz.scraping.data.entity.*;


/**
 * An implementation of {@link IBrokerBuilder} constructs instances of {@link Broker}
 * by means of an implementation of {@link IMasterDataProvider}.
 * 
 * @author Philipp Kunz
 */

public interface IBrokerBuilder {
	
	/**
	 * Sets an instance of {@link IMasterDataProvider}.
	 * 
	 * @throws NullPointerException - if masterDataProvider is null.
	 * @param masterDataProvider - an instance of {@link IMasterDataProvider}.
	 */
	void setMasterDataProvider(IMasterDataProvider masterDataProvider);
	
	/**
	 * Sets a list of instances of {@link Saluation} that can be asigned to an 
	 * instance of {@link Broker} to construct. 
	 * 
	 * @throws NullPointerException - if salutations is null.
	 * @param salutations - a list of instances of {@link Salutation}.
	 */
	void setSaluations(List<Salutation> salutations);
	
	/**
	 * Sets a list of instances of {@link Country} that can be asigned to an 
	 * instance of {@link Broker} to construct. 
	 * 
	 * @throws NullPointerException - if countries is null.
	 * @param countries - a list of instances of {@link Country}.
	 */
	void setCountries(List<Country> countries);
	
	/**
	 * Sets a list of instances of {@link Concession} that can be asigned to an 
	 * instance of {@link Broker} to construct. 
	 * 
	 * @throws NullPointerException - if concessions is null.
	 * @param legalStatuses - a list of instances of {@link Concession}.
	 */
	void setConcessions(List<Concession> legalStatuses);

	/**
	 * Sets a list of instances of {@link LegalStatus} that can be asigned to an 
	 * instance of {@link Broker} to construct. 
	 * 
	 * @throws NullPointerException - if legalStatuses is null.
	 * @param legalStatuses - a list of instances of {@link LegalStatus}.
	 */
	void setLegalStatuses(List<LegalStatus> legalStatuses);
	
	/**
	 * Sets a list of instances of {@link FinancialProductProvider} that can be asigned
	 * to an instance of {@link FinancialProductProvider} to construct. 
	 * 
	 * @throws NullPointerException - if productProviders is null.
	 * @param productProviders - a list of instances of {@link FinancialProductProvider}.
	 */
	void setFinancialProductProviders(List<FinancialProductProvider> productProviders);
	
	/**
	 * Sets the data source to attach to each constructed instance of {@link Broker}.
	 * 
	 * @throws NullPointerException - if datasource is null.
	 * @param datasource - an instance of {@link Datasource} to asign to each constructed
	 * instance of {@link Broker}.
	 */
	void setDatasource(Datasource datasource);

	/**
	 * Constructs instances of {@link Broker} on the basis of the passed information. Before 
	 * invocation, the interface's set-methods must have been called. 
	 * 
	 * @throws IllegalStateException - if the interface's set-methods have not been invoked 
	 * properly.
	 * @return a {@link List} of instances of {@link Broker}.
	 */
	List<Broker> build();
	
}