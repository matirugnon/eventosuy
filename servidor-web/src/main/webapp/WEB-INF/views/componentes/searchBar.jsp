<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="searchValue"
    value="${empty param.busqueda ? (empty busqueda ? '' : busqueda) : param.busqueda}" />
<c:set var="tipoValue"
    value="${empty param.tipo ? (empty tipo ? 'todos' : tipo) : param.tipo}" />
<c:set var="ordenValue"
    value="${empty param.orden ? (empty orden ? 'fechaDesc' : orden) : param.orden}" />
<div style="padding: 0.75rem 1.5rem; border-bottom: 1px solid #e3e3e3; background: #fff;">
    <form action="${pageContext.request.contextPath}/inicio" method="get"
        style="margin: 0 auto; max-width: 1200px; display: flex; gap: 0.5rem; flex-wrap: wrap; align-items: center;">
        <label for="globalSearchInput" style="position: absolute; left: -9999px;">Buscar eventos o ediciones</label>
        <input id="globalSearchInput" name="busqueda" type="text" maxlength="140"
            value="${fn:escapeXml(searchValue)}" placeholder="Buscar evento o edición..."
            style="padding: 0.5rem 1rem; flex: 2; min-width: 200px; border-radius: 6px; border: 1px solid #ccc;"
            aria-label="Buscar eventos o ediciones" />

        <select name="tipo"
            style="padding: 0.5rem; border-radius: 6px; border: 1px solid #ccc; min-width: 140px;">
            <option value="todos" ${tipoValue == 'todos' ? 'selected' : ''}>Todos</option>
            <option value="eventos" ${tipoValue == 'eventos' ? 'selected' : ''}>Solo eventos</option>
            <option value="ediciones" ${tipoValue == 'ediciones' ? 'selected' : ''}>Solo ediciones</option>
        </select>

        <select name="orden"
            style="padding: 0.5rem; border-radius: 6px; border: 1px solid #ccc; min-width: 140px;">
            <option value="nombreAsc" ${ordenValue == 'nombreAsc' ? 'selected' : ''}>Nombre A–Z</option>
            <option value="nombreDesc" ${ordenValue == 'nombreDesc' ? 'selected' : ''}>Nombre Z–A</option>
            <option value="fechaAsc" ${ordenValue == 'fechaAsc' ? 'selected' : ''}>Fecha más próxima</option>
            <option value="fechaDesc" ${ordenValue == 'fechaDesc' ? 'selected' : ''}>Fecha más lejana</option>
        </select>

        <button type="submit"
            style="padding: 0.5rem 1.25rem; border-radius: 6px; border: none; background-color: #182080; color: white; cursor: pointer;">
            Buscar</button>
    </form>
</div>
<script>
(function () {
    const form = document.querySelector('#globalSearchInput')?.closest('form');
    if (!form) return;
    const input = form.querySelector('input[name="busqueda"]');
    const fechaDesde = form.querySelector('input[name="fechaDesde"]');
    const fechaHasta = form.querySelector('input[name="fechaHasta"]');

    form.addEventListener('submit', function (event) {
        (function () {
            const form = document.querySelector('#globalSearchInput')?.closest('form');
            if (!form) return;
            const input = form.querySelector('input[name="busqueda"]');

            form.addEventListener('submit', function () {
                if (input) {
                    input.value = input.value.trim();
                }
            });
        })();
    });
