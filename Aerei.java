class Aereo{
    private static int n;
    private int num;
    private String direzione;

    Aereo(String d){num=n++;direzione=d;}
    //ATTENZIONE: invocare questo metodo quando l'aereo è in pista
    public void stampa(){System.out.println("Aereo num "+num+" - "+direzione);}
}

class GeneraArrivi extends Thread{
    private Controllore contr;

    GeneraArrivi(Controllore c){contr=c;}
    public void run(){
        while(true){
            contr.add_arrivi(new Aereo("in arrivo"));
        }
    }
}

class GeneraPartenze extends Thread{
    private Controllore contr;
    GeneraPartenze(Controllore c){contr=c;}
    public void run(){
        while(true){
            contr.add_partenze(new Aereo("in partenza"));
        }
    }
}


class Controllore extends Thread{
	 Object pista=new Object();
 
    private Vector<Aereo> coda_arrivi=new Vector<Aereo>();
    private Vector<Aereo> coda_partenze=new Vector<Aereo>();
  

  public void add_arrivi(Aereo a){
  		synchronized (coda_arrivi){
  			coda_arrivi.add(a);
  			coda_arrivi.notifyAll();
  	    }
  }

  public void add_partenze(Aereo a){
  	synchronized (coda_partenze){
  		coda_partenze.add(a);
  		coda_partenze.notifyAll();
  	}
  }
  
  public void gestisci_arrivo(){
  	Aereo a=null;

  	synchronized(coda_arrivi){
  		if(coda_arrivi.isEmpty()){
  			new TS(coda_arrivi).start();
  			return;
  		}
  		else{
  			a=coda_arrivi.remove(0);
  		}
  	}
  	synchronized(pista){
  		a.stampa();
  	}

  }

    public void gestisci_partenza(){
  	Aereo a=null;

  	synchronized(coda_partenze){
  		if(coda_partenze.isEmpty()){
  			new TS(coda_partenze).start();
  			return;
  		}
  		else{
  			a=coda_partenze.remove(0);
  		}
  	}
  	synchronized(pista){
  		a.stampa();
  	}


  private char prox_transito(){
        int r=(int)(Math.random()*2);
        if(r==0) return 'A';
        else return 'P';
  }

    public void run(){
        while(true){
            char c=prox_transito();
            if(c=='A') gestisci_arrivo();
            else gestisci_partenza();
        }
    }

      private class TS extends Thread{
        Vector<Aereo> coda;
        
        TS(Vector<Aereo> s){coda=s;};

        public void run(){

       }

      }

    public static  void  main(String[] args){
        Controllore contr=new Controllore();
        GeneraArrivi gA=new GeneraArrivi(contr);
        GeneraPartenze gP=new GeneraPartenze(contr);
        gP.start(); gA.start(); contr.start();
    }

}