import model.*;
import controller.*;
import view.*;

public class Main {
    public static void main(String[] args) {
        PIMModel model = new PIMModel();
        PIMView view = new PIMView();
        PIMcontroller controller = new PIMcontroller(model, view);
        controller.run();
    }
}