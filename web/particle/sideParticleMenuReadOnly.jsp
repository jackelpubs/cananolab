<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/sidemenu.css">
<script type="text/javascript" src="javascript/sidemenu.js"></script>

<c:choose>
	<c:when test="${!empty theParticle}">
		<c:set var="particleName"
			value="${theParticle.domainParticleSample.name}" scope="session" />
		<c:set var="particleId" value="${theParticle.domainParticleSample.id}"
			scope="session" />
		<c:set var="location" value="${theParticle.location}" scope="session" />
	</c:when>
</c:choose>
<table summary="" cellpadding="0" cellspacing="0" border="0"
	height="100%" width="150">
	<tr>
		<td class="subMenuPrimaryTitle" height="21">
			NAVIGATION TREE
		</td>
	</tr>
	<tr>
		<td>
			<ul class="slidingmenu" id="menuroot">

				<li id="view_particle">
					<b>${fn:toUpperCase(location)} PARTICLE</b>
					<br>
					<span class="pname"><c:out value="${particleName}" /> </span>
				</li>
				<li class="controlList">
					<c:url var="url" value="submitNanoparticleSample.do">
						<c:param name="dispatch" value="setupView" />
						<c:param name="particleId" value="${particleId}" />
						<c:param name="location" value="${location}" />
					</c:url>
					<a href="${url}" class="subMenuSecondary">GENERAL INFORMATION</a>
				</li>
				<li class="controlList">
					<c:url var="url" value="submitNanoparticleSample.do">
						<c:param name="dispatch" value="setupView" />
						<c:param name="particleId" value="${particleId}" />
						<c:param name="location" value="${location}" />
					</c:url>
					<a href="${url}" class="subMenuSecondary">SAMPLE COMPOSITION</a>
				</li>
				<li class="controlList">
					<c:url var="url" value="submitNanoparticleSample.do">
						<c:param name="dispatch" value="setupView" />
						<c:param name="particleId" value="${particleId}" />
						<c:param name="location" value="${location}" />
					</c:url>
					<a href="${url}" class="subMenuSecondary">CHARACTERIZATION</a>
				</li>
				<li class="controlList">
					<c:url var="url" value="submitPublication.do">
						<c:param name="dispatch" value="setupView" />
						<c:param name="particleId" value="${particleId}" />
						<c:param name="location" value="${location}" />
					</c:url>
					<a href="${url}" class="subMenuSecondary">PUBLICATION</a>
				</li>
			</ul>
		</td>
	</tr>
	<tr>
		<td class="subMenuFill">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="subMenuPrimaryTitle" height="27">
			QUICK LINKS
		</td>
	</tr>
	<tr>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onclick="openWindow('https://wiki.nci.nih.gov/display/ICR/caNanoLab', '', '800', '800')"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			height="20">
			<a class="subMenuSecondary">caNanoLab Wiki</a>
		</td>
	</tr>
	<tr>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onclick="openWindow('http://www.cancer.gov', '', '800', '800')"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			height="20">
			<a class="subMenuSecondary">NCI HOME</a>
		</td>
	</tr>
	<tr>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver')"
			onclick="openWindow('http://ncicb.nci.nih.gov/', '', '800', '800')"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle')"
			height="20">
			<a class="subMenuSecondary">NCICB HOME</a>
		</td>
	</tr>
	<tr>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver')"
			onclick="openWindow('http://ncl.cancer.gov/', '', '800', '800')"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle')"
			height="20">
			<a class="subMenuSecondary">NCL HOME</a>
		</td>
	</tr>
	<tr>
		<td class="subMenuFill" height="100%">
			&nbsp;
		</td>
	</tr>

	<tr>
		<td class="subMenuFooter" height="22">
			&nbsp;
		</td>
	</tr>
</table>
