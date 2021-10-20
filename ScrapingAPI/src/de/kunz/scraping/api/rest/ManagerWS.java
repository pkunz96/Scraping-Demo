package de.kunz.scraping.api.rest;

import java.io.*;
import java.util.*;

import javax.ejb.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

import de.kunz.scraping.manager.*;
import de.kunz.scraping.conf.IScrapingConfiguration;
import de.kunz.scraping.data.access.*;
import de.kunz.scraping.data.access.CountryAccessEJBLocal.CountryIdentifier;
import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.identification.IdentityBeanLocal;
import de.kunz.scraping.manager.IContext;
import de.kunz.scraping.api.views.*;

 
@Stateless
@Path("manager")
public class ManagerWS {
	
	@EJB
	private ScrapingBeanLocal scrapingManager;
	
	@EJB
	private DatasourceAccessEJB datasourceAccess;
	
	@EJB
	private ZipCodeAccessEJBLocal zipCodeAccess;
	
	@EJB
	private CountryAccessEJB countryAccess;
	
	
	@EJB
	private IdentityBeanLocal identity;
	
	private final SecurityClient authClient;
	
	public ManagerWS() {
		authClient = new SecurityClient();
	}
	
	@GET
	@Path("queries")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ContextView> getQueries(@QueryParam("accessToken") String accessToken) {
		final List<ContextView> contextViewList = new LinkedList<>();
		{
			for(IContext context: this.scrapingManager.getRunningQueries()) {
				contextViewList.add(new ContextView(context));
			}
		}
		return contextViewList;
	}  
	 
	@GET
	@Path("runTestQuery")
	public void executeTestQuery() throws IOException {
		final Country country = this.countryAccess.getCountry(CountryIdentifier.GERMANY);
		final ZipCode pattern = new ZipCode();
		pattern.setZipCode("7018x");
		pattern.setCountry(country);
		pattern.setPattern(true);
		final List<ZipCode> zipCodePatterns = new LinkedList<>(); 
		zipCodePatterns.add(pattern);
		final List<String> datasources = new LinkedList<>();
		datasources.add("ERGO");
		this.scrapingManager.executeQuery(zipCodePatterns, datasources);
	}
	 
	
	@GET
	@Path("testConf")
	public void testConft() throws IOException {
		final Broker firstBroker = new Broker(); 
		{
			final Person person = new Person();
			person.setPrename("Michael");
			person.setLastname("Ewald Ueber");
			final PhoneNumber phoneNumber = new PhoneNumber();
			phoneNumber.setPhoneNumber("071233762207");
			final List<PhoneNumber> phoneNumberList = person.getPhoneNumberList();
			phoneNumberList.add(phoneNumber);
			person.setPhoneNumberList(phoneNumberList);
			firstBroker.setPerson(person);
			person.setBroker(firstBroker);
		} 
		final Broker secondBroker = new Broker(); 
		{
			final Person person = new Person();
			person.setPrename("Michael"); 
			person.setLastname("Ewald Über");
			final PhoneNumber phoneNumber = new PhoneNumber();
			phoneNumber.setPhoneNumber("071233762207");
			final List<PhoneNumber> phoneNumberList = person.getPhoneNumberList();
			phoneNumberList.add(phoneNumber);
			person.setPhoneNumberList(phoneNumberList);
			secondBroker.setPerson(person);
			person.setBroker(secondBroker);
		}
		
		
		System.out.println(identity.isIdentic(firstBroker, secondBroker));
	
	}
	
	@POST  
	@Path("queries")
	@Produces(MediaType.APPLICATION_JSON)
	public Response executeQuery(@QueryParam("displayName") String displayName, @QueryParam("zipCodePattern") List<String> zipCodePatterns, @QueryParam("datasource") List<String> datasources, @QueryParam("accessToken") String accessToken) {
		
		
		/*
		
		Response response;
		try {
			if((displayName == null) || (zipCodePatterns == null) || (datasources == null) || (accessToken == null)) {
				response = Response.status(Status.BAD_REQUEST).build();
			}
			else if(!authClient.authorize(accessToken, SecurityClient.Operation.EXECUTE_QUERY)) {
				response = Response.status(Status.UNAUTHORIZED).build();
			}
			else {
				final Context context = this.scrapingManager.executeQuery(zipCodePatterns, datasources); 
				context.setDisplayName(displayName);
				response = Response.ok(new ContextView(context)).build();
			}
		} catch(IOException e0) {
			response = Response.serverError().build();
		} 
		return response;*/
		return null; 
	}
  
	@GET
	@Path("result")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getResult(@QueryParam("queryID") Long queryID,  @QueryParam("accessToken") String accessToken) {
		Response response;
		try {
			if((queryID == null) || (accessToken == null)) {
				response = Response.status(Status.BAD_REQUEST).build();
			}
			else if(!authClient.authorize(accessToken, SecurityClient.Operation.GET_RESULT)) {
				response = Response.status(Status.UNAUTHORIZED).build();
			}
			else {
				final Set<IContext> runnigQueries = this.scrapingManager.getRunningQueries();
				IContext context = null;
				for(IContext curContext : runnigQueries) {
					if((curContext.getContextId() == queryID) && (IContext.QUERY_DONE.equals(curContext.getState()))) {
						context = curContext;
						break;
					}
				}
				if(context != null) {
					List<Broker> brokerList = context.getQueryResultList();
					final List<BrokerView> brokerViewList = new LinkedList<>();
					for(Broker broker : brokerList) {
						brokerViewList.add(new BrokerView(broker, true));
					}
					response = Response.ok(brokerViewList).build();
				}  
				else {
					response = Response.status(Status.NOT_FOUND).build();
				}
			}
		} catch(IOException e0) {
			response = Response.serverError().build(); 
		} 
		return response;
	} 
	
	 
	@GET 
	@Path("datasources")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getDatasources(@QueryParam("accessToken") String accessToken) 
			throws IOException {
		final List<String> datasourceNames = new LinkedList<>();
		{
			final List<Datasource> datasoucres = this.datasourceAccess.getDatasources();
			for(Datasource datasource : datasoucres) {
				datasourceNames.add(datasource.getDatasourceName());
			}
		}
		return datasourceNames;
	}
	
	
	@GET  
	@Path("zipCodePatterns")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ZipCodeView> getZipCodesPatterns(@QueryParam("accessToken") String accessToken) 
			throws IOException {
		final List<ZipCodeView> zipCodePatternViews = new LinkedList<>();
		for(ZipCode zipCodePattern: this.zipCodeAccess.getZipCodePatterns()) {
			zipCodePatternViews.add(new ZipCodeView(zipCodePattern));
		} 
		return zipCodePatternViews;
	}	
}	
      