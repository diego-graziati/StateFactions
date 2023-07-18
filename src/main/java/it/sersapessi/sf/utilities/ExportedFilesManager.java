package it.sersapessi.sf.utilities;

import it.sersapessi.sf.StateFactions;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ExportedFilesManager {

    private static String jarFolder;

    public static void exportFiles() throws Exception {

        jarFolder = new File(StateFactions.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
        jarFolder+="/"+Constants.Utility.PLUGIN_NAME;

        StateFactions.logger.log(new LogRecord(Level.INFO,"Path: "+jarFolder+Constants.Resources.ExportedResPaths.Lang.LANG_FOLDER+"/"));
        File out = new File(jarFolder+Constants.Resources.ExportedResPaths.Lang.LANG_FOLDER+"/");
        if(!out.exists()){
            if(out.mkdir()){
                StateFactions.logger.log(new LogRecord(Level.INFO,"External lang folder created"));
            }else{
                StateFactions.logger.log(new LogRecord(Level.INFO,"External lang folder can't be created"));

                throw new RuntimeException();
            }
        }

        exportFile(Constants.Resources.InternalResPaths.Lang.EN_US);
        exportFile(Constants.Resources.InternalResPaths.Lang.IT_IT);
    }

    //Credits: https://stackoverflow.com/questions/10308221/how-to-copy-file-inside-jar-to-outside-the-jar#:~:text=63-,First%20of%20all,-I%20want%20to
    //This method simply exports an internal file outside the jar to the given location. If the file already exists, then
    //nothing happens
    private static void exportFile(String resourceName) throws Exception {
        File externalFile = new File(jarFolder + resourceName);

        if(!externalFile.exists()){
            InputStream stream = null;
            OutputStream resStreamOut = null;
            try {
                stream = StateFactions.class.getResourceAsStream(resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
                if(stream == null) {
                    throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
                }

                int readBytes;
                byte[] buffer = new byte[4096];
                StateFactions.logger.log(new LogRecord(Level.INFO,"PathToExportedResource: "+externalFile.getAbsolutePath()));
                resStreamOut = new FileOutputStream(externalFile);
                while ((readBytes = stream.read(buffer)) > 0) {
                    resStreamOut.write(buffer, 0, readBytes);
                }

                stream.close();
                resStreamOut.close();
            } catch (Exception ex) {
                throw ex;
            }
        }else{
            StateFactions.logger.log(new LogRecord(Level.INFO,externalFile.getName()+" already exists"));
        }
    }

    //This method is identical to the exportFile() method, except for ignoring the existence of a file outside the jar.
    //It is used to overwrite the file outside because it is corrupted in some ways.
    public static void restoreExternalFile(String resourceName) throws Exception {
        File externalFile = new File(jarFolder + resourceName);

        InputStream stream = null;
        OutputStream resStreamOut = null;
        try {
            stream = StateFactions.class.getResourceAsStream(resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
            if(stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            StateFactions.logger.log(new LogRecord(Level.INFO,"PathToExportedResource: "+externalFile.getAbsolutePath()));
            resStreamOut = new FileOutputStream(externalFile);
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
