package classes;

public class MainMenuController {

    public void abrirPDF(){
        Main.setReturn_root("main");
        if(ViewerController.abrirNovo())
            Main.showViewer();
    }
    public void abrirDicionario(){
        Main.setReturn_root("main");
        Main.showDictionary();
    }

}
