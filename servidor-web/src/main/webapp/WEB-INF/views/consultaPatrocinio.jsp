<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Consulta de Patrocinio · eventos.uy</title>
<link rel="icon" type="image/png"
	href="${pageContext.request.contextPath}/img/favicon.png">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/styles.css">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap"
	rel="stylesheet">
</head>
<body>
	<div>
		<!-- Header : ahora el header esta en la carpeta componentes, para que se cambie en una sola pag y sea igual para todas-->
		<jsp:include page="/WEB-INF/views/componentes/header.jsp" />

		<div class="content">
			<jsp:include page="/WEB-INF/views/componentes/aside.jsp" />

			<main>
				<section class="panel">
					<div class="panel-header">
						Patrocinio:
						<div
							style="margin-top: 0.25rem; font-weight: 600; font-size: 1.1rem; white-space: normal;">${patrocinio.institucion}
							/ ${patrocinio.edicion}</div>
					</div>

					<div class="panel-body">
						<c:if test="${not empty institucion and not empty institucion.logo}">
							<div
								style="display: flex; align-items: center; gap: 1rem; margin-bottom: 1rem; padding-bottom: 1rem; border-bottom: 1px solid #eee;">
								<img
									src="${pageContext.request.contextPath}/${institucion.logo}"
									alt="Logo de ${institucion.nombre}"
									style="max-width: 80px; max-height: 60px; border-radius: 4px; border: 1px solid #ddd;">
								<div>
									<strong>${institucion.nombre}</strong><br> <span
										style="color: #666; font-size: 0.9rem;">${institucion.descripcion}</span>
								</div>
							</div>
						</c:if>

						<p>
							<strong>Edición:</strong> ${patrocinio.edicion}
						</p>
						<p>
							<strong>Institución:</strong> ${patrocinio.institucion}
						</p>
						<p>
							<strong>Nivel:</strong> ${patrocinio.nivel}
						</p>
						<p>
							<strong>Aporte económico:</strong> $${patrocinio.monto} UYU
						</p>
						<p>
							<strong>Tipo de registro:</strong> ${patrocinio.tipoDeRegistro}
						</p>
						<p>
							<strong>Cantidad de registros gratuitos:</strong>
							${patrocinio.cantidadGratis}
						</p>
					</div>
				</section>

				<section class="panel" style="margin-top: 2rem;">
					<div class="panel-header">Volver a edición</div>
					<div class="panel-body">
						<a
							href="${pageContext.request.contextPath}/consultaEdicion?edicion=${patrocinio.edicion}"
							class="btn-primary">← Volver a la edición
							${patrocinio.edicion}</a>
					</div>
				</section>
			</main>
		</div>
	</div>
</body>
</html>