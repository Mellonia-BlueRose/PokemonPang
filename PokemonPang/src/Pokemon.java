import javafx.scene.image.Image;

public enum Pokemon {
	BULBASAUR("bulbasaur.png"), 		// 이상해씨, 0
	CHARMANDER("charmander.png"), 	// 피아리, 1
	CYNDAQUIL("cyndaquil.png"), 		// 브케인, 2 
	EEVEE("eevee.png"),				// 이브이, 3
	JIGGLYPUFF("jigglypuff.png"),		// 푸린, 4 
	PIKACHU("pikachu.png"), 			// 피카추, 5
	SQUIRTLE("squirtle.png"), 			// 꼬부기, 6
	POKEBALL("pokeball.png"),			// 체크 용도 
	DRAGONITE("dragonite.png"), 		// 망나뇽  특수 아이템 
	GASTLY("phantom.png"), 			// 고오스  특수 아이템
	SNORLAX("snorlax.png"), 			// 잠맘보  특수 아이템
	PUCRIN("pucrin.png"),				// 푸크린 특수 아이템
	MEW("mew.png"),      				// 뮤 특수 아이템
	TODOGAS("todogas.png"),				// 또도가스 특수 아이템 
	RAICHU("raichu.png"),				// 라이츄 특수 아이템
	CELEBI("celebi.png"),				// 세레비 특수 아이템
	METAMONG("metamong.png"); 			// 메타몽 특수 아이템
	private Image image;
	private Pokemon(String fileName){
		this.image = new Image(fileName);
	}
	public Image getImage(){
		return image;
	}
}
