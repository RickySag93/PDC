/*pagina  145 libro di p3*/

public class Ex{
    Stato s=new Stato();
    int nT;
    Ex(int k){this.nT=k;}

    public class Stato{
        int st=0;
        synchronized void changeStato(){
            st=((st+1)%nT);
            this.notifyAll();
        }
    }

    public class T extends Thread{
        int n;
        T(String i){
            super(i);
            n=(Ex.this.nT++);

        }

        public void run(){
            while(true){
                synchronized (Ex.this.s){
                    try{
                        while(Ex.this.s.st!=n){
                            Ex.this.s.wait();
                        }
                        s.changeStato();
                    }catch(InterruptedException e){}

                    System.out.println(this);
                }
            }
        }
    }



    public static void main(String []a){
        Ex e=new Ex(0);
        Ex.T t1=e.new T("essere");

        Ex.T t2=e.new T("o");

        Ex.T t3=e.new T("essere carlo");
        t1.start();
        t2.start();
        t3.start();

        
    }
}