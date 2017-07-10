class Modulo{
    private static int n=0;
    private String contenuto;
    Modulo(){contenuto="modulo n."+n; n=n+1;}
    private Modulo(Modulo m){n=m.n; contenuto=m.contenuto;}
    public String toString(){return contenuto;}

    void modifica(String s){
        try{ contenuto=contenuto+s;
            Thread.sleep((int)(Math.random()*60));
        }catch(Exception e){}
    }

    Modulo copia(){
        return new Modulo(this);
    }
}

class Repository {
    private Modulo[] codiceSorgente;
    private int totS;


    Repository(){
        codiceSorgente=new Modulo[4];
        for(int i=0;i<codiceSorgente.length;i++) codiceSorgente[i]=new Modulo();
    }

    public String toString(){
        String s="Contenuto del repository:\n";
        for(Modulo m:codiceSorgente) s=s+m+" \n";
        return s;
    }

    private void build(){
        System.out.println(this);
    }

    Modulo getCopyM(int i){
        return codiceSorgente[i].copia();
    }

    boolean testM(int i, Modulo m){
        return codiceSorgente[i].toString()==m.toString();
    }

    void setM(int i, Modulo m){
        totS++;
        codiceSorgente[i]=m;
    }

    public void begin(int n){
        new Thread(){
            public void run(){
                try{
                    synchronized(Repository.this){
                        while(totS<n)
                            Repository.this.wait();
                        Repository.this.build();
                    }
                }catch(InterruptedException e){}
            }
        }.start();
    }
}

class Sviluppatore extends Thread{
    private Repository r;
    private String nome;
    private int nMod;
    boolean modifica=false;
    Sviluppatore(Repository rep, String n){
        r=rep;nome=n; nMod=((int)(Math.random()*10)%4);//sceglie un modulo a caso da modificare
    }
    public void run(){
        while(!modifica){
            Modulo originale=r.getCopyM(nMod);
            Modulo originale2=r.getCopyM(nMod);

            originale2.modifica("modificato da sviluppatore " + nome);
            synchronized(r){

                if(!r.testM(nMod,originale2)){
                    r.setM(nMod,originale2);
                    modifica=true;

                    r.notify();
                }
            }
        }
    }
}
public class CI{
    public void end(Sviluppatore[] elenco){
        for(Sviluppatore s : elenco){
            try{
                s.join();
            }catch (InterruptedException e){}
        }
        System.out.println("fine main");
    }
    public static void main(String[] a){
        Repository r=new Repository();
        Sviluppatore[] elencoS=new Sviluppatore[18];

        for(int i=0;i<elencoS.length;i++){
            elencoS[i]=new Sviluppatore(r,"sviluppatore "+i);
        }


        r.begin(elencoS.length);

        for(Sviluppatore s : elencoS){
            s.start();
        }
        System.out.println("creato sviluppatore ");
        CI i=new CI();
        i.end(elencoS);
    }
}
