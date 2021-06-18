package Coin_Game;
//https://programmingworld1.tistory.com/11를 바탕으로 코드를 제작

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class CoinEatGame extends JFrame {
	
	GamePanel panel; // 패널 선언
	GameThread gThread; // 스레드 선언
	
	private Audio backgroundMusic; // bgm 오디오 생성
	//Kubbi - Digestive biscuit (NO COPYRIGHT) 8-bit Musicc
	
	int playerlife = 5; // 플레이어 라이프 선언 (라이프가 0이 되면 게임 오버)
	public int score; //점수
	
	public CoinEatGame() {
		
		setTitle("동전 먹기 게임"); // 메인 프레임 제목 지정
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(300, 300, 700, 700); // 메인 프레임 크기 지정
		setResizable(false); // 프레임 크기 고정 
		
		backgroundMusic = new Audio("Audio/backgroundMusic.wav", true); // bgm 파일 결로 선언
		
		panel = new GamePanel(); // 게임 패널 생성
		add(panel,BorderLayout.CENTER); // 레이아웃 위치 설정

		setVisible(true); //프레임을 보이게 한다.
		gThread = new GameThread(); // 게임 스레드 생성


		//프레임에 키보드 입력에 반응하는 keyListner 등록
		addKeyListener(new KeyListener() {			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}	
			
			
			//키보드를 땠을 때 호출, 방향키 4개에 반응합니다. 
			@Override
			public void keyReleased(KeyEvent e) {
				
				int keyCode = e.getKeyCode();
				
				//방향키 4개 구분	
				switch( keyCode ) {
				case KeyEvent.VK_LEFT:
					panel.dx = 0; 
					break;
				case KeyEvent.VK_RIGHT:
					panel.dx = 0;
					break;

				case KeyEvent.VK_UP:
					panel.dy = 0;
					break;

				case KeyEvent.VK_DOWN:
					panel.dy = 0;
					break;
					
				}
			
			}			

			
			//키보드를 눌렀을 때 호출, 방향키 4개에 반응합니다.
			@Override
			public void keyPressed(KeyEvent e) {
				
				int keyCode = e.getKeyCode();
				
				//방향키 4개 구분	
				switch( keyCode ) {

				case KeyEvent.VK_LEFT:
					panel.dx = -8; // 왼쪽으로 8씩 이동
					break;

				case KeyEvent.VK_RIGHT:
					panel.dx = 8;  // 오른쪽으로 8씩 이동
					break;

				case KeyEvent.VK_UP:
					panel.dy = -8;  // 위로으로 8씩 이동
					break;

				case KeyEvent.VK_DOWN:
					panel.dy = 8;  // 아래로 8씩 이동
					break;	
					
				case KeyEvent.VK_ENTER: // ENTER을 눌러 게임 시작
					
					gThread.start(); // 스레드 시작
					backgroundMusic.start(); // bgm 시작
					break;	
					
					
				}
				
			}
		});
	}



	class GamePanel extends JPanel { //게임화면 그려낼 Panel

		
		//화면에 보여질 이미지 객체 참조변수 - 멤버변수
		Image imgBack, imgPlayer, imgNormalCoin, imgReducedCoin, imgSpecialCoin;

		int width, height;//패널 사이즈 가져오기

		int x, y, w, h; //x,y : 플레이어의 중심 좌표 / w,h : 이미지 절반폭;

		int dx = 0, dy = 0; //  d = 플레이어 이미지의 이동속도, 이동방향
		
		
		
		private Audio playergetcoinSound; // 플레이어가 노말, 스폐셜코인을 얻을때의 오디오 생성
		private Audio playerhitSound; // 플레이어가  방해 코인을 얻을때의 오디오  오디오 생성
		
		//코인 객체 참조변수, 코인이 여러개일수있으므로 ArrayList(유동적 배열) 활용
		ArrayList<Normal_Coin> NCoin = new ArrayList<Normal_Coin>();
		ArrayList<Reduced_Coin> Rcoin = new ArrayList<Reduced_Coin>();
		ArrayList<Special_Coin> Scoin = new ArrayList<Special_Coin>();
		
		
		public GamePanel() { // 이미지 저장

			//GUI 관련 프로그램의 편의를 위해 만들어진 도구상자(Toolkit) 객체 
			Toolkit toolkit = Toolkit.getDefaultToolkit();

			imgBack = toolkit.getImage("images/bg.png"); //배경 이미지
			imgPlayer = toolkit.getImage("images/player.png"); //플레이어 이미지 객체
			imgNormalCoin = toolkit.getImage("images/NormalCoin.png"); // 코인 이미지 객체 
			imgReducedCoin = toolkit.getImage("images/ReducedCoin.png"); // 방해 코인 이미지 객체 
			imgSpecialCoin = toolkit.getImage("images/SpecialCoin.png");  // 스페셜 코인 이미지 객체
			
		}	


		//화면에 보여질때 보여질 내용물 작업을 수행하는 메소드 : 자동 실행(콜백 메소드)
		@Override
			protected void paintComponent(Graphics g) {
			//화면에 보여질 작업 코딩

			if( width == 0 || height == 0) { //처음 호출시엔 느리게 만들어 안보이지 않다가 이후 보임

				//생성자에서 사이즈를 구하려하면 무조건 0임, 아직 패널이 안붙어서 사이즈를 모르기때문이다.
				//width = getWidth(); 
				//height = getHeight();
				
				width = getWidth();
				height = getHeight();

				//이미지 조절 (리사이징)
				imgBack = imgBack.getScaledInstance(width, height, Image.SCALE_SMOOTH); 
				imgPlayer = imgPlayer.getScaledInstance(64, 64, Image.SCALE_SMOOTH);

				x = width / 2; //플레이어의 좌표 계산
				y = height - 100; //플레이어의 좌표 계산
				w = 64; //이미지 절반넓이
				h = 64; //이미지 절반높이

			}			

			//이곳에 화가객체가 있으므로 그림 그리는 작업은 무조건 여기서시작		
			g.drawImage(imgBack, 0, 0, this); //배경 그리기			

			for(Normal_Coin t : NCoin ) {
				g.drawImage(t.img, t.x-t.w, t.y-t.h, this); // 코인 이미지 그리기
			}
			
			for(Reduced_Coin t : Rcoin ) {
				g.drawImage(t.img, t.x-t.w, t.y-t.h, this); // 방해 코인 이미지 그리기
			}	
			
			for(Special_Coin t : Scoin ) {
				g.drawImage(t.img, t.x-t.w, t.y-t.h, this); // 스폐셜 코인 이미지 그리기
			}
			
			g.drawImage(imgPlayer, x - w, y - h, this); //플레이어	 이미지 그리기
			g.setFont(new Font(null, Font.BOLD, 20)); //점수 폰트 크기및 진하기 조정
			g.drawString("Score : " + score,10, 30); //점수 표시하기
		
			g.setFont(new Font(null, Font.BOLD, 20)); //점수 폰트 크기및 진하기 조정
			g.drawString("Life : " + playerlife ,10, 50); //점수 표시하기
			
			g.drawString("ENTER을 눌러 게임 시작",10, 70); // 게임 시작하는 방법 설명
			
			
			if ( score >= 500  ) // 스코어가 500이 되면 게임 클리어 표시
			{
				gThread.interrupt(); // 스레드 종료
				g.setFont(new Font(null, Font.BOLD, 50)); // 폰트 크기및 진하기 조정
				g.drawString("G A M E  C L E A R",125, 300);
				backgroundMusic.stop();  // 배경음악 종료
									
			}	
			
			if ( playerlife == 0  ) // 라이프가 0이 되면 게임 오버 표시
			{
				gThread.interrupt(); // 스레드 종료
				g.setFont(new Font(null, Font.BOLD, 50)); // 폰트 크기및 진하기 조정
				g.drawString("G A M E  O V E R",125, 300);
				backgroundMusic.stop(); // 배경음악 종료
						
			}	
		}                                       

		
		void move() { //플레이어 및 코인 움직이기 메소드 (좌표 변경)

			//코인들 움직이기
			//중간에 배열의 개수 변경될 여지가 있다면
			//맨 마지막 요소부터 거꾸로 0번 요소까지 역으로 처리해야함.

			for(int i = NCoin.size()-1; i >= 0; i--) {
				Normal_Coin t = NCoin.get(i);			
				t.move();
				if(t.isDead)  //ArrayList에서 제거
					NCoin.remove(i);
			}
			
			for(int i = Rcoin.size()-1; i >= 0; i--) {
				Reduced_Coin t = Rcoin.get(i);			
				t.move();
				if(t.isDead)  //ArrayList에서 제거
					Rcoin.remove(i);
			}	
			
			for(int i = Scoin.size()-1; i >= 0; i--) {
				Special_Coin t = Scoin.get(i);			
				t.move();
				if(t.isDead)  //ArrayList에서 제거
					Scoin.remove(i);
			}	
			
			x += dx; // 객체 이동한거리만큼 더하기
			y += dy; // 객체 이동한거리만큼 더하기

			//모든 객체 좌표가 화면 밖으로 나가지 않도록 제한
			if(x < w) x = w;
			if(x > width - w) x = width - w;
			if(y < h) y = h;
			if(y > height - h) y = height - h;

		}

		//코인 생성 메소드
		void makeCoin() { 

			//생성자에서 사이즈를 구하려하면 무조건 0임, 아직 패널이 안붙어서 사이즈를 모르기때문이다.
			if(width == 0 || height == 0) return;

			Random rnd = new Random(); // 랜덤 함수 선언
			
			int n = rnd.nextInt(20); // 랜덤 함수 범위 설정
			if( n == 1 ) { // 랜덤으로 생성된 숫자가 1이면 코인을 랜덤한 위치에 생성
				NCoin.add(new Normal_Coin(imgNormalCoin, width, height));
			}
			
			
			int i = rnd.nextInt(30);// 랜덤 함수 범위 설정
			if( i == 2 ) { // 랜덤으로 생성된 숫자가 2이면 코인을 랜덤한 위치에 생성
				Rcoin.add(new Reduced_Coin(imgReducedCoin, width, height));
			}
			
			int j = rnd.nextInt(70);// 랜덤 함수 범위 설정
			if( j == 3 ) { // 랜덤으로 생성된 숫자가 3이면 코인을 랜덤한 위치에 생성
				Scoin.add(new Special_Coin(imgSpecialCoin, width, height));
			}	
			
			
		}

		
		//충돌체크 작업 계산 클래스
		void checkCollision() { // 플레이어와 코인의 충돌
			
			playergetcoinSound = new Audio("Audio/playergetcoinSound.wav", false);
			playerhitSound = new Audio("Audio/playerhitSound.wav", false);
			
			if ( score >= 500  ) // 스코어가 500이 되면 게임 클리어
			{
				gThread.interrupt(); // 스레드 종료
				
			}
			
			if (playerlife == 0 ) { // 라이프가 0이 되면 게임 오버
				
				gThread.interrupt(); // 스레드 종료
			}
			
			for(Normal_Coin t : NCoin) { // 노말 코인 충돌 체크
				
				int left = t.x - t.w; 
				int right = t.x + t.w; 
				int top = t.y - t.h;
				int bottom = t.y + t.h;

				//중심 좌표와 이미지가 부딪힌지 확인 
				if(x > left && x < right && y > top && y < bottom) { 
					t.isDead = true; //충돌했음
					score += 10; // 점수 추가
					playergetcoinSound.start(); // 코인 얻을때 소리 재생
				}
					
			}
			
			for(Reduced_Coin t : Rcoin) { // 방해 코인 충돌 체크
				
				int left = t.x - t.w; 
				int right = t.x + t.w; 
				int top = t.y - t.h;
				int bottom = t.y + t.h;

				//중심 좌표와 이미지가 부딪힌지 확인 
				if(x > left && x < right && y > top && y < bottom) {
					t.isDead = true; //충돌했음
					score -= 10; // 점수 제거
					playerlife--; // 플레이어 라이프 감소
					playerhitSound.start(); // 방해 코인 얻을때 소리 재생
				}	
					
			}
			
			for(Special_Coin t : Scoin) { // 스페셜 코인 충돌 체크
				
				int left = t.x - t.w; 
				int right = t.x + t.w; 
				int top = t.y - t.h;
				int bottom = t.y + t.h;

				//중심 좌표와 이미지가 부딪힌지 확인 
				if(x > left && x < right && y > top && y < bottom) {
					t.isDead = true; //충돌했음
					score += 30; // 점수 추가
					playerlife++; // 플레이어 라이프 회복
					playergetcoinSound.start(); // 코인 얻을때 소리 재생
					
				}
					
			}
			
		}
		
		
	}
	
	
	
	//일정 시간마다 게임화면을 갱신시키는 작업 수행하는 별도 스레드 클래스
	
	class GameThread extends Thread {
		 
		@Override
		public void run() {
			while(true) {
				
				//코인 객체 만들어내는 기능 메소드 호출
				panel.makeCoin();
				
				// 플레이어 이동 기능 호출	
				panel.move(); 
				
				//충돌 체크 기능 호출
				panel.checkCollision();
				
				//여러장면 만들기 위해 일정시간마다 다시 그리기(re painting)
				panel.repaint();//GamePanel의 화면 갱신
				
				if(Thread.interrupted()) { // 스레드 종료
	                break;
	            }

				try { //화면 갱신 시간 선언
				
					sleep(15);
					
				} catch (InterruptedException e) {}
			}
		}
	}

	// 메인 함수에서 메인 프레임 실행
	public static void main(String[] args) {
		new CoinEatGame(); 

	}

}