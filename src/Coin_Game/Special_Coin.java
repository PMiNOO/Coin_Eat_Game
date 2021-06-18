package Coin_Game;

import java.awt.Image;
import java.util.Random;


public class Special_Coin {

	Image img; //�̹��� ��������
	int x, y; //���� �̹��� �߽� ��ǥ
	int w, h; //���� �̹��� ������, ���ݳ���
	int dy; //������ ��ȭ��
	
	int width, height; //ȭ��(panel)�� ������
	
	//��ü�� �׾����� ����
	boolean isDead = false;

	public Special_Coin(Image imgSpecialCoin, int width, int height) {

		//ȭ��(panel)�� ������
		this.width = width;
		this.height = height;

		//������� �� �ʱ�ȭ
		img = imgSpecialCoin.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
		w = 32; //�̹��� ���ݳ���
		h = 32; //�̹��� ���ݳ���
			
		Random rnd = new Random(); // ���� �Լ� ����
		
		x = rnd.nextInt(width - w * 2) + w; //w ~ width - w �����ϰ� �������� ���ε��� �߽� ��ǥ ���
		y = -h; // �����ϰ� �������� ���ε��� �߽� ��ǥ ���
		dy =+ rnd.nextInt(35) + 1;// ���� �������� �ӵ� �����ϰ�

	}

	void move() { //Coin�� �����̴� ��� �޼ҵ�

		y += dy;// ������ �Ʒ��� �������� ��ŭ ����
		
		//���� ȭ�� ������ ���������� ��ü ���ֱ�
		if( y > height + h ) { //ArrayList���� ����
			isDead = true; //���ŵ�
		}
	}
}