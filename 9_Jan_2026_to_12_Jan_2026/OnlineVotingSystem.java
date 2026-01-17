package Collection;

public class OnlineVotingSystem  {

    public static void main(String[] args) {

        VotingService service = new VotingService();

        Voter v1 = new Voter("V1", "Aman");
        Voter v2 = new Voter("V2", "Neha");
        Voter v3 = new Voter("V3", "Rahul");

        v1.setAge(service.parseAge("25"));
        v2.setAge(service.parseAge("17"));
        v3.setAge(service.parseAge("abc"));

        service.displayStatus(v1);
        service.displayStatus(v2);
        service.displayStatus(v3);
    }
}


class Voter {

    private String voterId;
    private String voterName;
    private Integer age;  

    Voter(String voterId, String voterName) {
        this.voterId = voterId;
        this.voterName = voterName;
    }

    public String getVoterName() {
        return voterName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}


class VotingService {

    Integer parseAge(String ageStr) {
        try {
            return Integer.valueOf(ageStr);   
        } catch (NumberFormatException e) {
            return null;   
        }
    }

    boolean isEligible(Integer age) {
        return age != null && age >= 18;
    }

    void displayStatus(Voter voter) {

        Integer age = voter.getAge();

        if (age == null) {
            System.out.println(voter.getVoterName() + " --> Invalid Age Input");
        } else if (isEligible(age)) {
            System.out.println(voter.getVoterName() + " --> Eligible to Vote");
        } else {
            System.out.println(voter.getVoterName() + " --> Not Eligible to Vote");
        }
    }
}

