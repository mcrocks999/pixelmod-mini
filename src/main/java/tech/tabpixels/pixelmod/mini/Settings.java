package tech.tabpixels.pixelmod.mini;

import java.io.FileReader;
import java.io.FileWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@SuppressWarnings("unchecked")
public class Settings {
	public static Integer selection = 0;
	public static String workingDirectory = "";
    public static String minecraftDirectory = "";
    public static String technicDirectory = "";
    public static String modpackDirectory = "";
    public static String multimcDirectory = "";
    
    public static void load()
    {
    	findWorkingDir();
    	
    	JSONParser parser = new JSONParser();
    	try {
			JSONObject obj = (JSONObject) parser.parse(new FileReader("mini-config.json"));
			System.out.println(obj.toJSONString());
			try {minecraftDirectory = (String) obj.get("minecraftDir");}catch(Exception e){}
			try {technicDirectory = (String) obj.get("technicDir");}catch(Exception e){}
			try {multimcDirectory = (String) obj.get("multimcDir");}catch(Exception e){}
		} catch (Exception e) {e.printStackTrace();}
    	
    	defaultSetup();
    }
	
	public static void save()
    {
    	JSONObject obj = new JSONObject();
    	obj.put("minecraftDir", minecraftDirectory);
    	obj.put("technicDir", technicDirectory);
    	obj.put("multimcDir", multimcDirectory);
    	
    	try {
    		FileWriter file = new FileWriter("mini-config.json");
			file.write(obj.toJSONString());
			file.close();
		} catch(Exception e) {e.printStackTrace();}
    }
	
	public static void findWorkingDir() {
		String OS = (System.getProperty("os.name")).toUpperCase();
		if (OS.contains("WIN")) {
		    workingDirectory = System.getenv("AppData");
		} else {
		    workingDirectory = System.getProperty("user.home");
		    workingDirectory += "/Library/Application Support";
		}
	}
	
	public static void defaultSetup()
	{
		if (Settings.technicDirectory.length()<1) {Settings.technicDirectory = workingDirectory+"/.technic/modpacks";}
		if (Settings.minecraftDirectory.length()<1) {Settings.minecraftDirectory = workingDirectory+"/.minecraft/mods";}
	}
}