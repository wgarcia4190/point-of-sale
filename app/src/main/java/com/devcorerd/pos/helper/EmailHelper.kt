package com.devcorerd.pos.helper

import com.devcorerd.pos.BuildConfig
import com.mailjet.client.MailjetClient
import com.mailjet.client.MailjetRequest
import com.mailjet.client.resource.Contact
import com.mailjet.client.resource.Email
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.Executors


/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
class EmailHelper private constructor() {
    companion object {
        fun sendEmail(to: String, html: String) {
            Executors.newSingleThreadExecutor().submit {
                val client = MailjetClient(BuildConfig.PUBLIC_KEY, BuildConfig.SECRET_KEY)
                val recipients = JSONArray()
                        .put(JSONObject().put(Contact.EMAIL, to))


                val mailRequest = MailjetRequest(Email.resource)
                        .property(Email.FROMEMAIL, "noreply@adess.com")
                        .property(Email.FROMNAME, "No Reply")
                        .property(Email.SUBJECT, "Recibo de Compra")
                        .property(Email.HTMLPART, html)
                        .property(Email.RECIPIENTS, recipients)

                client.post(mailRequest)
            }

        }
    }


}