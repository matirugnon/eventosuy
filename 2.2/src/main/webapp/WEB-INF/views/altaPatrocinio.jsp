<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Alta Patrocinio · eventos.uy</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

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
            <option value="${tipo.nombre}">${tipo.nombre}</option>
          </c:forEach>
        </select>
      </label>

      <!-- Info tipo registro -->
      <div id="infoTipoRegistro" class="info-box hidden">
        <c:forEach var="tipo" items="${tiposRegistro}">
          <div id="desc-${tipo.nombre}" class="desc-tipo hidden" style="white-space: pre-wrap;">
            <p><strong>Descripción:</strong> ${tipo.descripcion}</p>
            <p><strong>Costo:</strong> ${tipo.costo} UYU</p>
            <p><strong>Cupo:</strong> ${tipo.cupo}</p>
          </div>
        </c:forEach>
      </div>

      <!-- Todo lo demás oculto inicialmente -->
      <div id="formDetalles" class="hidden">

        <!-- Institución -->
        <label class="form-group" for="institucion">
          <span class="label-text">Institución *</span>
          <select id="institucion" name="institucion" required>
            <option value="">Seleccione una institución…</option>
            <c:forEach var="inst" items="${instituciones}">
              <option value="${inst}">${inst}</option>
            </c:forEach>
          </select>
        </label>

        <!-- Nivel patrocinio -->
        <label class="form-group" for="nivelPatrocinio">
          <span class="label-text">Nivel de Patrocinio *</span>
          <select id="nivelPatrocinio" name="nivelPatrocinio" required>
            <option value="">Seleccione nivel…</option>
            <option value="platino">Platino</option>
            <option value="oro">Oro</option>
            <option value="plata">Plata</option>
            <option value="bronce">Bronce</option>
          </select>
        </label>

        <!-- Aporte económico -->
        <label class="form-group" for="aporteEconomico">
          <span class="label-text">Aporte Económico (UYU) *</span>
          <input id="aporteEconomico" name="aporteEconomico" type="number" min="0" step="0.01"
                 placeholder="Ej. 10000" required />
        </label>

        <!-- Registros gratuitos -->
        <label class="form-group" for="registrosGratuitos">
          <span class="label-text">Cantidad de registros gratuitos *</span>
          <input id="registrosGratuitos" name="registrosGratuitos" type="number" min="0" step="1"
                 placeholder="Ej. 10" required />
        </label>

        <!-- Código patrocinio -->
        <label class="form-group" for="codigoPatrocinio">
          <span class="label-text">Código de Patrocinio *</span>
          <input id="codigoPatrocinio" name="codigoPatrocinio" type="text" maxlength="20"
                 placeholder="Código único" required />
        </label>
      </div>

      <div id="msg" class="alta-patrocinio-message" style="color:#c00;"></div>

      <div class="alta-patrocinio-buttons">
        <button type="submit" class="btn-primary">Crear patrocinio</button>
        <button type="button" class="btn-outline"
                onclick="window.location.href='${pageContext.request.contextPath}/edicionesOrganizadas'">Cancelar</button>
      </div>

    </form>
  </section>
</main>

<script>
(() => {
  const tipoRegistroSelect = document.getElementById('tipoRegistro');
  const formDetalles = document.getElementById('formDetalles');
  const infoTipoRegistroDiv = document.getElementById('infoTipoRegistro');
  const msg = document.getElementById('msg');
  const descTipos = document.querySelectorAll('.desc-tipo');

  tipoRegistroSelect.addEventListener('change', () => {
    const seleccionado = tipoRegistroSelect.value;

    // Oculto todo por defecto
    descTipos.forEach(div => div.classList.add('hidden'));

    if (seleccionado) {
      // Mostrar info general y el div específico
      infoTipoRegistroDiv.classList.remove('hidden');
      formDetalles.classList.remove('hidden');

      const divMostrar = document.getElementById(`desc-${seleccionado}`);
      if (divMostrar) divMostrar.classList.remove('hidden');
    } else {
      infoTipoRegistroDiv.classList.add('hidden');
      formDetalles.classList.add('hidden');
    }

    msg.textContent = '';
  });

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
})();
</script>

</body>
</html>

