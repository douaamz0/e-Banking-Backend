package ma.emsi.e_bankingbackend.exceptions;

//une exception surveillée qui hérite de Exception il fuat soit encapsuler le code
//dans try catch sinon utuliser throws
public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
