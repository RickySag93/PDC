import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Host1 {
    public static void main(String[] a) throws RemoteException, MalformedURLException {
        LocateRegistry.createRegistry(1099);
        Scacchiera s=new Scacchiera();

        try {
            Naming.rebind("s",s);
        } catch (RemoteException e) {
            e.printStackTrace();}


        System.out.println("punteggio finale TASTIERA" );

    }
}
