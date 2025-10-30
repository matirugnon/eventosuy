<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Evento | eventos.uy</title>
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
		<jsp:include page="/WEB-INF/views/componentes/header.jsp" />

		<div class="content">
			<jsp:include page="/WEB-INF/views/componentes/aside.jsp" />

			<main>
				<section class="panel">
					<div class="panel-body">
						<c:if test="${not empty eventoMensaje}">
							<c:set var="eventoBgColor" value="#fdecea" />
							<c:set var="eventoTextColor" value="#b32025" />
							<c:choose>
								<c:when test="${eventoMensajeTipo eq 'success'}">
									<c:set var="eventoBgColor" value="#eaf7ea" />
									<c:set var="eventoTextColor" value="#2d7a32" />
								</c:when>
								<c:otherwise>
									<c:set var="eventoBgColor" value="#fdecea" />
									<c:set var="eventoTextColor" value="#b32025" />
								</c:otherwise>
							</c:choose>
							<div
								style="margin-bottom: 1rem; padding: 0.75rem 1rem; border-radius: 8px; background-color: <c:out value='${eventoBgColor}'/>; color: <c:out value='${eventoTextColor}'/>;">
								${eventoMensaje}
							</div>
						</c:if>

						<c:if test="${not empty eventoDetalle}">
						<div class="event-detail">
							<article class="event-item" style="border-bottom: 0;">
								<div class="event-image"
									style="margin-bottom: 1rem; max-width: 250px;">
									<img
										src="${not empty eventoDetalle.imagen ? pageContext.request.contextPath.concat(eventoDetalle.imagen) : pageContext.request.contextPath.concat('/img/eventoSinImagen.png')}"
										alt="${eventoDetalle.nombre}"
										style="width: 100%; height: auto; border-radius: 8px;">
								</div>
								<div>
									<h3 style="margin: 0 0 0.25rem 0;">${eventoDetalle.nombre}</h3>
									<c:if test="${eventoFinalizado}">
										<span
											style="display: inline-block; padding: 0.2rem 0.6rem; border-radius: 999px; background-color: #b32025; color: white; font-size: 0.85rem; margin-bottom: 0.5rem;">
											Evento finalizado
										</span>
									</c:if>

									<p style="margin: 0.25rem 0;">
										<strong>Descripción:</strong> ${eventoDetalle.descripcion}
									</p>
									<p style="margin: 0.25rem 0;">
										<strong>Sigla:</strong> ${eventoDetalle.sigla}
									</p>
									<p style="margin: 0.25rem 0;">
										<strong>Categorías:</strong>
										<c:forEach items="${eventoDetalle.categorias}" var="categoria"
											varStatus="status">
											${fn:trim(categoria)}<c:if test="${!status.last}">, </c:if>
										</c:forEach>
									</p>
									<p style="margin: 0.25rem 0;">
										<strong>Fecha alta:</strong>
										${eventoDetalle.dia}/${eventoDetalle.mes}/${eventoDetalle.anio}
									</p>

									<c:if test="${puedeFinalizar}">
										<form method="post"
											action="${pageContext.request.contextPath}/consultaEvento"
											style="margin-top: 1rem;">
											<input type="hidden" name="evento"
												value="${eventoSeleccionado}" />
											<button type="submit"
												style="padding: 0.5rem 1rem; border-radius: 6px; background-color: #b32025; color: #fff; border: none; cursor: pointer;">
												Finalizar evento
											</button>
										</form>
									</c:if>

									<c:if test="${eventoFinalizado}">
										<p style="margin-top: 1rem; color: #555;">
											No se publican ediciones porque este evento ya fue finalizado.
										</p>
									</c:if>
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
											<a class="mini-card" href="${edicionUrl}">
												<img
													src="${not empty edicion.imagen ? pageContext.request.contextPath.concat(edicion.imagen) : pageContext.request.contextPath.concat('/img/eventoSinImagen.png')}"
													alt="${edicion.nombre}" />
												<div class="mini-card-title">${edicion.nombre}</div>
											</a>
										</c:forEach>
									</div>
								</div>
							</c:if>
						</div>
						</c:if>
						<c:if test="${empty eventoDetalle}">
							<div class="event-detail">
								<p style="margin: 1rem 0;">No se encontraron datos para el evento solicitado.</p>
							</div>
						</c:if>
					</div>
				</section>
			</main>
		</div>
	</div>

	<footer></footer>
</body>
</html>
