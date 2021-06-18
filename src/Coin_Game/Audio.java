package Coin_Game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio { // ȿ���� �� bgm�� �����ϱ� ���� Ŭ���� ����
    private Clip clip; // ������ Ŭ�� ����
    private File audioFile; // ������ �ֱ� ���� ��������� ����
    private AudioInputStream audioInputStream; // ����� ��Ʈ�� ����
    private boolean isLoop; // ���� �ݺ��� ���� ���� ����

    public Audio(String pathName, boolean isLoop) {
        try {
        	//Ŭ���� ��������� ����
            clip = AudioSystem.getClip();
            audioFile = new File(pathName);
            audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            clip.open(audioInputStream); 
        
            // 3������ ���� ó�� ����
        } catch (LineUnavailableException e) { //������ ��� �Ұ��̱� ������ ������ ������
            e.printStackTrace();
        } catch (IOException e) { // ����� ������ ���� ���� ������
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {// �������� �ʴ� ����� �����϶�
            e.printStackTrace();
        }
    }

    public void start() {
    	
        clip.setFramePosition(0); // Ŭ���� ����� �������� ���ηκ��� ���
        clip.start(); // Ŭ�� ����
        if (isLoop) clip.loop(Clip.LOOP_CONTINUOUSLY); // ���� ���� ����
        
    }

    public void stop() {
    	clip.stop(); // Ŭ�� ����
    }
}
