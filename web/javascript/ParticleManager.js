var emptyOption = [{label:"", value:""}];

function retrieveParticleNames() {
	var particleType = document.getElementById("particleType").value;
	ParticleManager.getNewParticleNamesByType(particleType, populateParticleNames);
}
function resetParticleNames() {
	dwr.util.removeAllOptions("particleName");
	dwr.util.addOptions("particleName", emptyOption, "value", "label");
}
function populateParticleNames(particleNames) {
   //get previous selection
	var selectedParticleName = dwr.util.getValue("particleName");
	//remove option that's the same as previous selection
	var updatedParticleNames = new Array();
	if (particleNames != null) {
		for (i = 0; i < particleNames.length; i++) {
			if (particleNames[i]!= selectedParticleName) {
				updatedParticleNames.push(particleNames[i]);
			}
		}
	}
	dwr.util.addOptions("particleName", updatedParticleNames);
}
function removeOrgVisibility() {
	var poc = document.getElementById("primaryPOCList").value;
	
	if(poc.charAt(0) == "[" &&
			poc.charAt(otext.length - 1) == "]") 
			return false;
			
	NanoparticleSampleManager.removeOrgVisibility(poc, function (data) {
		dwr.util.removeAllOptions("visibilityGroup");
		dwr.util.addOptions("visibilityGroup", data);
	});
	return false;
}
function removeOrgVisibility(selectId) {
	var poc = document.getElementById(selectId).value;
	
	if(poc.charAt(0) == "[" &&
			poc.charAt(otext.length - 1) == "]") 
			return false;
			
	NanoparticleSampleManager.removeOrgVisibility(poc, function (data) {
		dwr.util.removeAllOptions("visibilityGroup");
		dwr.util.addOptions("visibilityGroup", data);
	});
	return false;
}

function setNanoparticleDropdowns() {
	var searchLocations = getSelectedOptions(document.getElementById("searchLocations"));
	NanoparticleSampleManager.getNanoparticleEntityTypes(searchLocations, function (data) {
			dwr.util.removeAllOptions("nanoparticleEntityTypes");
			dwr.util.addOptions("nanoparticleEntityTypes", data);
		});
	NanoparticleSampleManager.getFunctionalizingEntityTypes(searchLocations, function (data) {
			dwr.util.removeAllOptions("functionalizingEntityTypes");
			dwr.util.addOptions("functionalizingEntityTypes", data);
		});
		
	NanoparticleSampleManager.getFunctionTypes(searchLocations, function (data) {
			dwr.util.removeAllOptions("functionTypes");
			dwr.util.addOptions("functionTypes", data);
		});
	return false;
}

