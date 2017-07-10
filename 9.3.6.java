interface R extends Remote{
    public void m() throws RemoteException; 
    public void n() throws RemoteException;
}

class RImpl extends UnicastRemoteObject implements{
    boolean condizione=false;
    RImpl() throws RemoteException;
    public void m() throws RemoteException{
        try{
            synchronized(this){
                while(condizione==false){
                    wait();
                }

            }
        }catch(Exception e){}
    } 
    public void n() throws RemoteException{
        try{
            synchronized(this){
                    condizione=true;
                    notify();
                }
            }
        catch(Exception e){}
    }
}



/*SERVER*/

public void main(String[]a){
    public void main(String []a){
        R r=new Rimpl();
        Naming.rebind("pippo",a);
    }
}

/*CLIENT*/

public void main(String[]a){
    private boolean cond=false;

    public void main(String []a){
        R r=(R) Naming.lookup("pippo");
        new Thread{
            public void run(){
                r.m();
                cond=true;          
            }
        }.start(); 

        new Thread{
            public void run(){
                while(!cond)
                    r.n();
            }
        }.start();       
    }
}