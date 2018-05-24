import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Timer;

/**
 * 한국기술교육대학교 컴퓨터공학부
 * 2017년도 2학기 학기 프로젝트: 포켓몬팡
 * @author 박영호 
 * 포켓몬팡의 모델 클래스: 게임 로직 구현
 */

public class PangGridModel {
	private Pokemon[][] gridData 
		= new Pokemon[PangUtility.GRIDSIZE][PangUtility.GRIDSIZE];
	private Random randomGen = new Random();
	private int score = 0;
	private int comboCount = 0;
	private int pangCount = 0;
	
	private int gameOverCount = 60;
	private int defaultScore = 100; // 기본 점수.
	/*
	 * 특수 아이템 관련 멤버들
	 */
	private int [] listOfPang = {0, 0, 0, 0, 0, 0, 0};
	private boolean ghostTime = false;
	private int snorlaxCount = 0;
	private int dragoniteCount = 0;
	private int pucrinCount = 0;
	private int raichuCount = 0;
	private int todogasCount = 0;
	private int metamongCount = 0;
	private int afterCelebiCount = 0; // 세레비 등장 후 카운트.
	private int afterMewCount = 0; // 뮤 등장 후 카운트.
	private boolean existMew = false; // 뮤는 처음엔 없다.
	private boolean existCelebi = false; // 세레비도 처음엔 없다.
	/*
	 * 뷰 및 뷰와 통신할 때 사용하는 데이터
	 */
	private PangGridView view;
	private ModelToViewData data = new ModelToViewData(new ArrayList<Location>(), gridData);
	
