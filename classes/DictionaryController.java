package classes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.ObservableList;//Para o listView
import javafx.collections.FXCollections;//Para o listView

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;
import java.io.*;

public class DictionaryController implements Initializable {

    // FXML

    @FXML
    private ListView<String> lv_termos;

    @FXML
    private ChoiceBox<String> cb_selecionarIdioma;

    @FXML
    private Label lb_significado;

    @FXML
    private TextField tf_pesquisarTermo;

    //Métodos

    public void initialize(URL location, ResourceBundle resources) {
        cb_selecionarIdioma.setValue("Inglês");
        cb_selecionarIdioma.getItems().addAll("Inglês", "Português");

        lv_termos.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> exibirSignificado());

        Database con = new Database();
        try {
            ArrayList<Palavra> banco = new ArrayList<>(con.lerBanco());
            ObservableList<String> items = FXCollections.observableArrayList ();
            for(int i = 0; i < banco.size(); i++){
                items.add(banco.get(i).getEnglish());
            }
            lv_termos.setItems(items);
        } catch(SQLException e){
            System.out.println("Erro ao carregar dicionário: "+e.getMessage());
        }
        Collections.sort(lv_termos.getItems());
    }

    public void pesquisarTermo(){
        String termo = tf_pesquisarTermo.getText().trim().toUpperCase();
        try{
            lv_termos.getSelectionModel().select(termo);
        } catch (Exception exc){
            Main.showMessage("Termo não encontrado.");
        }
    }

    public void adicionarTermo(){
        Main.showNewTermDialogue();
    }

    public void editarTermo() {
        // 1 - Preenchemos os campos do NewTermDialogue com informações da palavra selecionada.
        NewTermDialogueController.refresh(lv_termos.getSelectionModel().getSelectedItem(), cb_selecionarIdioma.getValue());
        // 2 - Mostramos a root.
        Main.showNewTermDialogue();

    }

    private void exibirSignificado(){
        Database con = new Database();
        Palavra aux;
        if(cb_selecionarIdioma.getValue().equals("Português")){
            try {
                aux = con.buscaPalavraPortugues(lv_termos.getSelectionModel().getSelectedItem().toUpperCase());
                lb_significado.setText(aux.getSignificado_pt());
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
        if(cb_selecionarIdioma.getValue().equals("Inglês")){
            try {
                aux = con.buscaPalavra(lv_termos.getSelectionModel().getSelectedItem().toUpperCase());
                lb_significado.setText(aux.getSignificado_en());
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void alternarList(){

        Database con = new Database();
        try {
            ArrayList<Palavra> banco = new ArrayList<>(con.lerBanco());
            ObservableList<String> items = FXCollections.observableArrayList ();

            if(cb_selecionarIdioma.getValue().equals("Inglês"))
                for (int i = 0; i < banco.size(); i++)
                    items.add(banco.get(i).getEnglish());
            else
                for (int i = 0; i < banco.size(); i++)
                    items.add(banco.get(i).getPortugues());

            lv_termos.setItems(items);
            Collections.sort(lv_termos.getItems());

        }catch(SQLException e){
            System.out.println("Erro ao carregar dicionário: "+e.getMessage());
        }

    }

    public void alternarLabel_Ingles(){// Alternar label de significados para inglês.
        Database con = new Database();
        /*if(cb_selecionarIdioma.getValue().equals("Portugues")){
            try {
                Palavra aux = con.buscaPalavraPortugues(lv_termos.getSelectionModel().getSelectedItem());
                lb_significado.setText(aux.getSignificado_pt());
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        else*/
        if(cb_selecionarIdioma.getValue().equals("Ingês")) {
            try {
                Palavra aux = con.buscaPalavraPortugues(lv_termos.getSelectionModel().getSelectedItem());
                lb_significado.setText(aux.getSignificado_en());
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void alternarLabel_Portugues(){ // Alternar label de significados para português.
        Database con = new Database();
        if(cb_selecionarIdioma.getValue().equals("Portugues")){
            try {
                Palavra aux = con.buscaPalavraPortugues(lv_termos.getSelectionModel().getSelectedItem());
                //Setar o significado aqui
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        /*else if(cb_selecionarIdioma.getValue().equals("Ingês")) {
            try {
                con.buscaPalavraPortugues(lv_termos.getSelectionModel().getSelectedItem());
                //Setar o significado aqui
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

         */
    }

    public void ouvirPronuncia(){

        if(cb_selecionarIdioma.getValue().equals("Inglês")) {
            Database con = new Database();
            try {
                Palavra aux = con.buscaPalavra(lv_termos.getSelectionModel().getSelectedItem());
                File file = new File("src\\sounds\\"+aux.getSom());
                PlaySound som = new PlaySound();
                som.playSound(file.getAbsolutePath().replace("\\","/"));

            }catch (SQLException e){
                System.out.println(e.getMessage());
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void voltar(){
        Main.showLastSeenRoot();
    }

}
