package classes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.Base64;
import java.util.ResourceBundle;

public class ViewerController implements Initializable{

    /*
        Precisamos que alguns atributos e métodos sejam estáticos. Alguns destes podem ser próprios do arquivo FXML
        associado, e por isso devem ser replicados, já que métodos e atributos FXML não podem ser estáticos.
    */

    // FXML

    @FXML
    private WebView webView;        // Deve ser replicado se tiver de ser usado como estático.

    @FXML
    private ChoiceBox<String> cb_idioma;

    @FXML
    private Label lb_significado;

    @FXML
    private Label lb_traducao;

    @FXML
    private TextField tf_termoAtraduzir, tf_inicioLeitura, tf_fimLeitura;

    // Outros

    private static WebEngine engine;
    private static Stage stage;
    private static FileChooser fileChooser;
    private static WebView webView_static;
    private static File file;

    //Métodos

    public void initialize(URL location, ResourceBundle resources) {
        try{

            webView_static = webView;           // Ponteiro 'estático' para atributo FXML.

            stage = new Stage();
            stage.setTitle("Abrir");

            fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extensionFilter);

            cb_idioma.getItems().addAll("Português", "Inglês");

            engine = webView.getEngine();
            String url = getClass().getResource("../resources/web/viewer.html").toExternalForm();
            engine.setUserStyleSheetLocation(getClass().getResource("../styles/web.css").toExternalForm());
            engine.setJavaScriptEnabled(true);
            engine.load(url);

        } catch (Exception exc){
            System.out.println("Falha na inicialização de controlador - ViewerController.");
            System.out.println("Mensagem de erro:" + exc.getMessage());
            exc.printStackTrace();
            System.exit(0);
        }
        System.out.println("Inicialização de controlador concluída - ViewerController.");

