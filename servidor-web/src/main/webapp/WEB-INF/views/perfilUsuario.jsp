<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Perfil de Usuario · eventos.uy</title>
<link rel="icon" type="image/png"
	href="${pageContext.request.contextPath}/img/favicon.png">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/styles.css" />
<link rel="preconnect" href="https://fonts.googleapis.com" />
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
<link
	href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap"
	rel="stylesheet" />
<style>
.auth-card {
	width: 760px;
	max-width: 95%;
	margin: 0 auto;
}

.table {
	width: 100%;
	border-collapse: collapse;
}

.table th, .table td {
	padding: .6rem;
	border-bottom: 1px solid #ddd;
	text-align: left;
}

.actions {
	display: flex;
	gap: .5rem;
	flex-wrap: wrap;
}

.btn-outline {
	display: inline-block;
	padding: .4rem .7rem;
	border: 1px solid #ccc;
	border-radius: 10px;
	text-decoration: none;
}

.user-details-list p {
	margin: 0.5rem 0;
}

/* Tabs y contenedor fijo */
.tabs {
	display: flex;
	border-bottom: 2px solid #e0e0e0;
	margin-bottom: 1rem;
}

.tab {
	padding: 0.75rem 1.5rem;
	background: #f8f9fa;
	border: 1px solid #ddd;
	border-bottom: none;
	cursor: pointer;
	border-radius: 8px 8px 0 0;
	margin-right: 4px;
	transition: all 0.2s;
}

.tab.active {
	background: white;
	border-bottom: 2px solid white;
	margin-bottom: -2px;
	font-weight: 600;
}

.tab:hover:not(.active) {
	background: #e9ecef;
}

.tab-content {
	min-height: 400px;
	background: white;
	padding: 1.5rem;
	border-radius: 0 8px 8px 8px;
	border: 1px solid #e0e0e0;
	margin-bottom: 1rem;
}

.tab-panel {
	display: none;
}

.tab-panel.active {
	display: block;
}

.profile-header {
	display: flex;
	gap: 1rem;
	align-items: center;
	margin-bottom: 1.5rem;
	padding-bottom: 1rem;
	border-bottom: 1px solid #eee;
}

.profile-avatar {
	width: 110px;
	height: 110px;
	border-radius: 50%;
	object-fit: cover;
}

.follow-info {
	display: flex;
	align-items: center;
	gap: 1rem;
	margin-left: auto;
}

.follow-counts {
	display: flex;
	gap: 1.5rem;
	text-align: center;
}

.follow-count-number {
	display: block;
	font-size: 1.25rem;
	font-weight: 600;
	color: #182080;
}

.follow-count-label {
	display: block;
	font-size: 0.75rem;
	text-transform: uppercase;
	color: #6c757d;
	letter-spacing: 0.04em;
}

.follow-form button {
	padding: 0.45rem 1.1rem;
	border-radius: 999px;
	border: none;
	cursor: pointer;
	font-weight: 600;
}

.follow-form .btn-outline {
	border: 1px solid #182080;
	background-color: transparent;
	color: #182080;
}

.follow-form .btn-primary {
	background-color: #182080;
	color: #fff;
}

