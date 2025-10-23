<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Ediciones organizadas · eventos.uy</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/styles.css">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap"
	rel="stylesheet">
<style>
.ediciones-list {
	display: flex;
	flex-direction: column;
	gap: 1rem;
	margin-top: 1rem;
}

.edicion-item {
	display: flex;
	align-items: center;
	gap: 1rem;
	padding: 1rem;
	border: 1px solid #ddd;
	border-radius: 8px;
	background-color: #f9f9f9;
}

.edicion-image {
	width: 80px;
	height: 80px;
	object-fit: cover;
	border-radius: 8px;
}

.edicion-details {
	flex: 1;
}

.edicion-title {
	margin: 0 0 0.5rem 0;
	font-size: 1.1rem;
	font-weight: 600;
	color: #333;
}

.edicion-info {
	margin: 0;
	font-size: 0.9rem;
	color: #666;
	line-height: 1.4;
}

.edicion-actions {
	display: flex;
	flex-direction: column;
	gap: 0.5rem;
}

.btn-edicion {
	padding: 0.5rem 1rem;
	background-color: #007bff;
	color: white;
	text-decoration: none;
	border-radius: 4px;
	font-size: 0.9rem;
	text-align: center;
	transition: background-color 0.2s;
	width: 100%;
}

.btn-edicion:hover {
	background-color: #0056b3;
}

.no-content {
	text-align: center;
	padding: 2rem;
	color: #666;
	background-color: #f8f9fa;
	border-radius: 8px;
	margin-top: 1rem;
}

.error-message {
	color: #c00;
	margin-top: 1rem;
	padding: 0.75rem;
	background-color: #fff2f2;
	border: 1px solid #ffcdd2;
	border-radius: 4px;
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
					<h2 style="margin: 0 0 1.5rem 0; color: #182080;">Ediciones
						Organizadas</h2>
				</div>
				<div class="panel-body">
					<c:if test="${not empty error}">
						<div class="error-message">⚠️ ${error}</div>
					</c:if>

					<c:choose>
						<c:when test="${empty edicionesOrganizadas}">
							<div class="no-content">
								<p>No tienes ediciones organizadas aún.</p>
							</div>
						</c:when>
						<c:otherwise>
							<div class="ediciones-list">
								<c:forEach var="edicion" items="${edicionesOrganizadas}">
									<div class="edicion-item">
										<img
											src="${not empty edicion.imagen ? pageContext.request.contextPath.concat(edicion.imagen) : pageContext.request.contextPath.concat('/img/eventoSinImagen.jpeg')}"
											alt="${edicion.nombre}" class="edicion-image" />
										<div class="edicion-details">
											<h3 class="edicion-title">
												${edicion.evento} - ${edicion.nombre}
												<c:if test="${edicionesPasadas[edicion.nombre]}">
													<span
														style="color: #888; font-size: 0.9rem; font-weight: normal;">
														(Finalizada)</span>
												</c:if>
											</h3>
											<p class="edicion-info">
												<strong>Sigla:</strong> ${edicion.sigla}<br> <strong>Ubicación:</strong>
												${edicion.ciudad}, ${edicion.pais}<br> <strong>Fecha:</strong>
												${edicion.fechaInicio.dia}/${edicion.fechaInicio.mes}/${edicion.fechaInicio.anio}
												-
												${edicion.fechaFin.dia}/${edicion.fechaFin.mes}/${edicion.fechaFin.anio}
											</p>
										</div>
										<div class="edicion-actions">
											<c:if test="${!edicionesPasadas[edicion.nombre]}">
												<a class="btn-edicion"
													href="altaTipoRegistro?edicion=${edicion.nombre}">Alta
													Tipo Registro</a>
												<a class="btn-edicion"
													href="altaPatrocinio?edicion=${edicion.nombre}">Alta
													Patrocinio</a>
											</c:if>
											<a class="btn-edicion"
												href="registrosEdicion?edicion=${edicion.nombre}">Consultar
												registros</a>
										</div>
									</div>
								</c:forEach>
							</div>
						</c:otherwise>
					</c:choose>
				</div>
			</section>
		</main>
	</div>

	<footer></footer>
</body>
</html>