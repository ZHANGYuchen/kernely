package org.kernely.invoice;

import static junit.framework.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.kernely.core.common.AbstractServiceTest;
import org.kernely.invoice.dto.InvoiceCreationRequestDTO;
import org.kernely.invoice.dto.InvoiceDTO;
import org.kernely.invoice.dto.InvoiceLineCreationRequestDTO;
import org.kernely.invoice.dto.InvoiceLineDTO;
import org.kernely.invoice.model.Invoice;
import org.kernely.invoice.service.InvoiceService;
import org.kernely.project.dto.OrganizationCreationRequestDTO;
import org.kernely.project.dto.OrganizationDTO;
import org.kernely.project.dto.ProjectCreationRequestDTO;
import org.kernely.project.dto.ProjectDTO;
import org.kernely.project.service.OrganizationService;
import org.kernely.project.service.ProjectService;

import com.google.inject.Inject;

public class InvoiceServiceTest extends AbstractServiceTest{

	private static final String NAME_ORGANIZATION1 = "Kernely CORP";
	private static final String NAME_ORGANIZATION2 = "Kernely CORP";
	private static final String NAME_PROJECT1 = "Secret Project 1";
	private static final String NAME_PROJECT2 = "Secret Project 2";
	private static final String NAME_PROJECT3 = "Secret Project 3";
	private static final String NAME_PROJECT4 = "Secret Project 4";
	private static final Date DATE_PUBLICATION = new DateTime().plusMonths(1).withDayOfMonth(3).toDateMidnight().toDate();
	private static final Date DATE_TERM = new DateTime().plusMonths(2).withDayOfMonth(3).toDateMidnight().toDate();
	private static final String DATE_PUBLICATION_STRING = new DateTime().plusMonths(1).withDayOfMonth(3).toDateMidnight().toString("MM/dd/yyyy");
	private static final String DATE_TERM_STRING = new DateTime().plusMonths(2).withDayOfMonth(3).toDateMidnight().toString("MM/dd/yyyy");
	private static final String OBJECT1 = "INVOICE1 OBJECT";
	private static final String OBJECT2 = "INVOICE2 OBJECT";
	private static final String OBJECT3 = "INVOICE3 OBJECT";
	private static final String OBJECT4 = "INVOICE4 OBJECT";
	
	private static final String DESIGNATION1 = "Line Invoice Designation 1";
	private static final String DESIGNATION1_MODIFIED = "Line Invoice Designation 1";
	private static final float QUANTITY1 = 1.5F;
	private static final float QUANTITY1_MODIFIED = 2.0F;
	private static final float UNITPRICE1 = 10F;
	private static final float UNITPRICE1_MODIFIED = 5.0F;
	private static final float AMOUNT1 = QUANTITY1 * UNITPRICE1;
	private static final float AMOUNT1_MODIFIED = QUANTITY1_MODIFIED * UNITPRICE1_MODIFIED;
	
	
	
	
	@Inject
	private InvoiceService invoiceService;
	
	@Inject
	private ProjectService projectService;

	@Inject
	private OrganizationService organizationService;

	
	
	private OrganizationDTO createOrganization1ForTest(){
		OrganizationCreationRequestDTO organizationRequest = new OrganizationCreationRequestDTO(1, NAME_ORGANIZATION1, null, null, null, null, null, null);
		return organizationService.createOrganization(organizationRequest);
	}
	
	private OrganizationDTO createOrganization2ForTest(){
		OrganizationCreationRequestDTO organizationRequest = new OrganizationCreationRequestDTO(1, NAME_ORGANIZATION2, null, null, null, null, null, null);
		return organizationService.createOrganization(organizationRequest);
	}
	
	private ProjectDTO createProject1ForTest(){
		ProjectCreationRequestDTO projectRequest = new ProjectCreationRequestDTO(NAME_PROJECT1, 1, NAME_PROJECT1, NAME_ORGANIZATION1);
		return projectService.createProject(projectRequest);
	}
	
	private ProjectDTO createProject2ForTest(){
		ProjectCreationRequestDTO projectRequest = new ProjectCreationRequestDTO(NAME_PROJECT2, 2, NAME_PROJECT2, NAME_ORGANIZATION1);
		return projectService.createProject(projectRequest);
	}
	
	private ProjectDTO createProject3ForTest(){
		ProjectCreationRequestDTO projectRequest = new ProjectCreationRequestDTO(NAME_PROJECT3, 3, NAME_PROJECT3, NAME_ORGANIZATION2);
		return projectService.createProject(projectRequest);
	}
	
