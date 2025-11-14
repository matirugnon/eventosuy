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

        .details dl {
            display: grid;
            grid-template-columns: 150px 1fr;
            row-gap: 0.6rem;
            column-gap: 1.5rem;
        }

        .details dt {
            font-weight: 600;
            color: #182080;
        }

        .details dd {
            margin: 0;
            color: #333;
            word-break: break-word;
        }

        .logs h2 {
            font-size: 1.05rem;
            color: #182080;
            margin-bottom: 0.8rem;
        }

        pre {
            background: #0f111a;
            color: #d7ffe9;
            font-family: 'JetBrains Mono', 'SFMono-Regular', Consolas, monospace;
            padding: 1.1rem;
            border-radius: 12px;
            max-height: 340px;
            overflow: auto;
            font-size: 0.9rem;
            line-height: 1.5;
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
                Algo inesperado ocurrió en el servidor. Debajo vas a encontrar información técnica y el log detallado para diagnosticarlo.
            </p>

            <section class="details">
                <h2>📋 Información de la solicitud</h2>
                <dl>
                    <dt>URL</dt>
                    <dd><%= request.getRequestURI() %></dd>
                    <dt>Método</dt>
                    <dd><%= request.getMethod() %></dd>
                    <dt>Mensaje</dt>
                    <dd><%= (exception != null && exception.getMessage() != null) ? exception.getMessage() : "Sin mensaje disponible" %></dd>
                </dl>
            </section>

            <section class="logs">
                <h2>🧾 Stack trace</h2>
                <pre><%
                    if (exception != null) {
                        java.io.StringWriter sw = new java.io.StringWriter();
                        exception.printStackTrace(new java.io.PrintWriter(sw));
                        out.print(sw.toString());
                    } else {
                        out.print("No se recibió información detallada del error.");
                    }
                %></pre>
            </section>

            <div class="actions">
                <a href="javascript:history.back()" class="btn btn-outline">← Volver</a>
                <a href="<%= request.getContextPath() %>/inicio" class="btn btn-primary">Ir al inicio</a>
            </div>

            <p class="timestamp">
                Generado: <%= new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date()) %>
            </p>
        </article>
    </main>
</body>
</html>
