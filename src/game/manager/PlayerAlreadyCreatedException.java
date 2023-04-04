package game.manager;

public class PlayerAlreadyCreatedException extends Exception {
    public PlayerAlreadyCreatedException(String errorMessage) {
        super(errorMessage);
    }
}
