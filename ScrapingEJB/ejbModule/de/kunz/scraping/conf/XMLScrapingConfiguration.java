package de.kunz.scraping.conf;

import java.io.*;
import java.beans.*;
import java.util.logging.*;

import javax.xml.bind.*;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name="ScrapingConfiguration", namespace="http://exp-ai.de/services/conf/scraping")
final class XMLScrapingConfiguration implements IScrapingConfiguration, PropertyChangeListener {

	private static XMLScrapingConfiguration instance;
	
	private static Logger LOG = Logger.getLogger(XMLScrapingConfiguration.class.getName());

	private static final String SCRAPING_CONF_XML_HOME_DIR = "./conf/de/exp/ai/scraping/";
	private static final String SCRAPING_CONF_XML_PATH =  SCRAPING_CONF_XML_HOME_DIR + "configuration.xml";

	private static OutputStream getConfOutputStream() 
			throws IOException {
		new File(SCRAPING_CONF_XML_HOME_DIR).mkdirs();
		final File scrapingFile = new File(SCRAPING_CONF_XML_PATH);
		scrapingFile.createNewFile();
		return new FileOutputStream(scrapingFile);
	}
	
	private static InputStream getConfInputStream() 
			throws IOException {
		new File(SCRAPING_CONF_XML_HOME_DIR).mkdirs();
		final File scrapingFile = new File(SCRAPING_CONF_XML_PATH);
		scrapingFile.createNewFile();
		return new FileInputStream(scrapingFile);
	}
	
	public static synchronized XMLScrapingConfiguration getInstance() {
		if(instance == null) {
			try {
				final InputStream confIn =  getConfInputStream();
				instance = JAXB.unmarshal(confIn, XMLScrapingConfiguration.class);
				instance.init();
			} catch(IOException e0) {
				LOG.log(Level.CONFIG, e0.getMessage());
				throw new IllegalStateException(e0);
			}
		}
		return instance;
	}

	@XmlElement(name="SourcingConfiguration")
	private final XMLSourcingConfiguration sourcingConfiguration;
	
	@XmlElement(name="MappingConfiguration")
	private final XMLMappingConfiguration mappingConfiguration;

	@XmlElement(name="IdentificationConfiguration")
	private final XMLIdentificationConfiguration identificationConfiguration;
	
	@XmlElement(name="ReductionConfiguration")
	private final XMLReductionConfiguration reductionConfiguration;
	
	@XmlElement(name="SynchronizationConfiguration")
	private final XMLSynchronizationConfiguration synchronizationConfiguration;
	
	@XmlElement(name="ProxyConfiguration")
	private XMLProxyConfiguration proxyConfiguration;
	
	public XMLScrapingConfiguration() {
		super();
		this.sourcingConfiguration = new XMLSourcingConfiguration();
		this.mappingConfiguration = new XMLMappingConfiguration();
		this.identificationConfiguration = new XMLIdentificationConfiguration();
		this.reductionConfiguration = new XMLReductionConfiguration();
		this.synchronizationConfiguration = new XMLSynchronizationConfiguration();
	}

	@Override
	public IProxyConfiguration getProxyConfiguration() {
		if(this.proxyConfiguration == null) {
			this.proxyConfiguration = new XMLProxyConfiguration();
			this.proxyConfiguration.addPropertyChangeListener(this);
			persist();
		}
		return this.proxyConfiguration;
	}
	
	@Override
	public ISourcingConfiguration getSourcingConfiguration() {
		return sourcingConfiguration;
	}

	@Override
	public IMappingConfiguration getMappingConfiguration() {
		return mappingConfiguration;
	}
	
	@Override
	public IIdentificationConfiguration getIdentificationConfiguration() {
		return this.identificationConfiguration;
	}
 
	@Override
	public IReductionConfiguration getReductionConfiguration() {
		return reductionConfiguration;
	}

	@Override
	public ISynchronizationConfiguration getSynchronizationConfiguration() {
		return synchronizationConfiguration;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		persist();
	} 

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mappingConfiguration == null) ? 0 : mappingConfiguration.hashCode());
		result = prime * result + ((reductionConfiguration == null) ? 0 : reductionConfiguration.hashCode());
		result = prime * result + ((sourcingConfiguration == null) ? 0 : sourcingConfiguration.hashCode());
		result = prime * result
				+ ((synchronizationConfiguration == null) ? 0 : synchronizationConfiguration.hashCode());
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
		XMLScrapingConfiguration other = (XMLScrapingConfiguration) obj;
		if (mappingConfiguration == null) {
			if (other.mappingConfiguration != null)
				return false;
		} else if (!mappingConfiguration.equals(other.mappingConfiguration))
			return false;
		if (reductionConfiguration == null) {
			if (other.reductionConfiguration != null)
				return false;
		} else if (!reductionConfiguration.equals(other.reductionConfiguration))
			return false;
		if (sourcingConfiguration == null) {
			if (other.sourcingConfiguration != null)
				return false;
		} else if (!sourcingConfiguration.equals(other.sourcingConfiguration))
			return false;
		if (synchronizationConfiguration == null) {
			if (other.synchronizationConfiguration != null)
				return false;
		} else if (!synchronizationConfiguration.equals(other.synchronizationConfiguration))
			return false;
		return true;
	}
	
	void init() {
		this.sourcingConfiguration.addListener(this);
		this.mappingConfiguration.addListener(this);
		this.identificationConfiguration.addListener(this);
		this.reductionConfiguration.addListener(this);
		this.synchronizationConfiguration.addListener(this);
		this.sourcingConfiguration.init();
		this.mappingConfiguration.init();
		this.identificationConfiguration.init();
		this.reductionConfiguration.init();
		this.synchronizationConfiguration.init();
		if(this.proxyConfiguration != null) {
			this.proxyConfiguration.addPropertyChangeListener(this);
		}
	} 
	
	private void persist() {
		OutputStream destFile = null;
		try {
			destFile = getConfOutputStream();
			JAXB.marshal(this, destFile);
		} catch(IOException e0) {
			LOG.log(Level.CONFIG, e0.getMessage());
		}
		finally {
			if(destFile != null) {
				try {
					destFile.close(); 
				} catch(IOException e0) {
					LOG.log(Level.CONFIG, e0.getMessage());
				}
			}
		}
	}
}
