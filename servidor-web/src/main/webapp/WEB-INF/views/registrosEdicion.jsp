<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Registros de Edición · eventos.uy</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/styles.css">
<style>
.table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 1rem;
}

.table th, .table td {
	padding: .6rem;
	border-bottom: 1px solid #e5e5e5;
	text-align: left;
}

.actions {
	display: flex;
	gap: .5rem;
}

.table-container {
	width: 100%;
	overflow-x: auto;
	-webkit-overflow-scrolling: touch;
}
</style>
</head>
<body>
	<!-- Header : ahora el header esta en la carpeta componentes, para que se cambie en una sola pag y sea igual para todas-->
	<jsp:include page="/WEB-INF/views/componentes/header.jsp" />

	<div class="content">
		<jsp:include page="/WEB-INF/views/componentes/aside.jsp" />

		<main>
			<section class="panel">
				<div class="panel-header">
					Registros - Edición:<br>${edicion.nombre}</div>
				<div class="panel-body">
					<c:choose>
						<c:when test="${empty registros}">
							<p>No hay registros para esta edición.</p>
						</c:when>
						<c:otherwise>
							<div class="table-container">
								<table class="table">
									<thead>
										<tr>
											<th>Asistente</th>
											<th>Tipo de Registro</th>
											<th>Acciones</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="reg" items="${registros}">
											<tr>
												<td>${reg.asistente}</td>
												<td>${reg.tipoDeRegistro}</td>
												<td>
													<div class="actions">
														<!-- Solo mostrar el enlace si es el organizador de esta edición -->
														<c:if
															test="${role == 'organizador' && nickname == edicion.organizador}">
															<a class="btn-edicion"
																href="${pageContext.request.contextPath}/consultaRegistro?asistente=${fn:escapeXml(reg.asistente)}&edicion=${fn:escapeXml(edicion.nombre)}&tipoRegistro=${fn:escapeXml(reg.tipoDeRegistro)}">Ver
																registro</a>
														</c:if>
														<a class="btn-outline"
															href="${pageContext.request.contextPath}/perfilUsuario?nickname=${fn:escapeXml(reg.asistente)}&from=edicion&edicion=${fn:escapeXml(edicion.nombre)}">Ver
															perfil</a>
													</div>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</c:otherwise>
					</c:choose>

					<div style="margin-top: 1rem;">
						<a class="btn-outline"
							href="${pageContext.request.contextPath}/edicionesOrganizadas">
							Ir a mis ediciones</a>
						<c:if test="${not empty urlConsulta}">
							<a class="btn-outline"
								href="${urlConsulta}">
								Consultar edición</a>
						</c:if>
					</div>
				</div>
			</section>
		</main>
	</div>
</body>
</html>
