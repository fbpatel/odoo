package email;

import utilidades.Constantes;
import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mail {

    private InternetAddress[] addressTo;

    public static void enviarMailFile(File archivoAdjunto, String emailTo, String textoEmail, String asunto) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.user", Constantes.getEMAIL_NOTIFICA());
            props.setProperty("mail.smtp.password", Constantes.getEMAIL_NOTIFICA_PASS());
            props.setProperty("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props, null);
            // session.setDebug(true);

            // Se compone la parte del texto
            BodyPart texto = new MimeBodyPart();
            texto.setText(textoEmail);

            // Se compone el adjunto con la imagen
            BodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(new DataHandler(new FileDataSource(archivoAdjunto)));
            adjunto.setFileName(archivoAdjunto.getName());

            // Una MultiParte para agrupar texto e imagen.
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            multiParte.addBodyPart(adjunto);

            // Se compone el correo, dando to, from, subject y el
            // contenido.
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(props.getProperty("mail.smtp.user")));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(emailTo));
            message.setSubject(asunto);
            message.setContent(multiParte);

            // Se envia el correo.
            Transport t = session.getTransport("smtp");
            t.connect(props.getProperty("mail.smtp.user"), props.getProperty("mail.smtp.password"));
            t.sendMessage(message, message.getAllRecipients());
            t.close();
            System.out.println("Email enviado");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\n ERROR:"
                    + "\n----------------- Con fichero -------------\n"
                    + Constantes.getEMAIL_ERROR()
                    + "\n-----------------\n"
                    + "Email: " + emailTo + "\n");
        }
    }

    public static boolean enviarMail(String emailTo, String textoEmail, String asunto) {
        boolean ok = false;
        try {
            // Propiedades de la conexión
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.user", Constantes.getEMAIL_NOTIFICA());
            props.setProperty("mail.smtp.password", Constantes.getEMAIL_NOTIFICA_PASS());
            props.setProperty("mail.smtp.auth", "true");

            // Preparamos la sesion
            Session session = Session.getDefaultInstance(props);

            // Construimos el mensaje
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(props.getProperty("mail.smtp.user")));
            message.addRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(emailTo));
            message.setSubject(asunto);
            message.setText(textoEmail);

            // Lo enviamos.
            Transport t = session.getTransport("smtp");
            t.connect(props.getProperty("mail.smtp.user"), props.getProperty("mail.smtp.password"));
            t.sendMessage(message, message.getAllRecipients());

            // Cierre.
            t.close();
            System.out.println("Email enviado");
            ok = true;
        } catch (Exception e) {
            System.out.println("\n" + e.getMessage()
                    + "\n-----------------\n"
                    + Constantes.getEMAIL_ERROR()
                    + "\n-----------------\n"
                    + "Email: " + emailTo + "\n");
        }
        return ok;
    }
    
    

    public static boolean compruebaEmail(String email) {
        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(Constantes.getPATTERN_EMAIL());

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
