<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
						class="btn-primary">Cerrar sesión</a>
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