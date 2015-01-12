/*
 *    This file is part of Lucterios.
 *
 *    Lucterios is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    Lucterios is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with Lucterios; if not, write to the Free Software
 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *	Contributeurs: Fanny ALLEAUME, Pierre-Olivier VERSCHOORE, Laurent GAY
 */

package org.lucterios.utils;

import java.io.FileInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Scanner;

import org.lucterios.gui.GUIGenerator;

public class Tools {

	public final static int EPSILON_RATIO = 1;

	public interface DefaultThemeCallBack {
		public int getDefaultValue();

		public void setDefaultValue(int value);
	}

	public interface InfoDescription {
		void sendSupport(String aTitle, String aComplement);
	}

	static public StringList readFileText(File fileName, String encoding)
			throws IOException {
		StringList result = new StringList();
		Scanner scanner = new Scanner(new FileInputStream(fileName), encoding);
		try {
			while (scanner.hasNextLine()) {
				result.add(scanner.nextLine());
			}
		} finally {
			scanner.close();
		}
		return result;
	}

	static public String parseISToString(java.io.InputStream is)
			throws LucteriosException {
		String result = "";
		if (is != null)
			try {
				java.io.DataInputStream din = new java.io.DataInputStream(is);
				StringBuffer out = new StringBuffer();
				byte[] b = new byte[2048];
				for (int n; (n = din.read(b)) != -1;)
					out.append(new String(b, 0, n));
				result = out.toString();
			} catch (IOException e) {
				throw new LucteriosException("Erreur de parsing en chaine", e);
			}
		return result;
	}

	static public String padRigth(String aValue, int size) {
		String result = aValue;
		while (result.length() < size)
			result += " ";
		return result;
	}

	static public String replace(String aValue, String aTarget,
			String aRemplacement) {
		String result = aValue;
		int len = aTarget.length();
		int pos;
		while ((pos = result.indexOf(aTarget)) != -1) {
			result = result.substring(0, pos) + aRemplacement
					+ result.substring(pos + len);
		}
		return result;
	}

	static public String convertLuctoriosFormatToHtml(String text) {
		text = text.replaceAll(">", "&gt;");
		text = text.replaceAll("<", "&lt;");
		text = replace(text, "{[bold]}", "<b>");
		text = replace(text, "{[/bold]}", "</b>");
		text = replace(text, "{[italc]}", "<i>");
		text = replace(text, "{[/italc]}", "</i>");
		text = replace(text, "{[italic]}", "<i>");
		text = replace(text, "{[/italic]}", "</i>");
		text = replace(text, "{[newline]}", "<br>");
		text = replace(text, "{[underline]}", "<u>");
		text = replace(text, "{[/underline]}", "</u>");
		text = replace(text, "{[center]}", "<center>");
		text = replace(text, "{[/center]}", "</center>");
		text = replace(text, "{[hr/]}", "<hr/>");
		text = replace(text, "<hr>", "<hr/>");
		if (System.getProperty("java.runtime.version").startsWith("1.5."))
			text = replace(text, "<hr/>",
					"<table width='100%' border='1'></table>");
		text = replace(text, "{[br/]}", "<br/>");
		text = replace(text, "{[", "<");
		text = replace(text, "]}", ">");
		if ((text.length() > 0) && text.charAt(0) == '/')
			text = "&#47;" + text.substring(1);
		return text;
	}

	final static String FORGOTTEN_CHAR = ":/\\<>| \"'";

	static public String getFileNameWithoutForgottenChar(String aText) {
		String new_text = aText;
		for (int idx = 0; idx < FORGOTTEN_CHAR.length(); idx++)
			new_text = new_text.replace(FORGOTTEN_CHAR.charAt(idx), '_');
		return new_text;
	}

	static public String HTMLEntityEncode(String s) {
		StringBuffer buf = new StringBuffer();
		int len = (s == null ? -1 : s.length());
		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);
			if (c == '\n')
				buf.append("<br>");
			else if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0'
					&& c <= '9')
				buf.append(c);
			else
				buf.append("&#" + (int) c + ";");
		}
		return buf.toString();
	}

	static public void saveFileStream(File aFile, InputStream aStream)
			throws LucteriosException {
		FileOutputStream fos;
		try {
			int BUFFER = 2048;
			byte data[] = new byte[BUFFER];

			fos = new FileOutputStream(aFile);
			BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
			int count;
			while ((count = aStream.read(data, 0, BUFFER)) != -1) {
				dest.write(data, 0, count);
			}
			dest.flush();
			dest.close();
		} catch (FileNotFoundException e) {
			throw new LucteriosException("Echec de sauvegarde", e);
		} catch (IOException e) {
			throw new LucteriosException("Echec de sauvegarde", e);
		}
	}

	static public void copyFile(File in, File out) throws LucteriosException {
		try {
			FileInputStream fileInputStream = new FileInputStream(in);
			FileOutputStream fileOutputStream = new FileOutputStream(out);
			FileChannel inChannel = fileInputStream.getChannel();
			FileChannel outChannel = fileOutputStream.getChannel();
			try {
				// magic number for Windows, 64Mb - 32Kb)
				int maxCount = (64 * 1024 * 1024) - (32 * 1024);
				long size = inChannel.size();
				long position = 0;
				while (position < size)
					position += inChannel.transferTo(position, maxCount,
							outChannel);
			} finally {
				if (inChannel != null)
					inChannel.close();
				if (outChannel != null)
					outChannel.close();
				fileInputStream.close();
				fileOutputStream.close();
			}
		} catch (IOException ioe) {
			throw new LucteriosException("Echec de copie", ioe);
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	public static void clearGC() {
		Logging.getInstance().writeLog("%%% clearGC() %%%", "BEGIN", 2);
		Runtime rt = Runtime.getRuntime();
		boolean loop_again = true;
		long epsilon = (EPSILON_RATIO * rt.maxMemory() / 100);
		long latest_free = rt.freeMemory();
		while (loop_again) {
			rt = Runtime.getRuntime();
			System.gc();
			rt.runFinalization();
			long current_free = rt.freeMemory();
			loop_again = Math.abs(latest_free - current_free) > epsilon;
			latest_free = current_free;
		}
		Logging.getInstance().writeLog("%%% clearGC() %%%", "END", 2);
	}

	static public GUIGenerator findGenerator(String arg)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		GUIGenerator result = null;
		Class<?> generatorClass = Class.forName(arg);
		result = (GUIGenerator) generatorClass.newInstance();
		return result;
	}

}
