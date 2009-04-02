<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table id="summarySection1" width="95%" align="center"
	style="display: block" class="summaryViewLayer2">
	<tr>
		<th align="left">
			Nanomaterial Entity &nbsp;&nbsp;&nbsp;
			<a href="nanomaterialEntity.do?dispatch=setupNew&sampleId=${sampleId}" class="addlink"><img align="middle"
					src="images/btn_add.gif" border="0" /> </a> &nbsp;&nbsp;&nbsp;
			<c:if test="${!empty compositionForm.map.comp.nanomaterialEntities}">
				<a href="/nanopmaterialEntity.do?dispatch=delete&sampleId=${sampleId}" class="addlink"><img align="middle"
						src="images/btn_delete.gif" border="0" /> </a>
			</c:if>
		</th>
	</tr>
	<tr>
		<td align="left">
			<jsp:include page="/bodyMessage.jsp?bundle=particle" />
		</td>
	</tr>
	<c:choose>
		<c:when test="${!empty compositionForm.map.comp.nanomaterialEntities}">
			<logic:iterate name="compositionForm"
				property="comp.nanomaterialEntities" id="entity" indexId="ind">
				<c:if test="${!empty entity.className}">
					<tr>
						<td>
							<div class="indented4">
								<table class="summaryViewLayer3" width="95%" align="center">
									<tr>
										<th valign="top" align="left">
											${entity.className}
										</th>
										<th valign="top" align="right">
											<a
												href="nanomaterialEntity.do?dispatch=setupUpdate&sampleId=${sampleId}&dataId=${entity.domainEntity.id}">Edit</a>
										</th>
									</tr>
									<tr>
										<td class="cellLabel">
											Description
										</td>
										<td>
											<c:choose>
												<c:when
													test="${!empty fn:trim(entity.emulsion.description)}">
													<c:out
														value="${fn:replace(entity.emulsion.description, cr, '<br>')}"
														escapeXml="false" />
												</c:when>
												<c:otherwise>N/A
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<%--
									<tr>
										<td valign="top" colspan="2">
											PROPERTIES
											<div class="indented4">

												<jsp:include
													page="/sample/composition/nanomaterialEntity/body${entity.className}Info.jsp">
													<jsp:param name="entityIndex" value="${ind}" />
												</jsp:include>

											</div>
										</td>
									</tr>
--%>
									<tr>
										<td class="cellLabel">
											Composing Elements
										</td>
										<td>
											<jsp:include page="bodyComposingElementView.jsp">
												<jsp:param name="entityIndex" value="${ind}" />
											</jsp:include>
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
				</c:if>
			</logic:iterate>
		</c:when>
		<c:otherwise>
			<tr>
				<td>
					<div class="indented4">
						N/A
					</div>
				</td>
			</tr>
		</c:otherwise>
	</c:choose>
</table>
<div id="summarySeparator1">
	<br>
</div>



