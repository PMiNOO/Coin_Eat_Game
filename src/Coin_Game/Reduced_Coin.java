package Coin_Game;

import java.awt.Image;
import java.util.Random;

public class Reduced_Coin {

	Image img; //�̹��� ��������
	int x, y; //���� �̹��� �߽� ��ǥ
	int w, h; //���� �̹��� ������, ���ݳ���
	int dy; //������ ��ȭ��
	
	int width, height; //ȭ��(panel)�� ������
	
	//��ü�� �׾����� ����
	boolean isDead = false;

	public Reduced_Coin(Image imgReducedCoin, int width, int height) {

		//ȭ��(panel)�� ������
		this.width = width;
		this.height = height;

		//������� �� �ʱ�ȭ
		img = imgReducedCoin.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
		w = 32; //�̹��� ���ݳ���
		h = 32; //�̹��� ���ݳ���
			
		Random rnd = new Random(); // ���� �Լ� ����
		
		x = rnd.nextInt(width - w * 2) + w; //w ~ width - w �����ϰ� �������� ���ε��� �߽� ��ǥ ���
		y = -h; // �����ϰ� �������� ���ε��� �߽� ��ǥ ���
		dy =+ rnd.nextInt(20) + 1;// ���� �������� �ӵ� �����ϰ�

	}

	void move() { //Coin�� �����̴� ��� �޼ҵ�

		y += dy;
		
		//���� ȭ�� ������ ���������� ��ü ���ֱ�
		if( y > height + h ) { //ArrayList���� ����
			isDead = true; // ��ü ���ŵ�
		}
	}
}