class Casella {
    private boolean bianco;
    private int contenuto=0; //numero pedine su casella
    Casella(boolean b){bianco =b;}
    void swapColor(){ bianco= !bianco; }
    int size(){return contenuto;}

    synchronized public int add(){
        int mangiate=0;
        if(this.bianco){
            contenuto++;
        }
        else{
            mangiate=contenuto;
            contenuto=1;
        }
        return mangiate;
    }
}
public interface ScacchieraInterface extends Remote{
  
    public static final int NRIGHE = 8;
    public static final int NCOLONNE = 8;
    public int add(int riga, int colonna)throws RemoteException;
    public void swap()throws RemoteException;
    public void setColore()throws RemoteException;
    public int getColore()throws RemoteException;
}

class Scacchiera extends UnicastRemoteObject implements ScacchieraInterface {

    private Casella[] board = new Casella[NRIGHE*NCOLONNE];

    private int ngiocatori_finefase1=0;
    private Object lock_colore=new Object();

    synchronized public int getColore(){
        return this.ngiocatori_finefase1;
    }

    synchronized public void setColore(){          
        this.ngiocatori_finefase1++;
        synchronized (lock_colore){
			try{
				if( this.ngiocatori_finefase1<2){
			    	this.lock_colore.notify();					
					this.lock_colore.wait();
				}
				this.ngiocatori_finefase1++;
				if(this.ngiocatori_finefase1==3)this.swap();
			}
			catch(InterruptedException e){}
        }
    }

    public Scacchiera() throws RemoteException{
        boolean bianco = true;
        for (int i=0; i<NRIGHE*NCOLONNE; i++){
            board[i] = new Casella(bianco); bianco = !bianco;
        }
    }
 

    public void swap(){for(Casella c: board) c.swapColor(); }

    public int tot(){
        int t=0;
        for(Casella c:board){ t += c.size();}
        return t;
    }

    public int add(int riga, int colonna) throws RemoteException {
        return board[(riga*NCOLONNE)+colonna].add();
    }
}

interface GiocatoreInterface extends Remote, Runnable{
    public int getScore()throws RemoteException;
    public void run()throws RemoteException;
}

class Giocatore extends UnicastRemoteObject implements GiocatoreInterface{
    private String name;
    private ScacchieraInterface scacchiera;
    private int score=0;
    private boolean finito=false;
    public boolean isFinito(){return this.finito;}
    
    Giocatore(String n, ScacchieraInterface s)throws RemoteException{
        name=n; scacchiera=s;
    }
    
    public int getScore()throws RemoteException {
        return this.score;
    }
    
    public void run(){
        for(int i=0; i<30; i++){
            int r=((int)(Math.random()*10))%ScacchieraInterface.NRIGHE;
            int c=((int)(Math.random()*10))%ScacchieraInterface.NCOLONNE;
            score +=scacchiera.add(r,c);
        }

        s.setColore();

        for(int i=0; i<30; i++){
            int r=((int)(Math.random()*10))%ScacchieraInterface.NRIGHE;
            int c=((int)(Math.random()*10))%ScacchieraInterface.NCOLONNE;
            score +=scacchiera.add(r,c);
        }
        this.finito=true;
		
    }

}
public class SERVER {
    public static void main(String[] a) throws InterruptedException {
        Scacchiera s=new Scacchiera();
        Naming.rebind("s",s);
        GiocatoreInterface g1=(GiocatoreInterface)Naming.lookup("g1");
        GiocatoreInterface g2=(GiocatoreInterface)Naming.lookup("g2");

        while(!g1.isFinito()||!g2.isFinito()){
            try{
                Thread.sleep(100);
            }
            catch(InterruptedException e){}
        }
        System.out.println("totale RIMASTE " + s.tot());
    }
}

public class CLIENT1 {
    public static void main(String[] a) throws InterruptedException {
        ScacchieraInterface s=(ScacchieraInterface)Naming.lookup("s");

        Runnable g2=new Giocatore("mario",s);
        Naming.rebind("g2",g2);

        Thread t=new Thread(g2);
        t.start();
        t.join();
		
        System.out.println("totale GIOCATORE 2 " + g2.getScore());

    }
}

public class CLIENT2 {
    public static void main(String[] a) throws InterruptedException {
        ScacchieraInterface s=(ScacchieraInterface)Naming.lookup("s");

        Runnable g1=new Giocatore("pippo",s);
        Naming.rebind("g1",g1);
		
        Thread t=new Thread(g1);
        t.start();
        t.join();

        System.out.println("totale GIOCATORE 1 " +  g1.getScore());
    }
}

/*
domdanda 2: il server */
