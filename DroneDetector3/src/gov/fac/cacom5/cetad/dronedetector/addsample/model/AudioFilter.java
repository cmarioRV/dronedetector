package gov.fac.cacom5.cetad.dronedetector.addsample.model;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import gov.fac.cacom5.cetad.dronedetector.utils.Utils;

public class AudioFilter extends FileFilter {

	public AudioFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
	        return true;
	    }

	    String extension = Utils.getExtension(f);
	    if (extension != null) {
	        if (extension.equals(Utils.wav)) {
	                return true;
	        } else {
	            return false;
	        }
	    }

	    return false;
	}

	@Override
	public String getDescription() {
		return "Archivos de audio";
	}

}
