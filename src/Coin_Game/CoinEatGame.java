package Coin_Game;
//https://programmingworld1.tistory.com/11�� �������� �ڵ带 ����

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
	
	GamePanel panel; // �г� ����
	GameThread gThread; // ������ ����
	
	private Audio backgroundMusic; // bgm ����� ����
	//Kubbi - Digestive biscuit (NO COPYRIGHT) 8-bit Musicc
	
	int playerlife = 5; // �÷��̾� ������ ���� (�������� 0�� �Ǹ� ���� ����)
	public int score; //����
	
	public CoinEatGame() {
		
		setTitle("���� �Ա� ����"); // ���� ������ ���� ����
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(300, 300, 700, 700); // ���� ������ ũ�� ����
		setResizable(false); // ������ ũ�� ���� 
		
		backgroundMusic = new Audio("Audio/backgroundMusic.wav", true); // bgm ���� ��� ����
		
		panel = new GamePanel(); // ���� �г� ����
		add(panel,BorderLayout.CENTER); // ���̾ƿ� ��ġ ����

		setVisible(true); //�������� ���̰� �Ѵ�.
		gThread = new GameThread(); // ���� ������ ����


		//�����ӿ� Ű���� �Է¿� �����ϴ� keyListner ���
		addKeyListener(new KeyListener() {			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}	
			
			
			//Ű���带 ���� �� ȣ��, ����Ű 4���� �����մϴ�. 
			@Override
			public void keyReleased(KeyEvent e) {
				
				int keyCode = e.getKeyCode();
				
				//����Ű 4�� ����	
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

			
			//Ű���带 ������ �� ȣ��, ����Ű 4���� �����մϴ�.
			@Override
			public void keyPressed(KeyEvent e) {
				
				int keyCode = e.getKeyCode();
				
				//����Ű 4�� ����	
				switch( keyCode ) {

				case KeyEvent.VK_LEFT:
					panel.dx = -8; // �������� 8�� �̵�
					break;

				case KeyEvent.VK_RIGHT:
					panel.dx = 8;  // ���������� 8�� �̵�
					break;

				case KeyEvent.VK_UP:
					panel.dy = -8;  // �������� 8�� �̵�
					break;

				case KeyEvent.VK_DOWN:
					panel.dy = 8;  // �Ʒ��� 8�� �̵�
					break;	
					
				case KeyEvent.VK_ENTER: // ENTER�� ���� ���� ����
					
					gThread.start(); // ������ ����
					backgroundMusic.start(); // bgm ����
					break;	
					
					
				}
				
			}
		});
	}



	class GamePanel extends JPanel { //����ȭ�� �׷��� Panel

		
		//ȭ�鿡 ������ �̹��� ��ü �������� - �������
		Image imgBack, imgPlayer, imgNormalCoin, imgReducedCoin, imgSpecialCoin;

		int width, height;//�г� ������ ��������

		int x, y, w, h; //x,y : �÷��̾��� �߽� ��ǥ / w,h : �̹��� ������;

		int dx = 0, dy = 0; //  d = �÷��̾� �̹����� �̵��ӵ�, �̵�����
		
		
		
		private Audio playergetcoinSound; // �÷��̾ �븻, ����������� �������� ����� ����
		private Audio playerhitSound; // �÷��̾  ���� ������ �������� �����  ����� ����
		
		//���� ��ü ��������, ������ �������ϼ������Ƿ� ArrayList(������ �迭) Ȱ��
		ArrayList<Normal_Coin> NCoin = new ArrayList<Normal_Coin>();
		ArrayList<Reduced_Coin> Rcoin = new ArrayList<Reduced_Coin>();
		ArrayList<Special_Coin> Scoin = new ArrayList<Special_Coin>();
		
		
		public GamePanel() { // �̹��� ����

			//GUI ���� ���α׷��� ���Ǹ� ���� ������� ��������(Toolkit) ��ü 
			Toolkit toolkit = Toolkit.getDefaultToolkit();

			imgBack = toolkit.getImage("images/bg.png"); //��� �̹���
			imgPlayer = toolkit.getImage("images/player.png"); //�÷��̾� �̹��� ��ü
			imgNormalCoin = toolkit.getImage("images/NormalCoin.png"); // ���� �̹��� ��ü 
			imgReducedCoin = toolkit.getImage("images/ReducedCoin.png"); // ���� ���� �̹��� ��ü 
			imgSpecialCoin = toolkit.getImage("images/SpecialCoin.png");  // ����� ���� �̹��� ��ü
			
		}	


		//ȭ�鿡 �������� ������ ���빰 �۾��� �����ϴ� �޼ҵ� : �ڵ� ����(�ݹ� �޼ҵ�)
		@Override
			protected void paintComponent(Graphics g) {
			//ȭ�鿡 ������ �۾� �ڵ�

			if( width == 0 || height == 0) { //ó�� ȣ��ÿ� ������ ����� �Ⱥ����� �ʴٰ� ���� ����

				//�����ڿ��� ����� ���Ϸ��ϸ� ������ 0��, ���� �г��� �Ⱥپ ����� �𸣱⶧���̴�.
				//width = getWidth(); 
				//height = getHeight();
				
				width = getWidth();
				height = getHeight();

				//�̹��� ���� (������¡)
				imgBack = imgBack.getScaledInstance(width, height, Image.SCALE_SMOOTH); 
				imgPlayer = imgPlayer.getScaledInstance(64, 64, Image.SCALE_SMOOTH);

				x = width / 2; //�÷��̾��� ��ǥ ���
				y = height - 100; //�÷��̾��� ��ǥ ���
				w = 64; //�̹��� ���ݳ���
				h = 64; //�̹��� ���ݳ���

			}			

			//�̰��� ȭ����ü�� �����Ƿ� �׸� �׸��� �۾��� ������ ���⼭����		
			g.drawImage(imgBack, 0, 0, this); //��� �׸���			

			for(Normal_Coin t : NCoin ) {
				g.drawImage(t.img, t.x-t.w, t.y-t.h, this); // ���� �̹��� �׸���
			}
			
			for(Reduced_Coin t : Rcoin ) {
				g.drawImage(t.img, t.x-t.w, t.y-t.h, this); // ���� ���� �̹��� �׸���
			}	
			
			for(Special_Coin t : Scoin ) {
				g.drawImage(t.img, t.x-t.w, t.y-t.h, this); // ����� ���� �̹��� �׸���
			}
			
			g.drawImage(imgPlayer, x - w, y - h, this); //�÷��̾�	 �̹��� �׸���
			g.setFont(new Font(null, Font.BOLD, 20)); //���� ��Ʈ ũ��� ���ϱ� ����
			g.drawString("Score : " + score,10, 30); //���� ǥ���ϱ�
		
			g.setFont(new Font(null, Font.BOLD, 20)); //���� ��Ʈ ũ��� ���ϱ� ����
			g.drawString("Life : " + playerlife ,10, 50); //���� ǥ���ϱ�
			
			g.drawString("ENTER�� ���� ���� ����",10, 70); // ���� �����ϴ� ��� ����
			
			
			if ( score >= 500  ) // ���ھ 500�� �Ǹ� ���� Ŭ���� ǥ��
			{
				gThread.interrupt(); // ������ ����
				g.setFont(new Font(null, Font.BOLD, 50)); // ��Ʈ ũ��� ���ϱ� ����
				g.drawString("G A M E  C L E A R",125, 300);
				backgroundMusic.stop();  // ������� ����
									
			}	
			
			if ( playerlife == 0  ) // �������� 0�� �Ǹ� ���� ���� ǥ��
			{
				gThread.interrupt(); // ������ ����
				g.setFont(new Font(null, Font.BOLD, 50)); // ��Ʈ ũ��� ���ϱ� ����
				g.drawString("G A M E  O V E R",125, 300);
				backgroundMusic.stop(); // ������� ����
						
			}	
		}                                       

		
		void move() { //�÷��̾� �� ���� �����̱� �޼ҵ� (��ǥ ����)

			//���ε� �����̱�
			//�߰��� �迭�� ���� ����� ������ �ִٸ�
			//�� ������ ��Һ��� �Ųٷ� 0�� ��ұ��� ������ ó���ؾ���.

			for(int i = NCoin.size()-1; i >= 0; i--) {
				Normal_Coin t = NCoin.get(i);			
				t.move();
				if(t.isDead)  //ArrayList���� ����
					NCoin.remove(i);
			}
			
			for(int i = Rcoin.size()-1; i >= 0; i--) {
				Reduced_Coin t = Rcoin.get(i);			
				t.move();
				if(t.isDead)  //ArrayList���� ����
					Rcoin.remove(i);
			}	
			
			for(int i = Scoin.size()-1; i >= 0; i--) {
				Special_Coin t = Scoin.get(i);			
				t.move();
				if(t.isDead)  //ArrayList���� ����
					Scoin.remove(i);
			}	
			
			x += dx; // ��ü �̵��ѰŸ���ŭ ���ϱ�
			y += dy; // ��ü �̵��ѰŸ���ŭ ���ϱ�

			//��� ��ü ��ǥ�� ȭ�� ������ ������ �ʵ��� ����
			if(x < w) x = w;
			if(x > width - w) x = width - w;
			if(y < h) y = h;
			if(y > height - h) y = height - h;

		}

		//���� ���� �޼ҵ�
		void makeCoin() { 

			//�����ڿ��� ����� ���Ϸ��ϸ� ������ 0��, ���� �г��� �Ⱥپ ����� �𸣱⶧���̴�.
			if(width == 0 || height == 0) return;

			Random rnd = new Random(); // ���� �Լ� ����
			
			int n = rnd.nextInt(20); // ���� �Լ� ���� ����
			if( n == 1 ) { // �������� ������ ���ڰ� 1�̸� ������ ������ ��ġ�� ����
				NCoin.add(new Normal_Coin(imgNormalCoin, width, height));
			}
			
			
			int i = rnd.nextInt(30);// ���� �Լ� ���� ����
			if( i == 2 ) { // �������� ������ ���ڰ� 2�̸� ������ ������ ��ġ�� ����
				Rcoin.add(new Reduced_Coin(imgReducedCoin, width, height));
			}
			
			int j = rnd.nextInt(70);// ���� �Լ� ���� ����
			if( j == 3 ) { // �������� ������ ���ڰ� 3�̸� ������ ������ ��ġ�� ����
				Scoin.add(new Special_Coin(imgSpecialCoin, width, height));
			}	
			
			
		}

		
		//�浹üũ �۾� ��� Ŭ����
		void checkCollision() { // �÷��̾�� ������ �浹
			
			playergetcoinSound = new Audio("Audio/playergetcoinSound.wav", false);
			playerhitSound = new Audio("Audio/playerhitSound.wav", false);
			
			if ( score >= 500  ) // ���ھ 500�� �Ǹ� ���� Ŭ����
			{
				gThread.interrupt(); // ������ ����
				
			}
			
			if (playerlife == 0 ) { // �������� 0�� �Ǹ� ���� ����
				
				gThread.interrupt(); // ������ ����
			}
			
			for(Normal_Coin t : NCoin) { // �븻 ���� �浹 üũ
				
				int left = t.x - t.w; 
				int right = t.x + t.w; 
				int top = t.y - t.h;
				int bottom = t.y + t.h;

				//�߽� ��ǥ�� �̹����� �ε����� Ȯ�� 
				if(x > left && x < right && y > top && y < bottom) { 
					t.isDead = true; //�浹����
					score += 10; // ���� �߰�
					playergetcoinSound.start(); // ���� ������ �Ҹ� ���
				}
					
			}
			
			for(Reduced_Coin t : Rcoin) { // ���� ���� �浹 üũ
				
				int left = t.x - t.w; 
				int right = t.x + t.w; 
				int top = t.y - t.h;
				int bottom = t.y + t.h;

				//�߽� ��ǥ�� �̹����� �ε����� Ȯ�� 
				if(x > left && x < right && y > top && y < bottom) {
					t.isDead = true; //�浹����
					score -= 10; // ���� ����
					playerlife--; // �÷��̾� ������ ����
					playerhitSound.start(); // ���� ���� ������ �Ҹ� ���
				}	
					
			}
			
			for(Special_Coin t : Scoin) { // ����� ���� �浹 üũ
				
				int left = t.x - t.w; 
				int right = t.x + t.w; 
				int top = t.y - t.h;
				int bottom = t.y + t.h;

				//�߽� ��ǥ�� �̹����� �ε����� Ȯ�� 
				if(x > left && x < right && y > top && y < bottom) {
					t.isDead = true; //�浹����
					score += 30; // ���� �߰�
					playerlife++; // �÷��̾� ������ ȸ��
					playergetcoinSound.start(); // ���� ������ �Ҹ� ���
					
				}
					
			}
			
		}
		
		
	}
	
	
	
	//���� �ð����� ����ȭ���� ���Ž�Ű�� �۾� �����ϴ� ���� ������ Ŭ����
	
	class GameThread extends Thread {
		 
		@Override
		public void run() {
			while(true) {
				
				//���� ��ü ������ ��� �޼ҵ� ȣ��
				panel.makeCoin();
				
				// �÷��̾� �̵� ��� ȣ��	
				panel.move(); 
				
				//�浹 üũ ��� ȣ��
				panel.checkCollision();
				
				//������� ����� ���� �����ð����� �ٽ� �׸���(re painting)
				panel.repaint();//GamePanel�� ȭ�� ����
				
				if(Thread.interrupted()) { // ������ ����
	                break;
	            }

				try { //ȭ�� ���� �ð� ����
				
					sleep(15);
					
				} catch (InterruptedException e) {}
			}
		}
	}

	// ���� �Լ����� ���� ������ ����
	public static void main(String[] args) {
		new CoinEatGame(); 

	}

}