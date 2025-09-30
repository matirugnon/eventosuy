<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Evento · eventos.uy</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
  <link rel="preconnect" href="https://fonts.googleapis.com" />
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
  <link href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap" rel="stylesheet" />
</head>
<body>
  <div>
    <header class="header">
      <h1><a href="${pageContext.request.contextPath}/inicio" style="color: inherit; text-decoration: none;">eventos.uy</a></h1>
     
      <div class="header-right">
        <c:choose>
          <c:when test="${not empty role}">
            <div class="user-badge" style="display: flex; align-items: center; gap: 0.5rem;">
              <img class="avatar" src="${avatar}" alt="Avatar de usuario" />
              <span class="nickname">${nickname}</span>
              <a href="${pageContext.request.contextPath}/logout" class="btn-primary">Cerrar sesión</a>
            </div>
          </c:when>
          <c:otherwise>
            <a href="${pageContext.request.contextPath}/login" class="btn-primary">Iniciar sesión</a>
            <a href="${pageContext.request.contextPath}/signup" class="btn-primary">Registrarse</a>
          </c:otherwise>
        </c:choose>
      </div>
    </header>

    <div class="content">
      <aside class="sidebar">
        <div class="panel sidebar">
          <div class="panel-header">Categorías</div>
          <ul class="menu-list">
            <c:forEach items="${categorias}" var="categoria">
              <li><a href="${pageContext.request.contextPath}/inicio?categoria=${categoria}">${categoria}</a></li>
            </c:forEach>
          </ul>
        </div>
        
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
      
      <main>
        <section class="panel">
          <div class="panel-body">
            <div class="event-detail">
              <article class="event-item" style="border-bottom:0;">
                <div class="event-image" style="margin-bottom: 1rem; max-width: 250px;">
                  <img src="${pageContext.request.contextPath}/img/eventoSinImagen.jpeg" alt="${eventoSeleccionado.evento.nombre}" style="width: 100%; height: auto; border-radius: 8px;">
                </div>
                <div>
                  <h3 style="margin:0 0 0.25rem 0;">${eventoSeleccionado.evento.nombre}</h3>
                  <p style="margin:0.25rem 0;"><strong>Descripción:</strong> ${eventoSeleccionado.evento.descripcion}</p>
                  <p style="margin:0.25rem 0;"><strong>Sigla:</strong> ${eventoSeleccionado.evento.sigla}</p>
                  <p style="margin:0.25rem 0;"><strong>Categorías:</strong> 
                    <c:forEach items="${eventoSeleccionado.evento.categorias}" var="categoria" varStatus="status">
                      ${categoria}<c:if test="${!status.last}">, </c:if>
                    </c:forEach>
                  </p>
                  <p style="margin:0.25rem 0;"><strong>Fecha Alta:</strong> 
                    ${eventoSeleccionado.evento.fechaEvento.dia}/${eventoSeleccionado.evento.fechaEvento.mes}/${eventoSeleccionado.evento.fechaEvento.anio}
                  </p>
                </div>
              </article>
              
              <c:if test="${not empty eventoSeleccionado.ediciones}">
                <div>
                  <div class="panel-header" style="border-radius:10px 10px 0 0;">Ediciones</div>
                  <div class="panel-body event-editions">
                    <c:forEach items="${eventoSeleccionado.ediciones}" var="edicion">
                      <c:url var="edicionUrl" value="/consultaEdicion">
                        <c:param name="edicion" value="${edicion}" />
                      </c:url>
                      <a class="mini-card" href="${edicionUrl}">
                        <img src="${pageContext.request.contextPath}/img/eventoSinImagen.jpeg" alt="${edicion}" />
                        <div class="mini-card-title">${edicion}</div>
                      </a>
                    </c:forEach>
                  </div>
                </div>
              </c:if>
            </div>
          </div>
        </section>
      </main>
    </div>
  </div>

  <footer></footer>
</body>
</html>