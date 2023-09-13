package emailer

import Config
import model.gametime.GamesWithSeats
import util.asHtmlEmail
import util.log
import java.util.*
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailService {

    private val properties = getEmailProps()

    fun sendEmailNotification(subject: String, gamesWithSeats: GamesWithSeats) {
        // Create an email session
        val session = getSession()

        try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(Config.Email.EMAIL))
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Config.Email.EMAIL))
            message.subject = subject

            // Build the email, simple html table for readability
            val emailBody = gamesWithSeats.asHtmlEmail()

            message.setContent(emailBody, "text/html")

            // Send the email
            Transport.send(message)

            log("Email notification sent successfully")
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }

    private fun getSession(): Session? {
        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(Config.Email.EMAIL, Config.Email.PASSWORD)
            }
        })
        return session
    }

    private fun getEmailProps(): Properties {
        val props = Properties()
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.required"] = "true"
        props["mail.smtp.ssl.protocols"] = "TLSv1.2"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.port"] = "587"
        return props
    }

}