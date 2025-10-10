<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Alta Patrocinio · eventos.uy</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
  <style>
    .hidden { display: none; }
  </style>
</head>
<body>

	<div>
        <header class="header">
            <h1><a href="<%= request.getContextPath() %>/inicio">eventos.uy</a></h1>
            <div class="header-right">
                <c:choose>
                    <c:when test="${not empty role}">
                        <div class="user-badge" style="display: flex; align-items: center; gap: 0.5rem;">
                            <a href="${pageContext.request.contextPath}/miPerfil" style="display: flex; align-items: center; gap: 0.5rem; text-decoration: none; color: inherit;">
                                <img class="avatar" 
                                     src="${not empty avatar ? pageContext.request.contextPath.concat(avatar) : pageContext.request.contextPath.concat('/img/usSinFoto.webp')}" 
                                     alt="Avatar de ${nickname}" />
                                <span class="nickname">${nickname}</span>
                            </a>
                            <a href="${pageContext.request.contextPath}/logout" class="btn-primary">Cerrar sesión</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <nav class="nav-links">
                            <a href="${pageContext.request.contextPath}/login">Iniciar sesión</a>
                            <a href="${pageContext.request.contextPath}/signup">Registrarse</a>
                        </nav>
                    </c:otherwise>
                </c:choose>
            </div>
        </header>

        <div class="content">
            <aside class="sidebar">
                <c:choose>
                  <c:when test="${role == 'organizador'}">
                    <div class="panel sidebar">
                      <div class="panel-header">Mi perfil</div>
                      <ul class="menu-list">
                        <li><a href="${pageContext.request.contextPath}/altaEvento">Alta Evento</a></li>
                        <li><a href="${pageContext.request.contextPath}/altaEdicion">Alta Edición</a></li>
                        <li><a href="altaInstitucion">Alta Institución</a></li>
                        <li><a href="edicionesOrganizadas" class="active">Ediciones Organizadas</a></li>
                        <li><a href="#">Consulta Registro</a></li>
                      </ul>
                    </div>
                  </c:when>
                  <c:when test="${role == 'asistente'}">
                    <div class="panel sidebar">
                      <div class="panel-header">Mi perfil</div>
                      <ul class="menu-list">
                        <li><a href="#">Registro a Edición</a></li>
                      </ul>
                    </div>
                  </c:when>
                </c:choose>

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
                    ">
                        Ver listado de usuarios
                    </a>
                </div>
            </aside>

<main class="alta-patrocinio-container">
  <section class="panel">
    <div class="panel-header">
      Alta de Patrocinio / ${edicionSeleccionada}
    </div>

    <form id="formPatrocinio" class="alta-patrocinio-form"
          action="${pageContext.request.contextPath}/altaPatrocinio"
          method="post" novalidate>

      <!-- Edición fija, oculta -->
      <input type="hidden" name="edicion" value="${edicionSeleccionada}" />

      <!-- Selector tipo de registro -->
      <label class="form-group">
        <span class="label-text">Tipo de registro *</span>
        <select id="tipoRegistro" name="tipoRegistro" required>
          <option value="">Seleccionar tipo de registro…</option>
          <c:forEach var="tipo" items="${tiposRegistro}">
            <option value="${tipo.nombre}" 
                    data-id="${tipo.nombre}"
                    <c:if test="${tipoRegistroSeleccionado == tipo.nombre}">selected</c:if>>
              ${tipo.nombre}
            </option>
          </c:forEach>
        </select>
      </label>

      <!-- Info tipo registro dinámico -->
      <div id="infoTipoRegistro" class="info-box hidden">
        <c:forEach var="tipo" items="${tiposRegistro}">
          <div id="desc-${tipo.nombre}" class="desc-tipo hidden" style="white-space: pre-wrap;">
            <p><strong>Descripción:</strong> ${tipo.descripcion}</p>
            <p><strong>Costo:</strong> ${tipo.costo} UYU</p>
            <p><strong>Cupo:</strong> ${tipo.cupo}</p>
          </div>
        </c:forEach>
      </div>

      <!-- Detalles ocultos inicialmente -->
      <div id="formDetalles" class="hidden">

        <!-- Institución -->
        <label class="form-group" for="institucion">
          <span class="label-text">Institución *</span>
          <select id="institucion" name="institucion" required>
            <option value="">Seleccione una institución…</option>
            <c:forEach var="inst" items="${instituciones}">
              <option value="${inst}" <c:if test="${institucionSeleccionada == inst}">selected</c:if>>${inst}</option>
            </c:forEach>
          </select>
        </label>

        <!-- Nivel patrocinio -->
        <label class="form-group" for="nivelPatrocinio">
          <span class="label-text">Nivel de Patrocinio *</span>
          <select id="nivelPatrocinio" name="nivelPatrocinio" required>
            <option value="">Seleccione nivel…</option>
            <option value="Platino" <c:if test="${nivelSeleccionado == 'Platino'}">selected</c:if>>Platino</option>
            <option value="Oro" <c:if test="${nivelSeleccionado == 'Oro'}">selected</c:if>>Oro</option>
            <option value="Plata" <c:if test="${nivelSeleccionado == 'Plata'}">selected</c:if>>Plata</option>
            <option value="Bronce" <c:if test="${nivelSeleccionado == 'Bronce'}">selected</c:if>>Bronce</option>
          </select>
        </label>

        <!-- Aporte económico -->
        <label class="form-group" for="aporteEconomico">
          <span class="label-text">Aporte Económico (UYU) *</span>
          <input id="aporteEconomico" name="aporteEconomico" type="number" min="0" step="0.01"
                 placeholder="Ej. 10000" value="${aporteIngresado}" required />
        </label>

        <!-- Registros gratuitos -->
        <label class="form-group" for="registrosGratuitos">
          <span class="label-text">Cantidad de registros gratuitos *</span>
          <input id="registrosGratuitos" name="registrosGratuitos" type="number" min="0" step="1"
                 placeholder="Ej. 10" value="${registrosIngresados}" required />
        </label>

        <!-- Código patrocinio -->
        <label class="form-group" for="codigoPatrocinio">
          <span class="label-text">Código de Patrocinio *</span>
          <input id="codigoPatrocinio" name="codigoPatrocinio" type="text" maxlength="20"
                 placeholder="Código único" value="${codigoIngresado}" required />
        </label>
      </div>

      <div id="msg" class="alta-patrocinio-message" style="color:#c00;">
        ${msg}
      </div>

      <div class="alta-patrocinio-buttons">
        <button type="submit" class="btn-primary">Crear patrocinio</button>
        <button type="button" class="btn-outline"
                onclick="window.location.href='${pageContext.request.contextPath}/edicionesOrganizadas'">Cancelar</button>
      </div>

    </form>
  </section>
