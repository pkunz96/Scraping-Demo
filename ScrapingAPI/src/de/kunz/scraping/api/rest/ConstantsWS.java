package de.kunz.scraping.api.rest;

import java.io.*;
import java.util.*;

import javax.ejb.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import de.exp.ai.scraping.data.entity.*;
import de.exp.ai.scraping.data.access.*;

@Stateless
@Path("constants")
public class ConstantsWS {

	@EJB
	private CountryAccessEJBLocal countryAccess;
	
	@EJB
	private ConcessionAccessEJBLocal concessionAccess;
	 
	@EJB
	private LegalStatusAccessEJBLocal legalStatusAccess;
	
	@EJB
	private SalutationAccessEJBLocal salutationAcccess;
	
	@GET
	@Path("countries")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Country> findCountries(@QueryParam("accessToken") String accessToken) 
			throws IOException {
		return countryAccess.getCountries();
	}
	
	@GET
	@Path("concessions")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Concession> findConcessions(@QueryParam("accessToken") String accessToken) 
			throws IOException {
		return concessionAccess.getConcessions();
	}
	
	@GET
	@Path("legalStatuses") 
	@Produces(MediaType.APPLICATION_JSON)
	public List<LegalStatus> findLegalStatuses(@QueryParam("accessToken") String accessToken) 
			throws IOException {
		return legalStatusAccess.getLegalStatuses();
	}
	
	@GET 
	@Path("salutations")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Salutation> getSalutations(@QueryParam("accessToken") String accessToken) 
			throws IOException {
		return this.salutationAcccess.getSalutations();
	}
}
 