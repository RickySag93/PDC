//2014-01-07
interface I extends Remote{
	public void f()throws RemoteException;
	public void g()throws RemoteException;
}

public class IImpl extends UnicastRemoteObject implements I{

	public synchronized void f()throws RemoteException{
		for(int i=0;i<5;i++) 
			System.Out.println("FF");
	}
	public void g()throws RemoteException{
		for(int i=0;i<5;i++) 
			System.Out.println("GG");
	}
}

public class S{
	public static void main(String[] a){
		IImpl u= new IImpl();

		Naming.rebind("ui",u);

	}

}


public class C1{
	public void m(String s){
		for(int i=0;i<5;i++) 
			System.Out.println(s);
	}

	public static void main(String[] a){
		IImpl u= Naming.lookup("ui");

		class T1 extends Thread{
			public void run(){
				synchronized(u){
					m("A");
					u.f();
					m("B");
				}
			
			}
		}

		class T2 extends Thread{
			public void run(){
				synchronized(u){
					m("UNO");
					u.g();
					m("DUE");
				}
			}
		}

		T1 t1=new T1();
		T2 t2=new T2();
		t1.start();
		t2.start();
		
	}
}



public class C2{
	public static void main(String[] a){
		IImpl u= Naming.lookup("ui");
		u.f();
	}
}