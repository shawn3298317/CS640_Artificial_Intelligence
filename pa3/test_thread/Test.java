import java.util.*;
class positionTicTacToe {
    int x;
    int y;
    int z;
    int state;

    public void printPosition()
    {
        System.out.print("("+x+","+y+","+z+")");
        System.out.println("state: "+state);
    }

    positionTicTacToe(int setX,int setY,int setZ,int setState)
    {
        x = setX;
        y = setY;
        z = setZ;
        state = setState;
    }
    positionTicTacToe(int setX,int setY,int setZ)
    {
        x = setX;
        y = setY;
        z = setZ;
        state = -1;
    }

    positionTicTacToe create_copy(){
        return new positionTicTacToe(this.x, this.y, this.y, this.state);
    }

    @Override
    public String toString(){
        return "(" + x + "," + y + "," + z + ")=state(" + state + ")";
    }
}

class Test{
    public static void main(String[] args){
        List<positionTicTacToe> list = new ArrayList<positionTicTacToe>();
        list.add(new positionTicTacToe(0,0,0));


        for(int i=0; i<10; i++){

            List<positionTicTacToe> list_clone = new ArrayList<positionTicTacToe>();
            for(int j=0; j<list.size(); j++){
                list_clone.add(list.get(j).create_copy());
            }
            list_clone.get(j).state += i;
        }

        System.out.println("orig lst: ");
        System.out.println(list);
//        System.out.println("clone lst: ");
//        System.out.println(list_clone);
    }

}