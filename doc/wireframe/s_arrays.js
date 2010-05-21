// USE WORDWRAP AND MAXIMIZE THE WINDOW TO SEE THIS FILE
// v5

// === 1 === EXTRAS
s_hideTimeout=500;//1000=1 second
s_subShowTimeout=300;//if <=100 the menus will function like SM4.x
s_subMenuOffsetX=4;//pixels (if no subs, leave as you like)
s_subMenuOffsetY=1;
s_keepHighlighted=true;
s_autoSELECTED=false;//make the item linking to the current page SELECTED
s_autoSELECTEDItemsClickable=false;//look at IMPORTANT NOTES 1 in the Manual
s_autoSELECTEDTree=true;//look at IMPORTANT NOTES 1 in the Manual
s_autoSELECTEDTreeItemsClickable=true;//look at IMPORTANT NOTES 1 in the Manual
s_scrollingInterval=30;//scrolling for tall menus
s_rightToLeft=false;
s_hideSELECTsInIE=false;//look at IMPORTANT HOWTOS 7 in the Manual


// === 2 === Default TARGET for all the links
// for navigation to frame, calling functions or
// different target for any link look at
// IMPORTANT HOWTOS 1 NOTES in the Manual
s_target='self';//(newWindow/self/top)


// === 3 === STYLESHEETS- you can define different arrays and then assign
// them to any menu you want with the s_add() function
s_CSSTop=[
'#5C5C5C',	// BorderColorDOM ('top right bottom left' or 'all')
'#5C5C5C',	// BorderColorNS4
1,		// BorderWidth
'#5C5C5C',	// BgColor
1,		// Padding
'#5C5C5C',	// ItemBgColor
'#6A8989',	// ItemOverBgColor
'#FFFFFF',	// ItemFontColor
'#FFFFFF',	// ItemOverFontColor
'verdana,arial,helvetica,sans-serif',	// ItemFontFamily
'10px',		// ItemFontSize (css)
'1',		// ItemFontSize Netscape4 (look at KNOWN BUGS 3 in the Manual)
'bold',		// ItemFontWeight (bold/normal)
'left',		// ItemTextAlign (left/center/right)
3,		// ItemPadding
1,		// ItemSeparatorSize
'#8A8CCC',	// ItemSeparatorColor
'',		// IEfilter (look at Samples\IE4(5.5)Filters dirs)
true,				// UseSubImg
'',		// SubImgSrc
'',	// OverSubImgSrc
7,				// SubImgWidth
7,				// SubImgHeight
5,				// SubImgTop px (from item top)
'#6A8989',			// SELECTED ItemBgColor
'#FFFFFF',			// SELECTED ItemFontColor
'',	// SELECTED SubImgSrc
true,				// UseScrollingForTallMenus
'',	// ScrollingImgTopSrc
'',	// ScrollingImgBottomSrc
68,				// ScrollingImgWidth
12,				// ScrollingImgHeight
'',		// ItemClass (css)
'',		// ItemOverClass (css)
'',		// SELECTED ItemClass (css)
0,		// ItemBorderWidth
'#5C5C5C',	// ItemBorderColor ('top right bottom left' or 'all')
'#6A8989',	// ItemBorderOverColor ('top right bottom left' or 'all')
'#6A8989',	// SELECTED ItemBorderColor ('top right bottom left' or 'all')
0,		// ItemSeparatorSpacing
'',		// ItemSeparatorBgImage
'',		// ItemBgImage
'',		// ItemOnBgImage
''		// SELECTED ItemBgImage
];


// === 4 === MENU DEFINITIONS
//Workflow
s_add(
{
N:'workflow',	// NAME
LV:1,		// LEVEL (look at IMPORTANT NOTES 1 in the Manual)
MinW:130,	// MINIMAL WIDTH
T:138,		// TOP (look at IMPORTANT HOWTOS 6 in the Manual)
L:270,		// LEFT (look at IMPORTANT HOWTOS 6 in the Manual)
P:false,	// menu is PERMANENT (you can only set true if this is LEVEL 1 menu)
S:s_CSSTop	// STYLE Array to use for this menu
},
[		// define items {U:'url',T:'text' ...} look at the Manual for details
{U:'createWorkflow.html',T:'Execute Workflow'}
]
);
//administration
s_add(
{
N:'administration',	// NAME
LV:1,		// LEVEL (look at IMPORTANT NOTES 1 in the Manual)
MinW:130,	// MINIMAL WIDTH
T:138,		// TOP (look at IMPORTANT HOWTOS 6 in the Manual)
L:352,		// LEFT (look at IMPORTANT HOWTOS 6 in the Manual)
P:false,	// menu is PERMANENT (you can only set true if this is LEVEL 1 menu)
S:s_CSSTop	// STYLE Array to use for this menu
},
[		// define items {U:'url',T:'text' ...} look at the Manual for details
{U:'createSample.html',T:'Manage Samples'}
]
);

//Search
s_add(
{
N:'search',	// NAME
LV:1,		// LEVEL (look at IMPORTANT NOTES 1 in the Manual)
MinW:130,	// MINIMAL WIDTH
T:138,		// TOP (look at IMPORTANT HOWTOS 6 in the Manual)
L:457,		// LEFT (look at IMPORTANT HOWTOS 6 in the Manual)
P:false,	// menu is PERMANENT (you can only set true if this is LEVEL 1 menu)
S:s_CSSTop	// STYLE Array to use for this menu
},
[		// define items {U:'url',T:'text' ...} look at the Manual for details
{U:'searchWorkflow.html',T:'Search Workflow'},
{U:'searchSample.html',T:'Search Sample'}
]
);

///////////////////////////////////////////////////////////////////////
//TOOL TIP
///////////////////////////////////////////////////////////////////////

