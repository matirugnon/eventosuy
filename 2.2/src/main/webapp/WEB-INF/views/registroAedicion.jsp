<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro a Edición · eventos.uy</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap" rel="stylesheet">
</head>
<body>
    <div>
        <header class="header">
            <h1><a href="${pageContext.request.contextPath}/inicio" style="color: inherit; text-decoration: none;">eventos.uy</a></h1>
            <div class="header-right">
                <c:choose>
                    <c:when test="${not empty role}">
                        <div class="user-badge" style="display: flex; align-items: center; gap: 0.5rem;">
                            <a href="${pageContext.request.contextPath}/miPerfil" style="display: flex; align-items: center; gap: 0.5rem; text-decoration: none; color: inherit;">
                                <img class="avatar" 
                                     src="${not empty avatar ? pageContext.request.contextPath.concat(avatar) : pageContext.request.contextPath.concat('/img/eventoSinImagen.jpeg')}" 
                                     alt="Avatar de usuario" />
                                <span class="nickname">${nickname}</span>
                            </a>
                            <a href="${pageContext.request.contextPath}/logout" class="btn-primary">Cerrar sesión</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login" class="btn-primary">Iniciar sesión</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </header>

        <div class="content">
            <aside class="sidebar">
                <c:if test="${role == 'asistente'}">
                    <div class="panel sidebar">
                        <div class="panel-header">Mi perfil</div>
                        <ul class="menu-list">
                            <li><a href="${pageContext.request.contextPath}/registroAedicion">Registro a Edición</a></li>
                        </ul>
                    </div>
                </c:if>

                <!-- Categorías -->
                <div class="panel sidebar" style="margin-top: 1rem;">
                    <div class="panel-header">Categorías</div>
                    <ul class="menu-list">
                        <c:choose>
                            <c:when test="${empty categorias}">
                                <li><span class="muted">No hay categorías disponibles.</span></li>
                            </c:when>
                            <c:otherwise>
                                <li>
                                    <c:url var="urlTodas" value="/inicio"/>
                                    <a href="${urlTodas}">Todas</a>
                                </li>
                                <c:forEach items="${categorias}" var="categoria">
                                    <li>
                                        <c:url var="catUrl" value="/inicio">
                                            <c:param name="categoria" value="${categoria}"/>
                                        </c:url>
                                        <a href="${catUrl}">${categoria}</a>
                                    </li>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </div>

                <!-- Botón "Ver listado de Usuarios" -->
                <div style="margin-top: 2rem; border-top: 1px solid #e0e0e0; padding-top: 1rem;">
                    <a href="${pageContext.request.contextPath}/listarUsuarios" style="
                        display: flex;
                        align-items: center;
                        gap: 0.5rem;
                        color: #182080;
                        font-weight: 600;
                        text-decoration: none;
                        padding: 0.75rem;
                        border-radius: 6px;
                        transition: background-color 0.2s;
                        background-color: rgba(24, 32, 128, 0.05);
                    ">Ver listado de usuarios</a>
                </div>
            </aside>

            <main>
                <section class="panel" style="max-width: 700px; margin: 0 auto;">
                    <div class="panel-header">Registro a Edición de Evento</div>
                    <div class="panel-body">
                        <form method="post" action="${pageContext.request.contextPath}/registroAedicion">
                            <!-- Paso 1: Selección de evento -->
                            <div class="consulta-registro-form-row">
                                <label class="form-group" style="margin-bottom: 0;">
                                    <span class="label-text">Seleccioná un evento</span>
                                </label>
                                <select id="eventoSelect" required>
                                    <option value="">Seleccionar…</option>
                                    <c:forEach items="${eventos}" var="evento">
                                        <option value="${evento.nombre}">${evento.nombre}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <!-- Paso 2: Selección de edición -->
                            <div class="consulta-registro-form-row" id="step-edicion" style="display: none;">
                                <label class="form-group" style="margin-bottom: 0;">
                                    <span class="label-text">Seleccioná una edición</span>
                                </label>
                                <select id="edicionSelect" name="edicion" required>
                                    <option value="">Seleccionar…</option>
                                </select>
                            </div>

                            <!-- Paso 3: Detalle de edición y tipos de registro -->
                            <div id="step-detalle" style="display: none;">
                                <div class="panel" style="background: #f8f9ff;">
                                    <div class="panel-header" style="color: #182080;">Detalle de la edición</div>
                                    <div class="panel-body">
                                        <p><strong>Nombre:</strong> <span id="edicionNombre"></span></p>
                                        <p><strong>Fechas:</strong> <span id="edicionFechas"></span></p>
                                        <p><strong>Ciudad:</strong> <span id="edicionCiudad"></span></p>
                                        <p><strong>País:</strong> <span id="edicionPais"></span></p>
                                        <p><strong>Tipos de registro:</strong></p>
                                        <div id="tiposRegistro"></div>
                                    </div>
                                </div>
                            </div>

                            <!-- Paso 4: Registro -->
                            <div id="formRegistro" style="display: none; margin-top: 1.5rem;">
                                <div class="consulta-registro-form-row">
                                    <label class="form-group" style="margin-bottom: 0;">
                                        <span class="label-text">Tipo de registro</span>
                                    </label>
                                    <select id="tipoRegistroSelect" name="tipoRegistro" required></select>
                                </div>
                                <div class="consulta-registro-form-row">
                                    <label class="form-group" style="margin-bottom: 0;">
                                        <span class="label-text">¿Usar código de patrocinio?</span>
                                    </label>
                                    <input type="text" id="codigoPatrocinio" name="codigoPatrocinio" 
                                           placeholder="Ingresá el código (opcional)" 
                                           style="min-width: 220px; max-width: 340px; width: 100%;" />
                                </div>

                                <c:if test="${not empty mensaje}">
                                    <c:choose>
                                        <c:when test="${tipoMensaje == 'success'}">
                                            <div id="msg" style="margin-top: 0.5rem; min-height: 1.25rem; font-weight: 600; color: #2a7f2e;">
                                                ${mensaje}
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div id="msg" style="margin-top: 0.5rem; min-height: 1.25rem; font-weight: 600; color: #c00;">
                                                ${mensaje}
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>

                                <div style="display: flex; gap: 0.5rem; margin-top: 0.5rem;">
                                    <button type="submit" class="btn-primary" id="submitBtn">Registrarme</button>
                                    <a class="btn-outline" href="${pageContext.request.contextPath}/inicio">Cancelar</a>
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

        let edicionActual = null;
        let tiposActuales = [];

        eventoSelect.addEventListener('change', function() {
            edicionSelect.innerHTML = '<option value="">Seleccionar…</option>';
            stepEdicion.style.display = eventoSelect.value ? '' : 'none';
            stepDetalle.style.display = 'none';
            formRegistro.style.display = 'none';

            if (!eventoSelect.value) return;

            // Cargar ediciones via AJAX
            fetch('${pageContext.request.contextPath}/registroAedicion?action=getEdiciones&evento=' + encodeURIComponent(eventoSelect.value))
                .then(response => response.json())
                .then(ediciones => {
                    ediciones.forEach(edicion => {
                        const opt = document.createElement('option');
                        opt.value = edicion.id;
                        opt.textContent = edicion.nombre;
                        edicionSelect.appendChild(opt);
                    });
                })
                .catch(error => console.error('Error cargando ediciones:', error));
        });

        edicionSelect.addEventListener('change', function() {
            stepDetalle.style.display = 'none';
            formRegistro.style.display = 'none';

            if (!edicionSelect.value) return;

            // Cargar detalles de edición via AJAX
            fetch('${pageContext.request.contextPath}/registroAedicion?action=getEdicionDetails&edicion=' + encodeURIComponent(edicionSelect.value))
                .then(response => response.json())
                .then(edicion => {
                    edicionActual = edicion;
                    edicionNombre.textContent = edicion.nombre;
                    edicionFechas.textContent = edicion.fechas;
                    edicionCiudad.textContent = edicion.ciudad;
                    edicionPais.textContent = edicion.pais;
                    tiposActuales = edicion.tipos;

                    // Mostrar tipos de registro
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
        });
    </script>
</body>
</html>