import java.util.Vector;

public class Ristorante {
    private int nPrenotati;
    private final int maxPrenotazioni = 70;
    private int nOverbooked;
    private Vector<Cliente> overbooked = new Vector<Cliente>();
    public boolean stopPrenotazioni = false;

    public synchronized Risposta prenota(Cliente c) {
        if (stopPrenotazioni == true)


            return new Risposta(0, false, false);

        if (nPrenotati < maxPrenotazioni) {
            nPrenotati++;  System.out.println("sono il cliente " +nPrenotati+ "e prenoto");
            return new Risposta(nPrenotati, true, false);
        }
        nOverbooked++;
        overbooked.add(c);
        return new Risposta(0, false, true);
    }

    synchronized public  void disdici() {
        nPrenotati--;
        if (nOverbooked > 0) {
            Cliente c = overbooked.remove(0);
            nOverbooked--;
            nPrenotati++;
            c.avvisa(new Risposta(nPrenotati, true, false));
        }

    }

    public synchronized void stopPrenotazioni() {
        stopPrenotazioni = true;
        System.out.println("CHIUDONO LE PRENOTAZIONI, tot " + nPrenotati);
        for (int i = 0; i < nOverbooked; i++) {
            Cliente c = overbooked.remove(0);
            c.avvisa(new Risposta(0, false, false));
        }
    }

    public static void main(String[] args) {
        final Ristorante r = new Ristorante();

                Tempo t = new Tempo(r);
                t.start();


        for (int i = 1; i < 100; i++) {
            new Cliente(r).start();
            try{ Thread.sleep(100);}catch(InterruptedException e){}
        }
        try {
            t.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Mancano 2 giorni a capodanno");

    }

}


class Risposta {
    public int nPrenotazione;
    public boolean prenotato;
    public boolean overbooking;

    Risposta(int n, boolean p, boolean o){
        nPrenotazione=n;
        prenotato = p;
        overbooking=o;
    }
}
class Cliente extends Thread{
    Ristorante r;
    Risposta ris;
    Cliente(Ristorante rr){this.r=rr;}
    public synchronized void avvisa(Risposta s){
        ris=s;
        this.notify();
    }
    public void run(){
        synchronized(this){
            ris=r.prenota(this);
            try{
                while(!ris.prenotato || ris.overbooking){
                    this.wait();
                }
            }catch(InterruptedException e){}
        }

        int ncli=ris.nPrenotazione;
        int p=(int)(Math.random()*15);
        System.out.println(p);
                if(ris.prenotato && (p%3)==0){
                    try{ this.sleep(50);}catch(InterruptedException e){}
                    System.out.println("sono il cliente " + ncli+   " e disdico");
                    r.disdici();
                }
    }
}

class Tempo extends Thread {
    private Ristorante r;

    Tempo(Ristorante rr) {
        r = rr;
    }

    public void run() {
        for (int i = 1; i < 29; i++) {
            try {
                sleep(150);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        r.stopPrenotazioni();
    }
}