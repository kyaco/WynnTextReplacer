package net.kyaco.wynntr;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;

public class ResourcepackWriter
{
	private static Gson gson = new Gson();
	public static boolean addTranslationLine(String resourceName, String langCode, String str, String context)
	{
		String fileName = "resourcepacks/" + resourceName + "/assets/minecraft/lang/" + langCode + ".json";
		File file = new File(fileName);
		if (!file.exists()) return false;
		
		String hash = DigestUtils.sha1Hex(str);
		Map<String, String> m = new HashMap<>();
		m.put(context + "." + hash, str);
		String json = gson.toJson(m);
		String line = ",\r\n\t" + json.replaceFirst("^\\{", "");
		
		try (RandomAccessFile raf = new RandomAccessFile(file, "rw");) {
			byte lineBytes[] = line.getBytes("UTF-8");

			int len = 1024;
			int from = (int)file.length() - len;
			byte buff[] = new byte[1024];
			
			while(len == 1024) {
				if (from < 0) {
					len += from;
					from = 0;
				}
				raf.seek(from);
				raf.readFully(buff, 0, len);
				int index = ArrayUtils.lastIndexOf(buff, (byte)'}');
				if (index < 0) {
					from -= len;
					continue;
				}
				from += index;
				break;
			}
			if (from < 0) return false;
			raf.seek(from);
			raf.write(lineBytes);
		} catch(UnsupportedEncodingException e) {
			System.out.println("[TextReplacer] failed to encoding text:" + line);
			return false;
		} catch(IOException e){
			System.out.println("[TextReplacer] failed to write the text: " + line);
			return false;
		}
		return true;
	}
}