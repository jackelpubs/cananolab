
function getUnit(fileInd, datumInd) {
	var datumName = document.getElementById("datumName" + fileInd + "-" + datumInd).value;
	CharacterizationManager.getDerivedDatumValueUnits(datumName, function (data) {
		dwr.util.removeAllOptions("unit" + fileInd + "-" + datumInd);
		dwr.util.addOptions("unit" + fileInd + "-" + datumInd, [""]);
		dwr.util.addOptions("unit" + fileInd + "-" + datumInd, data);
		dwr.util.addOptions("unit" + fileInd + "-" + datumInd, ["[Other]"]);
	});
}
function setCharacterizationOptionsByCharTypeWithOther() {
	var charType = document.getElementById("charType").value;
	CharacterizationManager.getCharacterizationOptions(charType, function (data) {
		dwr.util.removeAllOptions("charName");
		dwr.util.addOptions("charName", [""]);
		dwr.util.addOptions("charName", data);
		dwr.util.addOptions("charName", ["[Other]"]);
	});
}
function setCharacterizationOptionsByCharType() {
	var charType = document.getElementById("charType").value;
	CharacterizationManager.getCharacterizationOptions(charType, function (data) {
		dwr.util.removeAllOptions("charName");
		dwr.util.addOptions("charName", data);
	});
}
function setAssayTypeOptionsByCharName() {
	var charName = document.getElementById("charName").value;
	CharacterizationManager.getAssayTypeOptions(charName, function (data) {
		dwr.util.removeAllOptions("assayType");
		dwr.util.addOptions("assayType", [""]);
		dwr.util.addOptions("assayType", data);
		dwr.util.addOptions("assayType", ["[Other]"]);
	});
}
function setCharacterizationDetail() {
	var charName = document.getElementById("charName").value;
	var inclueBlock = document.getElementById("characterizationDetail");
	CharacterizationManager.getCharacterizationDetailPage(charName, populatePage);
}
function populatePage(pageData) {
	var inclueBlock = document.getElementById("characterizationDetail");
	if (pageData == "") {
		inclueBlock.style.display = "none";
	} else {
		dwr.util.setValue("characterizationDetail", pageData, {escapeHtml:false});
		inclueBlock.style.display = "inline";
	}
}