	public PangGridModel(PangGridView view){
		this.view = view;
	}
	public void initAssign(){
		randomAssign();
		// debugAssign();
		//debugGridDataOutput();
		
		int[][] checkMatrix = new int[PangUtility.GRIDSIZE][PangUtility.GRIDSIZE];
		if(checkColumn(checkMatrix)){
			for(int r=0; r<PangUtility.GRIDSIZE; r++)
				for(int c=0; c<PangUtility.GRIDSIZE; c++)
					if(checkMatrix[r][c]==2) changePokemon(r,c);
		}
		//debugGridDataOutput();
		checkMatrix = new int[PangUtility.GRIDSIZE][PangUtility.GRIDSIZE];
		
		if(checkRow(checkMatrix)){
			for(int r=0; r<PangUtility.GRIDSIZE; r++)
				for(int c=0; c<PangUtility.GRIDSIZE; c++)
					if(checkMatrix[r][c]==2) changePokemon(r,c);
		}
		//debugGridDataOutput(); 
		
		data.locationList.clear();
		view.updateGrid(data);
		// 점수, 콤보 수, 팡 수, 특수 아이템 관련 값 등의 초기화
		score = comboCount = pangCount = snorlaxCount = dragoniteCount = 
				afterCelebiCount = afterMewCount = 
				pucrinCount = raichuCount = todogasCount = metamongCount = 0;
		Arrays.fill(listOfPang, 0);
		defaultScore = 100;
		gameOverCount = 60;
		ghostTime = existCelebi = existMew = false;
		view.updateCombo(comboCount+"");
		view.updateScore(score+"");
		view.updateTime(gameOverCount+"");
	}
	/**
	 * 7개 포켓몬들을 임의로 배치 
	 */
	private void randomAssign(){
		for(int r=0; r<gridData.length; r++){
			for(int c=0; c<gridData[r].length; c++){
				gridData[r][c] = Pokemon.values()[randomGen.nextInt(PangUtility.NUMBEROFMONS)];
			}
		}
	}
	/**
	 * 주어진 위치의 포켓몬을 바꾸어 팡을 제거함 
	 * 주변 4개와 다른 포켓몬으로 교체
	 */
	private void changePokemon(int r, int c){
		Set<Integer> set = new HashSet<>();
		Collections.addAll(set, 0,1,2,3,4,5,6);
		if(Location.valid(r-1,c)) set.remove(gridData[r-1][c].ordinal());
		if(Location.valid(r+1,c)) set.remove(gridData[r+1][c].ordinal());
		if(Location.valid(r,c-1)) set.remove(gridData[r][c-1].ordinal());
		if(Location.valid(r,c+1)) set.remove(gridData[r][c+1].ordinal());
		
		int idx = randomGen.nextInt(set.size());
		Integer[] list = new Integer[set.size()];
		set.toArray(list);
		gridData[r][c] = Pokemon.values()[list[idx]];
	}
	/**
	 * 행 기준으로 팡의 존재를 검사  
	 * 팡이 발견된 위치는 모두 1로 바꾸고
	 * 초기 팡 제거를 위해 포켓몬을 바꾸어야 하는 위치는 2로 바꿈
	 */
	private boolean checkRow(int[][] checkMatrix){
		boolean found = false;
		for(int r=0; r<PangUtility.GRIDSIZE; r++){
			for(int c=0; c<PangUtility.GRIDSIZE-2; c++){
				if(gridData[r][c].ordinal()<PangUtility.NUMBEROFMONS){
					int count = 1;
					for(int i=c+1; i<PangUtility.GRIDSIZE; i++){
						if(gridData[r][c]==gridData[r][i]) ++count;
						else break;
					}
					if(count>=3){
						++pangCount;
						updateScore(count);
						found = true;
						for(	int i=c; i<c+count; i++) checkMatrix[r][i] = 1;
						checkMatrix[r][c+2] = 2;
						if(count>=6) checkMatrix[r][c+5] = 2;
						if(count==5) ghostTime = true;
					}
					c += (count-1);
				}
			}
		}
		
		// debug
		// debugCheckMatrixOutput(checkMatrix);
		return found;
	}
	/**
	 * 열 기준으로 팡의 존재를 검사  
	 * 팡이 발견된 위치는 모두 1로 바꾸고
	 * 초기 팡 제거를 위해 포켓몬을 바꾸어야 하는 위치는 2로 바꿈
	 */
	private boolean checkColumn(int[][] checkMatrix){
		boolean found = false;
		for(int c=0; c<PangUtility.GRIDSIZE; c++){
			for(int r=0; r<PangUtility.GRIDSIZE-2; r++){
				if(gridData[r][c].ordinal()<PangUtility.NUMBEROFMONS){
					int count = 1;
					for(int i=r+1; i<PangUtility.GRIDSIZE; i++){
						if(gridData[r][c]==gridData[i][c]) ++count;
						else break;
					}
					if(count>=3){
						++pangCount;
						updateScore(count);
						found = true;
						for(	int i=r; i<r+count; i++) checkMatrix[i][c] = 1;
						checkMatrix[r+2][c] = 2;
						if(count>=6) checkMatrix[r+5][c] = 2;
						if(count==5) ghostTime = true;
					}
					r += (count-1);
				}
			}
		}
		
		// debug
		// debugCheckMatrixOutput(checkMatrix);		
		return found;
	}
	/*
	 * 점수 계산 방식:
	 * 1) 제거한 포켓몬 수 x 100
	 * 2) 5개 연속짜리 제거할 경우 보너스 500점 추가
	 * 3) 현재 콤보 수 x 100
	 */
	private void updateScore(int count){
		score += count*defaultScore;
		if(count==5) score += 500;
		score += comboCount*defaultScore;
		view.updateScore(score+"");
	}
	public void updateCombo(boolean isReset){
		comboCount = isReset? 0: comboCount+1;
		view.updateCombo(comboCount+"");
	}
	public void updateGameTime(){
		--gameOverCount;
		view.updateTime(gameOverCount+"");
	}
	public int getScore(){
		return score;
	}
	public int getTimeLeft(){
		return gameOverCount;
	}
	public int getCombo(){
		return comboCount;
	}
	public int getPangCount(){
		return pangCount;
	}
	/**
	 * 힌트 위치를 모두 찾음
	 * 특수 아이템은 힌트나 팡은 아니지만 그것을 선택하여 게임을 추가 진행할 수 있음
	 */
	boolean findHints(){
		boolean hasSpecialPokemon = false;
		ArrayList<Location> hintLocs = new ArrayList<>();
		for(int r=0; r<PangUtility.GRIDSIZE; r++){
			for(int c=0; c<PangUtility.GRIDSIZE; c++){
				if(gridData[r][c].ordinal()>=PangUtility.NUMBEROFMONS)
					hasSpecialPokemon = true;
				if(leftEquals(r,c)||rightEquals(r,c)||upEquals(r,c)||downEquals(r,c)){
					hintLocs.add(new Location(r,c));
				}
			}
		}
		if(!hintLocs.isEmpty()){
			// debug
			/*
			for(Location loc: hintLocs){
				System.out.print("("+loc.r+","+loc.c+"), ");
			}
			System.out.println();
			*/
			int idx = randomGen.nextInt(hintLocs.size());
			view.updateHintLoc(hintLocs.get(idx));
			return true;
		}
		else{
			view.updateHintLoc(null);
			return hasSpecialPokemon;
		}
	}
//	public boolean purinPang(){
//		int[][] checkMatrix = new int[PangUtility.NUMBEROFMONS][PangUtility.NUMBEROFMONS];
//		boolean flag = checkColumn(checkMatrix);
//		flag = checkRow(checkMatrix)||flag;
//		if(flag){
//			data.locationList.clear();
//			for(int r=0; r<PangUtility.NUMBEROFMONS; r++)
//				for(int c=0; c<PangUtility.NUMBEROFMONS; c++)
//					if(checkMatrix[r][c]>=1){
//						if(gridData[r][c] == Pokemon.JIGGLYPUFF)
//							purinPangCount++;
//					}
//		}
//		return true;
//	}
	// 특수 아이템 관련 메소드들
	
