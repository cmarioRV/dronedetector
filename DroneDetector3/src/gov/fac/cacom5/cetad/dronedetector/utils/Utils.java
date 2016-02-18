package gov.fac.cacom5.cetad.dronedetector.utils;

import java.io.File;

public class Utils {
	/*
	\u00e1 -> á
	\u00e9 -> é
	\u00ed -> í
	\u00f3 -> ó
	\u00fa -> ú
	\u00c1 -> Á
	\u00c9 -> É
	\u00cd -> Í
	\u00d3 -> Ó
	\u00da -> Ú
	\u00f1 -> ñ
	\u00d1 -> Ñ
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
