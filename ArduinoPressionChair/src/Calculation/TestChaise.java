package Calculation;

public class TestChaise {

	public static void main(String[] args) {
		Chaise chaise=new Chaise();

		Pied p1=new Pied(0.0, 0.0, 1);
		Pied p2=new Pied(1.0, 0.0, 2);
		Pied p3=new Pied(0.0, 2.0, 3);
		Pied p4=new Pied(1.0, 2.0, 4);

		p1.setValue(2);
		p2.setValue(2);
		p3.setValue(2);
		p4.setValue(2);

		chaise.addPied(p1);
		chaise.addPied(p2);
		chaise.addPied(p3);
		chaise.addPied(p4);

		chaise.calculateGpos();

		System.out.println("("+chaise.getGposX()+";"+chaise.getGposY()+")");


	}

}
