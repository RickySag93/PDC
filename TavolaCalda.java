import java.util.Vector;

/**
 * Created by aless on 27/06/2017.
 */
class Cliente extends Thread{
    private TavolaCalda mensa;
    private static int nC;
    private int numero;

    Cliente(TavolaCalda m){mensa=m; numero=nC++;}

    public void run(){
        mensa.prendi_primo(numero);
        mensa.prendi_secondo(numero);
        mensa.paga(this);
    }

    //metodo invocato dal cassiere della mensa
    public void pagamento(){
        System.out.println("cliente " + numero +" ha pagato");
    }

}

public class TavolaCalda {
    private int prox_primo_da_servire=0;
    private int prox_secondo_da_servire=0;
    private int nPagamenti=0;

    public Object lock_primo= new Object();

	 public Object lock_secondo= new Object();
	 public Object pag= new Object();

    public void prendi_primo(int i){

        synchronized(lock_primo){
            try{
                while(i!=prox_primo_da_servire){
                    lock_primo.wait();
                }

                System.out.println("cliente " + prox_primo_da_servire +"servito primo");
                prox_primo_da_servire+=1;

                lock_primo.notifyAll();
            }catch(InterruptedException e){}
        }
    }

    public void prendi_secondo(int i){
        synchronized(lock_secondo){
            try{
                while(i!=prox_secondo_da_servire){
                    lock_secondo.wait();
                }

                System.out.println("cliente " + prox_secondo_da_servire +"servito secondo");
                prox_secondo_da_servire+=1;

                lock_secondo.notifyAll();
            }catch(InterruptedException e){}
        }
    }

    public void paga(Cliente c){
        synchronized(pag){
            nPagamenti++;
            c.pagamento();
            pag.notifyAll();
        }
    }

    private void generaClienti(final int n){

        new Thread(){
            public void run(){
                for(int i=0;i<n;i++){
                    new Cliente(TavolaCalda.this).start();
                }
            }
        }.start();

    }

    private void attendiClienti(final int n){
        synchronized(pag){
            try{
                while(nPagamenti<n){
                    pag.wait();
                }
                System.out.println("Incassati " + n + " pagamenti");
            }catch(InterruptedException e){}
        }
    }

    public static void main(String []args){
        TavolaCalda m =new TavolaCalda();
        m.generaClienti(100);
        m.attendiClienti(100);
    }

}
import java.util.Vector;

/**
 * Created by aless on 27/06/2017.
 */
class Cliente extends Thread{
    private TavolaCalda mensa;
    private static int nC;
    private int numero;

    Cliente(TavolaCalda m){mensa=m; numero=nC++;}

    public void run(){
        mensa.prendi_primo(numero);
        mensa.prendi_secondo(numero);
        mensa.paga(this);
    }

    //metodo invocato dal cassiere della mensa
    public void pagamento(){
        System.out.println("cliente " + numero +" ha pagato");
    }

}

public class TavolaCalda {
    private int prox_primo_da_servire=0;
    private int prox_secondo_da_servire=0;
    private int nPagamenti=0;

    public Object lock_primo= new Object();

	 public Object lock_secondo= new Object();
	 public Object pag= new Object();

    public void prendi_primo(int i){

        synchronized(lock_primo){
            try{
                while(i!=prox_primo_da_servire){
                    lock_primo.wait();
                }

                System.out.println("cliente " + prox_primo_da_servire +"servito primo");
                prox_primo_da_servire+=1;

                lock_primo.notifyAll();
            }catch(InterruptedException e){}
        }
    }

    public void prendi_secondo(int i){
        synchronized(lock_secondo){
            try{
                while(i!=prox_secondo_da_servire){
                    lock_secondo.wait();
                }

                System.out.println("cliente " + prox_secondo_da_servire +"servito secondo");
                prox_secondo_da_servire+=1;

                lock_secondo.notifyAll();
            }catch(InterruptedException e){}
        }
    }

    public void paga(Cliente c){
        synchronized(pag){
            nPagamenti++;
            c.pagamento();
            pag.notifyAll();
        }
    }

    private void generaClienti(final int n){

        new Thread(){
            public void run(){
                for(int i=0;i<n;i++){
                    new Cliente(TavolaCalda.this).start();
                }
            }
        }.start();

    }

    private void attendiClienti(final int n){
        synchronized(pag){
            try{
                while(nPagamenti<n){
                    pag.wait();
                }
                System.out.println("Incassati " + n + " pagamenti");
            }catch(InterruptedException e){}
        }
    }

    public static void main(String []args){
        TavolaCalda m =new TavolaCalda();
        m.generaClienti(100);
        m.attendiClienti(100);
    }

}
