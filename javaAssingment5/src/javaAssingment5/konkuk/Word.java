package javaAssingment5.konkuk;

public class Word implements Comparable<Word>{
	String eng;
	String kor;
	//int freq; //빈도수
	int wrong_freq; //틀린횟수 누적
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.eng.hashCode()+this.kor.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof Word) {
			Word temp = (Word) obj; //다운캐스팅
			return this.eng.equals(temp.eng) && this.kor.equals(temp.kor);
		}else
			return false;
	}
	
	public Word(String eng, String kor) {
		super();
		this.eng = eng;
		this.kor = kor;
	}

	public Word(String eng, String kor, String wf) {
		super();
		this.eng = eng;
		this.kor = kor;
		this.wrong_freq = Integer.valueOf(wf);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return eng+" : "+kor;
	}

	@Override
	public int compareTo(Word o) {
		// TODO Auto-generated method stub
		return (this.wrong_freq-o.wrong_freq)*(-1); //오답빈도수기준 내림차순으로 정렬
	}

}
