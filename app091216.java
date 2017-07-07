
import java.lang.Math; class
import java.util.Vector;
class Auto{
    private int i;
    private String direzione;
    private static int n;

    Auto(String dir){i=n++; direzione=dir;}

    public String toString(){return " Auto n."+i+" proveniente da "+direzione;}
    public void attraversa(){System.out.println("ora passa " +this);}

}

class Incrocio{
    Vector<Auto> codaPrincipale=new Vector<Auto>();
    Vector<Auto> codaSecondaria=new Vector<Auto>();
    Object carlo= new Object();

    public class GestoreStradaPrincipale extends Thread{
        public void run(){
            while(true){
                synchronized(Incrocio.this){
                    try{
                        while(codaPrincipale.isEmpty()){
                            Incrocio.this.notify();
                            Incrocio.this.wait();
                        }
                        codaPrincipale.get(0).attraversa();
                        codaPrincipale.remove(0);



                    }catch(InterruptedException e){}

                }

            }
        }
    }

    public class GestoreStradaSecondaria extends Thread{
        public void run(){
            while(true){
                synchronized(Incrocio.this){
                    try{


                        while(codaSecondaria.isEmpty() || !codaPrincipale.isEmpty()){
                            Incrocio.this.wait();
                        }
                        synchronized(carlo){
                            (codaSecondaria.get(0)).attraversa();
                            codaSecondaria.remove(0);
                        }
                    }catch(InterruptedException e){}

                }
            }
        }
    }

    public class GeneraAuto extends Thread{
        public void run(){

                for(int i=0; i<5; i++){
                    synchronized(carlo){
                        codaPrincipale.add(new Auto("coda princ"));
                    }
                    codaSecondaria.add(new Auto("coda sec"));
                }
                synchronized(Incrocio.this){
                    Incrocio.this.notifyAll();
                }
                try{
                    sleep(300);
                }
                catch(Exception e){}
            }

    }


}

public class Main{
    public static void main(String[] a){
        Incrocio i=new Incrocio();
        Incrocio.GestoreStradaPrincipale gsp= i.new GestoreStradaPrincipale();
        Incrocio.GestoreStradaSecondaria gss= i.new GestoreStradaSecondaria();
        Incrocio.GeneraAuto ga=i.new GeneraAuto();
        gsp.start(); gss.start(); ga.start();
    }
}



////****roba server**//
interface R extends Remote{
	void g(){}
	void f(){}

}

class RImpl extends UnicastRemoteObjects implements Remote{
	 synchronized void f(){
		System.out.println("FF")
		System.out.println("FF")
		System.out.println("FF")
		System.out.println("FF")
		System.out.println("FF")
	}
	void g(){
		System.out.println("GG")
		System.out.println("GG")
		System.out.println("GG")
		System.out.println("GG")
		System.out.println("GG")
	}

}

class Server{
	public static void main(String []a){
		R r=new RImpl();
		Naming.rebind("r",r);

	}
}
class C1{
	void m(String s){
			System.out.println(s);
			System.out.println(s);
			System.out.println(s);
				System.out.println(s);
			System.out.println(s);	
	}
	public static void main(String []a){
		R r=(R)Naming.lookup("r");
		Thread t1=new Thread{
			public void run(){
				m("A");
				synchronized(r){
					r.f();
				}
				m("B");
			}
		}.start();

		Thread t2=new Thread{
			public void run(){
				m("UNO");
				synchronized(r){	
					r.g();
				}
				m("DUE");
			}
		}.start();
	}
}

class C2{

	public static void main(String []a){
		R r=(R)Naming.lookup("r");
		r.f();
	}
}