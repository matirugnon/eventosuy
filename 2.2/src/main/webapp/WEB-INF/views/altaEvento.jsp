<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Alta de Evento · eventos.uy</title>
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
        .form-group textarea:focus {
            border-color: #182080;
            outline: none;
            box-shadow: 0 0 0 2px rgba(24, 32, 128, 0.1);
        }
        .form-group textarea {
            resize: vertical;
            min-height: 100px;
        }
        .categorias-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
            gap: 0.75rem;
            margin-top: 0.5rem;
        }
        .categoria-item {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            padding: 0.5rem;
            background: #f8f9fa;
            border-radius: 8px;
            border: 1px solid #e9ecef;
            transition: all 0.2s;
        }
        .categoria-item:hover {
            background: #e9ecef;
        }
        .categoria-item input[type="checkbox"] {
            width: auto;
            margin: 0;
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
                        <li><a href="#">Alta Edición</a></li>
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
                        <h2 style="margin: 0 0 1.5rem 0; color: #182080;">Alta de Evento</h2>

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

                        <form action="${pageContext.request.contextPath}/altaEvento" method="post" enctype="multipart/form-data" id="formEvento">
                            <!-- Campos básicos -->
                            <div class="form-grid">
                                <div class="form-group">
                                    <label for="nombre">Nombre del evento *</label>
                                    <input type="text" id="nombre" name="nombre" 
                                           value="${param.nombre}" 
                                           required maxlength="120" 
                                           placeholder="ej. Concierto de Rock">
                                </div>

                                <div class="form-group">
                                    <label for="sigla">Sigla *</label>
                                    <input type="text" id="sigla" name="sigla" 
                                           value="${param.sigla}" 
                                           required maxlength="15" 
                                           placeholder="ej. CR2025">
                                </div>
                            </div>

                            <!-- Descripción -->
                            <div class="form-group">
                                <label for="descripcion">Descripción *</label>
                                <textarea id="descripcion" name="descripcion" 
                                          required maxlength="800" 
                                          placeholder="Breve descripción del evento...">${param.descripcion}</textarea>
                            </div>

                            <!-- Categorías -->
                            <div class="form-group">
                                <label>Categorías (selecciona al menos una) *</label>
                                <div class="categorias-grid">
                                    <c:forEach var="categoria" items="${categorias}">
                                        <div class="categoria-item">
                                            <input type="checkbox" 
                                                   id="cat_${categoria}" 
                                                   name="categorias" 
                                                   value="${categoria}"
                                                   <c:if test="${paramValues.categorias != null}">
                                                       <c:forEach var="selected" items="${paramValues.categorias}">
                                                           <c:if test="${selected == categoria}">checked</c:if>
                                                       </c:forEach>
                                                   </c:if>>
                                            <label for="cat_${categoria}">${categoria}</label>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>

                            <!-- Imagen -->
                            <div class="form-group">
                                <label for="imagen">Imagen del evento (opcional)</label>
                                <input type="file" id="imagen" name="imagen" accept="image/*">
                                <div class="preview-container" id="previewContainer">
                                    <img id="previewImg" class="preview-img" alt="Vista previa">
                                </div>
                            </div>

                            <!-- Botones -->
                            <div style="display: flex; gap: 0.75rem; margin-top: 2rem;">
                                <button type="submit" class="btn-primary">Crear evento</button>
                                <a href="${pageContext.request.contextPath}/inicio" class="btn-outline">Cancelar</a>
                            </div>
                        </form>
                    </div>
                </section>
            </main>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('formEvento');
            const imagenInput = document.getElementById('imagen');
            const previewContainer = document.getElementById('previewContainer');
            const previewImg = document.getElementById('previewImg');

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

            // Validación del formulario
            form.addEventListener('submit', function(e) {
                const categorias = document.querySelectorAll('input[name="categorias"]:checked');
                if (categorias.length === 0) {
                    e.preventDefault();
                    alert('Debe seleccionar al menos una categoría');
                    return false;
                }
            });
        });
    </script>
</body>
</html>