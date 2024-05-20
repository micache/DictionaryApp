import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import javax.sound.sampled.*;
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
     * Speak a word using unofficial Google Text-to-Speech API.
     *
     * @param text     Text to speak
     * @param language Language code (e.g., "en" for English)
     * @throws IOException If there is an error during text-to-speech conversion
     * @throws LineUnavailableException If a line cannot be opened because it is unavailable
     * @throws UnsupportedAudioFileException If the audio file format is not supported
     */
    public static void speak(String text, String language) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        String urlStr = String.format("https://translate.google.com/translate_tts?ie=UTF-8&q=%s&tl=%s&client=tw-ob",
                URLEncoder.encode(text, StandardCharsets.UTF_8), language);

        try (InputStream audioSrc = new URL(urlStr).openStream();
             InputStream bufferedIn = new BufferedInputStream(audioSrc)) {

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            try (SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {
                line.open(format);
                line.start();

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = audioStream.read(buffer, 0, buffer.length)) != -1) {
                    line.write(buffer, 0, bytesRead);
                }

                line.drain();
            }
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
