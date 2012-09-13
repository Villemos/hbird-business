package org.hbird.business.cfdp.receiver;

import java.io.File;
import java.io.FileFilter;


public class EofFileFilter implements FileFilter {

	public boolean accept(File file) {
		if (file.isDirectory()) {
			File eof = new File(file.getName() + "-EOF.pdu");
			if (eof.exists()) {
				return true;
			}
		}		
		
		return false;
	}
}
