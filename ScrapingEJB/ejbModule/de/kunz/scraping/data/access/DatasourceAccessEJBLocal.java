package de.kunz.scraping.data.access;

import java.io.*;
import java.util.*;

import javax.ejb.*;

import de.kunz.scraping.data.entity.*;

@Local
public interface DatasourceAccessEJBLocal {
	
	public enum DatasourceIdentifier {
		
		MANUAL_INPUT(0, "MANUAL_INPUT", null);
	
		private final long datasourceId;
		private final String datasourceName;
		private final String datasourceJNDIName;
		
		private DatasourceIdentifier(long datasourceId, String datasourceName, String datasourceJNDIName) {
			this.datasourceId = datasourceId;
			this.datasourceName = datasourceName;
			this.datasourceJNDIName = datasourceJNDIName;
		}

		public long getDatasourceId() {
			return datasourceId;
		}

		public String getDatasourceName() {
			return datasourceName;
		}

		public String getDatasourceJNDIName() {
			return datasourceJNDIName;
		}
	}
	
	public List<Datasource> getDatasources() 
			throws IOException;
	
	public Datasource getDatatource(String datasourceName)	
			throws IOException;
	
	/**
	 * Returns the managed instance of {@link Datasource} identified by the passed instance of {@link DatasourceIdentifier}.
	 * 
	 * @param datasourceIdentifier identifies an instance of {@link Datasource}.
	 * @return an instance of {@link Datasource}.
	 * @throws NullPointerException if datasourceIdentifier is null.
	 * @throws IOException if an I/O error occurs.
	 */
	public Datasource getDatasource(DatasourceIdentifier datasourceIdentifier)
			throws IOException;	
}
 

