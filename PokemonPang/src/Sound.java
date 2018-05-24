import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.media.AudioClip;

public class Sound {
	public static String dir;
    static {
        dir = "file:///" + System.getProperty("user.dir").replace('\\', '/').replaceAll(" ", "%20");
        try {
            dir = new java.net.URI(dir).toASCIIString();
        } 
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    private static AudioClip opening = new AudioClip(dir + "/opening.mp3");
	private static AudioClip pang = new AudioClip(dir + "/pang.mp3");
	private static AudioClip dragonite = new AudioClip(dir + "/dragonite.mp3");
	private static AudioClip pokemon = new AudioClip(dir + "/pokemon.mp3");
	private static AudioClip snorlax = new AudioClip(dir + "/clock.mp3");
	
	private static AudioClip opening2 = new AudioClip(dir + "/RGB_OPENING.mp3");
	private static AudioClip game_bgm1 = new AudioClip(dir + "/WILD_POKEMON_BATTLE.mp3");
	private static AudioClip game_bgm2 = new AudioClip(dir + "/RED_REMIX.mp3");
	private static AudioClip game_bgm3 = new AudioClip(dir + "/route.mp3");
	private static AudioClip champ_ending = new AudioClip(dir + "/RGB_ENDING.mp3");
	private static AudioClip normal_ending = new AudioClip(dir + "/MASIRA_TOWN2.mp3");
	
	
	private static AudioClip pika = new AudioClip(dir + "/pika.mp3");
	private static AudioClip pucrin = new AudioClip(dir + "/purin.mp3");
	private static AudioClip mew = new AudioClip(dir + "/mew.mp3");
	private static AudioClip todogas = new AudioClip(dir + "/todogas.mp3");
	private static AudioClip metamong = new AudioClip(dir + "/metamong.mp3");
	private static AudioClip celebi = new AudioClip(dir + "/celebi.mp3");

	//private static AudioClip bomb = new AudioClip(dir + "/bomb.mp3");
	
	private static Map<String, AudioClip> soundBox = new HashMap<>();
	static{
		soundBox.put("opening", opening);
		soundBox.put("pokemon", pokemon);
		soundBox.put("pang", pang);
		soundBox.put("dragonite", dragonite);
		soundBox.put("snorlax", snorlax);
		
		soundBox.put("opening2", opening2);
		soundBox.put("game_bgm1", game_bgm1);
		soundBox.put("game_bgm2", game_bgm2);
		soundBox.put("game_bgm3", game_bgm3);
		soundBox.put("champ_ending", champ_ending);
		soundBox.put("normal_ending", normal_ending);
		
		soundBox.put("pika", pika);
		soundBox.put("pucrin", pucrin);
		soundBox.put("mew", mew);
		soundBox.put("togogas", todogas);
		soundBox.put("metamong", metamong);
		soundBox.put("celebi", celebi);
	}
	
	public static void play(String key){	
		AudioClip clip = soundBox.get(key);
		if(clip!=null){
			clip.play();
		}
	}
	public static void stop(String key){
		AudioClip clip = soundBox.get(key);
		if(clip!=null){
			clip.stop();
		}
	}
}
