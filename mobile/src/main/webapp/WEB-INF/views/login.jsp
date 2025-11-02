<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">

<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Iniciar sesi�n � eventos.uy</title>
<link rel="icon" type="image/png" href="img/favicon.png">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/styles.css">
<link rel="preconnect" href="https://fonts.googleapis.com" />
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
<link
	href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap"
	rel="stylesheet" />
</head>

<body>
	<div>
		<header class="header">
			<h1>
				<a href="#">eventos.uy</a>
			</h1>
		</header>

		<div class="content single">
			<main>
				<section class="auth-container">
					<div class="auth-card">
						<h2>Iniciar sesión</h2>
					

						<form action="<%=request.getContextPath()%>/login" method="POST"
							class="auth-form" autocomplete="on">
							<label class="form-group"> <span class="label-text">Correo
									o nombre de usuario</span> <input type="text" name="usuario" required />
							</label> <label class="form-group"> <span class="label-text">Contraseña</span>
								<input type="password" name="password" required />
							</label>

							<button type="submit" class="btn-primary"
								style="margin-top: 1rem; width: 100%;">Iniciar sesión</button>
						</form>
						
						<div style="margin-top: 1.5rem; padding-top: 1.5rem; border-top: 1px solid #e5e5e5;">
							<form action="<%=request.getContextPath()%>/cargarDatos" method="POST"
								  style="text-align: center;">
								<button type="submit" class="btn-outline" 
									    style="width: 100%;">
									Cargar Datos de Prueba
								</button>
							</form>
						</div>

						<c:if test="${not empty error}">
							<p style="color: #dc3545; margin-top: 1rem; padding: 0.75rem; background-color: #f8d7da; border: 1px solid #f5c2c7; border-radius: 4px;">
								${error}
							</p>
						</c:if>
						
						<c:if test="${not empty sessionScope.datosMensaje}">
							<c:set var="alertColor" value="${sessionScope.datosMensajeTipo == 'success' ? '#d1e7dd' : '#f8d7da'}" />
							<c:set var="borderColor" value="${sessionScope.datosMensajeTipo == 'success' ? '#badbcc' : '#f5c2c7'}" />
							<c:set var="textColor" value="${sessionScope.datosMensajeTipo == 'success' ? '#0f5132' : '#842029'}" />
							<p style="color: ${textColor}; margin-top: 1rem; padding: 0.75rem; background-color: ${alertColor}; border: 1px solid ${borderColor}; border-radius: 4px;">
								${sessionScope.datosMensaje}
							</p>
							<c:remove var="datosMensaje" scope="session" />
							<c:remove var="datosMensajeTipo" scope="session" />
						</c:if>
					</div>
				</section>
			</main>
		</div>
	</div>
	<style>
.form-group {
	display: flex;
	flex-direction: column;
	margin-bottom: 1rem;
}
</style>
</body>

</html>