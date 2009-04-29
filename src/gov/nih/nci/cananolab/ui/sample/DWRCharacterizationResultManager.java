package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.dto.common.ColumnHeader;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.exception.CharacterizationResultException;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.sample.CharacterizationResultService;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationResultServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Methods for DWR Ajax
 *
 * @author pansu, tanq
 *
 */
public class DWRCharacterizationResultManager {
	int tempRowId = -1;
	private CharacterizationResultService service = new CharacterizationResultServiceLocalImpl();

	public FindingBean resetFinding() {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		FindingBean newFindingBean = new FindingBean();
		charBean.setTheFinding(newFindingBean);
		return newFindingBean;
	}

	public FindingBean findFindingById(String findingId)
			throws CharacterizationResultException {
		Finding finding = service.findFindingById(findingId);
		FindingBean findingBean = new FindingBean(finding);
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		charBean.setTheFinding(findingBean);
		return findingBean;
	}

	public String[] getConditionOptions() throws Exception {
		WebContext wctx = WebContextFactory.get();
		SortedSet<String> conditions = InitCharacterizationSetup.getInstance()
				.getConditions(wctx.getHttpServletRequest());
		return conditions.toArray(new String[conditions.size()]);
	}

	public String[] getConditionPropertyOptions(String conditionName)
			throws Exception {
		WebContext wctx = WebContextFactory.get();

		SortedSet<String> properties = LookupService
				.getDefaultAndOtherLookupTypes(conditionName, "property",
						"otherProperty");
		return properties.toArray(new String[properties.size()]);
	}

	public String[] getDatumNameOptions(String characterizationName)
			throws Exception {
		WebContext wctx = WebContextFactory.get();
		SortedSet<String> names = InitCharacterizationSetup.getInstance()
				.getDatumNamesByCharName(wctx.getHttpServletRequest(),
						characterizationName);
		return names.toArray(new String[names.size()]);
	}

	public String[] getColumnValueUnitOptions(String name, String property)
			throws Exception {
		WebContext wctx = WebContextFactory.get();
		String valueName = name;
		if (property != null) {
			valueName = property;
		}
		SortedSet<String> units = InitCharacterizationSetup.getInstance()
				.getValueUnits(wctx.getHttpServletRequest(), valueName);
		return units.toArray(new String[units.size()]);
	}

	public FileBean resetFile() {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		FileBean newFile = new FileBean();
		charBean.getTheFinding().setTheFile(newFile);
		return newFile;
	}

	public FileBean getFileFromList(int index) {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		List<FileBean> files = charBean.getTheFinding().getFiles();
		FileBean theFile = files.get(index);
		charBean.getTheFinding().setTheFile(theFile);
		return theFile;
	}

	public ColumnHeader addColumnHeader(int columnNumber) {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		List<ColumnHeader> columnHeaders = charBean.getTheFinding()
				.getColumnHeaders();
		ColumnHeader columnHeader = columnHeaders.get(columnNumber);
		return columnHeader;
	}

	public String addColumnHeader(ColumnHeader header) {
		return header.getDisplayName();
	}

	public String getSubmitColumnPage(int columnNumber)
			throws ServletException, IOException, BaseException {
		try {
			WebContext wctx = WebContextFactory.get();
			String page = "/sample/characterization/shared/bodySubmitDataConditionMatrixColumn.jsp?cInd="
					+ columnNumber;
			String content = wctx.forwardToString(page);
			return content;
		} catch (Exception e) {
			return "";
		}
	}
}
