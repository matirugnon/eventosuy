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
						<!-- Mensaje de error si el evento no ha comenzado -->
						<c:if test="${param.error == 'eventoNoIniciado'}">
							<div style="background-color: #fee; border: 1px solid #c33; border-radius: 4px; padding: 1rem; margin-bottom: 1.5rem; color: #c33;">
								<strong>⚠ Error:</strong> No puedes confirmar asistencia a un evento que aún no ha comenzado. 
								Debes esperar hasta la fecha de inicio del evento.
							</div>
						</c:if>
						
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
							<strong>Fecha de Registro:</strong>
							${registro.getFechaRegistro().getDia()}/${registro.getFechaRegistro().getMes()}/${registro.getFechaRegistro().getAnio()}
						</p>
						<p>
							<strong>Costo:</strong> $ ${registro.costo}
						</p>
						<p>
							<strong>Asistencia confirmada:</strong> 
							<c:choose>
								<c:when test="${registro.asistio}">
									<span style="color: green;">✓ Sí</span>
								</c:when>
								<c:otherwise>
									<span style="color: #666;">✗ No</span>
								</c:otherwise>
							</c:choose>
						</p>

						<!-- Botón para descargar constancia (solo si confirmó asistencia) -->
						<c:if test="${role == 'asistente' && nickname == registro.asistente && registro.asistio}">
							<div style="margin-top: 1.5rem;">
								<a href="${pageContext.request.contextPath}/descargaConstancia?asistente=${registro.asistente}&edicion=${registro.nomEdicion}&tipoRegistro=${registro.tipoDeRegistro}" 
								   class="btn-primary" 
								   style="display: inline-block; text-decoration: none;">
									📄 Descargar Constancia de Asistencia (PDF)
								</a>
							</div>
						</c:if>

						<!-- Botón para confirmar asistencia (solo para asistentes que no han confirmado) -->
						<c:if test="${role == 'asistente' && nickname == registro.asistente && !registro.asistio}">
							<form method="POST" action="${pageContext.request.contextPath}/registrarAsistencia" 
								  style="margin-top: 1rem;">
								<input type="hidden" name="asistente" value="${registro.asistente}" />
								<input type="hidden" name="edicion" value="${registro.nomEdicion}" />
								<input type="hidden" name="tipoRegistro" value="${registro.tipoDeRegistro}" />
								<c:if test="${not empty param.from}">
									<input type="hidden" name="from" value="${param.from}" />
								</c:if>
								<button type="submit" class="btn-primary" 
										onclick="return confirm('¿Confirmar tu asistencia a esta edición?');">
									Confirmar Asistencia
								</button>
							</form>
						</c:if>

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