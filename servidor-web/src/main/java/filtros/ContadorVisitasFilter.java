package filtros;


import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

@WebFilter("/consultaEvento")
public class ContadorVisitasFilter implements Filter {
	
	
	   @Override
	    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	            throws IOException, ServletException {
		   
		   try {
	            String nomEvento = request.getParameter("evento");

	            if (nomEvento != null && !nomEvento.isBlank()) {	              
					soap.PublicadorControlador pub = utils.SoapClientHelper.getPublicadorControlador();
					// El método incrementarVisitas puede no estar presente en los stubs del cliente
					// (dependiendo de la WSDL/cliente generado). Intentamos invocarlo por reflexión
					// si está disponible en tiempo de ejecución.
					try {
						java.lang.reflect.Method m = pub.getClass().getMethod("incrementarVisitas", String.class);
						m.invoke(pub, nomEvento);
					} catch (NoSuchMethodException nsme) {
						// Método no disponible en los stubs generados: ignorar silenciosamente
					}
	            }
	        } catch (Exception e) {
	            System.out.println("⚠ No se pudo incrementar visitas: " + e.getMessage());
	        }

	        // Continuar con la ejecución normal del servlet
	        chain.doFilter(request, response);
	    }
		   
		   
		   		   
		   
}
