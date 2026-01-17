package ObjectHandling;

public class EmailValidation {
    public static void main(String[] args){
        String emailHeader = "From: john.doe@company.com | To: jane.smith@company.com | Subject: Quarterly Financial Report | Date: 2025-03-10";
        EmailHeaderProcessor processor = new EmailHeaderProcessor(emailHeader);

        System.out.println("From          : " + processor.getFrom());
        System.out.println("To            : " + processor.getTo());
        System.out.println("Masked Sender : " + processor.getMaskedSender());
        System.out.println("Same Domain   : " + processor.isSameDomain());
        System.out.println("Subject       : " + processor.getSubject());
        System.out.println("Subject Length: " + processor.getSubjectLength());
        System.out.println("Date          : " + processor.getDate());
        
    }
}

class EmailHeaderProcessor{
    private String header;
    EmailHeaderProcessor(String header){
        this.header = header;
    }

    public String getFrom(){
        int s = header.indexOf("From:") + "from:".length();
        int e = header.indexOf("|",s);
        return header.substring(s,e).trim();

    }

    public String getTo(){
        int s = header.indexOf("To:")+ "To:".length();
        int e = header.indexOf("|",s);
        return header.substring(s,e).trim();
    }

    public String getSubject(){
        int s = header.indexOf("Subject:") + "Subject:".length();
        int e = header.indexOf("|",s);
        return header.substring(s,e).trim(); 
    }

    public String getDate(){
        int s = header.indexOf("Date:") + "Date:".length();
        return header.substring(s).trim();
    }

    public boolean isSameDomain(){
        String from = getFrom();
        String to = getTo();

        String fromDomain = from.substring(from.indexOf('@')+1);
        String toDomain = to.substring(to.indexOf('@')+1);

        return fromDomain.equalsIgnoreCase(toDomain);
    }

    public String getMaskedSender(){
        String from = getFrom();

        int atIdx = from.indexOf('@');
        String username = from.substring(0,atIdx);
        String domain = from.substring(atIdx);

        String masked = username.substring(0,2)+"***";

        return masked + domain;
    }

    public int getSubjectLength(){
        return getSubject().length();
    }
}
