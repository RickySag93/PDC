import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Host3 {
    public static void main(String[] a) throws RemoteException, NotBoundException, MalformedURLException, InterruptedException {
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ScacchieraInterface s=(ScacchieraInterface) Naming.lookup("s");
        Giocatore g=new Giocatore("Mario", s);
        g.start();
        g.join();
        s.stsampaFine();
        System.out.println("punteggio finale di g1 " + g.getScore());
    }
}
