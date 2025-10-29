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
									<p style="margin: 0.25rem 0;">
										<strong>Estado:</strong>
										${edicion.estado}
									</p>
								</div>
								
								<!-- Botón de registro solo para asistentes y ediciones aceptadas -->
								<c:if test="${role == 'asistente' && edicion.estado == 'ACEPTADA'}">
									<div style="margin-top: 1.5rem;">
										<a href="${pageContext.request.contextPath}/registroAEdicion?edicion=${edicion.nombre}" 
										   class="btn-primary" 
										   style="display: inline-block; padding: 0.75rem 1.5rem; background-color: #182080; color: white; text-decoration: none; border-radius: 8px; font-weight: 600; transition: background-color 0.2s;"
										   onmouseover="this.style.backgroundColor='#0d4f8c';"
										   onmouseout="this.style.backgroundColor='#182080';">
											Registrarse a esta edición
										</a>
									</div>
								</c:if>

								<c:if test="${not empty tiposDeRegistro}">
									<div class="panel" style="margin-top: 1rem; max-width: 200px;">
										<div class="panel-header">Tipos de registro</div>
										<div class="panel-body">
											<ul class="menu-list" style="margin-left: 0;">
												<c:forEach items="${tiposDeRegistro}" var="tipoRegistro">
													<c:url var="tipoRegistroUrl" value="/consultaTipoRegistro">
														<c:param name="tipo" value="${tipoRegistro}" />
														<c:param name="edicion" value="${edicion.nombre}" />
													</c:url>
													<li><a href="${tipoRegistroUrl}"
														style="display: block; padding: 0.5rem 0;">${tipoRegistro}</a></li>
												</c:forEach>
											</ul>
										</div>
									</div>
								</c:if>
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

								<div>
									<h3 style="color: #182080; margin-bottom: 1rem;">Organizador</h3>
									<div
										style="border-radius: 8px; display: flex; justify-content: center; padding: 0.75rem 0;">
										<a
											href="${pageContext.request.contextPath}/perfilUsuario?nickname=${edicion.organizador}&from=edicion&edicion=${edicion.nombre}"
											style="text-decoration: none;">
											<div style="position: relative; width: 168px; height: 112px;">
												<img
													src="${pageContext.request.contextPath}${avatarOrganizador}"
													alt="Avatar de ${edicion.organizador}"
													style="width: 168px; height: 112px; border-radius: 12px; object-fit: cover; border: 3px solid rgba(24, 32, 128, 0.15); cursor: pointer; transition: transform 0.2s, box-shadow 0.2s;"
													onmouseover="this.style.transform='scale(1.05)'; this.style.boxShadow='0 4px 8px rgba(0,0,0,0.2)';"
													onmouseout="this.style.transform='scale(1)'; this.style.boxShadow='none';"
													onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';" />
												<div
													style="display: none; width: 168px; height: 112px; border-radius: 12px; border: 3px solid rgba(24, 32, 128, 0.15); background: linear-gradient(135deg, #182080, #4a90e2); color: white; justify-content: center; align-items: center; font-size: 2rem; font-weight: bold; cursor: pointer; transition: transform 0.2s, box-shadow 0.2s;"
													onmouseover="this.style.transform='scale(1.05)'; this.style.boxShadow='0 4px 8px rgba(0,0,0,0.2)';"
													onmouseout="this.style.transform='scale(1)'; this.style.boxShadow='none';">
													${edicion.organizador.substring(0, 1).toUpperCase()}</div>
											</div>
										</a>
									</div>
									<div style="padding: 0.5rem; text-align: center;">
										<a
											href="${pageContext.request.contextPath}/perfilUsuario?nickname=${edicion.organizador}&from=edicion&edicion=${edicion.nombre}"
											style="text-decoration: none;"> <span
											style="font-weight: 600; color: #182080; cursor: pointer; transition: color 0.2s;"
											onmouseover="this.style.color='#0d4f8c';"
											onmouseout="this.style.color='#182080';">${edicion.organizador}</span>
										</a>
									</div>
								</div>

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
					</div>
				</section>
			</main>
		</div>
	</div>
</body>
</html>