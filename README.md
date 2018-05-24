# 포켓몬팡

## 1. 개요
2017년도 2학기 자바 프로그래밍 및 실습 과목 학기 프로젝트.

## 2. 개발 언어
Java

### 2.1. 개발 라이브러리
JavaFX

## 3. 개발 툴
Eclipse

## 4. 게임 규칙
### 4.1. 기본 규칙
기본 규칙은 애니팡과 유사함.
- 게임 시간은 1분
- 가로와 세로 7칸씩 총 49개의 칸으로 구성
- 7종류의 기본 포켓몬 등장
  - ![이상해씨](https://github.com/Mellonia/PokemonPang/blob/master/PokemonPang/src/bulbasaur.png?raw=true)이상해씨, ![파이리](https://github.com/Mellonia/PokemonPang/blob/master/PokemonPang/src/charmander.png?raw=true)파이리, ![꼬부기](https://github.com/Mellonia/PokemonPang/blob/master/PokemonPang/src/squirtle.png?raw=true)꼬부기, ![피카츄](https://github.com/Mellonia/PokemonPang/blob/master/PokemonPang/src/pikachu.png?raw=true)피카츄, ![이브이](https://github.com/Mellonia/PokemonPang/blob/master/PokemonPang/src/eevee.png?raw=true)이브이, ![푸린](https://github.com/Mellonia/PokemonPang/blob/master/PokemonPang/src/jigglypuff.png?raw=true)푸린, ![브케인](https://github.com/Mellonia/PokemonPang/blob/master/PokemonPang/src/cyndaquil.png?raw=true)브케인
- 가로 또는 세로의 칸을 서로 바꿔 3개 이상 같은 포켓몬이 나란히 놓이게 되면 포켓몬이 팡되면서 순간적으로 몬스터볼로 바뀌는 효과와 함께 점수가 올라간다. 그 후 해당 칸의 포켓몬이 바뀜.

### 4.2. 그 외의 규칙
- 콤보
  - 쉬지 않고 연속해서 맞춘 경우
    - 첫 콤보는 3초 이내
    - 그 다음부터는 2초, 1초, 0.75초, 0.5초


- 힌트
  - 콤보 시간 내에 맞추지 못하면 힌트 표시


- 점수 계산
  - 제거한 포켓몬 * 100
  - 5번 연속인 경우 500점 추가
  - 현재 콤보 수에 따라 100점 추가

### 4.3. 특수 아이템
아이템명|포켓몬|등장조건|효과
:---:|:---:|:---:|:---:
꿈먹기|![팬텀](https://github.com/Mellonia/PokemonPang/blob/master/PokemonPang/src/phantom.png?raw=true)팬텀|5칸을 한 번에 제거한 경우|제거한 포켓몬 수 x 100 + 콤보 수 x 100의 점수 증가
역린|![망나뇽](https://github.com/Mellonia/PokemonPang/blob/master/PokemonPang/src/dragonite.png?raw=true)망나뇽|10번의 팡을 할 때마다|제거한 포켓몬 수 x 100 + 콤보 수 x 100의 점수 증가
잠자기|![잠만보](https://github.com/Mellonia/PokemonPang/blob/master/PokemonPang/src/snorlax.png?raw=true)잠만보|콤보 수가 10 증가할 때마다|시간 5초 추가
노래하기|![푸크린](https://github.com/Mellonia/PokemonPang/blob/master/PokemonPang/src/pucrin.png?raw=true)푸크린|푸린을 10마리 제거할 때마다|10초동안 푸린의 노래가 들리며 모든 동작과 시간이 정지된다. 노래가 끝난 후 재동작된다.
10만볼트|![라이츄](https://github.com/Mellonia/PokemonPang/blob/master/PokemonPang/src/raichu.png?raw=true)라이츄|피카츄를 10마리 제거할 때마다|강력한 전기 공격으로 화면 상의 모든 물타입 포켓몬을 제거한다
대폭발|![또도가스](https://github.com/Mellonia/PokemonPang/blob/master/PokemonPang/src/todogas.png?raw=true)또도가스|매 20,000점마다|또도가스를 둘러싸고 있는 포켓몬들을 모두 제거
변신|![메타몽](https://github.com/Mellonia/PokemonPang/blob/master/PokemonPang/src/metamong.png?raw=true)메타몽|매 5,000점마다|점수와 시간의 변화는 없고 무작위로 아무 포켓몬으로 변한다
시간이동|![세레비](https://github.com/Mellonia/PokemonPang/blob/master/PokemonPang/src/Celebi.png?raw=true)세레비|게임 시간이 10초 이하로 남았을 경우, 세레비 등장 후 두 번 이상의 팡이 진행되면 세레비는 사라진다|점수를 반으로 깎는 대신 시간을 초기화한다
나쁜음모|![뮤](https://github.com/Mellonia/PokemonPang/blob/master/PokemonPang/src/mew.png?raw=true)뮤|게임 시간이 15초 이하로 남고 모든 포켓몬을 20마리 이상 팡했을 경우|팡 점수 2배로 증가
