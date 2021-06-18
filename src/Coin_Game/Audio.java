package Coin_Game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio { // 효과음 및 bgm을 실행하기 위한 클래스 생성
    private Clip clip; // 사운드의 클립 선언
    private File audioFile; // 파일을 넣기 위헤 오디오파일 선언
    private AudioInputStream audioInputStream; // 오디오 스트림 생성
    private boolean isLoop; // 무한 반복을 위한 루프 생성

    public Audio(String pathName, boolean isLoop) {
        try {
        	//클립과 오디오파일 연결
            clip = AudioSystem.getClip();
            audioFile = new File(pathName);
            audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            clip.open(audioInputStream); 
        
            // 3가지의 예외 처리 선언
        } catch (LineUnavailableException e) { //라인이 사용 불가이기 때문에 열리지 않을때
            e.printStackTrace();
        } catch (IOException e) { // 오디오 파일이 존재 하지 않을때
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {// 지원하지 않는 오디오 파일일때
            e.printStackTrace();
        }
    }

    public void start() {
    	
        clip.setFramePosition(0); // 클립의 오디오 데이터의 선두로부터 재생
        clip.start(); // 클립 시작
        if (isLoop) clip.loop(Clip.LOOP_CONTINUOUSLY); // 무한 루프 생성
        
    }

    public void stop() {
    	clip.stop(); // 클립 멈춤
    }
}
