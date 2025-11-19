<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%
// Leer mensajes de sesión si existen
String datosMensaje = (String) session.getAttribute("datosMensaje");
String datosMensajeTipo = (String) session.getAttribute("datosMensajeTipo");
if (datosMensaje != null) {
	request.setAttribute("mensaje", datosMensaje);
	request.setAttribute("tipoMensaje",
	datosMensajeTipo != null && datosMensajeTipo.equals("info") ? "success" : "error");
	session.removeAttribute("datosMensaje");
	session.removeAttribute("datosMensajeTipo");
}

// Leer parámetros de preselección
String eventoParam = request.getParameter("evento") != null ? request.getParameter("evento") : "";
String edicionParam = request.getParameter("edicion") != null ? request.getParameter("edicion") : "";
%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Registro a Edición · eventos.uy</title>
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
		<jsp:include page="/WEB-INF/views/componentes/header.jsp" />

		<div class="content">
			<jsp:include page="/WEB-INF/views/componentes/aside.jsp" />

			<main>
				<section class="panel" style="max-width: 700px; margin: 0 auto;">
					<div class="panel-header">Registro a Edición de Evento</div>
					<div class="panel-body">
						<form method="post"
							action="${pageContext.request.contextPath}/registroAedicion">
							<!-- Paso 1: Selección de evento -->
							<div class="consulta-registro-form-row">
								<label class="form-group" style="margin-bottom: 0;"> <span
									class="label-text">Seleccioná un evento</span>
								</label> <select id="eventoSelect" required>
									<option value="">Seleccionar…</option>
									<c:forEach items="${eventos}" var="evento">
										<option value="${evento.nombre}"
											${evento.nombre == eventoParam ? "selected" : ""}>
											${evento.nombre}</option>
									</c:forEach>
								</select>
							</div>

							<!-- Paso 2: Selección de edición -->
							<div class="consulta-registro-form-row" id="step-edicion"
								style="display: none;">
								<label class="form-group" style="margin-bottom: 0;"> <span
									class="label-text">Seleccioná una edición</span>
								</label> <select id="edicionSelect" name="edicion" required>
									<option value="">Seleccionar…</option>
								</select>
							</div>

							<!-- Paso 3: Detalle de edición y tipos de registro -->
							<div id="step-detalle" style="display: none;">
								<div class="panel" style="background: #f8f9ff;">
									<div class="panel-header" style="color: #182080;">Detalle
										de la edición</div>
									<div class="panel-body">
										<p>
											<strong>Nombre:</strong> <span id="edicionNombre"></span>
										</p>
										<p>
											<strong>Fechas:</strong> <span id="edicionFechas"></span>
										</p>
										<p>
											<strong>Ciudad:</strong> <span id="edicionCiudad"></span>
										</p>
										<p>
											<strong>País:</strong> <span id="edicionPais"></span>
										</p>
										<p>
											<strong>Tipos de registro:</strong>
										</p>
										<div id="tiposRegistro"></div>
									</div>
								</div>
							</div>

							<!-- Paso 4: Registro -->
							<div id="formRegistro" style="display: none; margin-top: 1.5rem;">
								<div class="consulta-registro-form-row">
									<label class="form-group" style="margin-bottom: 0;"> <span
										class="label-text">Tipo de registro</span>
									</label> <select id="tipoRegistroSelect" name="tipoRegistro" required></select>
								</div>
								<div class="consulta-registro-form-row">
									<label class="form-group" style="margin-bottom: 0;"> <span
										class="label-text">¿Usar código de patrocinio?</span>
									</label> <input type="text" id="codigoPatrocinio"
										name="codigoPatrocinio"
										placeholder="Ingresá el código (opcional)"
										style="min-width: 220px; max-width: 340px; width: 100%;" />
								</div>

								<c:if test="${not empty mensaje}">
									<c:choose>
										<c:when test="${tipoMensaje == 'success'}">
											<div id="msg"
												style="margin-top: 0.5rem; min-height: 1.25rem; font-weight: 600; color: #2a7f2e;">
												${mensaje}</div>
										</c:when>
										<c:otherwise>
											<div id="msg"
												style="margin-top: 0.5rem; min-height: 1.25rem; font-weight: 600; color: #c00;">
												${mensaje}</div>
										</c:otherwise>
									</c:choose>
								</c:if>

								<div style="display: flex; gap: 0.5rem; margin-top: 0.5rem;">
									<button type="submit" class="btn-primary" id="submitBtn">Registrarme</button>
									<a class="btn-outline"
										href="${pageContext.request.contextPath}/inicio">Cancelar</a>
								</div>
							</div>
						</form>
					</div>
				</section>
			</main>
		</div>
	</div>

	<script>
        const eventoSelect = document.getElementById('eventoSelect');
        const edicionSelect = document.getElementById('edicionSelect');
        const stepEdicion = document.getElementById('step-edicion');
        const stepDetalle = document.getElementById('step-detalle');
        const edicionNombre = document.getElementById('edicionNombre');
        const edicionFechas = document.getElementById('edicionFechas');
        const edicionCiudad = document.getElementById('edicionCiudad');
        const edicionPais = document.getElementById('edicionPais');
        const tiposRegistroDiv = document.getElementById('tiposRegistro');
        const tipoRegistroSelect = document.getElementById('tipoRegistroSelect');
        const formRegistro = document.getElementById('formRegistro');

        const eventoParam = "<%=eventoParam%>";
        const edicionParam = "<%=edicionParam%>";

        let edicionActual = null;
        let tiposActuales = [];

        function cargarEdiciones(eventoNombre, callback) {
            edicionSelect.innerHTML = '<option value="">Seleccionar…</option>';
            stepEdicion.style.display = eventoNombre ? '' : 'none';
            stepDetalle.style.display = 'none';
            formRegistro.style.display = 'none';

            if (!eventoNombre) return;

            fetch('${pageContext.request.contextPath}/registroAedicion?action=getEdiciones&evento=' + encodeURIComponent(eventoNombre))
                .then(response => response.json())
                .then(ediciones => {
                    ediciones.forEach(edicion => {
                        const opt = document.createElement('option');
                        opt.value = edicion.id;
                        opt.textContent = edicion.nombre;
                        if(edicion.nombre === edicionParam) opt.selected = true;
                        edicionSelect.appendChild(opt);
                    });
                    if(callback) callback();
                })
                .catch(error => console.error('Error cargando ediciones:', error));
        }

        function cargarDetalleEdicion(edicionId) {
            stepDetalle.style.display = 'none';
            formRegistro.style.display = 'none';
            if (!edicionId) return;

            fetch('${pageContext.request.contextPath}/registroAedicion?action=getEdicionDetails&edicion=' + encodeURIComponent(edicionId))
                .then(response => response.json())
                .then(edicion => {
                    edicionActual = edicion;
                    edicionNombre.textContent = edicion.nombre;
                    edicionFechas.textContent = edicion.fechas;
                    edicionCiudad.textContent = edicion.ciudad;
                    edicionPais.textContent = edicion.pais;
                    tiposActuales = edicion.tipos;

                    tiposRegistroDiv.innerHTML = '';
                    tipoRegistroSelect.innerHTML = '';
                    edicion.tipos.forEach(tipo => {
                        const p = document.createElement('p');
                        p.textContent = tipo.nombre + ' (Cupo: ' + tipo.cupo + ', Costo: $' + tipo.costo + ')';
                        tiposRegistroDiv.appendChild(p);

                        const opt = document.createElement('option');
                        opt.value = tipo.id;
                        opt.textContent = tipo.nombre;
                        tipoRegistroSelect.appendChild(opt);
                    });

                    stepDetalle.style.display = '';
                    formRegistro.style.display = '';
                })
                .catch(error => console.error('Error cargando detalles de edición:', error));
        }

        // Preseleccionar evento y edición si vienen por URL
        if(eventoParam) {
            eventoSelect.value = eventoParam;
            cargarEdiciones(eventoParam, function(){
                const edicionSeleccionada = Array.from(edicionSelect.options).find(opt => opt.selected);
                if(edicionSeleccionada) cargarDetalleEdicion(edicionSeleccionada.value);
            });
        }

        eventoSelect.addEventListener('change', function() {
            cargarEdiciones(eventoSelect.value);
        });

        edicionSelect.addEventListener('change', function() {
            cargarDetalleEdicion(edicionSelect.value);
        });

		// Submit AJAX
		const form = document.querySelector('form[action*="registroAedicion"]');
		const submitBtn = document.getElementById('submitBtn');
		function ensureMsgEl() {
			let msgEl = document.getElementById('msg');
			if (!msgEl) {
				msgEl = document.createElement('div');
				msgEl.id = 'msg';
				msgEl.style.marginTop = '0.5rem';
				msgEl.style.minHeight = '1.25rem';
				msgEl.style.fontWeight = '600';
				const btnContainer = submitBtn.parentElement;
				btnContainer.parentElement.insertBefore(msgEl, btnContainer);
			}
			return msgEl;
		}

		function setMessage(text, success) {
			const el = ensureMsgEl();
			el.textContent = text || '';
			el.style.color = success ? '#2a7f2e' : '#c00';
		}

		function clearMessage() {
			const el = document.getElementById('msg');
			if (el) el.textContent = '';
		}

		eventoSelect.addEventListener('change', clearMessage);
		edicionSelect.addEventListener('change', clearMessage);
		tipoRegistroSelect.addEventListener('change', clearMessage);

		form.addEventListener('submit', function(e) {
			e.preventDefault();
			submitBtn.disabled = true;

			const params = new URLSearchParams();
			params.append('edicion', edicionSelect.value || '');
			params.append('tipoRegistro', tipoRegistroSelect.value || '');
			params.append('codigoPatrocinio', document.getElementById('codigoPatrocinio').value || '');

			fetch(form.action, { method: 'POST', body: params.toString(), headers: { 'X-Requested-With': 'XMLHttpRequest', 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' } })
				.then(resp => resp.json())
				.then(obj => {
					if (obj.success) {
						setMessage(obj.message || 'Registro exitoso', true);
						submitBtn.disabled = false;
					} else {
						setMessage(obj.message || 'Ocurrió un error', false);
						submitBtn.disabled = false;
					}
				})
				.catch(err => {
					console.error('Error enviando registro:', err);
					setMessage('Ocurrió un error al procesar la solicitud', false);
					submitBtn.disabled = false;
				});
		});
    </script>
</body>
</html>