	private ProjectDTO createProject4ForTest(){
		ProjectCreationRequestDTO projectRequest = new ProjectCreationRequestDTO(NAME_PROJECT4, 4, NAME_PROJECT4, NAME_ORGANIZATION2);
		return projectService.createProject(projectRequest);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void createInvoiceWithNullRequestTest(){
		invoiceService.createOrUpdateInvoice(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void createInvoiceWithUndefinedProjectTest(){
		InvoiceCreationRequestDTO request = new InvoiceCreationRequestDTO();
		request.id = 0;
		request.datePublication = DATE_PUBLICATION_STRING;
		request.dateTerm = DATE_TERM_STRING;
		request.object= OBJECT1;
		
		invoiceService.createOrUpdateInvoice(request);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void createInvoiceWithUndefinedPublicationDateTest(){
		createOrganization1ForTest();
		ProjectDTO project1 = createProject1ForTest();
		InvoiceCreationRequestDTO request = new InvoiceCreationRequestDTO();
		request.id = 0;
		request.dateTerm = DATE_TERM_STRING;
		request.projectId = project1.id;
		request.object= OBJECT1;
		
		invoiceService.createOrUpdateInvoice(request);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void createInvoiceWithUndefinedTermDateTest(){
		createOrganization1ForTest();
		ProjectDTO project1 = createProject1ForTest();
		InvoiceCreationRequestDTO request = new InvoiceCreationRequestDTO();
		request.id = 0;
		request.datePublication = DATE_PUBLICATION_STRING;
		request.projectId = project1.id;
		request.object= OBJECT1;
		
		invoiceService.createOrUpdateInvoice(request);
	}
			
	@Test
	public void createInvoiceTest(){
		OrganizationDTO organization = createOrganization1ForTest();
		ProjectDTO project1 = createProject1ForTest();
		InvoiceCreationRequestDTO request = new InvoiceCreationRequestDTO();
		request.id = 0;
		request.datePublication = DATE_PUBLICATION_STRING;
		request.dateTerm = DATE_TERM_STRING;
		request.projectId = project1.id;
		request.object= OBJECT1;
		
		InvoiceDTO invoiceDTO = invoiceService.createOrUpdateInvoice(request);
		
		assertEquals(DATE_PUBLICATION, invoiceDTO.datePublication);
		assertEquals(DATE_TERM, invoiceDTO.dateTerm);
		assertEquals(DateTime.now().getMonthOfYear(), new DateTime(invoiceDTO.dateCreation).getMonthOfYear());
		assertEquals(DateTime.now().getDayOfMonth(), new DateTime(invoiceDTO.dateCreation).getDayOfMonth());
		assertEquals(DateTime.now().getHourOfDay(), new DateTime(invoiceDTO.dateCreation).getHourOfDay());
		assertEquals(DateTime.now().getMinuteOfHour(), new DateTime(invoiceDTO.dateCreation).getMinuteOfHour());
		
		assertEquals(OBJECT1, invoiceDTO.object);
		assertEquals(organization.name, invoiceDTO.organizationName);
		assertEquals(project1.name, invoiceDTO.projectName);
		assertEquals(Invoice.INVOICE_UNDEFINED, invoiceDTO.status);
	}
	
	@Test
	public void updateInvoiceTest(){
		OrganizationDTO organization = createOrganization1ForTest();
		ProjectDTO project1 = createProject1ForTest();
		InvoiceCreationRequestDTO request = new InvoiceCreationRequestDTO();
		request.id = 0;
		request.datePublication = DATE_PUBLICATION_STRING;
		request.dateTerm = DATE_TERM_STRING;
		request.projectId = project1.id;
		request.object= OBJECT1;
		
		InvoiceDTO invoiceDTO = invoiceService.createOrUpdateInvoice(request);
		
		assertEquals(DATE_PUBLICATION, invoiceDTO.datePublication);
		assertEquals(DATE_TERM, invoiceDTO.dateTerm);
		assertEquals(DateTime.now().getMonthOfYear(), new DateTime(invoiceDTO.dateCreation).getMonthOfYear());
		assertEquals(DateTime.now().getDayOfMonth(), new DateTime(invoiceDTO.dateCreation).getDayOfMonth());
		assertEquals(DateTime.now().getHourOfDay(), new DateTime(invoiceDTO.dateCreation).getHourOfDay());
		assertEquals(DateTime.now().getMinuteOfHour(), new DateTime(invoiceDTO.dateCreation).getMinuteOfHour());
		
		assertEquals(OBJECT1, invoiceDTO.object);
		assertEquals(organization.name, invoiceDTO.organizationName);
		assertEquals(project1.name, invoiceDTO.projectName);
		assertEquals(Invoice.INVOICE_UNDEFINED, invoiceDTO.status);
		
		request = new InvoiceCreationRequestDTO();
		request.id = invoiceDTO.id;
		request.datePublication = DATE_PUBLICATION_STRING;
		request.dateTerm = DATE_TERM_STRING;
		request.projectId = project1.id;
		request.object= OBJECT4;
		
		InvoiceDTO invoiceDTO2 = invoiceService.createOrUpdateInvoice(request);
		
		assertEquals(invoiceDTO.id, invoiceDTO2.id);
		assertEquals(DATE_PUBLICATION, invoiceDTO2.datePublication);
		assertEquals(DATE_TERM, invoiceDTO2.dateTerm);
		assertEquals(DateTime.now().getMonthOfYear(), new DateTime(invoiceDTO2.dateCreation).getMonthOfYear());
		assertEquals(DateTime.now().getDayOfMonth(), new DateTime(invoiceDTO2.dateCreation).getDayOfMonth());
		assertEquals(DateTime.now().getHourOfDay(), new DateTime(invoiceDTO2.dateCreation).getHourOfDay());
		assertEquals(DateTime.now().getMinuteOfHour(), new DateTime(invoiceDTO2.dateCreation).getMinuteOfHour());
		
		assertEquals(OBJECT4, invoiceDTO2.object);
		assertEquals(organization.name, invoiceDTO2.organizationName);
		assertEquals(project1.name, invoiceDTO2.projectName);
		assertEquals(Invoice.INVOICE_UNDEFINED, invoiceDTO2.status);
	}
	
	
	@Test
	public void getAllInvoiceTest(){
		OrganizationDTO organization = createOrganization1ForTest();
		ProjectDTO project1 = createProject1ForTest();
		InvoiceCreationRequestDTO request = new InvoiceCreationRequestDTO();
		request.id = 0;
		request.datePublication = DATE_PUBLICATION_STRING;
		request.dateTerm = DATE_TERM_STRING;
		request.projectId = project1.id;
		request.object= OBJECT1;
		
		invoiceService.createOrUpdateInvoice(request);
		
		List<InvoiceDTO> invoices = invoiceService.getAllInvoices();
		InvoiceDTO invoice = invoices.get(0);
		
		assertEquals(OBJECT1, invoice.object);
		assertEquals(organization.name, invoice.organizationName);
		assertEquals(project1.name, invoice.projectName);
		assertEquals(Invoice.INVOICE_UNDEFINED, invoice.status);
	}
	
	public void getInvoicePerOrganizationAndProject(){
		OrganizationDTO organization1 = createOrganization1ForTest();
		ProjectDTO project1 = createProject1ForTest();
		ProjectDTO project2 = createProject2ForTest();
		
		OrganizationDTO organization2 = createOrganization2ForTest();
		ProjectDTO project3 = createProject3ForTest();
		ProjectDTO project4 = createProject4ForTest();
		
		
		InvoiceCreationRequestDTO request = new InvoiceCreationRequestDTO();
		request.id = 0;
		request.datePublication = DATE_PUBLICATION_STRING;
		request.dateTerm = DATE_TERM_STRING;
		request.projectId = project1.id;
		request.object= OBJECT1;
		
		invoiceService.createOrUpdateInvoice(request);
		
		request = new InvoiceCreationRequestDTO();
		request.id = 0;
		request.datePublication = DATE_PUBLICATION_STRING;
		request.dateTerm = DATE_TERM_STRING;
		request.projectId = project2.id;
		request.object= OBJECT2;
		
		invoiceService.createOrUpdateInvoice(request);
		
		request = new InvoiceCreationRequestDTO();
		request.id = 0;
		request.datePublication = DATE_PUBLICATION_STRING;
		request.dateTerm = DATE_TERM_STRING;
		request.projectId = project3.id;
		request.object= OBJECT3;
		
		invoiceService.createOrUpdateInvoice(request);
		
		request = new InvoiceCreationRequestDTO();
		request.id = 0;
		request.datePublication = DATE_PUBLICATION_STRING;
		request.dateTerm = DATE_TERM_STRING;
		request.projectId = project4.id;
		request.object= OBJECT4;
		
		invoiceService.createOrUpdateInvoice(request);
		
		List<InvoiceDTO> invoices = invoiceService.getInvoicesPerOrganizationAndProject(0, 0);
		assertEquals(4, invoices.size());
		
		invoices = invoiceService.getInvoicesPerOrganizationAndProject(organization1.id, 0);
		assertEquals(2, invoices.size());
		
		invoices = invoiceService.getInvoicesPerOrganizationAndProject(organization2.id, 0);
		assertEquals(2, invoices.size());
		
		invoices = invoiceService.getInvoicesPerOrganizationAndProject(organization1.id, project1.id);
		InvoiceDTO invoice = invoices.get(0);
		
		assertEquals(OBJECT1, invoice.object);
		assertEquals(organization1.name, invoice.organizationName);
		assertEquals(project1.name, invoice.projectName);
		assertEquals(Invoice.INVOICE_UNDEFINED, invoice.status);
		
		invoices = invoiceService.getInvoicesPerOrganizationAndProject(organization1.id, project2.id);
		invoice = invoices.get(0);
		
		assertEquals(OBJECT2, invoice.object);
		assertEquals(organization1.name, invoice.organizationName);
		assertEquals(project2.name, invoice.projectName);
		assertEquals(Invoice.INVOICE_UNDEFINED, invoice.status);
		
		invoices = invoiceService.getInvoicesPerOrganizationAndProject(organization1.id, project1.id);
		invoice = invoices.get(0);
		
		assertEquals(OBJECT3, invoice.object);
		assertEquals(organization2.name, invoice.organizationName);
		assertEquals(project3.name, invoice.projectName);
		assertEquals(Invoice.INVOICE_UNDEFINED, invoice.status);
		
		invoices = invoiceService.getInvoicesPerOrganizationAndProject(organization1.id, project1.id);
		invoice = invoices.get(0);
		
		assertEquals(OBJECT4, invoice.object);
		assertEquals(organization2.name, invoice.organizationName);
		assertEquals(project4.name, invoice.projectName);
		assertEquals(Invoice.INVOICE_UNDEFINED, invoice.status);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getInvoicePerOrganizationAndProjectWithWrongMatching(){
		OrganizationDTO organization1 = createOrganization1ForTest();
		
		createOrganization2ForTest();
		ProjectDTO project3 = createProject3ForTest();
		
		invoiceService.getInvoicesPerOrganizationAndProject(organization1.id, project3.id);
	}
	
	@Test
	public void getInvoiceByIdTest(){
		OrganizationDTO organization = createOrganization1ForTest();
		ProjectDTO project1 = createProject1ForTest();
		InvoiceCreationRequestDTO request = new InvoiceCreationRequestDTO();
		request.id = 0;
		request.datePublication = DATE_PUBLICATION_STRING;
		request.dateTerm = DATE_TERM_STRING;
		request.projectId = project1.id;
		request.object= OBJECT1;
		
		InvoiceDTO invoice = invoiceService.createOrUpdateInvoice(request);
		
		InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(invoice.id);
		
		assertEquals(OBJECT1, invoiceDTO.object);
		assertEquals(organization.name, invoiceDTO.organizationName);
		assertEquals(project1.name, invoiceDTO.projectName);
		assertEquals(Invoice.INVOICE_UNDEFINED, invoiceDTO.status);
	}
	
	@Test
	public void createInvoiceLine(){
		createOrganization1ForTest();
		ProjectDTO project1 = createProject1ForTest();
		InvoiceCreationRequestDTO request = new InvoiceCreationRequestDTO();
		request.id = 0;
		request.datePublication = DATE_PUBLICATION_STRING;
		request.dateTerm = DATE_TERM_STRING;
		request.projectId = project1.id;
		request.object= OBJECT1;
		
		InvoiceDTO invoiceDTO = invoiceService.createOrUpdateInvoice(request);
		
		InvoiceLineCreationRequestDTO requestLine = new InvoiceLineCreationRequestDTO();
		requestLine.designation = DESIGNATION1;
		requestLine.quantity = QUANTITY1;
		requestLine.unitPrice = UNITPRICE1;
		requestLine.invoiceId = invoiceDTO.id;
		
		InvoiceLineDTO invoiceLineDTO = invoiceService.createOrUpdateInvoiceLine(requestLine);
		assertEquals(DESIGNATION1, invoiceLineDTO.designation);
		assertEquals(QUANTITY1, invoiceLineDTO.quantity);
		assertEquals(UNITPRICE1, invoiceLineDTO.unitPrice);
		assertEquals(AMOUNT1, invoiceLineDTO.amount);
		assertEquals(invoiceDTO.id, invoiceLineDTO.invoiceId);
		
	}
	
	@Test
	public void updateInvoiceLine(){
		createOrganization1ForTest();
		ProjectDTO project1 = createProject1ForTest();
		InvoiceCreationRequestDTO request = new InvoiceCreationRequestDTO();
		request.id = 0;
		request.datePublication = DATE_PUBLICATION_STRING;
		request.dateTerm = DATE_TERM_STRING;
		request.projectId = project1.id;
		request.object= OBJECT1;
		
		InvoiceDTO invoiceDTO = invoiceService.createOrUpdateInvoice(request);
		
		InvoiceLineCreationRequestDTO requestLine = new InvoiceLineCreationRequestDTO();
		requestLine.designation = DESIGNATION1;
		requestLine.quantity = QUANTITY1;
		requestLine.unitPrice = UNITPRICE1;
		requestLine.invoiceId = invoiceDTO.id;
		
		InvoiceLineDTO invoiceLineDTO = invoiceService.createOrUpdateInvoiceLine(requestLine);
		assertEquals(DESIGNATION1, invoiceLineDTO.designation);
		assertEquals(QUANTITY1, invoiceLineDTO.quantity);
		assertEquals(UNITPRICE1, invoiceLineDTO.unitPrice);
		assertEquals(AMOUNT1, invoiceLineDTO.amount);
		assertEquals(invoiceDTO.id, invoiceLineDTO.invoiceId);
		
		InvoiceLineCreationRequestDTO requestLine2 = new InvoiceLineCreationRequestDTO();
		requestLine2.id = invoiceLineDTO.id;
		requestLine2.designation = DESIGNATION1_MODIFIED;
		requestLine2.quantity = QUANTITY1_MODIFIED;
		requestLine2.unitPrice = UNITPRICE1_MODIFIED;
		requestLine2.invoiceId = invoiceDTO.id;
		
		InvoiceLineDTO invoiceLineDTO2 = invoiceService.createOrUpdateInvoiceLine(requestLine2);
		assertEquals(DESIGNATION1_MODIFIED, invoiceLineDTO2.designation);
		assertEquals(QUANTITY1_MODIFIED, invoiceLineDTO2.quantity);
		assertEquals(UNITPRICE1_MODIFIED, invoiceLineDTO2.unitPrice);
		assertEquals(AMOUNT1_MODIFIED, invoiceLineDTO2.amount);
		assertEquals(invoiceDTO.id, invoiceLineDTO2.invoiceId);
	}
	
	@Test
	public void deleteLinesOfInvoiceTest(){
		createOrganization1ForTest();
		ProjectDTO project1 = createProject1ForTest();
		InvoiceCreationRequestDTO request = new InvoiceCreationRequestDTO();
		request.id = 0;
		request.datePublication = DATE_PUBLICATION_STRING;
		request.dateTerm = DATE_TERM_STRING;
		request.projectId = project1.id;
		request.object= OBJECT1;
		
		InvoiceDTO invoiceDTO = invoiceService.createOrUpdateInvoice(request);

		InvoiceLineCreationRequestDTO requestLine = new InvoiceLineCreationRequestDTO();
		requestLine.designation = DESIGNATION1;
		requestLine.quantity = QUANTITY1;
		requestLine.unitPrice = UNITPRICE1;
		requestLine.invoiceId = invoiceDTO.id;
		
		invoiceService.createOrUpdateInvoiceLine(requestLine);
		
		InvoiceLineCreationRequestDTO requestLine2 = new InvoiceLineCreationRequestDTO();
		requestLine2.designation = DESIGNATION1_MODIFIED;
		requestLine2.quantity = QUANTITY1_MODIFIED;
		requestLine2.unitPrice = UNITPRICE1_MODIFIED;
		requestLine2.invoiceId = invoiceDTO.id;
		
		invoiceService.createOrUpdateInvoiceLine(requestLine2);
				
		assertEquals(2, invoiceService.getLinesForInvoice(invoiceDTO.id).size());
		
		invoiceService.deleteAllInvoiceLines(invoiceDTO.id);
		
		assertEquals(0, invoiceService.getLinesForInvoice(invoiceDTO.id).size());
		
		
	}
}
