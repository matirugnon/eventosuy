<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Error 500 | Sistema de Gestión de Eventos</title>
<style>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap');

:root {
color-scheme: light;
}

* {
margin: 0;
padding: 0;
box-sizing: border-box;
}

body {
font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
background: #f5f5f5;
min-height: 100vh;
display: flex;
flex-direction: column;
}

header {
background: #182080;
color: #fff;
padding: 2rem clamp(1.5rem, 4vw, 4rem);
box-shadow: 0 2px 6px rgba(0, 0, 0, 0.12);
}

header h1 {
font-size: clamp(1.6rem, 3vw, 2rem);
font-weight: 600;
}

main {
flex: 1;
width: min(960px, 92vw);
margin: 2.5rem auto;
}

.card {
background: #fff;
border-radius: 18px;
padding: clamp(2rem, 4vw, 3rem);
box-shadow: 0 30px 70px rgba(12, 20, 58, 0.12);
}

.code {
font-size: clamp(3.5rem, 7vw, 5.5rem);
font-weight: 700;
color: #182080;
letter-spacing: -2px;
}

.title {
font-size: clamp(1.4rem, 3vw, 2rem);
font-weight: 600;
margin: 0.5rem 0 0.25rem;
color: #111;
}

.message {
color: #555;
line-height: 1.65;
margin-bottom: 1.25rem;
}

.details {
margin: 1.5rem 0 2rem;
background: #f8f9ff;
border-left: 4px solid #182080;
border-radius: 12px;
padding: 1.4rem 1.6rem;
}

.details h2 {
font-size: 1.05rem;
color: #182080;
margin-bottom: 0.85rem;
}

.details ul {
list-style: none;
display: flex;
flex-direction: column;
gap: 0.75rem;
padding: 0;
margin: 0;
}

.details li {
display: flex;
align-items: flex-start;
gap: 0.6rem;
color: #333;
line-height: 1.5;
}

.details li::before {
content: "•";
color: #182080;
font-weight: 700;
}

.actions {
margin-top: 2rem;
display: flex;
flex-wrap: wrap;
gap: 0.9rem;
}

.btn {
border: none;
border-radius: 10px;
padding: 0.85rem 1.5rem;
font-size: 0.95rem;
font-weight: 600;
text-decoration: none;
cursor: pointer;
transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.btn-primary {
background: #182080;
color: #fff;
}

.btn-primary:hover {
transform: translateY(-2px);
box-shadow: 0 12px 24px rgba(24, 32, 128, 0.22);
}

.btn-outline {
background: transparent;
color: #182080;
border: 1px solid #182080;
}

.btn-outline:hover {
background: rgba(24, 32, 128, 0.08);
}

.timestamp {
margin-top: 1.6rem;
color: #888;
font-size: 0.9rem;
}

@media (max-width: 640px) {
.details dl {
grid-template-columns: 1fr;
}
}
</style>
</head>
<body>
<header>
<h1>Sistema de Gestión de Eventos</h1>
</header>

<main>
<article class="card">
<div class="code">500</div>
<p class="title">Error interno del servidor</p>
<p class="message">
Algo inesperado ocurrió y no pudimos completar tu solicitud. El equipo ya recibe un aviso con lo sucedido.
</p>

<section class="details">
<h2>¿Qué podés hacer?</h2>
<ul>
	<li>Volver a intentar la acción dentro de unos segundos.</li>
	<li>Regresar a la pantalla anterior y verificar si los datos se guardaron.</li>
	<li>Si el problema continúa, escribinos a soporte indicando la hora: <strong><%= new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date()) %></strong>.</li>
</ul>
</section>

<div class="actions">
<a href="javascript:history.back()" class="btn btn-outline">← Volver</a>
<a href="<%= request.getContextPath() %>/inicio" class="btn btn-primary">Ir al inicio</a>
</div>

<p class="timestamp">
Código: 500 · <%= request.getRequestURI() %>
</p>
</article>
</main>
</body>
</html>