	/*
	 * 특수아이템이 없는 랜덤 위치 확보
	 */
	public ArrayList<Location> getSpecialLocs(Pokemon pokemon){
		ArrayList<Location> locs = new ArrayList<>();
		for(int r=0; r<PangUtility.GRIDSIZE; r++)
			for(int c=0; c<PangUtility.GRIDSIZE; c++)
				if(gridData[r][c]==pokemon) locs.add(new Location(r,c));
		return locs;
	}
	private Location getNormalLoc(){
		Location location = null;
		while(true){
			int row = randomGen.nextInt(PangUtility.GRIDSIZE);
			int col = randomGen.nextInt(PangUtility.GRIDSIZE);
			if(gridData[row][col].ordinal()<PangUtility.NUMBEROFMONS){
				location = new Location(row, col);
				break;
			}
		}
		return location;
	}
	public void insertSpecialPokemon(){
		data.locationList.clear();
		if(ghostTime){
			Location ghostLoc = getNormalLoc();
			gridData[ghostLoc.r][ghostLoc.c] = Pokemon.GASTLY;
			ghostTime = false;
			data.locationList.add(ghostLoc);
		}
		if(comboCount/10>snorlaxCount){
			Location snorLoc = getNormalLoc();
			gridData[snorLoc.r][snorLoc.c] = Pokemon.SNORLAX;
			data.locationList.add(snorLoc);
			++snorlaxCount;
		}
		if(pangCount/10>dragoniteCount){
			Location dragonLoc = getNormalLoc();
			gridData[dragonLoc.r][dragonLoc.c] = Pokemon.DRAGONITE;
			data.locationList.add(dragonLoc);
			++dragoniteCount;
		}
		if(listOfPang[4]/10 > pucrinCount){
			Location pucrin = getNormalLoc();
			gridData[pucrin.r][pucrin.c] = Pokemon.PUCRIN;
			data.locationList.add(pucrin);
			++pucrinCount;
		}
		if(listOfPang[5]/10 > raichuCount){
			Location raichu = getNormalLoc();
			gridData[raichu.r][raichu.c] = Pokemon.RAICHU;
			data.locationList.add(raichu);
			++raichuCount;
		}
		if(score / 20000 > todogasCount){
			Location todogas = getNormalLoc();
			gridData[todogas.r][todogas.c] = Pokemon.TODOGAS;
			data.locationList.add(todogas);					
			++todogasCount;
		}
		if(score / 5000 > metamongCount ){
			Location metamong = getNormalLoc();
			gridData[metamong.r][metamong.c] = Pokemon.METAMONG;
			data.locationList.add(metamong);
			++metamongCount;
		}
		if(gameOverCount < 10 && !existCelebi && afterCelebiCount == 0){
			existCelebi = true;
			Location celebi = getNormalLoc();
			gridData[celebi.r][celebi.c] = Pokemon.CELEBI;
			data.locationList.add(celebi);
		}
		if(gameOverCount < 15 && checkMakeMew() && !existMew && afterMewCount == 0){
			existMew = true;
			Location mew = getNormalLoc();
			gridData[mew.r][mew.c] = Pokemon.MEW;
			data.locationList.add(mew);
		}
		view.updateGrid(data);
	}
	public boolean processSpecialPokemon(int r,int c){
		switch(gridData[r][c]){
		case DRAGONITE: 
			clearRowAndColumn(r, c); 
			Sound.play("dragonite");
			return true;
		case GASTLY: removeRandomPokemon(r,c); return true;
		case SNORLAX: 
			Sound.play("snorlax");
			increaseGameTime(r,c); 
			Sound.stop("snorlax");
			return true;
		case PUCRIN:
			sing(r,c);
			return true;
		case RAICHU:
			Sound.play("pika");
			thunderBolt(r,c);
			return true;
		case TODOGAS:
			Sound.play("todogas");
			explosion(r,c);
			return true;
		case MEW:
			Sound.play("mew");
			nastyPlot(r,c);
			return true;
		case METAMONG:
			Sound.play("metamong");
			transform(r,c);
			return true;
		case CELEBI:
			Sound.play("celebi");
			timeLeap(r,c);
			return true;
		default: return false;
		}
	}
	private void clearRowAndColumn(int row, int col){
		for(int r=0; r<PangUtility.GRIDSIZE; r++){
			gridData[r][col] = Pokemon.POKEBALL;
		}
		for(int c=0; c<PangUtility.GRIDSIZE; c++){
			gridData[row][c] = Pokemon.POKEBALL;
		}
		score += defaultScore*13;
		view.updateScore(score+"");
	}
	private void removeRandomPokemon(int row, int col){
		Pokemon removePokemon = Pokemon.values()[randomGen.nextInt(7)];
		int count = 1;
		for(int r=0; r<PangUtility.NUMBEROFMONS; r++){
			for(int c=0; c<PangUtility.NUMBEROFMONS; c++){
				if(gridData[r][c]==removePokemon){
					gridData[r][c]=Pokemon.POKEBALL;
					++count;
				}
			}
		}
		gridData[row][col]=Pokemon.POKEBALL;
		score += defaultScore*count;
		view.updateScore(score+"");
	}
	private void increaseGameTime(int r, int c){
		gridData[r][c] = Pokemon.POKEBALL;
		gameOverCount += 5;
		score += defaultScore;
		view.updateTime(gameOverCount+"");
		view.updateScore(score+"");
	}
	// 노래하기 : 노래를 해서 10초 동안 아무 것도 할 수 없도록 만든다. 하지만 10초간 여유가 생긴다.
	private void sing(int r, int c){
		gameOverCount += 10;
		try{
			Sound.stop("game_bgm1");
			Sound.stop("game_bgm2");
			Sound.stop("game_bgm3"); // 모든 배경음악 종료 
			Sound.play("pucrin"); // 푸린 노래 재생.
			Thread.sleep(10000);
		} catch(InterruptedException e){}
		Sound.play("game_bgm3"); // 다시 배경음악 재생.
		gridData[r][c] = Pokemon.POKEBALL;
		score += defaultScore;
		view.updateScore(score+"");
	}
	// 10만볼트 : 화면 상의 모든 물타입 포켓몬인 꼬부기를 다 팡시켜서 제거한다.
	private void thunderBolt(int row, int col){ 
		gridData[row][col] = Pokemon.POKEBALL;
		score += defaultScore;
		for(int r = 0; r < PangUtility.GRIDSIZE; r++){
			for(int c = 0; c < PangUtility.GRIDSIZE; c++){
				if(gridData[r][c] == Pokemon.SQUIRTLE){
					gridData[r][c] = Pokemon.POKEBALL;
					score += defaultScore;
				}
			}
		}
		view.updateScore(score+"");
	}
	// 대폭발 : 대폭발을 일으켜 둘러싸고있는 주위를 다 팡시켜서 제거한다.
	private void explosion(int r, int c){
		gridData[r][c] = Pokemon.POKEBALL;
		score += defaultScore;
		// 가장 자리가 아닌 안 쪽.
		if(r >= 1 && c >= 1 && r < PangUtility.GRIDSIZE-1 && c < PangUtility.GRIDSIZE-1){
			gridData[r-1][c-1] = gridData[r-1][c] = gridData[r-1][c+1]
			= gridData[r][c-1] = gridData[r][c+1]
			= gridData[r+1][c-1] = gridData[r+1][c] = gridData[r+1][c+1] = Pokemon.POKEBALL;
			score+=defaultScore*15;
		} 
		// 각 꼭지점.
		else if(r == 0 && c == 0){
			gridData[r+1][c] = gridData[r][c+1] = Pokemon.POKEBALL;
			score+=defaultScore*2;
		}
		else if(r == 0 && c == PangUtility.GRIDSIZE-1){
			gridData[r][c-1] = gridData[r+1][c] = Pokemon.POKEBALL;
			score+=defaultScore*2;
		}
		else if(r == PangUtility.GRIDSIZE-1 && c == 0){
			gridData[r-1][c] = gridData[r][c+1] = Pokemon.POKEBALL;
			score+=defaultScore*2;
		}
		else if(r == PangUtility.GRIDSIZE-1 && c == PangUtility.GRIDSIZE-1){
			gridData[r-1][c] = gridData[r][c-1] = Pokemon.POKEBALL;
			score+=defaultScore*2;
		}
		// 꼭지점이 아닌 가장자리.
		else if(r == 0 && c != 0 && c != PangUtility.GRIDSIZE-1){
			gridData[r][c-1] = gridData[r][c+1] = gridData[r+1][c] = Pokemon.POKEBALL;
			score+=defaultScore*4;
		}
		else if(r == PangUtility.GRIDSIZE-1 && c != 0 && c != PangUtility.GRIDSIZE-1){
			gridData[r][c-1] = gridData[r][c+1] = gridData[r-1][c] = Pokemon.POKEBALL;
			score+=defaultScore*4;
		}
		else if(c == 0 && r != 0 && r != PangUtility.GRIDSIZE-1){
			gridData[r-1][c] = gridData[r+1][c] = gridData[r][c+1] = Pokemon.POKEBALL;
			score+=defaultScore*4;
		}
		else if(c == PangUtility.GRIDSIZE-1 & r != 0 && r != PangUtility.GRIDSIZE-1){
			gridData[r-1][c] = gridData[r+1][c] = gridData[r][c-1] = Pokemon.POKEBALL;
			score+=defaultScore*4;
		}
		view.updateScore(score+"");
	}
	// 변신 : 랜덤으로 아무 포켓몬으로 변한다.
	public void transform(int r, int c){
		gridData[r][c] = Pokemon.values()[randomGen.nextInt(PangUtility.NUMBEROFMONS)];
	}
	// 게임 시간을 초기화하고 점수를 반으로 두동강낸다.
	public void timeLeap(int r, int c){ // 시간과 점수 초기화.
		gridData[r][c] = Pokemon.POKEBALL;
		score += defaultScore;
		gameOverCount = 60;
		score /= 2;
		view.updateScore(score+"");
		view.updateTime(gameOverCount+"");
	}
	// 팡 점수를 2배로 올림.
	public void nastyPlot(int r, int c){
		for(int i = 0; i < listOfPang.length; i++)
			listOfPang[i] = 0;
		gridData[r][c] = Pokemon.POKEBALL;
		score += defaultScore;
		defaultScore *= 2;
		view.updateScore(score+"");
	}
	// 팡을 통해 포켓몬이 몇마리나 터졌는지(...) 체크.
	public void checkPokemonPang(int r, int c){
		if(gridData[r][c] == Pokemon.BULBASAUR) listOfPang[0]++;
		else if(gridData[r][c] == Pokemon.CHARMANDER) listOfPang[1]++;
		else if(gridData[r][c] == Pokemon.CYNDAQUIL) listOfPang[2]++;
		else if(gridData[r][c] == Pokemon.EEVEE) listOfPang[3]++;
		else if(gridData[r][c] == Pokemon.JIGGLYPUFF) listOfPang[4]++;
		else if(gridData[r][c] == Pokemon.PIKACHU) listOfPang[5]++;
		else if(gridData[r][c] == Pokemon.SQUIRTLE) listOfPang[6]++;
//		System.out.println("이상해씨 : " + listOfPang[0] + ", 파이리 : " + listOfPang[1] + ", 브케인 : " + listOfPang[2] + ", 이브이 : " +
//		listOfPang[3] + ", 푸린 : " + listOfPang[4] + ", 피카츄 : " + listOfPang[5] + ", 꼬부기 : " + listOfPang[6]);
	}
	// 뮤 생성 조건 함수. 모든 일반 포켓몬이 20마리 이상 터졌을 경우 뮤 생성.
	public boolean checkMakeMew(){
		boolean makeMew = (listOfPang[0] >= 20);
		if(!makeMew) return makeMew;
		else{
			for(int i = 1; i < listOfPang.length; i++){
				makeMew &= (listOfPang[i] >= 20);
				if(!makeMew) return makeMew;
			}
		}
		return makeMew;
	}
	// 세레비 없애는 조건 함수.
	public void removeCelebi(){
		afterCelebiCount = 0;
		existCelebi = false;
		for(int r=0; r<PangUtility.NUMBEROFMONS; r++)
			for(int c=0; c<PangUtility.NUMBEROFMONS; c++)
				if(gridData[r][c] == Pokemon.CELEBI)
					gridData[r][c] = Pokemon.values()[randomGen.nextInt(7)];
	}
	// 뮤 없애는 조건 함수.
	public void removeMew(){
		afterMewCount = 0;
		existMew = false;
		for(int r=0; r<PangUtility.NUMBEROFMONS; r++)
			for(int c=0; c<PangUtility.NUMBEROFMONS; c++)
				if(gridData[r][c] == Pokemon.MEW)
					gridData[r][c] = Pokemon.values()[randomGen.nextInt(7)];	
	}
	public boolean isGameOver(){
		return gameOverCount==0;
	}
	// 특수 아이템 관련 메소드 끝
	
