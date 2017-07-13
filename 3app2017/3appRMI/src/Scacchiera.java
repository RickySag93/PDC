import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

class Scacchiera extends UnicastRemoteObject implements ScacchieraInterface {

    private Casella[] board = new Casella[NRIGHE*NCOLONNE];

    private int giocatori_fine=0;
    private int ngiocatori_finefase1=0;
    private Object lock_colore=new Object();


    public void trySwap(){
        this.ngiocatori_finefase1++;
        synchronized (lock_colore){
            try{
                if(ngiocatori_finefase1<2){
                    System.out.println("un giocatore sta aspettando");
                    lock_colore.wait();
                }
                this.ngiocatori_finefase1++;
                if(this.ngiocatori_finefase1==3)this.swap();
                lock_colore.notify();
            }
            catch(InterruptedException e){}
        }
    }

    synchronized public void stsampaFine() throws RemoteException{
        giocatori_fine++;
        if(giocatori_fine==2){
            System.out.println("fine dei giochi con " + this.tot());
        }
    }
    public Scacchiera() throws RemoteException {
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