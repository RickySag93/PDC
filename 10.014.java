/*B*/
interface D extends Remote{ //di tutti
	public void trylock()throws RemoteException;
}
public class DImpl extends UnicastRemoteObject implements D{//host1
	Object lock= new Object();
	static int cond=0;

	DImpl()throws RemoteException{};
	public void trylock()throws RemoteException{
		synchronized(lock){
			try{
				cond++;
				while(cond<2){
					lock.wait();
				}
				if(cond==2){
					cond++;
					lock.notify();
				}
			}catch(InterruptedException e){}
		}
	}
}
class C1{//host1
	void printC1(String s){
		System.out.println(s);
	}
}

class C2{//host2
	void printC2(String s){
		System.out.println(s);
	}
}

public class HOST1{

	public static void main(String []a){
		D d=new DImpl();
		Naming.rebind("d", d);

		C1 c=new C1();
		
		new Thread(){
			public void run(){
				for(int i=0; i<5; i++){
					c.printC1("A");
				}
				d.trylock();
				for(int i=0; i<5; i++){
					c.printC1("B");
				}
			}
		}.start();
	}
}

public class HOST2{

	public static void main(String []a){

		D e= (D)Naming.lookup("d");
		C2 d=new C2();

		new Thread(){
			public void run(){
				for(int i=0; i<5; i++){
					d.printC2("C");
				}
				e.trylock();
				for(int i=0; i<5; i++){
					d.printC2("D");
				}
			}
		}.start();

	}
}


/*A*/

class C1{
	void printC1(String s){
		System.out.println(s);
	}
}

class C2{
	void printC2(String s){
		System.out.println(s);
	}
}

public class Main{
	static int cond=0;

	public static void main(String []a){
		C1 c=new C1();
		C2 d=new C2();
		
		Object lock= new Object();

		new Thread(){
			public void run(){
				for(int i=0; i<5; i++){
					c.printC1("A");
				}
				synchronized(lock){
					try{
						Main.cond++;
						while(Main.cond<2){

							System.out.println("Sto aspettando 2");
							lock.wait();
						}

						System.out.println("Sono partito 1");
						lock.notify();
					}catch(InterruptedException e){}
				}
				for(int i=0; i<5; i++){
					c.printC1("B");
				}
			}
		}.start();

		new Thread(){
			public void run(){
				for(int i=0; i<5; i++){
					d.printC2("C");
				}
				synchronized(lock){
					try{
						Main.cond++;
						while(Main.cond<2){
							System.out.println("Sto aspettando 1");
							lock.wait();
						}

						System.out.println("partito 2");
						lock.notify();
					}catch(InterruptedException e){}
				}
				for(int i=0; i<5; i++){
					d.printC2("D");
				}
			}
		}.start();

	}
}