	/*
	 * 팡이 있는 위치들을 Pokemon.POKEBALL로 교체
	 */
	public boolean checkAndMark(){
		int[][] checkMatrix = new int[PangUtility.NUMBEROFMONS][PangUtility.NUMBEROFMONS];
		boolean flag = checkColumn(checkMatrix);
		flag = checkRow(checkMatrix)||flag;
		if(flag){
			data.locationList.clear();
			for(int r=0; r<PangUtility.NUMBEROFMONS; r++)
				for(int c=0; c<PangUtility.NUMBEROFMONS; c++)
					if(checkMatrix[r][c]>=1){
//						if(gridData[r][c] == Pokemon.JIGGLYPUFF){
//							purinPangCount++;
//							System.out.println(purinPangCount);
//						}
//						if(gridData[r][c] == Pokemon.PIKACHU){
//							pikachuPangCount++;
//							System.out.println(pikachuPangCount);
//						}
						checkPokemonPang(r, c);
						gridData[r][c] = Pokemon.POKEBALL;
						data.locationList.add(new Location(r,c));
					}
					if(existCelebi) afterCelebiCount++;
//					System.out.println("세레비카운트 : " + afterCelebiCount);
					if(afterCelebiCount >= 2) removeCelebi();
					if(existMew) afterMewCount++;
					if(afterMewCount >= 2) removeMew();
			view.updateGrid(data);
			return true;
		}
		return false;
	}
	/*
	 * 팡 위치 위에 있는 포켓몬들을 아래로 내려 포켓볼들이 맨 위에 위치하도록 함
	 * 열 단위로 작업
	 * 각 열마다 마직막 행부터 검사
	 * 		볼을 만나면 위치 기록
	 * 		볼을 만난 이후 포켓몽을 처음 만나면 최초 볼 위치와 교환
	 * 		그 다음 볼 위치 찾음
	 *  23@42@6 => 23@4@26 => 23@@426 => 2@@3426 => @@23426
	 *  다른 방법: 23@42@6 > 23426 추출 (큐에 삽입) > @@23456로 변경
	 */
	public void pushUpMarked(){
		/*
		for(int c=0; c<PangUtility.GRIDSIZE; c++){
			int ballLoc = -1;
			for(int r=PangUtility.GRIDSIZE-1; r>=0; r--){
				if(ballLoc==-1&&gridData[r][c]==Pokemon.POKEBALL){
					ballLoc = r;
				}
				else if(ballLoc!=-1&&gridData[r][c]!=Pokemon.POKEBALL){
					gridData[ballLoc][c] = gridData[r][c];
					gridData[r][c] = Pokemon.POKEBALL;
					int prevLoc = ballLoc;
					ballLoc = -1;
					for(int i=prevLoc-1; i>=0; i--)
						if(gridData[i][c]==Pokemon.POKEBALL){
							ballLoc = i; 
							break;
						}
				}
			}
		}*/
		Queue<Pokemon> queue = new ArrayDeque<>();
		for(int c=0; c<PangUtility.GRIDSIZE; c++){
			queue.clear();
			boolean hasBall = false;
			for(int r=PangUtility.GRIDSIZE-1; r>=0; r--){
				if(gridData[r][c]==Pokemon.POKEBALL) hasBall = true;
				else queue.add(gridData[r][c]);
			}
			//System.out.println(queue.toString());
			if(hasBall){
				int r = PangUtility.GRIDSIZE-1;
				while(!queue.isEmpty()){
					gridData[r][c] = queue.poll();
					--r;
				}
				for(;r>=0;r--) gridData[r][c]=Pokemon.POKEBALL;
			}
		}
	}
	/*
	 * 포켓볼들을 랜덤 포켓몽으로 교체함
	 * 바꾼 부분만 전달할 수 있고 전체 교체로 처리할 수도 있음
	 * replaceMarked는 항상 pushUpMarked 이후 호출되는 점 이용할 필요가 있음
	 */
	public void replaceMarked(){
		data.locationList.clear();
		/*
		// 전체 교체
		for(int c=0; c<PangUtility.NUMBEROFMONS; c++){
			for(int r=0; r<PangUtility.NUMBEROFMONS; r++){
				if(gridData[r][c]==Pokemon.POKEBALL){
					gridData[r][c] = Pokemon.values()[randomGen.nextInt(PangUtility.NUMBEROFMONS)];
				}
				else break;
			}
		}
		*/
		// 볼 있는 위치만 교체 
		for(int c=0; c<PangUtility.GRIDSIZE; c++){
			boolean hasPokeball = false;
			for(int r=0; r<PangUtility.GRIDSIZE; r++){
				if(gridData[r][c]==Pokemon.POKEBALL){
					gridData[r][c] = Pokemon.values()[randomGen.nextInt(PangUtility.NUMBEROFMONS)];
				}
				else break;
			}
			if(hasPokeball){
				for(int r=0; r<PangUtility.GRIDSIZE; r++) data.locationList.add(new Location(r,c));
			}
		}
		
		view.updateGrid(data);
	}
	
