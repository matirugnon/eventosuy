<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Consulta Tipo de Registro · eventos.uy</title>
<link rel="icon" type="image/png"
	href="${pageContext.request.contextPath}/img/favicon.png">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/styles.css">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap"
	rel="stylesheet">
</head>
<body>
	<div>
		<!-- Header : ahora el header esta en la carpeta componentes, para que se cambie en una sola pag y sea igual para todas-->
		<jsp:include page="/WEB-INF/views/componentes/header.jsp" />

		<div class="content">
			<jsp:include page="/WEB-INF/views/componentes/aside.jsp" />
			<main>
				<!-- Título Tipo de Registro -->
				<section class="panel">
					<div class="panel-header">
						Tipo de Registro:
						<div
							style="margin-top: 0.25rem; font-weight: 600; font-size: 1.1rem;">
							${tipoRegistro.nombre}</div>
					</div>

					<div class="panel-body"
						style="display: flex; flex-direction: column; gap: 0.5rem;">
						<p>
							<strong>Nombre:</strong> ${tipoRegistro.nombre}
						</p>
						<p>
							<strong>Descripción:</strong> ${tipoRegistro.descripcion}
						</p>
						<p>
							<strong>Costo:</strong> $${tipoRegistro.costo}
						</p>
						<p>
							<strong>Cupo:</strong> ${tipoRegistro.cupo} lugares
						</p>
					</div>
				</section>

				<!-- Botón Volver -->
				<section class="panel" style="margin-top: 2rem;">
					<div class="panel-header">Volver a edición</div>
					<div class="panel-body">
						<a
							href="${pageContext.request.contextPath}/consultaEdicion?edicion=${edicionInfo.nombre}"
							class="btn-primary"> ← Volver a la edición
							${edicionInfo.evento} - ${edicionInfo.nombre} </a>
					</div>
				</section>
			</main>

		</div>
	</div>
</body>
</html>