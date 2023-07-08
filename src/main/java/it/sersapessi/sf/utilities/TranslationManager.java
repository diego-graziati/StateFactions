package it.sersapessi.sf.utilities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.sersapessi.sf.StateFactions;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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
    public TranslationManager(@NotNull String lang) throws Exception {
        this.lang=lang;
        InputStream inStream = new FileInputStream(ExportedFilesRoutine.getPathToLocal()+Constants.Resources.ExportedResPaths.Lang.LANG_FOLDER+"/"+lang+".json");
        JsonElement fileElement = JsonParser.parseReader(new InputStreamReader(inStream, StandardCharsets.UTF_8));
        fileObject = fileElement.getAsJsonObject();

        inStream.close();
        if(!integrityRoutine(fileObject)){

            ExportedFilesRoutine.restoreExternalFile(Constants.Resources.ExportedResPaths.Lang.LANG_FOLDER+"/"+lang+".json");

            inStream = new FileInputStream(ExportedFilesRoutine.getPathToLocal()+Constants.Resources.ExportedResPaths.Lang.LANG_FOLDER+"/"+lang+".json");

            fileElement = JsonParser.parseReader(new InputStreamReader(inStream, StandardCharsets.UTF_8));
            fileObject = fileElement.getAsJsonObject();
        }


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

    public boolean integrityRoutine(JsonObject fileObject) throws IOException {
        Map<String,JsonElement> fileObjMap = fileObject.asMap();
        InputStream inStream = StateFactions.class.getResourceAsStream(Constants.Resources.InternalResPaths.Lang.LANG_FOLDER+"/"+lang+".json");
        //If "inStream"==null then the lang file is probably custom and made for a not officially supported language
        if(inStream != null){
            JsonElement internalFileElement = JsonParser.parseReader(new InputStreamReader(inStream, StandardCharsets.UTF_8));
            inStream.close();

            Map<String,JsonElement> internalFileObjMap = internalFileElement.getAsJsonObject().asMap();

            //I check every single key: if each key inside the internal lang file is present inside the external lang file,
            //then it means everything is alright. Otherwise, the external file needs to be "fixed", which is a fancier word to say
            //that it will be overwritten with the internal lang file, de facto resetting the lang file to its origin
            Object[] keySet_obj = fileObjMap.keySet().toArray();
            Object[] originalKeySet_obj = internalFileObjMap.keySet().toArray();

            for(Object originalKey_obj : originalKeySet_obj){
                String originalKey = (String) originalKey_obj;

                boolean isKey=false;
                for(Object key_obj : keySet_obj){
                    String key = (String) key_obj;

                    if(key.equals(originalKey)){
                        isKey=true;
                        break;
                    }
                }

                if(!isKey){
                    return false;
                }
            }
        }

        return true;
    }
}