	public boolean isValidSwap(Location srcLoc, Location destLoc){
		if((srcLoc.r==destLoc.r)&&(srcLoc.c==destLoc.c+1)){
			return (leftEquals(srcLoc.r,srcLoc.c)||rightEquals(destLoc.r,destLoc.c));
		}
		else if((srcLoc.r==destLoc.r)&&(srcLoc.c==destLoc.c-1)){
			return (rightEquals(srcLoc.r,srcLoc.c)||leftEquals(destLoc.r,destLoc.c));
		}
		else if((srcLoc.c==destLoc.c)&&(srcLoc.r==destLoc.r+1)){
			return (upEquals(srcLoc.r,srcLoc.c)||downEquals(destLoc.r,destLoc.c));
		}
		else if((srcLoc.c==destLoc.c)&&(srcLoc.r==destLoc.r-1)){
			return (downEquals(srcLoc.r,srcLoc.c)||upEquals(destLoc.r,destLoc.c));
		}
		else return false;
	}
	public void swap(Location srcLoc, Location destLoc){
		Pokemon tmp = gridData[srcLoc.r][srcLoc.c];
		gridData[srcLoc.r][srcLoc.c] = gridData[destLoc.r][destLoc.c];
		gridData[destLoc.r][destLoc.c] = tmp;
		data.locationList.clear();
		data.locationList.add(srcLoc);
		data.locationList.add(destLoc);
		view.updateGrid(data);
	}
	
