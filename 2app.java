public class PassaggioLivello{
	public static void main(String[] a){
		Casello c=new Casello();
		c.start();
		for(int i = 0; i<7;i++){
			new Auto(c).start();
		}
		Treno=new Treno(c); t.start();

	}
}

class Treno extends Thread{
	private int distanza= 100;
	private Casello cc;
	Treno(Casello c){
		cc=c;
	}
	public void run(){
		try{
			while(distanza>50){
				distanza--;
				Thread.sleep((int)Math.random()*3);
			}
			cc.trenoInAvvicinamento();

			while(distanza>0){
				distanza--;
				Thread.sleep((int)Math.random()*3);
			}

			System.out.println("treno attreversa il passaggio a livello");
			cc.passaTreno();

		}catch(InterettuptedException e){}
	}
}

class Auto extends Thread{
	private static int num;
	private inte numero;
	private int distanza=100;
	Auto(Casello c){numero=num++; cc=c;}
	public void run(){
		cc.add();
		try{
			while(distanza>0){
				Thread.sleep((int)Math.random()*10);
				if(cc.aperto()){
					distanza--;
					System.out.println("Auto " +numero+"avanza ");

				}else{
					System.out.println("auto " + numero + "attende che riapranole sbarre");
					synchronized(this){
						while(!cc.aperto())
							this.wait();
					}
				    System.out.println("auto " + numero + "riprende la marcia");

				}
			}
			System.out.println("auto " + numero + " passa");
		}catch(InterettuptedException e){}
	}
}

class Casello extends Thread{
	private Auto[] autoInArrivo=new Auto[10];
	private boolean trenoInArrivo =false;
	private boolean trenoPassato=false;
	private boolean sbarreSu=true;

	public synchronized void add(Auto a){
		autoInArrivo[sizecoda]=a;
		sizecoda++;
	}

	public synchronized boolean aperto(){
			return sbarreSu;
	}
	
	public synchronized trenoInAvvicinamento(){
		System.out.println("treno in arrivo");
		trenoInArrivo=true;
		this.notify();
	}
	
	public synchronized passaTreno(){
		System.out.println("treno passa il passaggio livello");
		trenoPassato=true;
		this.notify();
	}
	
	private synchronized void abbassaSbarre(){
		sbarreSu=false;
	}

	private synchronized alzaSbarre(){
		sbarreSu=true;
		for (int i=0; i<sizecoda; i++){
			Auto a=autoInArrivo[i];
			synchronized(a){
				a.notify();
			}
		}
	}
	
	public void run(){
			try{
				synchronized(this){
					while(trenoInArrivo==false){
						wait();
					}
					abbassaSbarre();
					while(trenoPassato==false){
						wait();
					}
					alzaSbarre();
					System.out.println("alzate le sbarre");
				}
			}catch(InterettuptedException e){}
		}
	}
}