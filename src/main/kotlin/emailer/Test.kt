import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.rpc2
import com.google.api.client.util.Base64
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import com.google.api.services.gmail.model.Message
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.security.GeneralSecurityException
import java.util.Arrays

val APPLICATION_NAME = "Gmail API Kotlin Quickstart"
val JSON_FACTORY: JsonFactory = JacksonFactory().getDefaultInstance()
val TOKENS_DIRECTORY_PATH = "tokens"

val SCOPES = Arrays.asList(GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_COMPOSE, GmailScopes.MAIL_GOOGLE_COM)

fun getCredentials(HTTP_TRANSPORT: HttpTransport): Credential? {
    // Load client secrets.
    val inStream = FileInputStream("./credentials.json")
    val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(inStream))

    // Build flow and trigger user authorization request.
    val flow = GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
        .setDataStoreFactory(FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH)))
        .setAccessType("offline")
        .build()
    val receiver = LocalServerReceiver.Builder().setPort(8888).build()
    return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
}

fun sendEmailNotification(recipient: String, data: List<String>) {
    val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

    try {
        // Authorize the credentials
        val credentials = getCredentials(httpTransport)

        // Create a new Gmail client
        val service = Gmail.Builder(httpTransport, JSON_FACTORY, credentials)
            .setApplicationName(APPLICATION_NAME)
            .build()

        // Create a new message
        val message = MimeMessage(Session.getDefaultInstance(Properties()))

        // Set the message headers
        message.setFrom(InternetAddress("brandonnolan3123@gmail.com"))
        message.addRecipient(javax.mail.Message.RecipientType.TO, InternetAddress(recipient))
        message.subject = "API Notification"

        // Build the message body
        val messageBuilder = StringBuilder()
        messageBuilder.append("Data from API:\n\n")
        data.forEach { messageBuilder.append(it).append("\n") }
        val messageText = messageBuilder.toString()

        // Encode the message body as Base64
        val messageBytes = messageText.toByteArray(StandardCharsets.UTF_8)
        val messageEncoded = Base64.encodeBase64URLSafeString(messageBytes)

        // Create the message object
        val gmailMessage = Message()
        gmailMessage.raw = messageEncoded

        // Send the message
        service.users().messages().send("me", gmailMessage).execute()

        println("Email notification sent successfully.")
    } catch (e: GeneralSecurityException) {

    }
}
