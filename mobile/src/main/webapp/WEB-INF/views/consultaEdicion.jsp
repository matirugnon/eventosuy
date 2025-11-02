<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Consulta de Edición · eventos.uy</title>
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
				<section class="panel">
					<div class="panel-body">
						<!-- Video full-width (si existe) -->

						<div class="event-detail" style="display: flex; gap: 2rem;">
							<div style="flex: 1;">
								<div class="event-image"
									style="margin-bottom: 1rem; max-width: 250px;">
									<img
										src="${not empty edicion.imagen ? pageContext.request.contextPath.concat(edicion.imagen) : pageContext.request.contextPath.concat('/img/eventoSinImagen.png')}"
										alt="${edicion.nombre}"
										style="width: 100%; height: auto; border-radius: 8px;">
								</div>
							</div>

							<div style="flex: 2;">
								<div>
									<h2 style="margin: 0.5rem 0;">${edicion.nombre}</h2>
									<p style="margin: 0.25rem 0;">
										<strong>Ciudad:</strong> ${edicion.ciudad}
									</p>
									<p style="margin: 0.25rem 0;">
										<strong>País:</strong> ${edicion.pais}
									</p>
									<p style="margin: 0.25rem 0;">
										<strong>Sigla:</strong> ${edicion.sigla}
									</p>
									<p style="margin: 0.25rem 0;">
										<strong>Fecha Inicio:</strong>
										${edicion.fechaInicio.dia}/${edicion.fechaInicio.mes}/${edicion.fechaInicio.anio}
									</p>
									<p style="margin: 0.25rem 0;">
										<strong>Fecha Fin:</strong>
										${edicion.fechaFin.dia}/${edicion.fechaFin.mes}/${edicion.fechaFin.anio}
									</p>
								</div>
							</div>

							<div
								style="width: 250px; display: flex; flex-direction: column; gap: 2rem;">
								<c:if test="${not empty eventoPadre}">
									<div>
										<h3 style="color: #182080; margin-bottom: 1rem;">Evento</h3>
										<div style="border-radius: 8px;">
											<h4
												style="margin: 0; font-size: 1.1rem; color: #333; font-family: 'Inter', sans-serif;">
												<c:url var="eventoUrl" value="/consultaEvento">
													<c:param name="evento" value="${eventoPadre}" />
												</c:url>
												<a href="${eventoUrl}"
													style="color: inherit; text-decoration: none;">
													${eventoPadre} </a>
											</h4>
										</div>
									</div>
								</c:if>

								<c:if test="${not empty edicion.patrocinios}">
									<div>
										<h3 style="color: #182080; margin-bottom: 1rem;">Patrocinios</h3>
										<div class="panel"
											style="border: 1px solid #e0e0e0; border-radius: 8px;">
											<ul class="menu-list" style="margin: 0; padding: 0.5rem 0;">
												<c:forEach items="${edicion.patrocinios}" var="patrocinio">
													<li style="padding: 0.5rem 1rem;"><c:url
															var="patrocinioUrl" value="/consultaPatrocinio">
															<c:param name="codigo" value="${patrocinio}" />
															<c:param name="edicion" value="${edicion.nombre}" />
														</c:url> <a href="${patrocinioUrl}"
														style="display: flex; align-items: center; gap: 0.75rem; text-decoration: none; color: inherit;">
															<span style="font-weight: 500;">${patrocinio}</span>
													</a></li>
												</c:forEach>
											</ul>
										</div>
									</div>
								</c:if>
							</div>
						</div>
						<c:if test="${not empty edicion.video}">
							<div class="event-video-full"
								style="width: 100%; margin-bottom: 1rem;">
								<iframe id="videoIframe" data-video="${edicion.video}" src=""
									title="Video de la edición"
									style="width: 100%; aspect-ratio: 16/9; border-radius: 8px; border: 0;"
									allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
									allowfullscreen> </iframe>
							</div>
						</c:if>
					</div>
				</section>
			</main>
		</div>
	</div>
</body>
</html>

<script>
document.addEventListener('DOMContentLoaded', function() {
	var iframe = document.getElementById('videoIframe');
	if (!iframe) return;
	var video = iframe.dataset.video;
	if (!video) return;

	function toEmbed(url) {
		try {
			url = url.trim();
			// If already an embed URL, return as-is
			if (url.includes('/embed/')) return url;
			// Try to use URL constructor for robust parsing
			var parsed = new URL(url);
			var host = parsed.hostname.toLowerCase();
			// youtube.com/watch?v=...
			if (host.indexOf('youtube.com') !== -1) {
				var v = parsed.searchParams.get('v');
				if (v) return 'https://www.youtube.com/embed/' + v;
			}
			// youtu.be/ID
			if (host.indexOf('youtu.be') !== -1) {
				var parts = parsed.pathname.split('/');
				var id = parts.pop() || parts.pop();
				if (id) return 'https://www.youtube.com/embed/' + id;
			}
			// fallback: return original URL (may still be embeddable)
			return url;
		} catch (e) {
			// If URL parsing fails, attempt simple patterns
			if (url.indexOf('watch?v=') !== -1) {
				var p = url.split('watch?v=')[1].split('&')[0];
				return 'https://www.youtube.com/embed/' + p;
			}
			if (url.indexOf('youtu.be/') !== -1) {
				var p2 = url.split('youtu.be/')[1].split('?')[0];
				return 'https://www.youtube.com/embed/' + p2;
			}
			return url;
		}
	}

	var embed = toEmbed(video);
	if (embed) {
		iframe.src = embed;
	}
});
</script>