package ma.emsi.e_bankingbackend.exceptions;

public class BalanceNotSufficientException extends Exception {
    public BalanceNotSufficientException(String balanceNotSufficent) {
        super(balanceNotSufficent);
    }
}
