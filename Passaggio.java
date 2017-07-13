import java.util.Vector;

/**
 * Created by aless on 23/06/2017.
 */
class Passaggio{ }

class Stop extends Thread{
    Passaggio p;
    Vector<String> o= new Vector<String>();
    Stop(Passaggio x, String a){p=x; this.aggiungi(a);}

    public void run(){

            try {
                while(true){
                    synchronized (this) {
                        while (o.size() == 0) {
                            this.wait();
                        }
                    }

                    synchronized (p){
                        for (int i = 0; i <= o.size(); i++) {
                            System.out.println(o.get(i) + "macchina in transito");
                            o.remove(i);
                        }
                    }
                }

            }catch(InterruptedException e){
            }
    }

    public synchronized void aggiungi(String a){
        o.add(a);
        this.notifyAll();
    }

}

public class Avvio{
    public static void main(String[] arg) {
        Passaggio p =new Passaggio();
        Stop s1=new Stop(p,new String("a"));

        Stop s2=new Stop(p,new String("b"));

        s1.start();
        s2.start();

        for(int i=0; i<30; i++){
            s1.aggiungi("e");
            s2.aggiungi("pippo");
        }

    }
}
