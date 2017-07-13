class Forno{
    private int stato;

    public synchronized boolean e_vuoto() {return stato==0;}
    public synchronized void accendi() {stato=1;}
    public synchronized void spegni() {stato=2;}
    public synchronized boolean finito() {return stato==2;}
    public synchronized void svuota() {stato=0;}
}

class Pasticcere extends Thread {
    Forno[] forni = new Forno[5];
    int cotti_ancora_in_forno=0;

    Pasticcere() {
        for(int i=0;i<5;i++) forni[i]=new Forno();
    }

    private void impasta() {System.out.println("impasta"); try{sleep(70);}catch(InterruptedException e){}}
    private void farcisci() {System.out.println("farcisci"); try{sleep(70);}catch(InterruptedException e){}}
    public boolean puoiinforna() {
        for(int i=0; i<5; i++) {
            if(forni[i].e_vuoto()) {
                forni[i].accendi();
                return true;
            }
        }
        System.out.println("operazione fallita");
        return false;
    }

    public void run() {
        int k=0;
        for(;k<20;k++)  {
            if(puoiinforna()){
                impasta();
                final int t=k;
                new Thread() {
                    public void run(){
                        try{sleep(5000);}catch(InterruptedException e){}
                        synchronized(Pasticcere.this) {
                            System.out.println("in forno" + t);
                            Pasticcere.this.cotti_ancora_in_forno++;
                            Pasticcere.this.notify();
                        }
                    }
                }.start();
            }
            else{
                while(cotti_ancora_in_forno==0){
                    synchronized(this) {
                        try{this.wait();} catch(InterruptedException e){}
                    }
                }
            }

            int daFarcire=cotti_ancora_in_forno;
            for(int i=0;i<cotti_ancora_in_forno;i++) {
                forni[i].svuota();
            }
            cotti_ancora_in_forno=0;
            for(int i=0;i<daFarcire;i++) {
                farcisci();
            }
        }
    }
}

public class rimbalzi{
    public static void main(String[] a){
        Pasticcere p=new Pasticcere();
        p.start();
    }
}
