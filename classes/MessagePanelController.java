package classes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class MessagePanelController implements Initializable {

    @FXML
    private Text tx_mensagem;

    private static Text tx_mensagem_static;

    // Métodos

    public void initialize(URL url, ResourceBundle resourceBundle){
        tx_mensagem_static = tx_mensagem;       // Redundância justificada pelo fato de atributos FXML não poderem ser estáticos.
    }

    public static void setMessage(String msg){
        tx_mensagem_static.setText(msg);
    }

    public void close(){
        Main.getSecondary_s().close();
    }

}