        carregamentoPorTexto();//Carrega o banco através do txt
    }

    public void iniciar(){      // Método não estático que invoca método estático. Redundância se deve à restrição explicada.
        abrirNovo();
    }

    static boolean abrirNovo(){
        System.out.println("Abertura de documento requisitada.");
        try{
            file = fileChooser.showOpenDialog(stage);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] data = fileInputStream.readAllBytes();

            String base64 = Base64.getEncoder().encodeToString(data);
            webView_static.getEngine().executeScript("openFileFromBase64('" + base64 + "')");
            return true;
        } catch (Exception exc){
            System.out.println("Arquivo não aberto.");
            return false;
        }
    }

    public void traduzir(){

        System.out.println("Tradução requisitada.");

        Database con = new Database();
        Palavra aux = null;
        try {
            aux = con.buscaPalavra(tf_termoAtraduzir.getText().toUpperCase());

        }catch(SQLException e){
            lb_significado.setText("Encontramos um problema! Entre em contato com o suporte para soluciona-lo");
            System.out.println("Erro ao buscar palavra: "+ e.getMessage());
        }

        if(aux != null){
            lb_traducao.setText(aux.getPortugues());
            if(Palavra.idiomaAtual.equals("pt")){
                lb_significado.setText(aux.getSignificado_pt());
            }
            if(Palavra.idiomaAtual.equals("en")) {
                lb_significado.setText(aux.getSignificado_en());
            }
        }
        else{
            lb_significado.setText("Desculpe, não encontramos a tradução. ");
        }
    }

    public void alternarIdioma(){
        if(cb_idioma.getValue().equals("Inglês")) {
            alternar_Ingles();
        }else {
            alternar_Portugues();
        }
    }

    public void alternar_Ingles(){
        System.out.println("Mudança de idioma requitsitada.");
        cb_idioma.setValue("Inglês");

        //Alternar significados para o inglês.

        Palavra.idiomaAtual = "en";

        Database con = new Database();
        Palavra aux = null;
        try{
        aux = con.buscaPalavra(tf_termoAtraduzir.getText().toUpperCase());
        }catch(SQLException e) {
            System.out.println("Erro ao trocar significado para en!");
            lb_significado.setText("Encontramos um problema! Entre em contato com o suporte para soluciona-lo");
        }
        if(aux != null){
                lb_significado.setText(aux.getSignificado_en());
        }

        else{
            lb_significado.setText("Desculpe, não encontramos a tradução para o idioma solicitado. ");
        }
    }

    public void alternar_Portugues(){
        System.out.println("Mudança de idioma requitsitada.");
        cb_idioma.setValue("Português");
        // Alternar significados para o português.

        Palavra.idiomaAtual = "pt";

        Database con = new Database();
        Palavra aux = null;
        try{
            aux = con.buscaPalavra(tf_termoAtraduzir.getText().toUpperCase());
        }catch(SQLException e) {
            System.out.println("Erro ao trocar significado para pt!");
            lb_significado.setText("Encontramos um problema! Entre em contato com o suporte para soluciona-lo");
        }
        if(aux != null){
            lb_significado.setText(aux.getSignificado_pt());
        }

        else{
            lb_significado.setText("Desculpe, não encontramos a tradução para o idioma solicitado. ");
        }
    }

    public void fecharArquivo(){
        Main.setReturn_root("viewer");
        Main.showMainMenu();
    }

    public void verDicionario(){
        Main.setReturn_root("viewer");
        Main.showDictionary();
    }

    public void reproduzirPronuncia(){
        System.out.println("Leitura de termo requisitada.");
        Database con = new Database();
        PlaySound p = new PlaySound();
        try{
            Palavra aux = con.buscaPalavra(tf_termoAtraduzir.getText().toUpperCase());
            if(aux != null) {
                File file = new File("src\\sounds\\" + aux.getSom().toLowerCase());
                PlaySound som = new PlaySound();
                som.playSound(file.getAbsolutePath().replace("\\", "/"));
            }
            else{
                System.out.println("Palavra não encontrada!");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
     }

    public void reproduzirLeitura(){
        // Reproduzir pronuncia de todos os termos entre tf_inicio e tf_fim.
        System.out.println("Leitura de texto requisitada.");

        int inicio, fim;
        try{
            inicio = Integer.parseInt(tf_inicioLeitura.getText());
            fim = Integer.parseInt(tf_fimLeitura.getText());
        } catch (Exception exc){
            System.out.println("Entrada inválida.");
            Main.showMessage("Entrada inválida.");
            return;
        }

        try {

            System.out.println("Extraindo texto.");
            String texto = "";
            PDDocument document = PDDocument.load(file);
            PDPageTree pdPageTree = document.getPages();

            if (inicio <= 0 || fim <= 0 || fim > pdPageTree.getCount() || inicio > pdPageTree.getCount() || inicio > fim) {
                System.out.println("Entrada inválida.");
                Main.showMessage("Entrada inválida.");
                document.close();
                return;
            }

            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            pdfTextStripper.setStartPage(inicio);
            pdfTextStripper.setEndPage(fim);
            texto = pdfTextStripper.getText(document);
            document.close();

            Database con = new Database();
            PlaySound som = new PlaySound();
            File file;
            Palavra aux;
            for (String termo : texto.split(" ")) {
                if(termo.length() == 0)
                    continue;
                if ((termo.charAt(termo.length() - 1) == '.') || (termo.charAt(termo.length() - 1) == ',') || (termo.charAt(termo.length() - 1) == '\n')) {
                    System.out.println("Pause");
                    termo = termo.substring(0,termo.length()-1);
                }
                System.out.println(termo);
                aux = con.buscaPalavra(termo.toUpperCase());
                if(aux != null){
                    file = new File("src\\sounds\\" + aux.getSom().toLowerCase());
                    som.playSound(file.getAbsolutePath().replace("\\", "/"));
                }

                //while(PlaySound.executando());
            }

        } catch (Exception exc) {
            System.out.println("Erro na leitura de texto.");
            System.out.println("Mensagem de erro: " + exc.getMessage());
            System.out.println("Rastro de Pilha:");
            exc.printStackTrace();
            Main.showMessage("A leitura não pôde ser realizada.");
        }
    }

    public void carregamentoPorTexto(){ //Entrada do bd via txt

        Database con = new Database();

        try {//Mudar o caminho absoluto seria ótimo
            FileReader arq = new FileReader("C:\\Users\\joaov\\IdeaProjects\\PDFSirAbrahamLincoln\\src\\database\\input.txt");
            BufferedReader lerArq = new BufferedReader(arq);

            String linha = lerArq.readLine();

            while (linha != null) {
                String[] result = linha.split("/");

                try {
                    con.inserir(result[1].toUpperCase(), result[0].toUpperCase(), result[2].toUpperCase(), result[3].toUpperCase(), result[0].toLowerCase()+".wav");
                    System.out.println(result[0]+" foi adicionado");
                } catch(Exception e){}

                linha = lerArq.readLine();
            }

            arq.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
                    e.getMessage());
        }
    }

}