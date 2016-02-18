package gov.fac.cacom5.cetad.dronedetector.utils;

import java.io.File;

public class Utils {
	/*
	\u00e1 -> �
	\u00e9 -> �
	\u00ed -> �
	\u00f3 -> �
	\u00fa -> �
	\u00c1 -> �
	\u00c9 -> �
	\u00cd -> �
	\u00d3 -> �
	\u00da -> �
	\u00f1 -> �
	\u00d1 -> �
	*/
	public static final String version = "1.0";
	public final static String wav = "wav";

    /**
     * Get the extension of a file.
     */  
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}
