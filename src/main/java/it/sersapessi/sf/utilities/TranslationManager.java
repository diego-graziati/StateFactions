package it.sersapessi.sf.utilities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.sersapessi.sf.StateFactions;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * The <code>TranslationManager</code> handles all the strings' translations available inside the plugin.
 * Be sure to set the correct supported language inside the config.yml file.<br><br>
 *
 * The <code>TranslationManager</code> is called by the <code>{@link StateFactions#startupProcess()}</code> method. If the language is
 * not supported, this method provides the <code>TranslationManager</code> with the fallback language.<br><br>
 *
 * Right now the fallback language is: <span style="font-weight: bold;">en-US</span>.
 * */
public final class TranslationManager {

    private String lang;
    private JsonObject fileObject;

    /**
     * This is the <code>TranslationManager</code> constructor.
     *
     * @throws FileNotFoundException If this exception is thrown, the <code>{@link StateFactions#startupProcess()}</code> method
     *                                  will firstly provide the <code>TranslationManager</code> with the fallback language.
     *                                  If that doesn't work, the <code>{@link StateFactions#startupProcess()}</code> method will
     *                                  return a {@link RuntimeException}.
     * */
    public TranslationManager(@NotNull String lang) throws IOException {
        this.lang=lang;
        InputStream inStream = StateFactions.class.getResourceAsStream("/lang/"+lang+".json");
        assert inStream != null;
        JsonElement fileElement = JsonParser.parseReader(new InputStreamReader(inStream, StandardCharsets.UTF_8));
        fileObject = fileElement.getAsJsonObject();

        inStream.close();
    }

    /**
     * The <code>getString</code> method finds the string correlated to the stringId that is being passed as parameter.
     *
     * @param stringId this is the id that identifies the string that's being searched.
     *
     * @return the method returns the string correlated to the id passed as parameter.
     * */
    public String getString(@NotNull String stringId){
        return fileObject.get(stringId).getAsString();
    }
}
