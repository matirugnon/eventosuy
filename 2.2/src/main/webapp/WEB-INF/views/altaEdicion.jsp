<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Alta de Edición · eventos.uy</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap" rel="stylesheet">
    <style>
        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1rem;
        }
        @media (max-width: 640px) {
            .form-grid {
                grid-template-columns: 1fr;
            }
        }
        .form-group {
            display: flex;
            flex-direction: column;
            gap: 0.35rem;
            margin-bottom: 1rem;
        }
        .form-group label {
            font-weight: 600;
            color: #333;
        }
        .form-group input,
        .form-group select,
        .form-group textarea {
            width: 100%;
            padding: 0.75rem 1rem;
            border: 1px solid #ddd;
            border-radius: 12px;
            font-size: 1rem;
            font-family: inherit;
            box-sizing: border-box;
            transition: border-color 0.2s, box-shadow 0.2s;
        }
        .form-group input:focus,
        .form-group select:focus,
        .form-group textarea:focus {
            border-color: #182080;
            outline: none;
            box-shadow: 0 0 0 2px rgba(24, 32, 128, 0.1);
        }
        .preview-container {
            margin-top: 0.5rem;
            display: none;
        }
        .preview-img {
            max-width: 200px;
            max-height: 150px;
            border-radius: 8px;
            border: 1px solid #ddd;
        }
        .error-message {
            color: #dc3545;
            background: #f8d7da;
            border: 1px solid #f5c6cb;
            padding: 0.75rem;
            border-radius: 8px;
            margin-bottom: 1rem;
        }
        .success-message {
            color: #155724;
            background: #d4edda;
            border: 1px solid #c3e6cb;
            padding: 0.75rem;
            border-radius: 8px;
            margin-bottom: 1rem;
        }
        .note {
            color: #6c757d;
            font-size: 0.9rem;
            font-style: italic;
        }
    </style>
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
                        <a href="${pageContext.request.contextPath}/signup" class="btn-primary">Registrarse</a>
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
                        <li><a href="#">Alta Institución</a></li>
                        <li><a href="#">Alta Tipo de Registro</a></li>
                        <li><a href="#">Alta Patrocinio</a></li>
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

            <main>
                <section class="panel">
                    <div class="panel-body">
                        <h2 style="margin: 0 0 1.5rem 0; color: #182080;">Alta de Edición de Evento</h2>

                        <!-- Mostrar mensaje de error si existe -->
                        <c:if test="${not empty error}">
                            <div class="error-message">
                                ${error}
                            </div>
                        </c:if>

                        <!-- Mostrar mensaje de éxito si existe -->
                        <c:if test="${not empty mensaje}">
                            <div class="success-message">
                                ${mensaje}
                            </div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/altaEdicion" method="post" enctype="multipart/form-data" id="formEdicion">
                            <!-- Evento y Nombre -->
                            <div class="form-grid">
                                <div class="form-group">
                                    <label for="evento">Evento *</label>
                                    <select id="evento" name="evento" required>
                                        <option value="">Seleccionar evento...</option>
                                        <c:forEach var="evento" items="${eventos}">
                                            <option value="${evento}" 
                                                    <c:if test="${param.evento == evento}">selected</c:if>>
                                                ${evento}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label for="nombre">Nombre de la edición *</label>
                                    <input type="text" id="nombre" name="nombre" 
                                           value="${param.nombre}" 
                                           required maxlength="140" 
                                           placeholder="ej. Montevideo Comics 2025">
                                </div>
                            </div>

                            <!-- Sigla y Ciudad -->
                            <div class="form-grid">
                                <div class="form-group">
                                    <label for="sigla">Sigla *</label>
                                    <input type="text" id="sigla" name="sigla" 
                                           value="${param.sigla}" 
                                           required maxlength="20" 
                                           placeholder="ej. COMICS25">
                                </div>

                                <div class="form-group">
                                    <label for="ciudad">Ciudad *</label>
                                    <input type="text" id="ciudad" name="ciudad" 
                                           value="${param.ciudad}" 
                                           required maxlength="60" 
                                           placeholder="ej. Montevideo">
                                </div>
                            </div>

                            <!-- País -->
                            <div class="form-grid">
                                <div class="form-group">
                                    <label for="pais">País *</label>
                                    <input type="text" id="pais" name="pais" 
                                           value="${param.pais}" 
                                           required maxlength="60" 
                                           placeholder="ej. Uruguay">
                                </div>
                                <div></div>
                            </div>

                            <!-- Fechas -->
                            <div class="form-grid">
                                <div class="form-group">
                                    <label for="fechaInicio">Fecha de inicio *</label>
                                    <input type="date" id="fechaInicio" name="fechaInicio" 
                                           value="${param.fechaInicio}" 
                                           required>
                                </div>

                                <div class="form-group">
                                    <label for="fechaFin">Fecha de fin *</label>
                                    <input type="date" id="fechaFin" name="fechaFin" 
                                           value="${param.fechaFin}" 
                                           required>
                                </div>
                            </div>

                            <!-- Imagen -->
                            <div class="form-group">
                                <label for="imagen">Imagen de la edición (opcional)</label>
                                <input type="file" id="imagen" name="imagen" accept="image/*">
                                <div class="preview-container" id="previewContainer">
                                    <img id="previewImg" class="preview-img" alt="Vista previa">
                                </div>
                            </div>

                            <!-- Botones -->
                            <div style="display: flex; gap: 0.75rem; margin-top: 2rem;">
                                <button type="submit" class="btn-primary">Crear edición</button>
                                <a href="${pageContext.request.contextPath}/inicio" class="btn-outline">Cancelar</a>
                            </div>

                            <p class="note" style="margin-top: 1rem;">La edición se da de alta en estado "Ingresada".</p>
                        </form>
                    </div>
                </section>
            </main>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('formEdicion');
            const imagenInput = document.getElementById('imagen');
            const previewContainer = document.getElementById('previewContainer');
            const previewImg = document.getElementById('previewImg');
            const fechaInicio = document.getElementById('fechaInicio');
            const fechaFin = document.getElementById('fechaFin');

            // Vista previa de imagen
            imagenInput.addEventListener('change', function(e) {
                const file = e.target.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = function(e) {
                        previewImg.src = e.target.result;
                        previewContainer.style.display = 'block';
                    };
                    reader.readAsDataURL(file);
                } else {
                    previewContainer.style.display = 'none';
                }
            });

            // Validación de fechas
            function validarFechas() {
                if (fechaInicio.value && fechaFin.value) {
                    const inicio = new Date(fechaInicio.value);
                    const fin = new Date(fechaFin.value);
                    
                    if (fin < inicio) {
                        fechaFin.setCustomValidity('La fecha de fin debe ser posterior a la fecha de inicio');
                    } else {
                        fechaFin.setCustomValidity('');
                    }
                }
            }

            fechaInicio.addEventListener('change', validarFechas);
            fechaFin.addEventListener('change', validarFechas);

            // Validación del formulario
            form.addEventListener('submit', function(e) {
                validarFechas();
                
                if (!form.checkValidity()) {
                    e.preventDefault();
                    e.stopPropagation();
                }
            });
        });
    </script>
</body>
</html>