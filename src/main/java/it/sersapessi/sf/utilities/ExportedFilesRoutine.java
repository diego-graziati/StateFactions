package it.sersapessi.sf.utilities;

import it.sersapessi.sf.StateFactions;
import it.sersapessi.sf.utilities.Constants;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ExportedFilesRoutine {

    private static String jarFolder;

    public static void exportFiles() throws Exception {

        jarFolder = new File(StateFactions.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
        jarFolder+="/"+Constants.Utility.PLUGIN_NAME;

        StateFactions.logger.log(new LogRecord(Level.INFO,"Path: "+jarFolder+Constants.Resources.ExportedResPaths.Lang.LANG_FOLDER+"/"));
        File out = new File(jarFolder+Constants.Resources.ExportedResPaths.Lang.LANG_FOLDER+"/");
        if(out.mkdir()){
            StateFactions.logger.log(new LogRecord(Level.INFO,"External lang folder created"));
            exportFile(Constants.Resources.InternalResPaths.Lang.EN_US);
            exportFile(Constants.Resources.InternalResPaths.Lang.IT_IT);
        }else{
            StateFactions.logger.log(new LogRecord(Level.INFO,"External lang folder can't be created"));

            throw new RuntimeException();
        }
    }

    //Credits: https://stackoverflow.com/questions/10308221/how-to-copy-file-inside-jar-to-outside-the-jar#:~:text=63-,First%20of%20all,-I%20want%20to
    private static void exportFile(String resourceName) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        try {
            stream = StateFactions.class.getResourceAsStream(resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
            if(stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            StateFactions.logger.log(new LogRecord(Level.INFO,"PathToExportedResource: "+jarFolder+resourceName));
            resStreamOut = new FileOutputStream(jarFolder + resourceName);
            StateFactions.logger.log(new LogRecord(Level.INFO,"resStreamOut: "+(resStreamOut==null?"null":resStreamOut)));
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }

            stream.close();
            resStreamOut.close();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static String getPathToLocal(){
        return jarFolder;
    }
}
