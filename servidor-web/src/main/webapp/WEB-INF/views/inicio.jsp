
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<style>
	select {
		padding: 0 10px !important;
	}
</style>

<link rel="preconnect" href="https://fonts.googleapis.com" />
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
<link
	href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap"
	rel="stylesheet" />
</head>

<body>
	<jsp:include page="/WEB-INF/views/componentes/header.jsp" />

	<div class="content">
		<jsp:include page="/WEB-INF/views/componentes/aside.jsp" />

		<main>
			<!-- Botón de carga de datos -->
			<form action="${pageContext.request.contextPath}/inicio"
				method="post" style="margin-bottom: 1rem;">
				<input type="hidden" name="accion" value="cargarDatos">
				<button type="submit"
					style="padding: 0.4rem 0.8rem; border-radius: 6px; border: none; background: #2b6cb0; color: white; cursor: pointer;">
					Cargar datos (Servidor Central)</button>
			</form>

			<!-- Filtros -->
			<form action="${pageContext.request.contextPath}/inicio" method="get"
				style="margin-bottom: 1rem; display: flex; gap: 0.5rem; flex-wrap: wrap;">
				<input type="text" name="busqueda"
					placeholder="Buscar evento o edición..." value="${busqueda}"
					style="padding: 0.5rem 1rem; flex: 1; min-width: 200px; border-radius: 6px; border: 1px solid #ccc;">

				<select name="tipo"
					style="padding: 0.5rem; border-radius: 6px; border: 1px solid #ccc;">
					<option value="todos" ${tipo == 'todos' ? 'selected' : ''}>Todos</option>
					<option value="eventos" ${tipo == 'eventos' ? 'selected' : ''}>Solo
						eventos</option>
					<option value="ediciones" ${tipo == 'ediciones' ? 'selected' : ''}>Solo
						ediciones</option>
				</select>
				
				<select name="orden"
					style="padding: 0.5rem; border-radius: 6px; border: 1px solid #ccc;">
					<option value="nombreAsc" ${orden == 'nombreAsc' ? 'selected' : ''}>Nombre
						A–Z</option>
					<option value="nombreDesc"
						${orden == 'nombreDesc' ? 'selected' : ''}>Nombre Z–A</option>
					<option value="fechaAsc" ${orden == 'fechaAsc' ? 'selected' : ''}>Fecha
						más próxima</option>
					<option value="fechaDesc" ${orden == 'fechaDesc' ? 'selected' : ''}>Fecha
						más lejana</option>
				</select>

				<button type="submit"
					style="padding: 0.5rem 1rem; border-radius: 6px; border: none; background-color: #182080; color: white; cursor: pointer;">
					Filtrar</button>
			</form>

			<h2>Eventos y Ediciones Activas</h2>

			<!-- Resultados -->
			<c:choose>
				<c:when test="${empty resultados}">
					<div class="event">
						<p class="muted">No hay resultados disponibles.</p>
					</div>
				</c:when>
				<c:otherwise>
					<c:forEach var="item" items="${resultados}">
						<c:choose>
							
							<c:when test="${item.tipo == 'edicion'}">
								<c:url var="urlDetalle" value="/consultaEdicion">
									<c:param name="edicion" value="${item.nombre}" />
								</c:url>
								<div class="event">
									<a href="${urlDetalle}"
										style="text-decoration: none; color: inherit; display: flex; gap: 1rem; width: 100%;">
										<div class="event-image"
											style="flex-shrink: 0; width: 120px; height: 120px; border-radius: 8px; overflow: hidden;">
											<img
												src="${not empty item.imagen ? pageContext.request.contextPath.concat(item.imagen) : pageContext.request.contextPath.concat('/img/eventoSinImagen.png')}"
												alt="${item.nombre}"
												style="width: 100%; height: 100%; object-fit: cover;" />
										</div>
										<div class="event-content"
											style="flex: 1; display: flex; flex-direction: column; justify-content: center;">
											<h3 style="margin: 0 0 0.5rem 0;">${item.nombre}</h3>
											<p style="margin: 0;">
												<strong>Evento:</strong> ${item.evento}
											</p>
											<p style="margin: 0;">
												<strong>Ciudad:</strong> ${item.ciudad}, ${item.pais}
											</p>
										</div>
									</a>
								</div>
							</c:when>

							
							<c:otherwise>
								<c:url var="urlDetalle" value="/consultaEvento">
									<c:param name="evento" value="${item.nombre}" />
								</c:url>
								<div class="event">
									<a href="${urlDetalle}"
										style="text-decoration: none; color: inherit; display: flex; gap: 1rem; width: 100%;">
										<div class="event-image"
											style="flex-shrink: 0; width: 120px; height: 120px; border-radius: 8px; overflow: hidden;">
											<img
												src="${not empty item.imagen ? pageContext.request.contextPath.concat(item.imagen) : pageContext.request.contextPath.concat('/img/eventoSinImagen.png')}"
												alt="${item.nombre}"
												style="width: 100%; height: 100%; object-fit: cover;" />
										</div>
										<div class="event-content"
											style="flex: 1; display: flex; flex-direction: column; justify-content: center;">
											<h3 style="margin: 0 0 0.5rem 0;">${item.nombre}</h3>
											<p style="margin: 0;">${empty item.descripcion ? 'Sin descripción' : item.descripcion}</p>
											<c:if test="${not empty item.categorias}">
												<p style="margin: 0.25rem 0 0 0; font-size: 0.9rem; color: #666;">
													<strong>Categorías:</strong> ${item.categorias}
												</p>
											</c:if>
										</div>
									</a>
								</div>
							</c:otherwise>
						</c:choose>
					</c:forEach>

				</c:otherwise>
			</c:choose>


			<div style="margin-top: 1rem; text-align: center;">
				<c:if test="${totalPages > 1}">
					<c:set var="prevPage" value="${currentPage - 1}" />
					<c:set var="nextPage" value="${currentPage + 1}" />

					<c:url var="prevUrl" value="/inicio">
						<c:param name="page" value="${prevPage}" />
						<c:param name="busqueda" value="${busqueda}" />
						<c:param name="tipo" value="${tipo}" />
						<c:param name="categoria" value="${categoriaSeleccionada}" />
						<c:param name="orden" value="${orden}" />
					</c:url>

					<c:url var="nextUrl" value="/inicio">
						<c:param name="page" value="${nextPage}" />
						<c:param name="busqueda" value="${busqueda}" />
						<c:param name="tipo" value="${tipo}" />
						<c:param name="categoria" value="${categoriaSeleccionada}" />
						<c:param name="orden" value="${orden}" />
					</c:url>

					<a class="btn-primary" href="${currentPage > 1 ? prevUrl : '#'}"
						style="${currentPage == 1 ? 'pointer-events:none; opacity:0.5;' : ''}">Anterior</a>
                    Página ${currentPage} de ${totalPages}
                    <a class="btn-primary"
						href="${currentPage < totalPages ? nextUrl : '#'}"
						style="${currentPage == totalPages ? 'pointer-events:none; opacity:0.5;' : ''}">Siguiente</a>
				</c:if>
			</div>
		</main>
	</div>
</body>
</html> 

