package de.kunz.scraping.data.entity;

import javax.persistence.*;

@Entity
@Table(name="DATASOURCE")
public class Datasource {

	@Id
	@Column(name="datasource_id")
	private long datasourceId;
	
	@Column(name="datasource_name", nullable = false, unique=true)
	private String datasourceName;
	
	@Column(name="priority", nullable = false)
	private int priority;
	
	@Column(name="datasource_class_path")
	private String datasourceClassPath;
	
	@Column(name="datasource_jdni_name") 
	private String datasrouceJNDIName;
	
	public Datasource() {
		super();
	}

	public long getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(long datasourceId) {
		this.datasourceId = datasourceId;
	}

	public String getDatasourceName() {
		return datasourceName;
	}

	public void setDatasourceName(String datasourceName) {
		this.datasourceName = datasourceName;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getDatasourceClassPath() {
		return datasourceClassPath;
	}

	public void setDatasourceClassPath(String datasourceClassPath) {
		this.datasourceClassPath = datasourceClassPath;
	}

	public String getDatasrouceJNDIName() {
		return datasrouceJNDIName;
	}

	public void setDatasrouceJNDIName(String datasrouceJNDIName) {
		this.datasrouceJNDIName = datasrouceJNDIName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((datasourceClassPath == null) ? 0 : datasourceClassPath.hashCode());
		result = prime * result + (int) (datasourceId ^ (datasourceId >>> 32));
		result = prime * result + ((datasourceName == null) ? 0 : datasourceName.hashCode());
		result = prime * result + ((datasrouceJNDIName == null) ? 0 : datasrouceJNDIName.hashCode());
		result = prime * result + priority;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Datasource other = (Datasource) obj;
		if (datasourceClassPath == null) {
			if (other.datasourceClassPath != null)
				return false;
		} else if (!datasourceClassPath.equals(other.datasourceClassPath))
			return false;
		if (datasourceId != other.datasourceId)
			return false;
		if (datasourceName == null) {
			if (other.datasourceName != null)
				return false;
		} else if (!datasourceName.equals(other.datasourceName))
			return false;
		if (datasrouceJNDIName == null) {
			if (other.datasrouceJNDIName != null)
				return false;
		} else if (!datasrouceJNDIName.equals(other.datasrouceJNDIName))
			return false;
		if (priority != other.priority)
			return false;
		return true;
	} 

	@Override 
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.datasourceId + ", " + this.datasourceName + ", " + this.datasourceClassPath + ", " + this.datasrouceJNDIName + "}";
	}	
}
 