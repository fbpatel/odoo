package email;

import java.io.File;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import utilidades.Constantes;

public class MailVarios {

    

    public void enviarMail(File archivoAdjunto, List<String> usuariosDestinatarios, String textoEmail, String asunto) {
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

            //Recogemos todos los usuarios destinatarios
            Address [] receptores = new Address [usuariosDestinatarios.size()];
            for (int i = 0; i < usuariosDestinatarios.size(); i++) {
                try {
                    receptores[i] = new InternetAddress(usuariosDestinatarios.get(i));
                } catch (AddressException ex) {
                    System.out.println(ex);
                }
            }

            // Se compone el correo, dando to, from, subject y el
            // contenido.
            MimeMessage message = new MimeMessage(session);
            //message.setFrom(receptores);
            message.addRecipients(Message.RecipientType.TO,receptores);
            message.setSubject(asunto);
            message.setContent(multiParte);

            // Se envia el correo.
            Transport t = session.getTransport("smtp");
            t.connect(props.getProperty("mail.smtp.user"), props.getProperty("mail.smtp.password"));
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
