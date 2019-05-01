package com.liferay.sopapo.hotfixdata.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtil {

	public static File extractFile(String fileToExtract, String zipUrl)
			throws IOException {

		if (fileToExtract == null || zipUrl == null)
			return null;

		URL url = new URL(zipUrl);
		ZipInputStream zin = new ZipInputStream(url.openStream());
		ZipEntry ze = zin.getNextEntry();

		while (!ze.getName().equals(fileToExtract)) {
			zin.closeEntry();
			ze = zin.getNextEntry();
		}

		File tempFile = File.createTempFile(fileToExtract, "unziped");
		FileOutputStream fos = new FileOutputStream(tempFile);
		int next = zin.read();

		while (next != -1) {
			fos.write(next);
			next = zin.read();
		}

		fos.flush();

		fos.close();

		return tempFile;
	}

}
