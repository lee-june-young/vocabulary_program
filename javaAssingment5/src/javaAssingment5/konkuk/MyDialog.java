package javaAssingment5.konkuk;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

//단어추가하는 다이얼로그
@SuppressWarnings("serial")
public class MyDialog extends JDialog {

	MyFrame parent = null; //부모정보
	JTextField uEng; //영어단어 입력창
	JTextField uKor; //한국어 뜻 입력창
	JButton okBtn, cancelBtn;
	
	public MyDialog(MyFrame parent, String title, boolean modal) {
		super(parent, title, modal);
		this.parent = parent;
		this.setSize(200,200);				
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); // dispose();
		init();	
		initLisenter();
		this.setVisible(true);	
	}
	
	public void init() {	
		this.setLayout(new GridLayout(3,2));
		
		this.add(new JLabel("영단어"));
		uEng = new JTextField(15);
		this.add(uEng);
		
		this.add(new JLabel("뜻"));
		uKor = new JTextField(15);
		this.add(uKor);
		
		okBtn = new JButton("입력");
		cancelBtn = new JButton("취소");
		this.add(okBtn);
		this.add(cancelBtn);
		
	}
	
	public void initLisenter() {
		
		okBtn.addActionListener(e->{ //입력 버튼 눌렀을 때 수행하는 일
			// TODO Auto-generated method stub
			String sEng = uEng.getText().trim();
			String sKor = uKor.getText().trim();
			if(sEng.length()==0 || sKor.length()==0) { //0이면 입력안한거니까.
				JOptionPane.showMessageDialog(null, "빈칸을 모두 입력해주세요. 필수입력 항목입니다.");
				return;
			}
			
			if(!( ((sEng.charAt(0)>='a'&&sEng.charAt(0)<='z')||(sEng.charAt(0)>='A'&&sEng.charAt(0)<='Z'))  && ((sKor.charAt(0)>='가'&&sKor.charAt(0)<='힣')||sKor.charAt(0)=='～'||sKor.charAt(0)=='~')) ) {
				JOptionPane.showMessageDialog(null, "영단어에는 영어를, 뜻에는 한국어를 입력해주세요.");
				return;
			}
			
			parent.voc.addWord(new Word(sEng, sKor)); //단어추가시키기
			parent.voc.addFile(); //단어장파일에도 추가시키기
			//창 종료하는 작업
			parent.dlg = null; //부모의 dlg를 null값으로 초기화
			dispose();
			}
		);
		
		cancelBtn.addActionListener(e->{ //취소 버튼 눌렀을 때 수행하는 일
				// TODO Auto-generated method stub				
				//창 종료하는 작업	
				parent.dlg = null;
				dispose();				
			}
		);
		
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) { //윈도우창 닫기 눌렀을 때 수행하는 일
				// TODO Auto-generated method stub
				super.windowClosing(e);	
				//창 종료하는 작업
				parent.dlg = null;				
				dispose();
			}			
		});
	}
}
