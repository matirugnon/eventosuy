<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Alta de Institución · eventos.uy</title>
<link rel="icon" type="image/png"
	href="${pageContext.request.contextPath}/img/favicon.png">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/styles.css">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap"
	rel="stylesheet">
<style>
.form-group {
	display: flex;
	flex-direction: column;
	margin-bottom: 1rem;
}

.form-group input, .form-group textarea {
	padding: 0.5rem;
	border-radius: 4px;
	border: 1px solid #ccc;
	font-size: 1rem;
}

.form-group input[type="file"] {
	border: none;
}

.message {
	margin-top: 1rem;
	min-height: 1.5rem;
}

.message.success {
	color: #2a7f2e;
}

.message.error {
	color: #c00;
}

.error-message {
	color: #c00;
	margin-top: 1rem;
	padding: 0.75rem;
	background-color: #fff2f2;
	border: 1px solid #ffcdd2;
	border-radius: 4px;
}

.logo-preview {
	margin-top: 0.5rem;
	max-width: 200px;
	max-height: 100px;
	border: 1px solid #ddd;
	border-radius: 4px;
	display: none;
}

.file-info {
	margin-top: 0.5rem;
	font-size: 0.875rem;
	color: #666;
}
</style>
</head>
<body>
	<div>
		<!-- Header : ahora el header esta en la carpeta componentes, para que se cambie en una sola pag y sea igual para todas-->
		<jsp:include page="/WEB-INF/views/componentes/header.jsp" />

		<%-- Copiar mensaje flash desde sesión a request si existe (para mostrar después de redirect) --%>
		<% String datosMensaje = (String) session.getAttribute("datosMensaje");
			   String datosMensajeTipo = (String) session.getAttribute("datosMensajeTipo");
			   if (datosMensaje != null) {
				   if ("info".equals(datosMensajeTipo)) {
					   request.setAttribute("success", datosMensaje);
				   } else {
					   request.setAttribute("error", datosMensaje);
				   }
				   session.removeAttribute("datosMensaje");
				   session.removeAttribute("datosMensajeTipo");
			   }
			%>

		<div class="content">
			<jsp:include page="/WEB-INF/views/componentes/aside.jsp" />
			<main>
				<section class="panel">
					<div class="panel-body">
						<h2 style="margin: 0 0 1.5rem 0; color: #182080;">Alta de
							Institución</h2>
						<c:if test="${not empty error}">
							<div class="error-message">⚠️ ${error}</div>
						</c:if>

						<c:if test="${not empty success}">
							<div class="message success">✅ ${success}</div>
						</c:if>

						<form method="post" action="altaInstitucion"
							enctype="multipart/form-data" novalidate>
							<label class="form-group"> <span class="label-text">Nombre
									de la institución *</span> <input type="text" id="nombre"
								name="nombre" value="${not empty nombre ? nombre : ''}" required
								maxlength="100">
							</label> <label class="form-group"> <span class="label-text">Descripción
									*</span> <textarea id="descripcion" name="descripcion" rows="4"
									required maxlength="500">${not empty descripcion ? descripcion : ''}</textarea>
							</label> <label class="form-group"> <span class="label-text">Sitio
									Web *</span> <input type="url" id="sitioWeb" name="sitioWeb"
								value="${not empty sitioWeb ? sitioWeb : ''}"
								placeholder="https://..." required>
							</label> <label class="form-group"> <span class="label-text">Logo
									(opcional)</span> <input type="file" id="logo" name="logo"
								accept="image/*">
								<div class="file-info">Formatos soportados: JPG, PNG, GIF.
									Tamaño máximo: 5MB</div> <img id="logoPreview" class="logo-preview"
								alt="Preview del logo">
							</label>

							<div style="display: flex; gap: 1rem; margin-top: 2rem;">
								<button type="submit" class="btn-primary">Crear
									Institución</button>
								<button type="button" class="btn-outline"
									onclick="window.location.href='inicio'">Cancelar</button>
							</div>
						</form>
					</div>
				</section>
			</main>
		</div>

		<script>
			// Validación adicional en el frontend
			document
					.querySelector('form')
					.addEventListener(
							'submit',
							function(e) {
								const nombre = document
										.getElementById('nombre').value.trim();
								const descripcion = document
										.getElementById('descripcion').value
										.trim();
								const sitioWeb = document
										.getElementById('sitioWeb').value
										.trim();

								if (!nombre || !descripcion || !sitioWeb) {
									e.preventDefault();
									alert('Todos los campos obligatorios deben estar completos.');
									return;
								}

								// Validar formato básico de URL
								if (!sitioWeb.match(/^https?:\/\/.+/)) {
									e.preventDefault();
									alert('El sitio web debe comenzar con http:// o https://');
									return;
								}
							});

			// Preview del logo
			document
					.getElementById('logo')
					.addEventListener(
							'change',
							function(e) {
								const file = e.target.files[0];
								const preview = document
										.getElementById('logoPreview');

								if (file) {
									// Validar tipo de archivo
									const validTypes = [ 'image/jpeg',
											'image/png', 'image/gif' ];
									if (!validTypes.includes(file.type)) {
										alert('Por favor seleccione un archivo de imagen válido (JPG, PNG, GIF)');
										e.target.value = '';
										preview.style.display = 'none';
										return;
									}

									// Validar tamaño (5MB máximo)
									const maxSize = 5 * 1024 * 1024; // 5MB en bytes
									if (file.size > maxSize) {
										alert('El archivo es demasiado grande. Tamaño máximo: 5MB');
										e.target.value = '';
										preview.style.display = 'none';
										return;
									}

									// Mostrar preview
									const reader = new FileReader();
									reader.onload = function(e) {
										preview.src = e.target.result;
										preview.style.display = 'block';
									};
									reader.readAsDataURL(file);

									console.log('Logo seleccionado:',
											file.name, 'Tamaño:',
											(file.size / 1024).toFixed(1)
													+ 'KB');
								} else {
									preview.style.display = 'none';
								}
							});
		</script>
</body>
</html>