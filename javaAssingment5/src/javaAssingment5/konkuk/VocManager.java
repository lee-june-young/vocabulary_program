package javaAssingment5.konkuk;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JRadioButton;


public class VocManager {
	
	MyFrame parent = null; 
	private String userName;
	private ArrayList<Word> voc = new ArrayList<Word>(100);
	static Scanner scan = new Scanner(System.in);
	String filename; //JFileChooser로 가져온 파일경로
	
	//퀴즈출제 관련
	int count = 0; // 문제출제수 카운트
	int correct = 0; // 정답개수 카운트
	int numberOfQuiz = 10;
	int answerindex; //퀴즈 정답	
	int[] quiz; 
	
	public String getUserName() {
		return userName;
	}

	// 생성자
	public VocManager(String userName, MyFrame parent) {
		this.userName = userName;
		this.parent = parent;
	}

	public void addWord(Word word) {
		voc.add(word);
	}
	
	public void addFile() {
		
		try (PrintWriter outfile = new PrintWriter(new File(filename))){
		
			
			for(Word word : voc) {
				outfile.println(word.eng+"\t"+word.kor);
			}
			
		}catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} 
		
	}
	
	public void save() {
		File temp = new File(filename);
		String fileID = temp.getName(); //파일이름 가져오기
		try (PrintWriter outfile = new PrintWriter(new File("saves/notefor("+fileID+").txt"))){
		
			outfile.println(filename); //파일경로 첫줄에 출력
			for(Word word : voc) {
				outfile.println(word.eng+"\t"+word.kor+"\t"+word.wrong_freq);
			}
			//save에 영어단어, 단어뜻, 오답수 저장해두기
			
		}catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} 
	}

	public void makeVoc(String fileName) {
		this.filename = fileName;

		try (Scanner scan = new Scanner(new File(fileName))) {
			if(scan.hasNextLine()) {
				boolean success = false;
				while (scan.hasNextLine()) {
					success= false;
					String str = scan.nextLine();
					String[] temp = str.split("\t"); // split이용, 영어단어와 뜻 분리
					if(((temp[0].charAt(0)>='a'&&temp[0].charAt(0)<='z')||(temp[0].charAt(0)>='A'&&temp[0].charAt(0)<='Z'))  && ((temp[1].charAt(0)>='가'&&temp[1].charAt(0)<='힣')||temp[1].charAt(0)=='～'||temp[1].charAt(0)=='~')) {
						this.addWord(new Word(temp[0].trim(), temp[1].trim()));
						success = true;
					}else if(((temp[1].charAt(0)>='a'&&temp[1].charAt(0)<='z')||(temp[1].charAt(0)>='A'&&temp[1].charAt(0)<='Z'))  && ((temp[0].charAt(0)>='가'&&temp[0].charAt(0)<='힣')||temp[0].charAt(0)=='～'||temp[0].charAt(0)=='~')) {
						//영어랑 한국어 순이 아니라 , 한국어, 영어 순으로 들어온 경우
						//자동으로 영어,한국어 순으로 바꾸어 저장해준다.
						this.addWord(new Word(temp[1].trim(), temp[0].trim()));
						success = true;
					}else {
						//~tab~형식이긴하나, ~들이 영어/한국어 또는 한국어/영어가 아닌경우
						JOptionPane.showMessageDialog(null, userName + "의 단어장이 생성되지 않았습니다. \n파일 형식을 확인하세요.","Message",JOptionPane.ERROR_MESSAGE );
						break;
					}
					
				}
				if(success==true) {
					JOptionPane.showMessageDialog(null, userName + "의 단어장이 생성되었습니다.","Message",JOptionPane.INFORMATION_MESSAGE );
					parent.flag=true;//단어장이 생성되면 flag를 true로 바꿔서 나머지 기능이 수행될 수 있게 해준다.
				}
				
			}
			else {
				//첫줄부터 읽어올 줄이 없으면, 아무내용도 없기때문에 단어장 생성이 안되게 처리해주었다.// 이경우는 img파일일수도 있고 빈 파일일 수도 있다.
				JOptionPane.showMessageDialog(null, userName + "의 단어장이 생성되지 않았습니다. \n파일 형식을 확인하세요.","Message",JOptionPane.ERROR_MESSAGE );
			}

		} catch(ArrayIndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(null, userName + "의 단어장이 생성되지 않았습니다. \n파일 형식을 확인하세요.","Message",JOptionPane.ERROR_MESSAGE );
	    }
		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, userName + "의 단어장이 생성되지 않았습니다. \n파일명을 확인하세요.","Message",JOptionPane.ERROR_MESSAGE );
		}
	}
	
	
	public void makesavedVoc(String fileName , String savedfileName) {
		this.filename = fileName;

		try (Scanner scan = new Scanner(new File(savedfileName))) {
			if(scan.hasNextLine()) {
				String str = scan.nextLine();//첫줄은 경로니까 읽고 버려준다.
				while (scan.hasNextLine()) {
					str = scan.nextLine();
					String[] temp = str.split("\t"); // split이용, 영어단어와 뜻 분리
					this.addWord(new Word(temp[0].trim(), temp[1].trim(), temp[2].trim()));
					
				}
				JOptionPane.showMessageDialog(null, userName + "의 단어장이 생성되었습니다.","Message",JOptionPane.INFORMATION_MESSAGE );
				parent.flag=true;//단어장이 생성되면 flag를 true로 바꿔서 나머지 기능이 수행될 수 있게 해준다.
			}
			else {
				//첫줄부터 읽어올 줄이 없으면, 아무내용도 없기때문에 단어장 생성이 안되게 처리해주었다.// 이경우는 img파일일수도 있고 빈 파일일 수도 있다.
				JOptionPane.showMessageDialog(null, userName + "의 단어장이 생성되지 않았습니다. \n파일 형식을 확인하세요.","Message",JOptionPane.ERROR_MESSAGE );
			}

		} catch(ArrayIndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(null, userName + "의 단어장이 생성되지 않았습니다. \n파일 형식을 확인하세요.","Message",JOptionPane.ERROR_MESSAGE );
	    }
		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, userName + "의 단어장이 생성되지 않았습니다. \n파일명을 확인하세요.","Message",JOptionPane.ERROR_MESSAGE );
		}
	}
	
	public void note() { //오답노트 메소드
		Collections.sort(voc, (o1,o2)->(o1.wrong_freq-o2.wrong_freq)*-1);
		for (int i = 0; i < 20; i++) { // 자주 틀리는 20개 출력
			if(voc.get(i).wrong_freq>0)
				parent.text2.append(voc.get(i).wrong_freq + "번->" + voc.get(i)+"\n");
			else //빈도수가 0인게 발견이 되면.
				break;
		}
		if(parent.text2.getText().equals("")) { //초기에 ""로 text2를 설정해뒀는데 이와 같으면 아무것도 추가가 안된거니까
			parent.text2.setText("\n※ 아직 틀린 단어가 없습니다.");
		}
	}
	
	public void searchVoc(String w) { // 단어검색 메소드
		// TODO Auto-generated method stub
		String sWord = w;
		sWord = sWord.trim();
		String result = "검색한 단어(" + sWord +")는 "+"단어장에 등록되어 있지 않습니다.";
		for (Word word : voc) {
			if (word != null && word.eng.equals(sWord)) { // 문자열 비교는 equals이용
				result = "검색한 단어("+word.eng+")의 뜻 : " + word.kor;
				break; // 더이상 돌릴 필요 없으니까
			}
		}
		parent.meaning.setText(result);
		

	}
	
	public void printAll() { //단어뜻 출력 메소드
		
		for(Word word : voc) {
			parent.text1.append(word.toString()+"\n");
		}
		parent.text1.setCaretPosition(0); //커서를 처음으로 위치시켜준다.

	}
	

	public int[] chooseQuizNum() {
		// TODO Auto-generated method stub
		int[] quiz = new int[4];
		Random rand = new Random();
		for (int i = 0; i < quiz.length; i++) {
			quiz[i] = rand.nextInt(voc.size());
			for (int j = 0; j < i; j++) {
				if (quiz[j] == quiz[i] || voc.get(quiz[j]).kor.equals(voc.get(quiz[i]).kor)) {
					i--;
					break;
				}
			}
		}
		return quiz;
	}

	// 객관식퀴즈메소드
	public void makeQuiz() {	
		Random rand = new Random();
		quiz = chooseQuizNum();
		answerindex = rand.nextInt(4);
		parent.quiztitle.setText("----------- 객관식 퀴즈 " + (count + 1) + "번 -----------");
		count++;
		parent.q.setText(voc.get(quiz[answerindex]).eng + "의 뜻은 무엇일까요?");
		for (int i = 0; i < quiz.length; i++) {
			//voc.get(quiz[i]).freq++; // 출제빈도 카운트-필요x
			parent.radio[i]=new JRadioButton(voc.get(quiz[i]).kor);
			parent.radio[i].addItemListener(new MyItemListener());
			parent.g.add(parent.radio[i]); //그룹에 추가
		
			parent.radio[i].setLocation(150,150+20*i);
			parent.radio[i].setSize(250,20);
			parent.quiz_panel.add(parent.radio[i]);
		}
	}
	
	
