<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<header class="header">
	<h1>
		<a href="<%=request.getContextPath()%>/inicio">eventos.uy</a>
	</h1>
	<div class="header-right"
		style="display: flex; align-items: center; gap: 1rem;">
		<c:choose>
			<c:when test="${not empty role}">
				<div class="user-badge"
					style="display: flex; align-items: center; gap: 0.5rem;">
					<a href="${pageContext.request.contextPath}/miPerfil"
						style="display: flex; align-items: center; gap: 0.5rem; text-decoration: none; color: inherit;">
						<img class="avatar"
						src="${not empty avatar ? pageContext.request.contextPath.concat(avatar) : pageContext.request.contextPath.concat('/img/usSinFoto.webp')}"
						alt="Avatar de usuario" /> <span class="nickname">${nickname}</span>
					</a> <a href="${pageContext.request.contextPath}/logout"
						class="btn-primary logout">Cerrar sesión</a>
				</div>
				<div class="responsiveHeaderButton">
					<button id="openModalBtn">| | |</button>
				</div>
				<div id="modal" class="modal">
					<div class="modal-content">
						<button id="closeModalBtn" class="close">&times;</button>
						<h2>
							<a href="${pageContext.request.contextPath}/logout">Cerrar
								sesión</a>
						</h2>
						<hr>
						<c:choose>
							<c:when test="${role == 'organizador'}">
								<div>
									<ul class="menu-list-responsive">
										<li><a
											href="${pageContext.request.contextPath}/altaEvento">Alta
												Evento</a></li>
										<li><a
											href="${pageContext.request.contextPath}/altaEdicion">Alta
												Edición</a></li>
										<li><a href="altaInstitucion">Alta Institución</a></li>
										<li><a href="edicionesOrganizadas">Ediciones
												Organizadas</a></li>
									</ul>
								</div>
							</c:when>
							<c:when test="${role == 'asistente'}">
								<div>
									<ul class="menu-list-responsive">
										<li><a
											href="${pageContext.request.contextPath}/misRegistros">Mis
												Registros</a></li>
									</ul>
								</div>
							</c:when>
						</c:choose>
						<hr>
						<ul class="menu-list-responsive">
							<c:choose>
								<c:when test="${empty categorias}">
									<li><span class="muted">No hay categorías
											disponibles.</span></li>
								</c:when>
								<c:otherwise>
									<li><c:url var="urlTodas" value="/inicio" /> <a
										href="${urlTodas}"
										class="${categoriaSeleccionada == 'todas' ? 'active' : ''}">Todas</a>
									</li>
									<c:forEach var="cat" items="${categorias}">
										<li><c:url var="catUrl" value="/inicio">
												<c:param name="categoria" value="${cat}" />
											</c:url> <a href="${catUrl}"
											class="${cat eq categoriaSeleccionada ? 'active' : ''}">${cat}</a>
										</li>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</ul>
						<hr>
						
					</div>
				</div>

			</c:when>
			<c:otherwise>
				<nav class="nav-links">
					<a href="${pageContext.request.contextPath}/login"
						style="color: white; text-decoration: none; font-weight: 600; padding: 0.5rem 1rem; border-radius: 6px; transition: background-color 0.2s;"
						onmouseover="this.style.backgroundColor='rgba(255,255,255,0.1)'"
						onmouseout="this.style.backgroundColor='transparent'">Iniciar
						sesión</a> <a href="${pageContext.request.contextPath}/signup"
						style="color: white; text-decoration: none; font-weight: 600; padding: 0.5rem 1rem; border-radius: 6px; transition: background-color 0.2s;"
						onmouseover="this.style.backgroundColor='rgba(255,255,255,0.1)'"
						onmouseout="this.style.backgroundColor='transparent'">Registrarse</a>
				</nav>
			</c:otherwise>
		</c:choose>
		<!-- Botón para cargar datos eliminado -->
	</div>
</header>
<script>
  const modal = document.getElementById('modal');
  const openBtn = document.getElementById('openModalBtn');
  const closeBtn = document.getElementById('closeModalBtn');

  openBtn.addEventListener('click', () => {
    modal.style.display = 'flex';
  });

  closeBtn.addEventListener('click', () => {
    modal.style.display = 'none';
  });
  window.addEventListener('click', (e) => {
    if (e.target === modal) {
      modal.style.display = 'none';
    }
  });
</script>