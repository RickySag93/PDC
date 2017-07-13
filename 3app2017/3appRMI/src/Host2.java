import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Vector;

public class Host2 {
    public static void main(String[] a) throws RemoteException, NotBoundException, MalformedURLException, InterruptedException {
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ScacchieraInterface s=(ScacchieraInterface) Naming.lookup("s");
        Giocatore g=new Giocatore("Pippo", s);
        g.start();
        g.join();
        s.stsampaFine();

        System.out.println("punteggio finale di g2 " + g.getScore());
    }
}
