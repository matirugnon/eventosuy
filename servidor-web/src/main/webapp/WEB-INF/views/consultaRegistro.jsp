<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Consulta de Registro · eventos.uy</title>
<link rel="icon" type="image/png"
	href="${pageContext.request.contextPath}/img/favicon.png">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/styles.css">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap"
	rel="stylesheet">
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
</style>
</head>
<body>
	<div>
		<!-- Header : ahora el header esta en la carpeta componentes, para que se cambie en una sola pag y sea igual para todas-->
		<jsp:include page="/WEB-INF/views/componentes/header.jsp" />

		<div class="content">
			<jsp:include page="/WEB-INF/views/componentes/aside.jsp" />

			<main>
				<section class="panel">
					<div class="panel-header">Consulta de Registro</div>
					<div class="panel-body">
						<!-- Datos básicos del registro -->
						<p>
							<strong>Asistente:</strong> ${registro.asistente}
						</p>
						<p>
							<strong>Evento:</strong> ${edicionInfo.evento}
						</p>
						<p>
							<strong>Edición:</strong> ${registro.nomEdicion}
						</p>
						<p>
							<strong>Tipo de Registro:</strong> ${registro.tipoDeRegistro}
						</p>
						<p>
							<strong>Fecha de Registro:</strong> ${registro.getFechaRegistro().getDia()}/${registro.getFechaRegistro().getMes()}/${registro.getFechaRegistro().getAnio()}
						</p>
						<p>
							<strong>Costo:</strong> $ ${registro.costo}
						</p>

						<!-- Enlaces relacionados -->
						<div style="margin-top: 1.5rem; display: flex; gap: .75rem;">
							<c:choose>
								<c:when test="${param.from == 'misRegistros'}">
									<a class="btn-outline"
										href="${pageContext.request.contextPath}/misRegistros">Volver
										a Mis Registros</a>
								</c:when>
								<c:otherwise>
									<a class="btn-outline"
										href="${pageContext.request.contextPath}/perfilUsuario?nickname=${registro.asistente}">Volver
										al perfil de usuario</a>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</section>
			</main>
		</div>
	</div>
</body>
</html>