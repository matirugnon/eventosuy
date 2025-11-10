package config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import com.itextpdf.io.exceptions.IOException;

public class Config {
   private static Properties props = new Properties();
   static {
       String home = System.getProperty("user.home");
       try (FileInputStream fis = new FileInputStream(home + "/config/config.properties")) {
           props.load(fis);
       } catch (IOException e) {
           System.out.println("No se pudo cargar la configuración: " + e.getMessage());
       } catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (java.io.IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
   }

   // Obtener la configuración del publicador
   public static String getPublisherHost(String publisher) {
       return props.getProperty(publisher + ".host");
   }

   public static int getPublisherPort(String publisher) {
       return Integer.parseInt(props.getProperty(publisher + ".port"));
   }

   public static String getPublisherUrl(String publisher) {
       return props.getProperty(publisher + ".url");
   }

   public static String get(String clave) {
       return props.getProperty(clave);
   }
}