	private boolean equals(Pokemon... list){
		if(list.length<=1) throw new IllegalArgumentException();
		Pokemon p = list[0];
		for(int i=1; i<list.length; i++)
			if(p!=list[i]) return false;
		return true;
	}
	
	/*
	 *  1) x@xx
	 *  2) 	 x
	 * 		x@
	 *  	 x
	 *  3) 	 x
	 *  	 x
	 *  	x@
	 *  4) 	x@
	 *  	 x
	 *  	 x
	 */
	private boolean rightEquals(int r, int c){
		return 
			((Location.valid(r, c+3) // x@xx
				&&equals(gridData[r][c],gridData[r][c+2],gridData[r][c+3]))||
			(Location.valid(r-1, c+1)&&Location.valid(r+1, c+1)
				&&equals(gridData[r][c],gridData[r-1][c+1],gridData[r+1][c+1]))||
			(Location.valid(r-2,c+1)
				&&equals(gridData[r][c],gridData[r-1][c+1],gridData[r-2][c+1]))||
			(Location.valid(r+2, c+1)
				&&equals(gridData[r][c],gridData[r+1][c+1],gridData[r+2][c+1])));
	}
	/*
	 *  1) xx@x
	 *  2) 	x
	 * 		@x
	 *  	x
	 *  3) 	x
	 *  	x
	 *  	@x
	 *  4) 	@x
	 *  	x
	 *  	x
	 */
	private boolean leftEquals(int r, int c){
		return ((Location.valid(r, c-3)
					&&equals(gridData[r][c],gridData[r][c-2],gridData[r][c-3]))||
			(Location.valid(r-1, c-1)&&Location.valid(r+1, c-1)
					&&equals(gridData[r][c],gridData[r-1][c-1],gridData[r+1][c-1]))||
			(Location.valid(r-2,c-1)
					&&equals(gridData[r][c],gridData[r-1][c-1],gridData[r-2][c-1]))||
			(Location.valid(r+2, c-1)
				&&equals(gridData[r][c],gridData[r+1][c-1],gridData[r+2][c-1])));
	}
	private boolean upEquals(int r, int c){
		return ((Location.valid(r-3, c)
					&&equals(gridData[r][c],gridData[r-2][c],gridData[r-3][c]))||
			(Location.valid(r-1, c-1)&&Location.valid(r-1, c+1)
				&&equals(gridData[r][c],gridData[r-1][c-1],gridData[r-1][c+1]))||	
			(Location.valid(r-1,c-2)
				&&equals(gridData[r][c],gridData[r-1][c-1],gridData[r-1][c-2]))||
			(Location.valid(r-1,c+2)
				&&equals(gridData[r][c],gridData[r-1][c+1],gridData[r-1][c+2])));
		
	}
	private boolean downEquals(int r, int c){
		return ((Location.valid(r+3, c)
					&&equals(gridData[r][c],gridData[r+2][c],gridData[r+3][c]))||
			(Location.valid(r+1, c-1)&&Location.valid(r+1, c+1)
				&&equals(gridData[r][c],gridData[r+1][c-1],gridData[r+1][c+1]))||		
			(Location.valid(r+1,c-2)
				&&equals(gridData[r][c],gridData[r+1][c-1],gridData[r+1][c-2]))||
			(Location.valid(r+1,c+2)
				&&equals(gridData[r][c],gridData[r+1][c+1],gridData[r+1][c+2])));
	}
	// debug related methods
	private void debugGridDataOutput(){
		for(int r=0; r<gridData.length; r++){
			for(int c=0; c<gridData[r].length; c++){
				System.out.print(gridData[r][c].ordinal()+",");
			}
			System.out.println();
		}
		System.out.println();
	}
	/*
	private void debugCheckMatrixOutput(int[][] checkMatrix){
		for(int r=0; r<gridData.length; r++){
			for(int c=0; c<gridData[r].length; c++){
				System.out.print(checkMatrix[r][c]+",");
			}
			System.out.println();
		}
		System.out.println();
	}
	*/
	private void debugAssign(){
		/*
		int[][] list = {
				{4,4,4,4,5,4,5},
				{3,0,2,2,2,0,4},
				{6,3,6,0,6,0,3},
				{6,6,5,0,2,0,5},
				{6,2,0,2,4,3,5},
				{6,2,3,3,3,5,5},
				{6,1,0,5,1,4,6},
		};
		*/
		int[][] list = {
				{4,4,4,4,5,4,5},
				{3,0,2,2,2,0,4},
				{6,3,6,0,6,0,3},
				{6,2,5,0,2,0,5},
				{3,6,0,2,4,3,5},
				{6,2,3,3,3,5,5},
				{6,1,0,5,1,4,6},
		};
		for(int r=0; r<gridData.length; r++){
			for(int c=0; c<gridData[r].length; c++){
				gridData[r][c] = Pokemon.values()[list[r][c]];
			}
		}
		
	}
}
