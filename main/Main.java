
package main;

import java.util.HashMap;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;

public class Main {

	public static HashMap<String, String> loadTable(String fname) throws Exception
	{
		HashMap<String, String> ret = new HashMap<String, String>();
		boolean found = false;

		System.out.printf("loading %s\n", fname);
		try {
			FileReader fin = new FileReader(fname);

			// BufferedInputStream bis = new BufferedInputStream();
			BufferedReader br = new BufferedReader(fin);
			String line;
			boolean started = false;

			while ((line = br.readLine()) != null) {
				// System.out.printf("%s\n", line);
				if (!started) {
					if (line.compareToIgnoreCase("%chardef begin") == 0) {
						found = true;
						started = true;
					}
				}
				else {
					if (line.compareToIgnoreCase("%chardef end") == 0) {
						started = false;
					}
					else {
						String[] s = line.split("\\s+");
						if (s.length > 1) {
							String tmp = ret.get(s[0]);
							if (tmp == null) {
								ret.put(s[0], s[1]);
							}
							else {
								ret.put(s[0], tmp + s[1]);
							}
						}
					}
				}
			}
			if (!found) {
				throw new Exception("not valid dictionary");
			}

			br.close();
			return ret;
		} catch (Exception ex) {
			// System.out.printf("exception: " + ex);
			throw ex;
			// return null;
		}
	}

	public static boolean Serialize(String name, Object o) throws Exception
	{
		try {
			System.out.printf("serializing to %s\n", name);
			File f = new File(name);
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(o);

			oos.close();
			return true;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static Object Deserialize(String name)
	{
		try {
			Object ret;
			FileInputStream fis = new FileInputStream(new File(name));
			ObjectInputStream ois = new ObjectInputStream(fis);

			ret = ois.readObject();

			ois.close();
			return ret;
		} catch (Exception ex) {
			return false;
		}
	}

	public static void usage()
	{
		System.out.printf("usage\n");
	}

	public static void main(String[] arg)
	{
		HashMap<String, String> dict;
		Object o;

		/*
		o = Deserialize("1.dat");
		if (o != null && o instanceof HashMap<?,?>) {
			dict = (HashMap<String, String>) o;
			System.out.printf("oao: %s\n", dict.get("oao"));
			System.out.printf("cai: %s\n", dict.get("cai"));
		}
		*/
		if (arg.length < 2) {
			usage();
			return;
		}

		try {
			// noseeing.cin
			dict = loadTable(arg[0]);
			Serialize(arg[1], (Object) dict);
		} catch (Exception ex) {
			System.out.printf("ex: " + ex + "\n");
		}

		return;
	}
}
