package de.kunz.scraping.api.views;

import de.exp.ai.scraping.data.entity.*;

public class ConcessionView {
	
	private Long concessionID = null;
	
	private String registrationNo = null;
	
	private Long datasourceID = null;
	
	public ConcessionView(BrokerConcessionMapping mapping) {
		this.registrationNo = mapping.getRegisterNo();
		final Concession concession = mapping.getConcession();
		final Datasource datasource = mapping.getDatasource();
		{
			if(concession != null) {
				this.concessionID = concession.getConcessionId();
			}
			if(datasource != null) {
				this.datasourceID = datasource.getDatasourceId();
			}
		}
	}

	public Long getConcessionID() {
		return concessionID;
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public Long getDatasourceID() {
		return datasourceID;
	}
}
