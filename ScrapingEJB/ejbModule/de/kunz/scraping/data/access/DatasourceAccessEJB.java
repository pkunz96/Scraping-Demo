package de.kunz.scraping.data.access;

import java.io.*;
import java.util.List;

import javax.ejb.*;
import javax.persistence.*;

import de.kunz.scraping.data.entity.*;

/**
 * Session Bean implementation class DatasourceAccessEJB
 */
@Stateless
@LocalBean
public class DatasourceAccessEJB implements DatasourceAccessEJBLocal {

	@PersistenceContext
	private EntityManager entityMananger;
	
    /**
     * Constructs an instance of DatasourceAccessEJB.
     */
    public DatasourceAccessEJB() {
    	super();
    }

	@Override
	public List<Datasource> getDatasources() 
			throws IOException {
		final List<Datasource> datasourceList;
		try {
			final String JPQL_QUERY_FIND_DATASOURCES = "SELECT c FROM Datasource c";
			final TypedQuery<Datasource> jpqlQueryFindDatasources = this.entityMananger.createQuery(JPQL_QUERY_FIND_DATASOURCES, Datasource.class);
			datasourceList = jpqlQueryFindDatasources.getResultList();
		}catch(PersistenceException e0) {
			throw new IOException(e0);
		}
		return datasourceList;
	}
	    
	@Override
	public Datasource getDatatource(String datasourceName) 
			throws IOException {
		Datasource datasource = null;
		if(datasourceName == null) {
			throw new NullPointerException();
		}
		else {
			/*final String JPQL_QUERY_FIND_DATASOURCE_BY_NAME = "SELECT c FROM Datasource c.datasource_name =:datasource_name";
			final TypedQuery<Datasource> jpqlQueryFindDatasources = this.entityMananger.createQuery(JPQL_QUERY_FIND_DATASOURCE_BY_NAME, Datasource.class);
			jpqlQueryFindDatasources.setParameter("datasource_name", datasourceName);
			final List<Datasource> resultList = jpqlQueryFindDatasources.getResultList();
			if(resultList.isEmpty()) {
				datasource = null;
			}
			else {
				final int FIRST_ELEMENT = 0;
				datasource = resultList.get(FIRST_ELEMENT);
			}*/
			final List<Datasource> datasources = getDatasources();
			for(Datasource ds : datasources) {
				if(datasourceName.equals(ds.getDatasourceName())) {
					return ds;
				} 
			}
		}
		return datasource;
	}

	@Override
	public Datasource getDatasource(DatasourceIdentifier datasourceIdentifier)
			throws IOException {
		Datasource datasource;
		if(datasourceIdentifier == null) {
			throw new NullPointerException();
		}
		else {
			try {
				final long datasourceId  = datasourceIdentifier.getDatasourceId();
				datasource = entityMananger.find(Datasource.class, datasourceId);
				if(datasource == null) {
					final String datasourceName = datasourceIdentifier.getDatasourceName();
					final String datasourceJNDIName = datasourceIdentifier.getDatasourceJNDIName();
					datasource = new Datasource();
					datasource.setDatasourceId(datasourceId);
					datasource.setDatasourceName(datasourceName);
					datasource.setDatasrouceJNDIName(datasourceJNDIName);
					entityMananger.persist(datasource);
				}
			}catch(PersistenceException e0) {
				throw new IOException(e0);
			}
		}
		return datasource;
	}
}



  