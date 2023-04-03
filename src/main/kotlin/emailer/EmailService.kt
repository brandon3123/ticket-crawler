package emailer

import io.github.cdimascio.dotenv.Dotenv
import model.gametime.Event
import model.gametime.Listing
import java.time.format.DateTimeFormatter
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
    private val env = Dotenv.configure().load()
    private val email = env["EMAIL"]
    private val password = env["PASSWORD"]
    private val niceDateFormat = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' h:mm a")

    fun sendEmailNotification(gamesWithSeats: Map<Event, List<Listing>>) {

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

            gamesWithSeats.forEach { (game, seats) ->
                val emailBody = emailBodyFor(game, seats)
                messageBuilder.append(emailBody).append("\n")
            }

            message.setText(messageBuilder.toString())

            Transport.send(message)

            println("Email notification sent successfully.")
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }

    private fun emailBodyFor(game: Event, seats: List<Listing>): String {
        val builder = StringBuilder()

        val gameTimeString = niceDateFormat.format(game.time).toString()

        builder.append("Found Seats for ${game.name} at $gameTimeString\n")

        seats.forEach {
            builder.append(
                "Price: $${it.price.total}, Section: ${it.spot.section}, Row: ${it.spot.row}\n"
            )
        }

        builder.append("\n")

        return builder.toString()
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