package emailer

import io.github.cdimascio.dotenv.Dotenv
import model.gametime.Listing
import java.util.*
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class Emailer {

    val properties = getProps()
    val env = Dotenv.configure().load()
    val email = env["EMAIL"]
    val password = env["PASSWORD"]
    fun sendEmailNotification(seats: List<Listing>) {

        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(email, password)
            }
        })

        try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(email))
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email))
            message.subject = "Flames Games Tickets - Ticket Crawler"

            val messageBuilder = StringBuilder()
            messageBuilder.append("Data from API:\n\n")
            seats.forEach { messageBuilder.append(it).append("\n") }
            message.setText(messageBuilder.toString())

            Transport.send(message)

            println("Email notification sent successfully.")
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }

    private fun getProps(): Properties {
        val props = Properties()
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.required"] = "true";
        props["mail.smtp.ssl.protocols"] = "TLSv1.2";
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.port"] = "587"
        return props
    }

}