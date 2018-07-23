package br.eng.iris.googlemaps.repository

import br.eng.iris.googlemaps.entity.FullParameters
import br.eng.iris.googlemaps.entity.HttpResponse
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class BaseRepository() {

    fun execute(fullParameters: FullParameters): HttpResponse{
        val conn: HttpURLConnection
        val response: HttpResponse

        val url: URL = URL(fullParameters.url)

        conn = url.openConnection() as HttpURLConnection
        conn.readTimeout = 100000
        conn.connectTimeout = 120000
        conn.requestMethod = fullParameters.method.toString()

        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        conn.setRequestProperty("charset", "utf-8")

        conn.useCaches = false

        //Faz a requisição
        conn.connect()

        if (conn.responseCode == 404){
            response = HttpResponse(conn.responseCode, "")
        } else {
            val inputStream: InputStream = conn.inputStream
            response = HttpResponse(conn.responseCode,getStringFromInputStream(inputStream))
        }

        return response

    }

    fun getStringFromInputStream(inputStream: InputStream): String {
        try {
            val strBuilder: StringBuilder = StringBuilder()
            val br: BufferedReader = BufferedReader(InputStreamReader(inputStream))

            for (line in br.readLine()) {
                strBuilder.append(line)
            }

            return strBuilder.toString()
        } catch (e: Exception) {
            return ""
        }
    }
}