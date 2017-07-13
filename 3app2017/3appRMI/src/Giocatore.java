import java.rmi.RemoteException;

class Giocatore extends Thread{
    private String name;
    private ScacchieraInterface scacchiera;
    private int score=0;

    Giocatore(String n, ScacchieraInterface s) {
        name=n; scacchiera=s;
    }

    public int getScore() {
        return this.score;
    }

    public void run(){
        System.out.println("iniziato gioco per " + name);
        for(int i=0; i<30; i++){
            int r=((int)(Math.random()*10))%ScacchieraInterface.NRIGHE;
            int c=((int)(Math.random()*10))%ScacchieraInterface.NCOLONNE;
            try {
                score +=scacchiera.add(r,c);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        try {
            System.out.println("provo a cambiare colore");
            scacchiera.trySwap();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        for(int i=0; i<30; i++){
            int r=((int)(Math.random()*10))%ScacchieraInterface.NRIGHE;
            int c=((int)(Math.random()*10))%ScacchieraInterface.NCOLONNE;
            try {
                score +=scacchiera.add(r,c);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }


    }

}