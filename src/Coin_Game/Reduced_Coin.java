package Coin_Game;

import java.awt.Image;
import java.util.Random;

public class Reduced_Coin {

	Image img; //이미지 참조변수
	int x, y; //코인 이미지 중심 좌표
	int w, h; //코인 이미지 절반폭, 절반높이
	int dy; //코인의 변화량
	
	int width, height; //화면(panel)의 사이즈
	
	//객체가 죽었는지 여부
	boolean isDead = false;

	public Reduced_Coin(Image imgReducedCoin, int width, int height) {

		//화면(panel)의 사이즈
		this.width = width;
		this.height = height;

		//멤버변수 값 초기화
		img = imgReducedCoin.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
		w = 32; //이미지 절반넓이
		h = 32; //이미지 절반높이
			
		Random rnd = new Random(); // 랜덤 함수 생성
		
		x = rnd.nextInt(width - w * 2) + w; //w ~ width - w 랜덤하게 떨어지는 코인들의 중심 좌표 계산
		y = -h; // 랜덤하게 떨어지는 코인들의 중심 좌표 계산
		dy =+ rnd.nextInt(20) + 1;// 코인 떨어지는 속도 랜덤하게

	}

	void move() { //Coin의 움직이는 기능 메소드

		y += dy;
		
		//만약 화면 밑으로 나가버리면 객체 없애기
		if( y > height + h ) { //ArrayList에서 제거
			isDead = true; // 객체 제거됨
		}
	}
}