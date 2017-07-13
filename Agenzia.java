class Persona extends Thread{
	String nome;
	Viaggio v;
	Persona(String o, Viaggio vi){this.nome; this.v=vi;}

	public void run(){

		if(v.prenota()){

			System.out.println("confermato viaggio per "+nome);
		}
		else{
			synchronized(v){
				try{
					while(!chiuso && !confermato){
						viaggio.wait();
					}

				}catch(InterruptedException e){}
			}
		}
	}
}
class Viaggio{
	int nMinimo;
	String nome;
	boolean confermato =false;
	boolean chiuso=false;
	Viaggio(String n, int o){this.nome=n; this.nMinimo=o;}
	Vector<Persona> codaViaggiatori;

	boolean synchronized prenota(Persona p){
		codaViaggiatori.add(p);
		if(codaViaggiatori.size()<nMinimo){
			return false;
		}
		confermato=true;
		this.notifyAll();
		return true;
	}

	void synchronized chiudi(){
		chiuso=true;
		if(confermato){
			for(int i=0; i<codaViaggiatori.size(); i++)
				System.out.println(codaViaggiatori.get(i));
		}
		else{
			System.out.println("Viaggio non confermato ma CHIUSO");
			this.notifyAll();
		}
	}
}
public class Agenzia {

	public static void main(String[] a){
		Viaggio v=new Viaggio("Namibia",4);
		Persona p1=new Persona("Alice",v);
		Persona p2=new Persona("Bob",v);
		Persona p3=new Persona("Carl",v);
		Persona p4=new Persona("Doug",v);
		Persona p5=new Persona("Eric",v);
		Persona p6=new Persona("Frank",v);
		p1.start();p2.start();p3.start();
		p4.start();p5.start();p6.start();
		try{ Thread.sleep(200); }catch(Exception e){}
		v.chiudi();
	}
}