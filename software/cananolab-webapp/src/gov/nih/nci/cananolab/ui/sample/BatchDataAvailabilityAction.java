/*
 The caNanoLab Software License, Version 1.5.2

 Copyright 2006 SAIC. This software was developed in conjunction with the National
 Cancer Institute, and so to the extent government employees are co-authors, any
 rights in such works shall be subject to Title 17 of the United States Code,
 section 105.

 */
package gov.nih.nci.cananolab.ui.sample;

/**
 * This class calls the Struts Action to execute an request
 *
 * @author lethai
 */

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.sample.DataAvailabilityService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

public class BatchDataAvailabilityAction extends Action {
	private DataAvailabilityService dataAvailabilityService;
	
	public DataAvailabilityService getDataAvailabilityService() {
		return dataAvailabilityService;
	}

	public void setDataAvailabilityService(
			DataAvailabilityService dataAvailabilityService) {
		this.dataAvailabilityService = dataAvailabilityService;
	}

	//option1 - generate new ones for samples that data availability has not been generated.
	//option2 - generate all: update existing one and generate new one.
	//option3 - delete all data availability
	//option4 - update data availability
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
		DynaActionForm theForm = (DynaActionForm)form;
		String option = theForm.getString("option");
		System.out.println("option: " + option);
		
		if("option1".equals(option)){
			//generate new ones
			generate(request);
		}else if("option2".equals(option)){
			//generate all
			generateAndUpdate(request);
		}else if("option3".equals(option)){
			// delete all
			delete(request);
		}else if("option4".equals(option)){
			// update
			update(request);
		}
		ActionMessages messages = new ActionMessages();
		ActionMessage message = new ActionMessage("message.generateDataAvailability");
		messages.add("message", message);
		saveMessages(request, messages);
		return mapping.findForward("success");
	}
	
	private void generate(HttpServletRequest request) throws Exception{
		SampleService service = getSampleService(request);		
		SecurityService securityService = getSecurityService(request);
		//find sample sampleIds
		//find data availability sampleIds
		//run generate for the one not in data availability sampleIds
		List<String> sampleIds = service.findSampleIdsBy("", "", null, null, null, null, null, null, null, null, null);
		Set<String> dataAvailabilitySampleIds = dataAvailabilityService.findSampleIds(securityService);
	
		System.out.println("samples: " +sampleIds.size() + ", existing data availability sampleIds: " + dataAvailabilitySampleIds.size());
		long start = System.currentTimeMillis();
		for(String sampleId : sampleIds){
			if(!dataAvailabilitySampleIds.contains(sampleId)){
				SampleBean sampleBean = service.findSampleById(sampleId, false);
				dataAvailabilityService.generateDataAvailability(sampleBean, securityService);
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("total time to generate: " +(end - start)/1000 + " seconds.");
		
	}

	private void generateAndUpdate(HttpServletRequest request) throws Exception{
		SampleService service = getSampleService(request);		
		SecurityService securityService = getSecurityService(request);
		// find data availability sampleIds
		// find sample sampleIds
		// run either update or generate data availability 
		List<String> sampleIds = service.findSampleIdsBy("", "", null, null, null, null, null, null, null, null, null);
		Set<String> dataAvailabilitySampleIds = dataAvailabilityService.findSampleIds(securityService);
		System.out.println("samples: " +sampleIds.size() + ", existing data availability samples: " +dataAvailabilitySampleIds.size());
		long start = System.currentTimeMillis();
		for(String sampleId : sampleIds){
			SampleBean sampleBean = service.findSampleById(sampleId, false);
			if(!dataAvailabilitySampleIds.contains(sampleId)){				
				dataAvailabilityService.generateDataAvailability(sampleBean, securityService);
			}else{
				dataAvailabilityService.saveDataAvailability(sampleBean, securityService);
			}
		}
		
		long end = System.currentTimeMillis();
		System.out.println("total time to generateAndUpdate: " +(end - start)/1000 + " seconds.");
	}
	
	private void delete(HttpServletRequest request) throws Exception{
		SampleService service = getSampleService(request);		
		SecurityService securityService = getSecurityService(request);
		dataAvailabilityService.deleteAll(securityService);
	}
	private void update(HttpServletRequest request) throws Exception{
		SampleService service = getSampleService(request);		
		SecurityService securityService = getSecurityService(request);
		// select all distinct sampleid from data availability table
		// call update on each one of them
		Set<String> sampleIds = dataAvailabilityService.findSampleIds(securityService);
		System.out.println("existing data availability samples: " +sampleIds.size());
		long start = System.currentTimeMillis();
		for(String sampleId : sampleIds){
			SampleBean sampleBean = service.findSampleById(sampleId, false);
			dataAvailabilityService.saveDataAvailability(sampleBean, securityService);
		}
		long end = System.currentTimeMillis();
		System.out.println("total time to update: " +(end - start)/1000 + " seconds.");
	}
	public boolean loginRequired() {
		return false;
	}
	
	private SampleService getSampleService(HttpServletRequest request) throws SecurityException{
		
		SampleService sampleService = (SampleService)request.getSession().getAttribute("sampleService");
		if(sampleService == null){
			sampleService = new SampleServiceLocalImpl(
					getSecurityService(request));
			request.getSession().setAttribute("sampleService", sampleService );
		}
		return sampleService;
	}
	
	private SecurityService getSecurityService (HttpServletRequest request) throws SecurityException{
		SecurityService securityService = (SecurityService) request
		.getSession().getAttribute("securityService");
		if (securityService == null) {
			securityService = new SecurityService(
					AccessibilityBean.CSM_APP_NAME);
		}
		return securityService;
	}

	
}