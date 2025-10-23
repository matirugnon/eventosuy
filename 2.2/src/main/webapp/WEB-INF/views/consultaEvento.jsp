<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Evento · eventos.uy</title>
<link rel="icon" type="image/png"
	href="${pageContext.request.contextPath}/img/favicon.png">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/styles.css" />
<link rel="preconnect" href="https://fonts.googleapis.com" />
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
<link
	href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap"
	rel="stylesheet" />
</head>
<body>
	<div>
		<!-- Header : ahora el header esta en la carpeta componentes, para que se cambie en una sola pag y sea igual para todas-->
		<jsp:include page="/WEB-INF/views/componentes/header.jsp" />

		<div class="content">
			<jsp:include page="/WEB-INF/views/componentes/aside.jsp" />

			<main>
				<section class="panel">
					<div class="panel-body">
						<div class="event-detail">
							<article class="event-item" style="border-bottom: 0;">
								<div class="event-image"
									style="margin-bottom: 1rem; max-width: 250px;">
									<img
										src="${not empty eventoSeleccionado.evento.imagen ? pageContext.request.contextPath.concat(eventoSeleccionado.evento.imagen) : pageContext.request.contextPath.concat('/img/eventoSinImagen.png')}"
										alt="${eventoSeleccionado.evento.nombre}"
										style="width: 100%; height: auto; border-radius: 8px;">
								</div>
								<div>
									<h3 style="margin: 0 0 0.25rem 0;">${eventoSeleccionado.evento.nombre}</h3>
									<p style="margin: 0.25rem 0;">
										<strong>Descripción:</strong>
										${eventoSeleccionado.evento.descripcion}
									</p>
									<p style="margin: 0.25rem 0;">
										<strong>Sigla:</strong> ${eventoSeleccionado.evento.sigla}
									</p>
									<p style="margin: 0.25rem 0;">
										<strong>Categorías:</strong>
										<c:forEach items="${eventoSeleccionado.evento.categorias}"
											var="categoria" varStatus="status">
                      ${categoria}<c:if test="${!status.last}">, </c:if>
										</c:forEach>
									</p>
									<p style="margin: 0.25rem 0;">
										<strong>Fecha Alta:</strong>
										${eventoSeleccionado.evento.fechaEvento.dia}/${eventoSeleccionado.evento.fechaEvento.mes}/${eventoSeleccionado.evento.fechaEvento.anio}
									</p>
								</div>
							</article>

							<c:if test="${not empty edicionesAceptadas}">
								<div>
									<div class="panel-header" style="border-radius: 10px 10px 0 0;">Ediciones</div>
									<div class="panel-body event-editions">
										<c:forEach items="${edicionesAceptadas}" var="edicion">
											<c:url var="edicionUrl" value="/consultaEdicion">
												<c:param name="edicion" value="${edicion.nombre}" />
											</c:url>
											<a class="mini-card" href="${edicionUrl}"> <img
												src="${not empty edicion.imagen ? pageContext.request.contextPath.concat(edicion.imagen) : pageContext.request.contextPath.concat('/img/eventoSinImagen.png')}"
												alt="${edicion.nombre}" />
												<div class="mini-card-title">${edicion.nombre}</div>
											</a>
										</c:forEach>
									</div>
								</div>
							</c:if>
						</div>
					</div>
				</section>
			</main>
		</div>
	</div>

	<footer></footer>
</body>
</html>