//workflow Search section assayName
s_add(
{N:'workflowSearch_assayName',LV:1,W:186,T:'mouseY-10',L:'mouseX+5',P:false,S:s_CSSTop},
[
{U:'',T:'Assay Name',SELECTED:true},
{U:'',T:'The Assay name could be wild carded by \'*\'.',NOROLL:true}
]
);
//Assay Type
s_add(
{N:'workflowSearch_assayType',LV:1,W:186,T:'mouseY+10',L:'mouseX+10',P:false,S:s_CSSTop},
[
{U:'',T:'Assay Type',SELECTED:true},
{U:'',T:'The Assay type could be selected from the list.',NOROLL:true}
]
);
//workflow Search section assay Run Date
s_add(
{N:'workflowSearch_assayRunDate',LV:1,W:186,T:'mouseY-10',L:'mouseX+5',P:false,S:s_CSSTop},
[
{U:'',T:'Assay Run Date',SELECTED:true},
{U:'',T:'The Assay date could be selected using the icon or entered in mm/dd/yyyy format.',ROLL:true}
]
);
//workflow Search section aliquot Id 
s_add(
{N:'workflowSearch_aliquotId',LV:1,W:186,T:'mouseY-10',L:'mouseX+5',P:false,S:s_CSSTop},
[
{U:'',T:'Aliquot Id',SELECTED:true},
{U:'',T:'The Assay name could be wild carded by \'*\'.',NOROLL:true}
]
);
//workflow Search on Masked aliquots 
s_add(
{N:'workflowSearch_maskedAliquots',LV:1,W:186,T:'mouseY-10',L:'mouseX+5',P:false,S:s_CSSTop},
[
{U:'',T:'Include Masked Aliquots',SELECTED:true},
{U:'',T:'Check to search on Masked Aliquots.',NOROLL:true}
]
);
//workflow Search on File names
s_add(
{N:'workflowSearch_fileName',LV:1,W:186,T:'mouseY-50',L:'mouseX+5',P:false,S:s_CSSTop},
[
{U:'',T:'File Name',SELECTED:true},
{U:'',T:'The File Name could be wild carded by \'*\'. Select whether the file is In or Out.',NOROLL:true}
]
);
//workflow Search File Submission Date
s_add(
{N:'workflowSearch_fileSubmissionDate',LV:1,W:186,T:'mouseY-100',L:'mouseX+5',P:false,S:s_CSSTop},
[
{U:'',T:'File Submission Date',SELECTED:true},
{U:'',T:'The File Submission date could be selected using the icon or entered in mm/dd/yyyy format.',NOROLL:true}
]
);
//workflow Search section File Type
s_add(
{N:'workflowSearch_fileType',LV:1,W:186,T:'mouseY-100',L:'mouseX+5',P:false,S:s_CSSTop},
[
{U:'',T:'File Type',SELECTED:true},
{U:'',T:'Available File extension type could be selected from the list .',NOROLL:true}
]
);
//workflow Search section File Submitter
s_add(
{N:'workflowSearch_fileSubmitter',LV:1,W:186,T:'mouseY+20',L:'mouseX+5',P:false,S:s_CSSTop},
[
{U:'',T:'File Submitter',SELECTED:true},
{U:'',T:'Available File submitter could be selected from the list .',NOROLL:true}
]
);
//workflow Search on Masked Files
s_add(
{N:'workflowSearch_maskedFiles',LV:1,W:186,T:'mouseY-10',L:'mouseX+5',P:false,S:s_CSSTop},
[
{U:'',T:'Include Masked Files',SELECTED:true},
{U:'',T:'Check to search on Masked Files.',NOROLL:true}
]
);

//////////////////////////////////////
//Sample Search on Masked Files
/////////////////////////////////////
s_add(
{N:'sampleSearch_sampleId',LV:1,W:186,T:'mouseY-70',L:'mouseX+5',P:false,S:s_CSSTop},
[
{U:'',T:'Sample ID',SELECTED:true},
{U:'',T:'The Sample ID could be could be selected from the list.',NOROLL:true}
]
);
s_add(
{N:'sampleSearch_lotId',LV:1,W:186,T:'mouseY-70',L:'mouseX+5',P:false,S:s_CSSTop},
[
{U:'',T:'Lot ID',SELECTED:true},
{U:'',T:'The Lot ID could be could be selected from the list.',NOROLL:true}
]
);
s_add(
{N:'sampleSearch_aliquotId',LV:1,W:186,T:'mouseY-70',L:'mouseX+5',P:false,S:s_CSSTop},
[
{U:'',T:'Aliquot ID',SELECTED:true},
{U:'',T:'The Aliquot ID could be could be selected from the list.',NOROLL:true}
]
);
s_add(
{N:'sampleSearch_sampleType',LV:1,W:186,T:'mouseY-10',L:'mouseX+120',P:false,S:s_CSSTop},
[
{U:'',T:'Sample Type',SELECTED:true},
{U:'',T:'The Sample Type could be could be selected from the list.',NOROLL:true}
]
);
s_add(
{N:'sampleSearch_dateAccessioned',LV:1,W:186,T:'mouseY-10',L:'mouseX+15',P:false,S:s_CSSTop},
[
{U:'',T:'Date Accessioned',SELECTED:true},
{U:'',T:'The Date at which the sample was entered into the system. The Date could be selected using the icon or entered in mm/dd/yyyy format.',NOROLL:true}
]
);
s_add(
{N:'sampleSearch_sampleSubmitter',LV:1,W:186,T:'mouseY-10',L:'mouseX+20',P:false,S:s_CSSTop},
[
{U:'',T:'Sample Submitter',SELECTED:true},
{U:'',T:'The Name of the user who submitted the Sample.',NOROLL:true}
]
);