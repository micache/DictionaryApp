import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class GoogleTranslate {

    /**
     * Translate a word from source language to target language using unofficial Google Translate API.
     *
     * @param text           Text to translate
     * @param sourceLanguage Source language code (e.g., "en" for English)
     * @param targetLanguage Target language code (e.g., "vi" for Vietnamese)
     * @return Translated text
     * @throws IOException If there is an error during translation
     */
    public static String translate(String text, String sourceLanguage, String targetLanguage) throws IOException {
        String urlStr = String.format("https://translate.googleapis.com/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s",
                sourceLanguage, targetLanguage, URLEncoder.encode(text, StandardCharsets.UTF_8));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(urlStr);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String jsonResponse = EntityUtils.toString(response.getEntity());
            JsonArray jsonArray = JsonParser.parseString(jsonResponse).getAsJsonArray();
            return jsonArray.get(0).getAsJsonArray().get(0).getAsJsonArray().get(0).getAsString();
        }
    }

    /**
     * Converts text to speech and plays the audio.
     *
     * @param text     The text to convert to speech.
     * @param language The language code of the text (e.g., "en" for English).
     * @throws IOException If an I/O error occurs.
     */
    public static void speak(String text, String language) throws IOException {
        String urlStr = String.format("https://translate.google.com/translate_tts?ie=UTF-8&q=%s&tl=%s&client=tw-ob",
                URLEncoder.encode(text, StandardCharsets.UTF_8), language);

        try (InputStream audioSrc = new URL(urlStr).openStream();
             InputStream bufferedIn = new BufferedInputStream(audioSrc)) {

            AdvancedPlayer player = new AdvancedPlayer(bufferedIn);
            player.play();
        } catch (JavaLayerException e) {
            throw new IOException("Error playing MP3", e);
        }
    }

    public static void main(String[] args) {
        try {
            // Test translation
            String translatedText = GoogleTranslate.translate("tôi đi ăn cơm", "vi", "en");
            System.out.println("Translated text: " + translatedText);

            // Test text-to-speech
            GoogleTranslate.speak("Neeko is the best decision!", "en");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
