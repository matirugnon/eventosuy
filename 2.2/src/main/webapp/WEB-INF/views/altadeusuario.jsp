<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">

<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Alta de usuario · eventos.uy</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css" />
  <link rel="preconnect" href="https://fonts.googleapis.com" />
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
  <link
    href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap"
    rel="stylesheet" />
</head>

<body>
  <div>
    <header class="header">
      <h1><a href="${pageContext.request.contextPath}/inicio" style="color: inherit; text-decoration: none;">eventos.uy</a></h1>
  
      <div>
        <a href="${pageContext.request.contextPath}/login" style="color: white; text-decoration: none; font-weight: 600; padding: 0.5rem 1rem; border-radius: 6px; transition: background-color 0.2s;" onmouseover="this.style.backgroundColor='rgba(255,255,255,0.1)'" onmouseout="this.style.backgroundColor='transparent'">Iniciar sesión</a>
        <a href="${pageContext.request.contextPath}/signup" style="color: white; text-decoration: none; font-weight: 600; padding: 0.5rem 1rem; border-radius: 6px; transition: background-color 0.2s;" onmouseover="this.style.backgroundColor='rgba(255,255,255,0.1)'" onmouseout="this.style.backgroundColor='transparent'">Registrarse</a>
      </div>
    </header>

    <div class="content single">
      <main>
        <section class="auth-container">
          <div class="auth-card">
            <h2>Alta de usuario</h2>
            
            <!-- Mostrar mensaje de error si existe -->
            <c:if test="${not empty error}">
              <div id="msg" style="color:#c00; margin-bottom:1rem; padding:0.5rem; background:#ffe6e6; border-radius:4px;">
                ${error}
              </div>
            </c:if>
            
            <!-- Mostrar mensaje de éxito si existe -->
            <c:if test="${not empty mensaje}">
              <div id="msg" style="color:#2a7f2e; margin-bottom:1rem; padding:0.5rem; background:#e6ffe6; border-radius:4px;">
                ${mensaje}
              </div>
            </c:if>

            <!-- Formulario que se envía al servlet -->
            <form id="formAlta" action="${pageContext.request.contextPath}/signup" method="post" 
                  class="auth-form" autocomplete="on" novalidate enctype="multipart/form-data">
              
              <!-- Rol -->
              <div class="form-group">
                <span class="label-text">Rol</span>
                <div style="display:flex; gap:1rem;">
                  <label class="row">
                    <input type="radio" name="rol" value="asistente" 
                           ${param.rol == 'organizador' ? '' : 'checked'}> Asistente
                  </label>
                  <label class="row">
                    <input type="radio" name="rol" value="organizador" 
                           ${param.rol == 'organizador' ? 'checked' : ''}> Organizador
                  </label>
                </div>
              </div>

            <label class="input-group">
              <span class="label-text">Nickname *</span>
              <input name="nickname" required maxlength="30" placeholder="nickname" 
                     value="${param.nickname}" />
            </label>

            <label class="input-group">
              <span class="label-text">Nombre *</span>
              <input name="nombre" required maxlength="60" placeholder="Nombre" 
                     value="${param.nombre}" />
            </label>

            <label class="input-group">
              <span class="label-text">Correo electrónico *</span>
              <input name="email" type="email" required placeholder="name@example.com" 
                     value="${param.email}" />
            </label>

            <div class="grid-2">
              <label class="input-group">
                <span class="label-text">Contraseña *</span>
                <input name="password" type="password" required minlength="6" autocomplete="new-password" />
              </label>
              <label class="input-group">
                <span class="label-text">Confirmar contraseña *</span>
                <input name="confirm" type="password" required minlength="6" autocomplete="new-password" />
              </label>
            </div>

            <label class="input-group">
              <span class="label-text">Imagen (opcional)</span>
              <input name="imagen" type="file" accept="image/*" />
            </label>

            <!--                Específico Asistente                 -->
            <div id="bloqueAsistente" style="margin-top:1rem;">
              <h3 style="color:#182080; font-size:1rem;">Datos de asistente</h3>
              <div style="display:grid; grid-template-columns:1fr 1fr; gap:.75rem; margin-top:.5rem;">
                <label class="input-group">
                  <span class="label-text">Apellido *</span>
                  <input name="apellido" maxlength="60" placeholder="Apellido" 
                         value="${param.apellido}" />
                </label>
                <label class="input-group">
                  <span class="label-text">Fecha de nacimiento *</span>
                  <input name="fechaNacimiento" type="date" 
                         value="${param.fechaNacimiento}" />
                </label>
              </div>
              <label class="input-group">
                <input type="checkbox" id="chkInstitucion" 
                       ${not empty param.institucion ? 'checked' : ''} />
                <span style="margin-left:.35rem;">Pertenezco a una institución</span>
              </label>
              <div id="bloqueInstitucion" style="display:${not empty param.institucion ? 'block' : 'none'}; margin-top:.5rem;">
                <label class="input-group">
                  <span class="label-text">Institución</span>
                  <select name="institucion">
                    <option value="">Seleccionar…</option>
                    <c:choose>
                      <c:when test="${not empty instituciones}">
                        <c:forEach var="inst" items="${instituciones}">
                          <option value="${inst}" ${param.institucion == inst ? 'selected' : ''}>${inst}</option>
                        </c:forEach>
                      </c:when>
                      <c:otherwise>
                        <!-- Fallback en caso de que no se carguen desde el servidor -->
                        <option value="Udelar" ${param.institucion == 'Udelar' ? 'selected' : ''}>Udelar</option>
                        <option value="ORT" ${param.institucion == 'ORT' ? 'selected' : ''}>Universidad ORT</option>
                        <option value="UM" ${param.institucion == 'UM' ? 'selected' : ''}>Universidad de Montevideo</option>
                      </c:otherwise>
                    </c:choose>
                  </select>
                </label>
              </div>
            </div>
            
            <!--                   Específico Organizador                 -->
            <div id="bloqueOrganizador" style="display:none; margin-top:1rem;">
              <h3 style="color:#182080; font-size:1rem;">Datos de organizador</h3>
              <label class="input-group" style="margin-top:.5rem;">
                <span class="label-text">Descripción (opcional)</span>
                <textarea name="descripcion" rows="3" maxlength="500"
                  placeholder="Somos una organización de…">${param.descripcion}</textarea>
              </label>
              <label class="input-group">
                <span class="label-text">Sitio web (opcional)</span>
                <input name="web" type="url" placeholder="https://mi-sitio.org" 
                       value="${param.web}" />
              </label>
            </div>

            <div id="msg" style="color:#c00; margin-top:.4rem; min-height:1.25rem;"></div>

            <button type="submit" class="btn-primary" style="margin-top:1rem; width:100%;">Crear cuenta</button>
            <div style="text-align:center; margin-top:1.5rem;">
              <a class="btn-outline" href="${pageContext.request.contextPath}/">Cancelar</a>
            </div>
          </form>
        </div>
      </section>
    </main>
  </div>

  <footer></footer>
  <style>
    .auth-card { 
      width: 760px; 
      max-width: 95%; 
      margin: 0 auto;
      background: #fff;
      padding: 2rem;
      border-radius: 12px;
      box-shadow: 0 4px 12px rgba(24, 32, 128, 0.08);
    }

    form {
      display: flex;
      flex-direction: column;
      gap: 1rem;
    }

    .input-group {
      display: flex;
      flex-direction: column;
    }
    
    .grid-2 {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 0.75rem;
    }
  </style>
  <script>
    (function () {
      const $ = s => document.querySelector(s);
      const $$ = s => document.querySelectorAll(s);

      const form = $('#formAlta');
      const rolRadios = $$('input[name="rol"]');
      const bloqueAs = $('#bloqueAsistente');
      const bloqueOr = $('#bloqueOrganizador');
      const chkInst = $('#chkInstitucion');
      const bloqueInst = $('#bloqueInstitucion');
      const inputImg = form.elements['imagen'];
      const msg = $('#msg');

      function toggleRol() {
        const rol = [...rolRadios].find(r => r.checked)?.value;
        const esAs = rol === 'asistente';
        bloqueAs.style.display = esAs ? '' : 'none';
        bloqueOr.style.display = esAs ? 'none' : '';
        // requeridos según rol
        form.elements['apellido'].required = esAs;
        form.elements['fechaNacimiento'].required = esAs;
      }

      function toggleInstitucion() {
        bloqueInst.style.display = chkInst.checked ? '' : 'none';
      }

      function validarPasswords() {
        const p1 = form.elements['password'].value.trim();
        const p2 = form.elements['confirm'].value.trim();
        return p1.length >= 6 && p1 === p2;
      }

      function validar() {
        msg.textContent = '';
        // validaciones HTML5
        if (!form.reportValidity()) return false;
        // contraseñas
        if (!validarPasswords()) { 
          msg.textContent = 'Las contraseñas no coinciden o son muy cortas (mínimo 6).'; 
          return false; 
        }
        return true;
      }

      // Eventos
      rolRadios.forEach(r => r.addEventListener('change', toggleRol));
      chkInst && chkInst.addEventListener('change', toggleInstitucion);
      
      form.addEventListener('submit', e => {
        if (!validar()) {
          e.preventDefault();
          return false;
        }
        // Si las validaciones pasan, el formulario se enviará al servidor
      });

      // init - preservar estado en caso de error
      toggleRol();
      toggleInstitucion();
    })();
  </script>
</body>

</html>