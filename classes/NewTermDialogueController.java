package classes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class NewTermDialogueController implements Initializable {

    @FXML
    private TextField tf_portugues, tf_ingles;

    @FXML
    private TextArea ta_significadoPortugues, ta_significadoIngles;

    private static TextField tf_portugues_static, tf_ingles_static;

    private static TextArea ta_significadoPortugues_static, ta_significadoIngles_static;

    private String caminhoAudio;

    //Métodos

    public void initialize(URL url, ResourceBundle resourceBundle){
        tf_portugues_static = tf_portugues;
        tf_ingles_static = tf_ingles;
        ta_significadoPortugues_static = ta_significadoPortugues;
        ta_significadoIngles_static = ta_significadoIngles;
    }

    public static void refresh(String term, String idioma){
        System.out.println(tf_ingles_static.getText());
        tf_ingles_static.setText("vamo ver entaum");
        Database con = new Database();
        Palavra aux;
        try{
            if(idioma.equals("Inglês"))
                aux = con.buscaPalavra(term.toUpperCase());
            else
                aux = con.buscaPalavraPortugues(term.toUpperCase());

            tf_portugues_static.setText(aux.getPortugues());
            tf_ingles_static.setText(aux.getEnglish());
            ta_significadoIngles_static.setText(aux.getSignificado_en());
            ta_significadoPortugues_static.setText(aux.getSignificado_pt());

        }catch(SQLException e){
            System.out.println("Não encontrado.");
            }

    }


    public void adicionarPronuncia(){
        try {
            // 1 - Selecionamos o arquivo de áudio.
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("WAV files (*.wav)", "*.pdf");
            fileChooser.getExtensionFilters().add(extensionFilter);
            //Ver como pegar o caminho
        } catch (Exception exc){
            exc.printStackTrace();
        }
    }

    public void voltarAoDicionario(){
        Main.showDictionary();
        tf_portugues.clear();
        tf_ingles.clear();
        ta_significadoIngles.clear();
        ta_significadoPortugues.clear();
        caminhoAudio = "";
    }

    public void salvarNovoTermo(){
        Database con = new Database();

        try {
            con.apagarTupla(tf_ingles_static.getText());
            con.inserir(tf_portugues_static.getText().toUpperCase(), tf_ingles_static.getText().toUpperCase(),
                    ta_significadoPortugues_static.getText().toUpperCase(), ta_significadoIngles_static.getText().toUpperCase(), caminhoAudio);
            System.out.println("Inserido com sucesso.");
            Main.showMessage("Inserido com sucesso.");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

}