public class UserLogin {
    public static void main(String[] args) {

        String username = "admin";
        String password = "12345";
        boolean isActive = true;

        boolean canLogin = 
                (!username.isEmpty() && !password.isEmpty() && isActive);

        String result = canLogin ? "Login Successful" : "Login Failed";
        System.out.println(result);
    }
}
