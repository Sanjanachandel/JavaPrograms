public class Solution{
    public static void main(String[] args){
        GymMember g1 = new GymMember(101,"Arjun");
        g1.show();

        GymMember g2 = new GymMember(102,"Neha");
        g2.show();

        GymMember g3 = new GymMember(103,"Ravi");
        g3.show();

        System.out.println("Total Members: "+GymMember.NumMem);
    

    }
}

class GymMember{
    int memberId;
    String memberName;
    int joiningFee = 500;
    static int NumMem;

    GymMember(int memberId,String memberName){
        this.memberId = memberId;
        this.memberName = memberName;
        this.NumMem++;
    }

    public void show(){
        System.out.println(memberId + " " + memberName+" Fee: "+joiningFee);
    }
}