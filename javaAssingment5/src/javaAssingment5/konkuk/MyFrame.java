package javaAssingment5.konkuk;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;


@SuppressWarnings("serial")
public class MyFrame extends JFrame {
	boolean flag = false; //단어장 생성여부 확인하기 위함.
	CardLayout cl = new CardLayout();
	JPanel screen;
	JPanel[] card;
	JButton btn1,btn2,btn3,btn4,btn5,btn6; //메인화면에 출력될 버튼들
	VocManager voc;
	JTextArea text1, text2;
	JButton home; //홈버튼
	JTextField search; //단어뜻 검색창
	JLabel meaning; //단어뜻 검색 결과
	JPanel quiz_panel; //퀴즈 출력할 패널
	JLabel quiztitle; //몇번째 퀴즈인지 알려줄 레이블
	JLabel q; //영단어뜻을 물어보는 레이블
	JRadioButton[] radio;
	MyDialog dlg=null;
	long starttime;//퀴즈시작시간 측정
	ButtonGroup g;
	String path; //JFileChooser에서 가져온 파일경로
	boolean saveFlag; //save파일이 있는지 여부 
	
	
	//생성자
	public MyFrame() {
		this("200000 홍길동");
	}
	
	public MyFrame(String title) {
		super(title);
		this.setSize(550,430);
		this.setResizable(false); //창 크기 고정
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) { //윈도우창 닫기 눌렀을 때 수행하는 일
				// TODO Auto-generated method stub
				super.windowClosing(e);
				if (flag==true) {
					voc.save();
				}
			}
			
		});
		init();
		this.setVisible(true);
	}
	
	
	public void init() {
		Container frame = this.getContentPane();
		initScreen();
		frame.add(screen,BorderLayout.CENTER);
	}
	
	public void initScreen() {
		screen = new JPanel(cl);//screen은 카드레이아웃을 가지게 된다.
		card= new JPanel[5]; // 5개의 화면을 만들어 운영할 것이다.
		for(int i=0; i<5; i++) {
			card[i] = new JPanel();
		}
		
		card0(); //메인메뉴 파트 구성
		//단어장 생성은 따로 패널 필요x =>JFileChooser이용
		card1(); //단어뜻 출력 파트 구성
		card2(); //단어뜻 검색 파트 구성
		card3(); //객관식 퀴즈 파트 구성.
		card4(); //오답노트 파트 구성
		//단어추가도 따로 패널 필요x => 다이얼로그이용
		
		for(int i=0; i<5; i++) {
			screen.add(card[i],String.valueOf(i));
		}
		cl.show(screen, "0"); //메인화면 보여주기
		
	}
	

	public void card0() { //메인화면
		card[0].setLayout(new BorderLayout()); 
		
		//학번&사진
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
		JLabel label = new JLabel("<나만의 영어 단어장>");
		label.setFont(new Font("궁서체",Font.BOLD, 20));
		JLabel name = new JLabel("202012816 이준영");
		ImageIcon img = new ImageIcon("img/Myphoto.jpg");
		JLabel imgChar = new JLabel(img);
		p1.add(label);
		p1.add(imgChar);
		p1.add(name);
		
		//버튼들
		JPanel p2 = new JPanel(new GridLayout(6,1));
		btn1 = new JButton("단어장 생성");
		btn2 =new JButton("단어 뜻 출력");
		btn3 =new JButton("단어 뜻 검색");
		btn4 =new JButton("객관식 퀴즈");
		btn5 =new JButton("오답 노트");
		btn6 = new JButton("단어 추가");
		btn1.addActionListener(new MyActionListener());
		btn2.addActionListener(new MyActionListener());
		btn3.addActionListener(new MyActionListener());
		btn4.addActionListener(new MyActionListener());
		btn5.addActionListener(new MyActionListener());
		btn6.addActionListener(new MyActionListener());
		p2.add(btn1);
		p2.add(btn2);
		p2.add(btn3);
		p2.add(btn4);
		p2.add(btn5);
		p2.add(btn6);
		card[0].add(p1, BorderLayout.CENTER);
		card[0].add(p2, BorderLayout.EAST);
	}
	
	public void card1() { //단어뜻 출력
		card[1].setLayout(new BorderLayout());
		text1 = new JTextArea();
		text1.setEditable(false); //출력창이므로 편집 불가능하게 만들어준다.
		home = new JButton("Home");
		home.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cl.show(screen, "0");
			}
			
		});
		card[1].add(new JScrollPane(text1), BorderLayout.CENTER);
		card[1].add(home, BorderLayout.SOUTH);
	}
	
	
	public void card2() { //단어뜻 검색 패널
		card[2].setLayout(new BorderLayout());
	
		
		JPanel p1 = new JPanel(new FlowLayout());
		JLabel label = new JLabel("검색할 단어를 입력하세요(영단어) : ");
		
		search = new JTextField(20); //단어뜻 검색창
		search.addActionListener(new ActionListener() { //검색 이벤트 처리

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String str1 = search.getText(); //사용자가 입력한 text를 가지고 온다.
				voc.searchVoc(str1);
				}
		});
		home = new JButton("Home");
		home.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cl.show(screen, "0");
			}
			
		});
		
		meaning = new JLabel("",SwingConstants.CENTER); 
		//VocManager의 searchVoc에서 parent.meaning.setText(result);를 이용해 내용을 수정해줄 예정
		card[2].add(meaning,BorderLayout.CENTER);
		
		p1.add(label);
		p1.add(search);
		card[2].add(p1, BorderLayout.NORTH);
		card[2].add(home, BorderLayout.SOUTH);
	}
	
	public void card3() { //객관식 퀴즈 카드
		card[3].setLayout(new BorderLayout());
		quiz_panel = new JPanel();
		quiz_panel.setLayout(null);
		quiztitle = new JLabel("",SwingConstants.CENTER);
		
		q = new JLabel("",SwingConstants.CENTER);
		q.setLocation(150,100);
		q.setSize(250,20);
		quiz_panel.add(q);
		
		radio = new JRadioButton[4]; //4지선다.
		g = new ButtonGroup();
		card[3].add(quiztitle, BorderLayout.NORTH);
		card[3].add(quiz_panel, BorderLayout.CENTER);
	 
	}
	
	public void card4() { //오답노트
		card[4].setLayout(new BorderLayout());
		text2 = new JTextArea();
		text2.setEditable(false); //출력창이므로 편집 불가능하게 만들어준다.
		home = new JButton("Home");
		home.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cl.show(screen, "0");
			}
			
		});
		card[4].add(new JScrollPane(text2), BorderLayout.CENTER);
		card[4].add(home, BorderLayout.SOUTH);
	}
	
	public void hasSave() { //단어장에 해당하는 save파일이 있는지 여부 확인
		saveFlag =false;
		File temp = new File("saves/");
		File[] contents = temp.listFiles();
		for(File file : contents) {
			try (Scanner scan = new Scanner(file)) {
				if(scan.hasNextLine()) { //save파일의 첫줄에는 경로가 들어가 있다
					String str = scan.nextLine().trim();
					if(str.equals(path.trim())) {
						voc.makesavedVoc(str,"saves/"+file.getName());
						saveFlag=true;
						break;
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				//아직 생성된 save파일이 없는 것으로 아무 처리도 해주지 않는다.
			}
		}
	}
	class MyActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource()==btn1) { //단어장 생성
				JFileChooser dlg = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("txt Files","txt");
				dlg.setFileFilter(filter); //dlg에 필터적용
				int result = dlg.showOpenDialog(MyFrame.this);
				if(result == JFileChooser.APPROVE_OPTION) { //파일 오픈을 승인했으면
					if (flag==true) { //실행 중에 새로운 단어장파일을 오픈 한경우
						voc.save(); //지금까지 썼던 단어장에 관한 정보를저장해준다.
					}
					flag=false;// 단어장 생성여부초기화
					voc= new VocManager("이준영", MyFrame.this);
					//단어장 읽어오기
					path = dlg.getSelectedFile().getAbsolutePath();
					hasSave(); //save파일이 있는지 여부확인
					if(saveFlag==false) { //save파일이 없으면
						voc.makeVoc(path);
					}
				}
		
			}else if(e.getSource()==btn2) { //단어 뜻 출력
				if(flag==true) {
					text1.setText(""); //text1내용 비우기
					voc.printAll(); //단어뜻 출력
					cl.show(screen, "1"); //card[1]보여주기
				}
				else
					JOptionPane.showMessageDialog(null, "단어장 생성부터 하셔야합니다.","Message",JOptionPane.ERROR_MESSAGE);
				
			}else if(e.getSource()==btn3) {
				if(flag==true) {
					search.setText(""); //검색창 비우기
					meaning.setText(""); //검색 결과 비우기
					cl.show(screen, "2"); //card[2]보여주기
				}
				else
					JOptionPane.showMessageDialog(null, "단어장 생성부터 하셔야합니다.","Message",JOptionPane.ERROR_MESSAGE);
				
			}else if(e.getSource()==btn4) { //객관식 퀴즈
				if(flag==true) {
					voc.count=0;
					voc.correct=0; // 객관식 퀴즈 시작하기 전에 count와 correct수 0으로 초기화
					voc.makeQuiz();
					cl.show(screen, "3"); //화면 보여주기
					//퀴즈시작
					starttime = System.currentTimeMillis(); //시간 측정시작
				}
				else
					JOptionPane.showMessageDialog(null, "단어장 생성부터 하셔야합니다.","Message",JOptionPane.ERROR_MESSAGE);
				
			}else if(e.getSource()==btn5) {
				if(flag==true) {
					text2.setText(""); //text2내용 비우기
					voc.note(); //오답노트 출력
					cl.show(screen, "4"); //card[4]보여주기
				}
				else
					JOptionPane.showMessageDialog(null, "단어장 생성부터 하셔야합니다.","Message",JOptionPane.ERROR_MESSAGE);
				
			}else if(e.getSource()==btn6) {
				if(flag==true) { //단어추가 기능
					//다이얼로그 띄우기
					if(dlg==null) { //dlg가 null일때만 다이얼로그 창 생성
						dlg = new MyDialog(MyFrame.this, "단어추가", false);
					}else { //아닐때는 포커스만 이동시켜주기
						dlg.requestFocus();
					}
				}
				else
					JOptionPane.showMessageDialog(null, "단어장 생성부터 하셔야합니다.","Message",JOptionPane.ERROR_MESSAGE);
				
			}
		}
		
	}
	
}
