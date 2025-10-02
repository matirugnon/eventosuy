<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Error · eventos.uy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <div class="container">
        <header class="header">
            <div class="nav-brand">
                <h1>eventos.uy</h1>
            </div>
            <div class="nav-links">
                <c:choose>
                    <c:when test="${sessionScope.usuario != null}">
                        <span class="nav-user">Hola, ${sessionScope.usuario}</span>
                        <a href="${pageContext.request.contextPath}/inicio" class="nav-link">Inicio</a>
                        <a href="${pageContext.request.contextPath}/miPerfil" class="nav-link">Mi Perfil</a>
                        <a href="${pageContext.request.contextPath}/logout" class="nav-link">Cerrar Sesión</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/inicio" class="nav-link">Inicio</a>
                        <a href="${pageContext.request.contextPath}/login" class="nav-link">Iniciar Sesión</a>
                        <a href="${pageContext.request.contextPath}/altaUsuario" class="nav-link">Registrarse</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </header>

        <main class="content">
            <div class="auth-container">
                <div class="auth-form">
                    <div class="auth-header">
                        <h2>Error</h2>
                        <p class="auth-subtitle">Se ha producido un error inesperado</p>
                    </div>
                    
                    <div class="error-content">
                        <c:if test="${not empty error}">
                            <div class="alert alert-error">
                                <strong>Error:</strong> ${error}
                            </div>
                        </c:if>
                        
                        <c:if test="${empty error}">
                            <div class="alert alert-error">
                                <strong>Error:</strong> Se ha producido un error inesperado. Por favor, inténtalo de nuevo.
                            </div>
                        </c:if>
                        
                        <div class="form-actions">
                            <a href="javascript:history.back()" class="btn-secondary">Volver</a>
                            <a href="${pageContext.request.contextPath}/inicio" class="btn-primary">Ir al Inicio</a>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</body>
</html>