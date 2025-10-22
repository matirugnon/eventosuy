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

<div> <!-- div global abierto -->

	<!-- Header : ahora el header esta en la carpeta componentes, para que se cambie en una sola pag y sea igual para todas-->
	<jsp:include page="/WEB-INF/views/componentes/header.jsp" />

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
                <li><a href="${pageContext.request.contextPath}/inicio">Todas</a></li>
                <c:forEach items="${categorias}" var="categoria">
                  <li><a href="${pageContext.request.contextPath}/inicio?categoria=${categoria}">${categoria}</a></li>
                </c:forEach>
              </c:otherwise>
            </c:choose>
        </ul>
    </div>

    <!-- Ver listado de usuarios -->
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

<main class="alta-patrocinio-container">
<section class="panel">
  <div class="panel-header">Alta de Patrocinio / ${edicionSeleccionada}</div>

  <form id="formPatrocinio" class="alta-patrocinio-form"
        action="${pageContext.request.contextPath}/altaPatrocinio"
        method="post" novalidate>

    <input type="hidden" name="edicion" value="${edicionSeleccionada}" />

    <label class="form-group">
      <span class="label-text">Tipo de registro *</span>
      <select id="tipoRegistro" name="tipoRegistro" required>
        <option value="">Seleccionar tipo de registro…</option>
        <c:forEach var="tipo" items="${tiposRegistro}">
          <option value="${tipo.nombre}" <c:if test="${tipoRegistroSeleccionado == tipo.nombre}">selected</c:if>>${tipo.nombre}</option>
        </c:forEach>
      </select>
    </label>

    <div id="infoTipoRegistro" class="info-box hidden">
      <c:forEach var="tipo" items="${tiposRegistro}">
        <div id="desc-${tipo.nombre}" class="desc-tipo hidden" style="white-space: pre-wrap;">
          <p><strong>Descripción:</strong> ${tipo.descripcion}</p>
          <p><strong>Costo:</strong> ${tipo.costo} UYU</p>
          <p><strong>Cupo:</strong> ${tipo.cupo}</p>
        </div>
      </c:forEach>
    </div>

    <div id="formDetalles" class="hidden">
      <label class="form-group">
        <span class="label-text">Institución *</span>
        <select name="institucion" required>
          <option value="">Seleccione una institución…</option>
          <c:forEach var="inst" items="${instituciones}">
            <option value="${inst}" <c:if test="${institucionSeleccionada == inst}">selected</c:if>>${inst}</option>
          </c:forEach>
        </select>
      </label>

      <label class="form-group">
        <span class="label-text">Nivel de Patrocinio *</span>
        <select name="nivelPatrocinio" required>
          <option value="">Seleccione nivel…</option>
          <option value="Platino" <c:if test="${nivelSeleccionado == 'Platino'}">selected</c:if>>Platino</option>
          <option value="Oro" <c:if test="${nivelSeleccionado == 'Oro'}">selected</c:if>>Oro</option>
          <option value="Plata" <c:if test="${nivelSeleccionado == 'Plata'}">selected</c:if>>Plata</option>
          <option value="Bronce" <c:if test="${nivelSeleccionado == 'Bronce'}">selected</c:if>>Bronce</option>
        </select>
      </label>

      <label class="form-group">
        <span class="label-text">Aporte Económico (UYU) *</span>
        <input name="aporteEconomico" type="number" min="0" step="0.01" placeholder="Ej. 10000" value="${aporteIngresado}" required />
      </label>

      <label class="form-group">
        <span class="label-text">Cantidad de registros gratuitos *</span>
        <input name="registrosGratuitos" type="number" min="0" step="1" placeholder="Ej. 10" value="${registrosIngresados}" required />
      </label>

      <label class="form-group">
        <span class="label-text">Código de Patrocinio *</span>
        <input name="codigoPatrocinio" type="text" maxlength="20" placeholder="Código único" value="${codigoIngresado}" required />
      </label>
    </div>

 

    <div class="alta-patrocinio-buttons">
      <button type="submit" class="btn-primary">Crear patrocinio</button>
      <button type="button" class="btn-outline" onclick="window.location.href='${pageContext.request.contextPath}/edicionesOrganizadas'">Cancelar</button>
    </div>
    
     <div id="msg" class="alta-patrocinio-message">
    <c:if test="${not empty msg}">
        ${msg}
    </c:if>
	</div>

  </form>
</section>
</main>

</div> <!-- div global cerrado -->

<script>
document.addEventListener("DOMContentLoaded", () => {
  const tipoRegistroSelect = document.getElementById('tipoRegistro');
  const formDetalles = document.getElementById('formDetalles');
  const infoTipoRegistroDiv = document.getElementById('infoTipoRegistro');
  const msg = document.getElementById('msg');
  const descTipos = document.querySelectorAll('.desc-tipo');

  const actualizarVista = () => {
	    const seleccionado = tipoRegistroSelect.value;
	    descTipos.forEach(div => div.classList.add('hidden'));

	    if (seleccionado && seleccionado !== "") {
	        infoTipoRegistroDiv.classList.remove('hidden');
	        formDetalles.classList.remove('hidden');
	        const divMostrar = document.getElementById("desc-" + seleccionado);
	        if (divMostrar) divMostrar.classList.remove('hidden');
	    } else {
	        infoTipoRegistroDiv.classList.add('hidden');
	        formDetalles.classList.add('hidden');
	    }

	    // Solo limpiar mensaje si no había mensaje del servidor
	    if (!msg.textContent.trim()) {
	        msg.textContent = '';
	    }
	};

  actualizarVista();
  tipoRegistroSelect.addEventListener('change', actualizarVista);

  document.getElementById('formPatrocinio').addEventListener('submit', e => {
    const form = e.target;
    if (!form.checkValidity()) {
      e.preventDefault();
      form.reportValidity();
      msg.textContent = 'Por favor complete todos los campos correctamente.';
    }
  });
});
</script>

</body>
</html>





