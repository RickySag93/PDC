import java.lang.Math; // headers MUST be above the first class

public class Torre{
    Integer nInEntrata = 0;
    Integer nInAlto = 0;
    Object scale=new Object();
    Object lock_entrata=new Object();

    class GuardiaEntrata extends Thread{
        public void run(){
            while(true){
                int k=0;
                synchronized(lock_entrata){
                    try{
                        while(Torre.this.nInEntrata==0){
                            System.out.println("sto aspettando persone da uccidere");
                            lock_entrata.wait();
                        }
                        if(Torre.this.nInEntrata<10){
                            k=Torre.this.nInEntrata;
                        }
                        else{k=10;}
                        nInEntrata-=k;
                        System.out.println("messe in coda "+k+" persone");

                    }catch(InterruptedException e){}
                }

                synchronized(scale){

                    Torre.this.nInAlto+=k;
                    System.out.println("saliti in " +k);

                    scale.notify();
                }
            }
        }

    }

    class GuardiaCima extends Thread{
        public void run(){
            while(true){
                synchronized(scale){
                    try{
                        while(nInAlto==0){
                            scale.wait();
                        }
                        if(Torre.this.nInAlto<10){
                            Torre.this.nInAlto=0;
                        }
                        else{Torre.this.nInAlto-=10;}

                        System.out.println("usciti in 10");
                        scale.notify();
                    }catch(InterruptedException e){}

                }
            }
        }
    }
    public void entrano(int k){
        synchronized (lock_entrata){
            nInEntrata+=k;
            System.out.println("arrivati in "+k);
            lock_entrata.notifyAll();
        }
    }

    public static void main(String []a){
        Torre carlo=new Torre();

        GuardiaCima gc=carlo.new GuardiaCima();
        GuardiaEntrata ge=carlo.new GuardiaEntrata();

        ge.start();
        gc.start();
        for(int i=0;i<50; i++){
            int k=(int)(1+ Math.random()*5);
            carlo.entrano(k);
            System.out.println("ARRIVATI "+k);

        }
    }
}