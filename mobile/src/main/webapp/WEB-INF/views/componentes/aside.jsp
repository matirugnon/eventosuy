<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<aside class="sidebar">
	<c:choose>
		<c:when test="${role == 'organizador'}">
			<div class="panel sidebar">
				<div class="panel-header">Mi perfil</div>
				<ul class="menu-list">
					<li><a href="${pageContext.request.contextPath}/altaEvento">Alta
							Evento</a></li>
					<li><a href="${pageContext.request.contextPath}/altaEdicion">Alta
							Edición</a></li>
					<li><a href="altaInstitucion">Alta Institución</a></li>
					<li><a href="edicionesOrganizadas">Ediciones Organizadas</a></li>
				</ul>
			</div>
		</c:when>
		<c:when test="${role == 'asistente'}">
			<div class="panel sidebar">
				<div class="panel-header">Mi perfil</div>
				<ul class="menu-list">
					<li><a href="${pageContext.request.contextPath}/misRegistros">Mis
							Registros</a></li>
				</ul>
			</div>
		</c:when>
	</c:choose>

	<!-- CategorÃ­as -->
	<div class="panel sidebar" style="margin-top: 1rem;">
		<div class="panel-header">Categorías</div>
		<ul class="menu-list">
			<c:choose>
				<c:when test="${empty categorias}">
					<li><span class="muted">No hay categorías disponibles.</span></li>
				</c:when>
				<c:otherwise>
					<li><c:url var="urlTodas" value="/inicio" /> <a
						href="${urlTodas}"
						class="${categoriaSeleccionada == 'todas' ? 'active' : ''}">Todas</a>
					</li>
					<c:forEach var="cat" items="${categorias}">
						<li><c:url var="catUrl" value="/inicio">
								<c:param name="categoria" value="${cat}" />
							</c:url> <a href="${catUrl}"
							class="${cat eq categoriaSeleccionada ? 'active' : ''}">${cat}</a>
						</li>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>

	
	
</aside>