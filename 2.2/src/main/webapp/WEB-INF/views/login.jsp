<!-- filepath: c:\Users\facu3\git\tpgr15\2.2\src\main\webapp\WEB-INF\views\login.jsp -->
<!DOCTYPE html>
<html lang="es">

<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Iniciar sesi�n  eventos.uy</title>
  <link rel="icon" type="image/png" href="img/favicon.png">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
  <link rel="preconnect" href="https://fonts.googleapis.com" />
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
  <link
    href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap"
    rel="stylesheet" />
</head>

<body>
  <div>
    <header class="header">
      <h1><a href="<%= request.getContextPath() %>/inicio">eventos.uy</a></h1>

      <div>

       <a href="${pageContext.request.contextPath}/signup">Registrarse</a>
      </div>
    </header>

    <div class="content single">
      <main>
        <section class="auth-container">
          <div class="auth-card">
            <h2>Iniciar sesi�n</h2>

            <form action="<%= request.getContextPath() %>/login" method="POST" class="auth-form" autocomplete="on">
              <label class="form-group">
                <span class="label-text">Correo o nombre de usuario</span>
                <input type="text" name="usuario" required />
              </label>

              <label class="form-group">
                <span class="label-text">Contrase�a</span>
                <input type="password" name="password" required />
              </label>

              <button type="submit" class="btn-primary" style="margin-top:1rem;width:100%;">Iniciar sesi�n</button>
            </form>

            <c:if test="${not empty error}">
              <p style="color: red; margin-top: 1rem;">${error}</p>
            </c:if>
          </div>
        </section>
      </main>
    </div>
  </div>
  <style>
    .form-group {
      display: flex;
      flex-direction: column;
      margin-bottom: 1rem;
    }
  </style>
</body>

</html>