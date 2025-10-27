<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Mis Registros · eventos.uy</title>
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

			<main class="main-content">
				<div class="page-header">
					<h2 style="margin: 0; color: #333; font-size: 1.8rem;">Mis
						Registros</h2>
					<p style="margin: 0.5rem 0 0 0; color: #666;">Ediciones a las
						que te has registrado</p>
				</div>

				<c:choose>
					<c:when test="${empty edicionesRegistradas}">
						<div
							style="text-align: center; padding: 3rem; background: white; border-radius: 8px; margin-top: 2rem; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);">
							<div style="font-size: 3rem; margin-bottom: 1rem; opacity: 0.3;">📝</div>
							<h3 style="color: #666; margin-bottom: 1rem;">No tienes
								registros aún</h3>
							<p style="color: #888; margin-bottom: 2rem;">Explora eventos
								y regístrate a las ediciones que te interesen.</p>
							<a href="${pageContext.request.contextPath}/inicio"
								class="btn-primary"
								style="background: #182080; color: white; padding: 0.75rem 1.5rem; border-radius: 6px; text-decoration: none; font-weight: 600; display: inline-block;">Explorar
								Eventos</a>
						</div>
					</c:when>
					<c:otherwise>
						<div style="margin-top: 2rem;">
							<c:forEach var="edicion" items="${edicionesRegistradas}">
								<div class="card"
									style="background: white; border-radius: 8px; padding: 1.5rem; margin-bottom: 1.5rem; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); border-left: 4px solid #182080;">
									<h3
										style="color: #182080; margin: 0 0 1rem 0; font-size: 1.3rem; font-weight: 600;">${edicion}</h3>

									<div style="display: grid; gap: 0.75rem; margin-top: 1rem;">
										<c:forEach var="registro" items="${misRegistros}">
											<c:if test="${registro.getNomEdicion() eq edicion}">
												<div
													style="background: #f8f9fa; padding: 1rem; border-radius: 6px; border: 1px solid #e9ecef; display: flex; justify-content: space-between; align-items: center;">
													<div>
														<div
															style="font-weight: 600; color: #182080; font-size: 1.1rem; margin-bottom: 0.25rem;">${registro.getTipoDeRegistro()}</div>
														<div style="color: #666; font-size: 0.9rem;">Registrado:
															${registro.getFechaRegistro()}</div>
													</div>
													<a
														href="${pageContext.request.contextPath}/consultaRegistro?asistente=${nickname}&edicion=${edicion}&tipoRegistro=${registro.getTipoDeRegistro()}&from=misRegistros"
														style="background: #182080; color: white; padding: 0.5rem 1rem; border-radius: 4px; text-decoration: none; font-size: 0.9rem; font-weight: 500; transition: background-color 0.2s;"
														onmouseover="this.style.backgroundColor='#0f1660'"
														onmouseout="this.style.backgroundColor='#182080'">Ver
														Detalles</a>
												</div>
											</c:if>
										</c:forEach>
									</div>
								</div>
							</c:forEach>
						</div>
					</c:otherwise>
				</c:choose>
			</main>
		</div>
	</div>
</body>
</html>