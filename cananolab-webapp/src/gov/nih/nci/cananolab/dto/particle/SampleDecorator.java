package gov.nih.nci.cananolab.dto.particle;

import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.SortableName;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletContext;

import org.displaytag.decorator.TableDecorator;

/**
 * This decorator is used to for decorate different properties of a sample to be
 * shown properly in the view page using display tag lib.
 *
 * @author pansu
 *
 */
public class SampleDecorator extends TableDecorator {

	public String getDetailURL() {
		SampleBean sample = (SampleBean) getCurrentRowObject();
		String sampleId = sample.getDomain().getId().toString();
		String dispatch = "summaryView";
		String linkLabel="View";
		if (sample.getUserUpdatable()) {
			dispatch = "summaryEdit";
			linkLabel="Edit";
		}
		StringBuilder sb = new StringBuilder("<a href=");
		sb.append("sample.do?dispatch=").append(dispatch).append(
				"&page=0&sampleId=");
		sb.append(sampleId).append('>');
		sb.append(linkLabel).append("</a>");
		String link = sb.toString();
		return link;
	}

	public SortableName getSampleName() {
		SampleBean sample = (SampleBean) getCurrentRowObject();
		String sampleName = sample.getDomain().getName();
		return new SortableName(sampleName);
	}

	public String getKeywordStr() {
		SampleBean sample = (SampleBean) getCurrentRowObject();
		String keywordsStr = sample.getKeywordsStr();
		String[] strs = keywordsStr.split("\r\n");
		return StringUtils.join(strs, "<br>");
	}

	public String getCompositionStr() throws BaseException {
		SampleBean sample = (SampleBean) getCurrentRowObject();
		SortedSet<String> compEntityNames = new TreeSet<String>();
		if (sample.getFunctionalizingEntityClassNames() != null) {
			for (String name : sample.getFunctionalizingEntityClassNames()) {
				String displayName = ClassUtils.getDisplayName(name);
				if (displayName.length() == 0) {
					compEntityNames.add(name);
				} else {
					compEntityNames.add(displayName);
				}
			}
		}
		if (sample.getNanomaterialEntityClassNames() != null) {
			for (String name : sample.getNanomaterialEntityClassNames()) {
				String displayName = ClassUtils.getDisplayName(name);
				if (displayName.length() == 0) {
					compEntityNames.add(name);
				} else {
					compEntityNames.add(displayName);
				}
			}
		}
		return StringUtils.join(compEntityNames, "<br>");
	}

	public String getFunctionStr() throws BaseException {
		SampleBean sample = (SampleBean) getCurrentRowObject();
		SortedSet<String> functionNames = new TreeSet<String>();
		if (sample.getFunctionClassNames() != null) {
			for (String name : sample.getFunctionClassNames()) {
				String displayName = ClassUtils.getDisplayName(name);
				if (displayName.length() == 0) {
					functionNames.add(name);
				} else {
					functionNames.add(displayName);
				}
			}
		}
		return StringUtils.join(functionNames, "<br>");
	}

	public String getCharacterizationStr() throws BaseException {
		SampleBean sample = (SampleBean) getCurrentRowObject();
		SortedSet<String> charNames = new TreeSet<String>();
		if (sample.getCharacterizationClassNames() != null) {
			for (String name : sample.getCharacterizationClassNames()) {
				String displayName = ClassUtils.getDisplayName(name);
				charNames.add(displayName);
			}
		}
		return StringUtils.join(charNames, "<br>");
	}

	public String getPointOfContactName() throws BaseException {
		SampleBean sample = (SampleBean) getCurrentRowObject();
		return sample.getPrimaryPOCBean().getDisplayName();
	}

	public String getDataAvailabilityMetricsStr() throws BaseException {
		// need to compute this value
		Integer completeness = 39;
		SampleBean sample = (SampleBean) getCurrentRowObject();
		List<DataAvailabilityBean> dataAvailability = sample.getDataAvailability();
		//calculate percentage for caNanoLab and MINChar
		StringBuilder sb = new StringBuilder();
		if(!sample.getHasDataAvailability()){
			sb.append("NA");
		}else{
			sb.append("<a href=");
			sb.append("sample.do?dispatch=summaryView&page=0&sampleId=");
			String sampleId = sample.getDomain().getId().toString();
			sb.append(sampleId).append('>');
			sb.append(calculateDataAvailabilityScore(dataAvailability)).append("</a>");
		}		
		
		String link = sb.toString();
		return link;
	}
	
	private String calculateDataAvailabilityScore(List<DataAvailabilityBean> dataAvailability){
		int size = dataAvailability.size();
		int minCharSize=0;
		ServletContext appContext = super.getPageContext().getServletContext();
		SortedSet<String> minchar = (SortedSet<String>)appContext.getAttribute("MINChar");
		Map<String , SortedSet<String>> attributes = (Map<String,SortedSet<String>>)appContext.getAttribute("caNano2MINChar");
		int totalMinCharSize = minchar.size();
		List<String> caNanoForMincharEntities = new ArrayList<String>();
		Set<String> keySet = attributes.keySet();
		for(String key: keySet){
			//System.out.println("key: " + key);
			SortedSet<String> values = attributes.get(key);
			String value = values.first();
			//System.out.println("first value: "+values.first() + " tostring: " + values.toString()+ ", size: " + values.size());
			for(String minCharEntity: minchar){
				if(minCharEntity.equalsIgnoreCase(value)){
					caNanoForMincharEntities.add(key);
				}
			}
		}
		for(DataAvailabilityBean bean: dataAvailability){
			String availableEntityName = bean.getAvailableEntityName();
			for(String s : caNanoForMincharEntities){				
				if(s.equalsIgnoreCase(availableEntityName)){
					minCharSize++;
				}
			}
		}
		Double caNanoLabScore = new Double(size*100/30);
		Double minCharScore = new Double(minCharSize*100/totalMinCharSize);
		
		String s = "caNanoLab: " + caNanoLabScore.intValue() + "%; MINChar: " + minCharScore.intValue() + "%";
		return s;
 	}
}