function changeMenuStyle(obj, new_style) { 
  obj.className = new_style; 
}

function showCursor(){
	document.body.style.cursor='hand';
}

function hideCursor(){
	document.body.style.cursor='default';
}

function confirmDelete(){
  if (confirm('Are you sure you want to delete?')){
    return true;
    }else{
    return false;
  }
}
function get_tree()
{

		d = new dTree('d');
		
		d.config.target="";
		
		d.add(0,-1,'Workflow');
		d.add(1,0,'Pre-screening Assays','javascript:void(0)', '', '', '');
		d.add(2,1,'STE-1','javascript:gotoPage(\'assayActionMenu.jsp\')', '', '', '');
		d.add(3,2,'run 1','javascript:void(0)');
		d.add(4,3,'In','javascript:gotoPage(\'inputActionMenu.jsp\')');
		d.add(5,4,'NCL6-7105-1','javascript:gotoPage(\'viewAliquot.do?aliquotId=NCL6-7105-1\')', '', '', '');
		d.add(6,4,'NCL6-7105-2','javascript:void(0)','','','');
		d.add(7,3,'Out','javascript:gotoPage(\'outputActionMenu.jsp\')');
		d.add(8,7,'NCL6.vaf','doc/astra_5.doc');
		d.add(9,2,'run 2','javascript:void(0)');
		d.add(10,9,'In','javascript:gotoPage(\'inputActionMenu.jsp\')');
		d.add(11,9,'Out','javascript:gotoPage(\'outputActionMenu.jsp\')');
		d.add(12,1,'STE-2','javascript:gotoPage(\'assayActionMenu.jsp\')');
		d.add(13,1,'STE-3','javascript:gotoPage(\'assayActionMenu.jsp\')', '', '', '');
		d.add(14,1,'PCC-1','javascript:gotoPage(\'assayActionMenu.jsp\')', '', '', '');
		d.add(15,0,'In Vitro Assays','javascript:void(0)', '', '', '');
		d.add(16,0,'In Vivo Assays','javascript:void(0)', '', '', '');
		d.add(17,0,'Physical Characterization Assays','javascript:void(0)', '', '', '');
		
		document.write(d);
}

function add_hist()	{
	document.getElementById('hists').options[document.getElementById('hists').options.length] = new Option("new text", "new value");
}

function gotoPage(pageURL)	{
	window.location.href=pageURL;
}

function openWindow(pageURL, name, width, height) {
    window.open (pageURL, name, "alwaysRaised,dependent,status,scrollbars,resizable,width="+width+",height="+height); 
    
}
/**
moveItems is a function used in moving items in one multiple select to the other.
*/

			function moveItems(fbox, tbox) 
			{
			var arrFbox = new Array();
			var arrTbox = new Array();
			var arrLookup = new Array();
			var i;
			for (i = 0; i < tbox.options.length; i++) {
			arrLookup[tbox.options[i].text] = tbox.options[i].value;
			arrTbox[i] = tbox.options[i].text;
			}
			var fLength = 0;
			var tLength = arrTbox.length;
			for(i = 0; i < fbox.options.length; i++) {
			arrLookup[fbox.options[i].text] = fbox.options[i].value;
			if (fbox.options[i].selected && fbox.options[i].value != "") {
			arrTbox[tLength] = fbox.options[i].text;
			tLength++;
			}
			else {
			arrFbox[fLength] = fbox.options[i].text;
			fLength++;
			   }
			}
			arrFbox.sort();
			arrTbox.sort();
			fbox.length = 0;
			tbox.length = 0;
			var c;
			for(c = 0; c < arrFbox.length; c++) {
			var no = new Option();
			no.value = arrLookup[arrFbox[c]];
			no.text = arrFbox[c];
			fbox[c] = no;
			}
			for(c = 0; c < arrTbox.length; c++) {
			var no = new Option();
			no.value = arrLookup[arrTbox[c]];
			no.text = arrTbox[c];
			tbox[c] = no;
			   }
			}

