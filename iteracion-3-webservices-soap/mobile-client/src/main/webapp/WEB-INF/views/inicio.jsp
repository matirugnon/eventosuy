
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>eventos.uy</title>
<link rel="icon" type="image/png"
	href="${pageContext.request.contextPath}/img/favicon.png">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/styles.css">


<link rel="preconnect" href="https://fonts.googleapis.com" />
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
<link
	href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap"
	rel="stylesheet" />

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap"
	rel="stylesheet">

</head>
<body>

	<!-- Header : ahora el header esta en la carpeta componentes, para que se cambie en una sola pag y sea igual para todas-->
	<jsp:include page="/WEB-INF/views/componentes/header.jsp" />

	<div class="content">
		<jsp:include page="/WEB-INF/views/componentes/aside.jsp" />

		<!-- Main: eventos -->
		<main>
			
			<!-- Barra de bÃºsqueda -->
			<form action="${pageContext.request.contextPath}/inicio" method="get"
				style="margin-bottom: 1rem; display: flex; gap: 0.5rem; flex-wrap: wrap;">
				<input type="hidden" name="categoria"
					value="${categoriaSeleccionada}" /> <input type="text"
					name="busqueda" placeholder="Buscar evento..." value="${busqueda}"
					style="padding: 0.5rem 1rem; flex: 1; min-width: 200px; border-radius: 6px; border: 1px solid #ccc;">
				<button type="submit"
					style="padding: 0.5rem 1rem; border-radius: 6px; border: none; background-color: #182080; color: white; cursor: pointer;">Buscar</button>
			</form>
			<h2>Próximos Eventos</h2>
			<c:choose>
				<c:when test="${empty eventosOrdenados}">
					<div class="event">
						<div class="event-content">
							<p class="muted">No hay eventos disponibles.</p>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<c:forEach var="e" items="${eventosOrdenados}">
						<div class="event">
							<c:url var="eventoUrl" value="/consultaEvento">
								<c:param name="evento" value="${e.nombre}" />
							</c:url>
							<a href="${eventoUrl}"
								style="text-decoration: none; color: inherit; display: flex; gap: 1rem; width: 100%;">
								<div class="event-image"
									style="flex-shrink: 0; width: 120px; height: 120px; border-radius: 8px; overflow: hidden;">
									<img
										src="${not empty e.imagen ? pageContext.request.contextPath.concat(e.imagen) : pageContext.request.contextPath.concat('/img/eventoSinImagen.png')}"
										alt="${e.nombre}"
										style="width: 100%; height: 100%; object-fit: cover;" />
								</div>
								<div class="event-content"
									style="flex: 1; display: flex; flex-direction: column; justify-content: center;">
									<h3 style="margin: 0 0 0.5rem 0;">${e.nombre}</h3>
									<p style="margin: 0;">${empty e.descripcion ? 'Sin descripción' : e.descripcion}</p>
								</div>
							</a>
						</div>
					</c:forEach>
				</c:otherwise>
			</c:choose>
			<div style="margin-top: 1rem; text-align: center;">
				<c:if test="${totalPages > 1}">
					<!-- Construimos la URL base incluyendo posibles parÃ¡metros de bÃºsqueda y categorÃ­a -->
					<c:set var="baseUrl"
						value="${pageContext.request.contextPath}/inicio" />

					<c:set var="prevPage" value="${currentPage - 1}" />
					<c:set var="nextPage" value="${currentPage + 1}" />

					<c:url var="prevUrl" value="/inicio">
						<c:param name="page" value="${prevPage}" />
						<c:if test="${not empty param.busqueda}">
							<c:param name="busqueda" value="${param.busqueda}" />
						</c:if>
						<c:if
							test="${not empty categoriaSeleccionada && categoriaSeleccionada != 'todas'}">
							<c:param name="categoria" value="${categoriaSeleccionada}" />
						</c:if>
					</c:url>

					<c:url var="nextUrl" value="/inicio">
						<c:param name="page" value="${nextPage}" />
						<c:if test="${not empty param.busqueda}">
							<c:param name="busqueda" value="${param.busqueda}" />
						</c:if>
						<c:if
							test="${not empty categoriaSeleccionada && categoriaSeleccionada != 'todas'}">
							<c:param name="categoria" value="${categoriaSeleccionada}" />
						</c:if>
					</c:url>

					<div style="margin-top: 1rem; text-align: center;">
						<a class="btn-primary" href="${currentPage > 1 ? prevUrl : '#'}"
							style="${currentPage == 1 ? 'pointer-events:none; opacity:0.5;' : ''}">Anterior</a>
						Página ${currentPage} de ${totalPages} <a class="btn-primary"
							href="${currentPage < totalPages ? nextUrl : '#'}"
							style="${currentPage == totalPages ? 'pointer-events:none; opacity:0.5;' : ''}">Siguiente</a>
					</div>
				</c:if>

			</div>
		</main>
	</div>

</body>
</html>