//	public void frequentWord() { // 빈출단어 출력 메소드
//		// TODO Auto-generated method stub
//		Collections.sort(voc, (o1,o2)->(o1.freq-o2.freq)*-1);
//		for (int i = 0; i < 5; i++) { // 빈출단어 5개 출력
//			System.out.println(voc.get(i).freq + "번->" + voc.get(i));
//		}
//	}

	
	class MyItemListener implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			int uanswer = -1;
			if(e.getStateChange()==ItemEvent.SELECTED) {
				if(e.getSource()==parent.radio[0]) {
					uanswer=1;
				}else if(e.getSource()==parent.radio[1]) {
					uanswer=2;
				}else if(e.getSource()==parent.radio[2]) {
					uanswer=3;
				}else if(e.getSource()==parent.radio[3]) {
					uanswer=4;
				}
				
				if(uanswer>0) {
					if(uanswer == (answerindex+1)) {
						JOptionPane.showMessageDialog(null, "정답입니다.","Message",JOptionPane.INFORMATION_MESSAGE);
						correct++;
					}else {
						JOptionPane.showMessageDialog(null, "틀렸습니다. 정답은 " + (answerindex + 1) + "번입니다.","Message",JOptionPane.INFORMATION_MESSAGE);
						voc.get(quiz[answerindex]).wrong_freq++; 
					}
					
					if(count<numberOfQuiz) {
						for (int i = 0; i < quiz.length; i++) {
							parent.quiz_panel.remove(parent.radio[i]); //기존퀴즈에 사용된 라디오버튼 보기 삭제
						}
						parent.quiz_panel.repaint();
						makeQuiz();
					}else {//퀴즈를 다 풀었으면
						long endtime = System.currentTimeMillis(); //시간측정완료
						JOptionPane.showMessageDialog(null, getUserName() + "님 " + numberOfQuiz + "문제 중 " + correct + "개 맞추셨고, 총 "
								+ ((endtime - parent.starttime) / 1000) + "초 소요되었습니다.","Message",JOptionPane.INFORMATION_MESSAGE);
						for (int i = 0; i < quiz.length; i++) {
							parent.quiz_panel.remove(parent.radio[i]); //기존퀴즈에 사용된 라디오버튼 보기 삭제
						}
						parent.quiz_panel.repaint();
						parent.cl.show(parent.screen, "0"); //메인화면으로 복귀
					}
				}
			}
		}
	}
	
	
}
