
import java.lang.Math; // headers MUST be above the first class

interface Albero extends Remote{
	Albero add(String s);
	void stampa();
	boolean presente(String s);
	int nNodi();
}

 interface Iteratore{
	boolean hasNext();
	String next();

}
 class AlberoVuoto extends UnicastRemoteObject implements Albero{
	 public Albero add(String s){
		return new AlberoImpl(s, new AlberoVuoto(), new AlberoVuoto());

	}
	public void stampa(){
		System.out.println(" - ");

	}
	 public boolean presente(String s){return false;}
	public int nNodi(){return 0;}

}

 class AlberoImpl extends UnicastRemoteObject implements Albero{
	private String info;
	private Albero figliosx;
	private Albero figliodx;

	AlberoImpl(String s){info=s; figliosx=new AlberoVuoto(); figliodx=new AlberoVuoto();}
	AlberoImpl(String s, Albero fs, Albero fd){info=s; figliosx=fs; figliodx=fd;}
	public int nNodi(){
		return figliodx.nNodi()+figliosx.nNodi()+1;

	}

	public Iteratore itera() {
		return new Iteratore() {
			AlberoImpl aI = AlberoImpl.this;
          
			public boolean hasNext() {
				return(!(aI.figliosx instanceof AlberoVuoto));
			}

			public String next() {
                  	String temp = aI.info;
              		if(hasNext()) {
						aI = (AlberoImpl)aI.figliosx;
                    }
            
           		return temp;			
			}
		};
	}

	synchronized public Albero add(String s ){
		int ns=figliosx.nNodi();
		int nd=figliodx.nNodi();
		if(ns>nd)figliodx=figliodx.add(s);
		else figliosx=figliosx.add(s);
		return this;
	}
	synchronized public void stampa() {
		figliosx.stampa(); System.out.println(info+ " "); figliodx.stampa();
	}
	synchronized public boolean presente(String s) {
		return (s.equals(info) || figliosx.presente(s) || figliodx.presente(s));
	}
}
}

class SERVER{
	public static void main(String[]a){

		Albero a=new AlberoImpl("pippo");
		a.rebind("a", a);
			a.add("pluto").add("paperino").add("minnie").add("topolino").add("gastone").add("paperone");
	
					a.add("qui");
				
				try{
					sleep(300);
				} catch(InterruptedException e) {}
				
					a.add("quo");
				
			}
		}

	}
}
 class CLIENT {
	public static void main(String[] arg) {
		Albero a=(Albero) Naming.lookup("a");
		Object lock=new Object();

		Thread t2=new Thread(){
			public void run(){
				synchronized(lock){
						if(!a.presente("quo")){
							System.out.println("BIANCO");
						}
						else{
							System.out.println("NERO");
						}
			
					}	
					try{
						sleep(300);
					} catch(InterruptedException e) {}
					System.out.println("ROSSO");
				
			}
		};
		Thread t3=new Thread(){
			public void run(){
				synchronized(lock){
						if(!a.presente("qui")){
							System.out.println("UNO");
						}
						else{
							System.out.println("DUE");
						}
					}	
					try{
						sleep(300);
					} catch(InterruptedException e) {}
					System.out.println("TREThier");
				
			}
		};
		t2.start();
		t3.start();
	}