.alert-error {
	background-color: #fdecea;
	color: #b71c1c;
	padding: 0.75rem 1rem;
	border-radius: 8px;
	margin-bottom: 1rem;
}
</style>
</head>
<body>
	<div>
		<!-- Header : ahora el header esta en la carpeta componentes, para que se cambie en una sola pag y sea igual para todas-->
		<jsp:include page="/WEB-INF/views/componentes/header.jsp" />

		<div class="content">
			<!-- Sidebar -->
			<jsp:include page="/WEB-INF/views/componentes/aside.jsp" />

			<!-- Main: Perfil de Usuario -->
			<main>
				<section class="auth-container">
					<div class="auth-card">
						<c:if test="${not empty perfilError}">
							<div class="alert-error">${perfilError}</div>
						</c:if>
						<!-- Encabezado: avatar + nombre del usuario -->
						<div class="profile-header">
							<img
								src="${not empty usuario.avatar ? pageContext.request.contextPath.concat(usuario.avatar) : pageContext.request.contextPath.concat('/img/usSinFoto.webp')}"
								alt="Avatar de ${usuario.nickname}" class="profile-avatar">
							<div>
								<h2 style="margin: 0; color: #333;">${usuario.nombre}
									<c:if
										test="${tipoUsuario == 'Asistente' && not empty asistente.apellido}">
                    ${asistente.apellido}
                  </c:if>
								</h2>
								<p style="margin: 0.25rem 0; color: #666; font-size: 1.1rem;">@${usuario.nickname}</p>
							</div>
							<div class="follow-info">
								<div class="follow-counts">
    								<div>
        								<a href="${pageContext.request.contextPath}/listarUsuarios?filtro=seguidores&nickname=${usuario.nickname}">
            								<span class="follow-count-number">${seguidoresCount}</span>
            								<span class="follow-count-label">Seguidores</span>
        								</a>
    								</div>
    								<div>
        								<a href="${pageContext.request.contextPath}/listarUsuarios?filtro=seguidos&nickname=${usuario.nickname}">
            								<span class="follow-count-number">${seguidosCount}</span>
            								<span class="follow-count-label">Siguiendo</span>
       									 </a>
    									</div>
									</div>
									
								<c:if test="${puedeSeguir}">
									<form method="post"
										action="${pageContext.request.contextPath}/perfilUsuario"
										class="follow-form">
										<input type="hidden" name="nickname"
											value="${perfilNickname}" />
										<c:choose>
											<c:when test="${siguiendo}">
												<input type="hidden" name="action" value="dejar" />
												<button type="submit" class="btn-outline">Dejar de seguir</button>
											</c:when>
											<c:otherwise>
												<input type="hidden" name="action" value="seguir" />
												<button type="submit" class="btn-primary">Seguir</button>
											</c:otherwise>
										</c:choose>
									</form>
								</c:if>
							</div>
						</div>

						<!-- Tabs de navegación -->
						<div class="tabs">
							<div class="tab active" onclick="showTab('datos')">Datos
								del usuario</div>
							<c:if
								test="${tipoUsuario == 'Asistente' && not empty registrosAsistente}">
								<div class="tab" onclick="showTab('registros')">Registros</div>
							</c:if>
							<c:if test="${tipoUsuario == 'Organizador'}">
								<div class="tab" onclick="showTab('ediciones')">Ediciones</div>
							</c:if>
						</div>

						<!-- Contenido de los tabs con tamaño fijo -->
						<div class="tab-content">
							<!-- Panel de Datos del Usuario -->
							<div id="datos" class="tab-panel active">
								<h3 style="color: #182080; margin-top: 0;">Datos del
									usuario</h3>
								<div class="user-details-list">
									<p>
										<strong>Nickname:</strong> ${usuario.nickname}
									</p>
									<p>
										<strong>Nombre:</strong> ${usuario.nombre}
									</p>
									<p>
										<strong>Correo:</strong> ${usuario.correo}
									</p>
									<p>
										<strong>Rol:</strong> ${tipoUsuario}
									</p>

									<!-- Información específica para Asistente -->
									<c:if
										test="${tipoUsuario == 'Asistente' && not empty asistente}">
										<p>
											<strong>Apellido:</strong> ${asistente.apellido}
										</p>
										<p>
											<strong>Fecha de nacimiento:</strong>
											${asistente.fechaNacimiento.dia}/${asistente.fechaNacimiento.mes}/${asistente.fechaNacimiento.anio}
										</p>
										<c:if test="${not empty asistente.institucion}">
											<p>
												<strong>Institución:</strong> ${asistente.institucion}
											</p>
										</c:if>
									</c:if>

									<!-- Información específica para Organizador -->
									<c:if
										test="${tipoUsuario == 'Organizador' && not empty organizador}">
										<c:if test="${not empty organizador.descripcion}">
											<p>
												<strong>Descripción:</strong> ${organizador.descripcion}
											</p>
										</c:if>
										<c:if test="${not empty organizador.link}">
											<p>
												<strong>Sitio web:</strong> <a href="${organizador.link}"
													target="_blank">${organizador.link}</a>
											</p>
										</c:if>
									</c:if>
								</div>
							</div>

							<!-- Panel de Registros para Asistente -->
							<c:if
								test="${tipoUsuario == 'Asistente' && not empty registrosAsistente}">
								<div id="registros" class="tab-panel">
									<h3 style="color: #182080; margin-top: 0;">Registros</h3>
									<table class="table">
										<thead>
											<tr>
												<th>Edición</th>
												<th>Tipo de Registro</th>
												<th>Fecha Registro</th>
												<th>Costo</th>
												<th>Acciones</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach var="registro" items="${registrosAsistente}">
												<tr>
													<td>${registro["edicion"]}</td>
													<td>${registro["tipoDeRegistro"]}</td>
													<td>${registro["fechaRegistro"]}</td>
													<td>$${registro["costo"]}</td>
													<td>
														<!-- Solo mostrar botón de consultar si el usuario tiene permisos específicos -->
														<c:set var="claveRegistro"
															value="${usuario.nickname}|${registro['edicion']}|${registro['tipoDeRegistro']}" />
														<c:choose>
															<c:when
																test="${registrosConsultables.contains(claveRegistro)}">
																<a
																	href="${pageContext.request.contextPath}/consultaRegistro?asistente=${usuario.nickname}&edicion=${registro['edicion']}&tipoRegistro=${registro['tipoDeRegistro']}&from=perfilUsuario"
																	class="btn-primary"
																	style="background-color: #182080; color: white; padding: 0.3rem 0.6rem; border-radius: 6px; text-decoration: none; font-size: 0.9rem;">
																	Consultar </a>
															</c:when>
															<c:otherwise>
																<span style="color: #666; font-style: italic;">No
																	disponible</span>
															</c:otherwise>
														</c:choose>
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</c:if>

							<!-- Panel de Ediciones para Organizador -->
							<c:if test="${tipoUsuario == 'Organizador'}">
								<div id="ediciones" class="tab-panel">
									<h3 style="color: #182080; margin-top: 0;">Ediciones</h3>
									<table class="table">
										<thead>
											<tr>
												<th>Evento</th>
												<th>Nombre</th>
												<th>Sigla</th>
												<th>Estado</th>
												<th>Acciones</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach var="edicion" items="${edicionesAceptadas}">
												<tr>
													<td>${edicion.evento}</td>
													<td>${edicion.nombre}</td>
													<td>${edicion.sigla}</td>
													<td><span style="color: #2a7f2e; font-weight: 600;">Aceptada</span></td>
													<td><a
														href="${pageContext.request.contextPath}/consultaEdicion?edicion=${edicion.nombre}"
														class="btn-primary"
														style="background-color: #182080; color: white; padding: 0.3rem 0.6rem; border-radius: 6px; text-decoration: none; font-size: 0.9rem;">
															Consultar </a></td>
												</tr>
											</c:forEach>
											<c:forEach var="edicion" items="${edicionesIngresadas}">
												<tr>
													<td>${edicion.evento}</td>
													<td>${edicion.nombre}</td>
													<td>${edicion.sigla}</td>
													<td><span style="color: #b8860b; font-weight: 600;">Ingresada</span></td>
													<td><a
														href="${pageContext.request.contextPath}/consultaEdicion?edicion=${edicion.nombre}"
														class="btn-primary"
														style="background-color: #182080; color: white; padding: 0.3rem 0.6rem; border-radius: 6px; text-decoration: none; font-size: 0.9rem;">
															Consultar </a></td>
												</tr>
											</c:forEach>
											<c:forEach var="edicion" items="${edicionesRechazadas}">
												<tr>
													<td>${edicion.evento}</td>
													<td>${edicion.nombre}</td>
													<td>${edicion.sigla}</td>
													<td><span style="color: #dc3545; font-weight: 600;">Rechazada</span></td>
													<td><a
														href="${pageContext.request.contextPath}/consultaEdicion?edicion=${edicion.nombre}"
														class="btn-primary"
														style="background-color: #182080; color: white; padding: 0.3rem 0.6rem; border-radius: 6px; text-decoration: none; font-size: 0.9rem;">
															Consultar </a></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</c:if>
						</div>
					</div>
				</section>
			</main>
		</div>
	</div>

	<footer></footer>

	<script>
    function showTab(tabName) {
      // Ocultar todos los paneles
      const panels = document.querySelectorAll('.tab-panel');
      panels.forEach(panel => panel.classList.remove('active'));
      
      // Desactivar todas las tabs
      const tabs = document.querySelectorAll('.tab');
      tabs.forEach(tab => tab.classList.remove('active'));
      
      // Mostrar el panel seleccionado
      const selectedPanel = document.getElementById(tabName);
      if (selectedPanel) {
        selectedPanel.classList.add('active');
      }
      
      // Activar la tab correspondiente
      const selectedTab = event.target;
      selectedTab.classList.add('active');
    }
  </script>
</body>
</html>
