package classes;

public class ExitPanelController{

    public void sim(){
        Main.getSecondary_s().close();
        Main.getPrimary_s().close();
    }
    public void nao(){
        Main.getSecondary_s().close();
    }

}
