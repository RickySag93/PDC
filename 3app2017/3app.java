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

class Scacchiera {
    public static final int NRIGHE = 8;
    public static final int NCOLONNE = 8;
    private Casella[] board = new Casella[NRIGHE*NCOLONNE];

    private int ngiocatori_finefase1=0;
    private Object lock_colore=new Object();

    synchronized public int getColore(){
        return this.ngiocatori_finefase1;
    }

    synchronized public Object setColore(){
        synchronized (lock_colore){
            this.ngiocatori_finefase1++;
            this.lock_colore.notifyAll();
            return this.lock_colore;
        }
    }

    public Scacchiera() {
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

    public int add(int riga, int colonna){
        return board[(riga*NCOLONNE)+colonna].add();
    }
}
class Giocatore extends Thread {
    private String name;
    private Scacchiera scacchiera;
    private int score=0;
    Giocatore(String n, Scacchiera s){name=n; scacchiera=s;}
    public int getScore(){
        return this.score;
    }
    public void run(){
        for(int i=0; i<30; i++){
            int r=((int)(Math.random()*10))%Scacchiera.NRIGHE;
            int c=((int)(Math.random()*10))%Scacchiera.NCOLONNE;
            score +=scacchiera.add(r,c);
        }
        Object lock=scacchiera.setColore();

        synchronized(lock){
            try{
                while(scacchiera.getColore()<2){
                    lock.wait();
                }
                if(scacchiera.getColore()==2){
                    scacchiera.swap();

                    scacchiera.setColore();
                }
            }catch(InterruptedException e){}
        }

        for(int i=0; i<30; i++){
            int r=((int)(Math.random()*10))%Scacchiera.NRIGHE;
            int c=((int)(Math.random()*10))%Scacchiera.NCOLONNE;
            score +=scacchiera.add(r,c);
        }

    }

}
public class Gioco {
    public static void main(String[] a) throws InterruptedException {
        Scacchiera s=new Scacchiera();
        Giocatore g1=new Giocatore("pippo",s);
        Giocatore g2=new Giocatore("mario",s);
        g1.start();
        g2.start();


        g1.join();
        g2.join();

        System.out.println("totale GIOCATORE 1 " +  g1.getScore());

        System.out.println("totale GIOCATORE 2 " + g2.getScore());

        System.out.println("totale RIMASTE " + s.tot());
    }
}