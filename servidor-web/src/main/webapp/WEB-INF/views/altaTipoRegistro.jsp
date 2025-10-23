<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Alta de Tipo de Registro · eventos.uy</title>
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
			

			<main>
				<section class="panel">
					<div class="panel-body">
						<h2 style="margin: 0 0 1.5rem 0; color: #182080;">Alta de
							Tipo de Registro - ${edicion.evento} - ${edicion.nombre}</h2>
						<c:if test="${not empty error}">
							<div class="error-message">⚠️ ${error}</div>
						</c:if>

						<c:if test="${not empty success}">
							<div class="success-message">
								✅ ${success} <br>
								<br> <a
									href="${pageContext.request.contextPath}/edicionesOrganizadas"
									class="btn-link">Volver a Ediciones Organizadas</a>
							</div>
						</c:if>

						<c:if test="${not empty edicion}">
							<div class="edicion-info-header">
								<h3>Edición: ${edicion.evento} - ${edicion.nombre}</h3>
								<p>
									<strong>Sigla:</strong> ${edicion.sigla} | <strong>Ubicación:</strong>
									${edicion.ciudad}, ${edicion.pais} | <strong>Fecha:</strong>
									${edicion.fechaInicio.dia}/${edicion.fechaInicio.mes}/${edicion.fechaInicio.anio}
									-
									${edicion.fechaFin.dia}/${edicion.fechaFin.mes}/${edicion.fechaFin.anio}
								</p>
							</div>
						</c:if>

						<form method="post" action="altaTipoRegistro"
							id="formAltaTipoRegistro" class="auth-form" novalidate>
							<input type="hidden" name="edicionNombre"
								value="${edicion.nombre}"> <label class="form-group">
								<span class="label-text">Edición</span> <input
								name="edicionNombre"
								value="${edicion.evento} - ${edicion.nombre}" readonly />
							</label> <label class="form-group"> <span class="label-text">Nombre
									del tipo de registro *</span> <input name="nombre" required
								maxlength="60" placeholder="Nombre único"
								value="${param.nombre}" />
							</label> <label class="form-group"> <span class="label-text">Descripción
									*</span> <textarea name="descripcion" required rows="3"
									maxlength="300" placeholder="Descripción del tipo de registro…">${param.descripcion}</textarea>
							</label>

							<div class="grid-2">
								<label class="form-group"> <span class="label-text">Costo
										(UYU) *</span> <input name="costo" type="number" min="0" step="0.01"
									required placeholder="0.00" value="${param.costo}" />
								</label> <label class="form-group"> <span class="label-text">Cupo
										*</span> <input name="cupo" type="number" min="1" step="1" required
									placeholder="Ej. 100" value="${param.cupo}" />
								</label>
							</div>

							<div id="msg"
								style="color: #c00; margin-top: .5rem; min-height: 1.5rem;"></div>

							<div
								style="margin-top: 1rem; display: flex; gap: 1rem; justify-content: center;">
								<button type="submit" class="btn-primary" style="flex: 1;">Alta
									Tipo de Registro</button>
								<button type="button" class="btn-outline"
									onclick="window.location.href='${pageContext.request.contextPath}/edicionesOrganizadas'">Cancelar</button>
							</div>
						</form>
					</div>
				</section>
			</main>
		</div>

		<footer></footer>

		<style>
.edicion-info-header {
	background-color: #f8f9fa;
	padding: 1rem;
	border-radius: 8px;
	margin-bottom: 1.5rem;
	border-left: 4px solid #007bff;
}

.edicion-info-header h3 {
	margin: 0 0 0.5rem 0;
	color: #333;
	font-size: 1.1rem;
}

.edicion-info-header p {
	margin: 0;
	color: #666;
	font-size: 0.9rem;
}

.form-group {
	display: flex;
	flex-direction: column;
	margin-bottom: 1rem;
}

.form-group .label-text {
	font-weight: 600;
	margin-bottom: 0.25rem;
	color: #333;
}

.form-group input, .form-group textarea {
	padding: 0.75rem;
	border-radius: 4px;
	border: 1px solid #ccc;
	font-size: 1rem;
	font-family: inherit;
}

.form-group input:focus, .form-group textarea:focus {
	outline: none;
	border-color: #007bff;
	box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
}

.form-group input[readonly] {
	background-color: #f8f9fa;
	color: #6c757d;
}

.grid-2 {
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 1rem;
}

.btn-outline {
	background-color: transparent;
	color: #6c757d;
	padding: 0.75rem 1.5rem;
	border: 1px solid #6c757d;
	border-radius: 4px;
	font-size: 1rem;
	cursor: pointer;
	transition: all 0.2s;
}

.btn-outline:hover {
	background-color: #6c757d;
	color: white;
}

.btn-link {
	color: #007bff;
	text-decoration: none;
	font-weight: 600;
}

.btn-link:hover {
	text-decoration: underline;
}

.error-message {
	color: #c00;
	margin-bottom: 1rem;
	padding: 0.75rem;
	background-color: #fff2f2;
	border: 1px solid #ffcdd2;
	border-radius: 4px;
}

.success-message {
	color: #155724;
	margin-bottom: 1rem;
	padding: 0.75rem;
	background-color: #d4edda;
	border: 1px solid #c3e6cb;
	border-radius: 4px;
}
</style>

		<script>
        (function () {
            const form = document.getElementById('formAltaTipoRegistro');
            const msg = document.getElementById('msg');

            function validarFormulario() {
                msg.textContent = '';
                return form.reportValidity();
            }

            form.addEventListener('submit', e => {
                if (!validarFormulario()) {
                    e.preventDefault();
                    return;
                }
                // Si la validación pasa, el formulario se envía normalmente al servidor
            });
        })();
    </script>
</body>
</html>