class Casella {
    private boolean bianco;
    private int contenuto=0; //numero pedine su casella
    Casella(boolean b){bianco =b;}
    void swapColor(){ bianco= !bianco; }
    int size(){return contenuto;}

    synchronized public int add(){
        int mangiate=0;
        if(this.bianco){
            contenuto++;
        }
        else{
            mangiate=contenuto;
            contenuto=1;
        }
        return mangiate;
    }
}