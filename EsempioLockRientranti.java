



class RSImpl{

    public synchronized void m(RCImpl c){
        System.out.println(c);

        System.out.println(this);
        try{
            System.out.println("metodo m di RS");
         
            System.out.println(c.m(this));

        } catch(Exception e){}


    }
}

class RCImpl {

    public int m(RSImpl s){
        synchronized(this){
            try{
                System.out.println("metodo m di RC");

                s.m( this);
            } catch(Exception e) {}
        }
        return 5;
    }
}

public class Main {
    public static void main(String[] a){

        RSImpl s = new RSImpl();

        RCImpl c= new RCImpl();
        s.m(c);
    }
}





 