</main>

<script>
document.addEventListener("DOMContentLoaded", () => {
  const tipoRegistroSelect = document.getElementById('tipoRegistro');
  const formDetalles = document.getElementById('formDetalles');
  const infoTipoRegistroDiv = document.getElementById('infoTipoRegistro');
  const msg = document.getElementById('msg');
  const descTipos = document.querySelectorAll('.desc-tipo');

  const actualizarVista = () => {
    // valor seleccionado
    const seleccionado = tipoRegistroSelect.value;
    console.log("Seleccionado:", seleccionado);

    // Ocultar todas las descripciones
    descTipos.forEach(div => div.classList.add('hidden'));

    if (seleccionado && seleccionado !== "") {
      infoTipoRegistroDiv.classList.remove('hidden');
      formDetalles.classList.remove('hidden');

      const divMostrar = document.getElementById("desc-" + seleccionado);
      console.log("Buscando:", "desc-" + seleccionado, "Encontrado:", divMostrar);

      if (divMostrar) {
        divMostrar.classList.remove('hidden');
        console.log("Mostrando:", divMostrar.id);
      }
    } else {
      infoTipoRegistroDiv.classList.add('hidden');
      formDetalles.classList.add('hidden');
    }

    msg.textContent = '';
  };

  // Al cargar la página → si hay selección previa, mostrar
  actualizarVista();

  // Al cambiar selección
  tipoRegistroSelect.addEventListener('change', actualizarVista);

  // Validación antes de enviar
  document.getElementById('formPatrocinio').addEventListener('submit', e => {
    e.preventDefault();
    msg.style.color = '#c00';
    msg.textContent = '';

    const form = e.target;
    if (!form.checkValidity()) {
      form.reportValidity();
      msg.textContent = 'Por favor complete todos los campos correctamente.';
      return;
    }

    const tipoRegistro = tipoRegistroSelect.value;
    const aporte = parseFloat(form.aporteEconomico.value);
    const registrosGratis = parseInt(form.registrosGratuitos.value);

    const costos = {
      <c:forEach var="tipo" items="${tiposRegistro}" varStatus="status">
        "${tipo.nombre}": ${tipo.costo}<c:if test="${!status.last}">,</c:if>
      </c:forEach>
    };

    const costoRegistro = costos[tipoRegistro];
    if (registrosGratis * costoRegistro > aporte * 0.20) {
      msg.textContent = '⚠️ El costo de los registros gratuitos supera el 20% del aporte económico.';
      return;
    }

    form.submit();
  });
});
</script>


</body>
</html>




