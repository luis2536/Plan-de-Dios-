// app/src/main/java/com/example/data/remote/GeminiApiService.kt
package com.example.data.remote

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiApiService {

    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun queryFinancialAssistant(prompt: String, contextInfo: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "Clave de API de Gemini no configurada en los Secretos. Puedes hacer preguntas o usar la simulación fuera de línea."
        }

        val url = "$BASE_URL/$MODEL_NAME:generateContent?key=$apiKey"

        val systemInstruction = """
            Eres el Asistente Financiero AI de 'Plan de Dios', un copiloto de economía doméstica y presupuestos de pareja.
            Tu objetivo es ayudar a la pareja (Él gana $260/mes) a optimizar sus gastos, administrar prioridades ($160/mes en alquiler, comida, internet, zapatos, deudas), calcular la duración de la despensa y tomar decisiones inteligentes al proyectar compras a crédito o de contado.
            Responde de manera motivadora, clara, profesional y empática en español latinoamericano.
            Contexto actual de la app:
            $contextInfo
        """.trimIndent()

        val jsonBody = JSONObject().apply {
            put("systemInstruction", JSONObject().apply {
                put("parts", JSONArray().put(JSONObject().put("text", systemInstruction)))
            })
            put("contents", JSONArray().put(JSONObject().apply {
                put("parts", JSONArray().put(JSONObject().put("text", prompt)))
            }))
        }

        val requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            val response = okHttpClient.newCall(request).execute()
            val responseBodyString = response.body?.string() ?: ""
            if (!response.isSuccessful) {
                return@withContext "Nota del Asistente Plan de Dios: Error en servidor ($ {response.code})."
            }

            val jsonResponse = JSONObject(responseBodyString)
            val candidates = jsonResponse.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val candidate = candidates.getJSONObject(0)
                val content = candidate.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    val text = parts.getJSONObject(0).optString("text")
                    if (text.isNotEmpty()) return@withContext text
                }
            }
            "El asistente AI procesó tu solicitud. Revisa tu presupuesto y proyecciones."
        } catch (e: Exception) {
            "Conexión fuera de línea. Asistente Local activo: Tu sueldo disponible es $260. Prioridades fijas: $160. Excedente estimado: $100."
        }
    }
}
