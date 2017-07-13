import java.util.Vector;

class Cliente{
    private int n;
    private static int nCliente;
    Cliente(){n=nCliente++;}
    public int chi(){return n;}
}


public class Posta{

    private static class Sportello{
        private int n;
        private static int nSportello;
        private boolean libero=true;
        Sportello(){
            n=nSportello++;
        }
        public boolean libero(){return libero;}
        public int n(){return n;}
        public void occupa(){libero=false;}
        public void libera(){libero=true;}
    }

    Sportello[] elencoSportelli =new Sportello[6];
    int nSportelli;
    private Vector<Cliente> coda=new Vector<Cliente>();

    Posta(){
        elencoSportelli[0]=new Sportello();
        elencoSportelli[1]=new Sportello();
        elencoSportelli[2]=new Sportello();
        nSportelli=3;
    }

    private int qualeLibero(){
        for(int i=0;i<nSportelli; i++)
            if(elencoSportelli[i].libero()){
                return i;
            }
        return -1;

    }

    private void apri(){
        if(nSportelli<5){
            elencoSportelli[nSportelli]=new Sportello();
            nSportelli++;
        }
    }

    private void chiudi(int i){ // chiude lo sportello i-esimo
        synchronized(elencoSportelli[i]){
            try{
                while(!elencoSportelli[i].libero){
                    elencoSportelli[i].wait();
                }
                elencoSportelli[i].occupa();

            }catch(InterruptedException e){}

        }

    }

    private class GeneraClienti extends Thread{

        public void run(){ // genera clienti che si mettono in coda
            for(int i=0;i<10;i++){
                coda.add(new Cliente());
            }
        }
    }

    private class GestoreClienti extends Thread{

        private void occupa(final Sportello s, final Cliente c){
            synchronized(s){
                System.out.println(s +" sta servendo "+ c);
                try{
                    sleep(30);
                }catch(InterruptedException e){}
                s.notifyAll();
            }
        }

        public void run(){
            while(true){
                for(int k=0;k<elencoSportelli.length && !coda.isEmpty() ;k++){
                    if(elencoSportelli[k].libero()){
                        occupa(elencoSportelli[k],coda.remove(0));
                    }
                }
            }

        }
    }


    public static void main(String[]a){
        Posta p =new Posta();
        GeneraClienti o= p.new GeneraClienti();
        o.start();
        GestoreClienti l =p.new GestoreClienti();
        l.start();
    }
}
	