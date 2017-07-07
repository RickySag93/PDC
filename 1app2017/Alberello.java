interface Albero {
	Albero add(String s);
	void stampa();
	boolean presente(String s);
	int nNodi();
}

interface Iteratore {
	boolean hasNext();
	String next();
}

class AlberoVuoto implements Albero {
	public Albero add(String s) {
		return new AlberoImpl(s, new AlberoVuoto(), new AlberoVuoto());
	}

	public void stampa() { System.out.print("- "); }
	public boolean presente(String s) { return false; }
	public int nNodi() { return 0; }
}

class AlberoImpl implements Albero {
	private String info;
	private Albero figlioSx;
	private Albero figlioDx;

	public Iteratore itera() {
		return new Iteratore() {
			AlberoImpl aI = AlberoImpl.this;
          
			public boolean hasNext() {
				return(!(aI.figlioSx instanceof AlberoVuoto));
			}

			public String next() {
                  	String temp = aI.info;
              		if(hasNext()) {
						aI = (AlberoImpl)aI.figlioSx;
                    }
              		
             		return temp;
					
			}
		};
	}


	AlberoImpl (String s) {
		info=s;
		figlioSx=new AlberoVuoto();
		figlioDx=new AlberoVuoto();
	}

	AlberoImpl(String s, Albero fs, Albero fd) {
		info=s;
		figlioSx=fs;
		figlioDx=fd;
	}

	public int nNodi() {
		return figlioSx.nNodi()+figlioDx.nNodi()+1;
	}

	public Albero add (String s) {
		int ns=figlioSx.nNodi();
		int nd=figlioDx.nNodi();
		if(ns>nd) 
			figlioDx=figlioDx.add(s);
		else 
			figlioSx=figlioSx.add(s);
		return this;
	}


	public void stampa() {
		figlioSx.stampa(); 
		System.out.print(info+" ");
		figlioDx.stampa();
	}

	public boolean presente(String s) {
		return (s.equals(info) || figlioSx.presente(s) || figlioDx.presente(s));
	}
}

class T1 extends Thread {
	Albero a;
	T1(Albero alb) {a=alb;}

	public void run() {
		try {
			synchronized(a) {
				a.add("qui");
			}
			Thread.sleep((int)Math.random()*3);
			synchronized(a) {
				a.add("quo");
			}
			a.stampa();
		} catch(InterruptedException e) {}
	}
}

class T2 extends Thread {
	Albero a;
	T2(Albero alb) {a=alb;}

	public void run() {
		try{
			synchronized(a) {
				if(a.presente("quo")) 
					System.out.println("BIANCO"); 
				else
					System.out.println("NERO"); 
				Thread.sleep((int)Math.random()*3);
				System.out.println("ROSSO"); 
			}
	} catch(InterruptedException e) {}
	}
}

class T3 extends Thread {
	Albero a;
	T3(Albero alb) {a=alb;}

	public void run() {
		try{
			synchronized(a) {
				if(a.presente("qui")) 
					System.out.println("UNO"); 
				else
					System.out.println("DUE"); 
				Thread.sleep((int)Math.random()*3);
				System.out.println("TRE"); 
			}
		} catch(InterruptedException e) {}
		
	}
}



public class Main{
	public static void main(String[] args) {
		Albero a = new AlberoImpl("pippo");
		a.add("pluto").add("paperino").add("minnie").add("topolino").add("gastone").add("paperone");
		a.stampa();

		T1 t1= new T1(a);
		T2 t2= new T2(a);
		T3 t3= new T3(a);

		t1.start();
		t2.start();
		t3.start();
      
      
      		try{ Thread.sleep(3000);}
		catch(Exception e) {}
      
      System.out.println(" ");
      System.out.println(" ");
      System.out.println(" ");
      
      	Iteratore i = ((AlberoImpl)a).itera();

      
          while(i.hasNext()) {
			System.out.println(i.next());
          }
	}
}