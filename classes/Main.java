package classes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    private static Stage primary_s, secondary_s;
    private static Scene primary_scene, secondary_scene;
    private static Parent main_root, dictionary_root, newTerm_root, viewer_root, return_root, exit_root, message_root;
    // return_root é util para transição entre roots.

    static Stage getPrimary_s(){
        return primary_s;
    }
    static Stage getSecondary_s(){
        return secondary_s;
    }

    static void setReturn_root(String root_name){
        switch (root_name){
            case("main"):
                return_root = main_root;
                break;
            case("dictionary"):
                return_root = dictionary_root;
                break;
            case("newTerm"):
                return_root = newTerm_root;
                break;
            case("viewer"):
                return_root = viewer_root;
                break;
        }
    }

    // Transições

    static void showLastSeenRoot(){ primary_scene.setRoot(return_root);}

    static void showViewer(){
        primary_scene.setRoot(viewer_root);
    }

    static void showDictionary(){ primary_scene.setRoot(dictionary_root); }

    static void showNewTermDialogue(){ primary_scene.setRoot(newTerm_root);}

    static void showMainMenu(){
        primary_scene.setRoot(main_root);
    }

    static void showMessage(String message){
        MessagePanelController.setMessage(message);
        secondary_scene.setRoot(message_root);
        secondary_s.setTitle("Mensagem");
        secondary_s.show();
    }

    @Override
    public void start(Stage primaryStage) {

        try{

            // Janela Secundária.

            exit_root = FXMLLoader.load(getClass().getResource("..\\fxml\\exitPanel.fxml"));
            message_root = FXMLLoader.load(getClass().getResource("..\\fxml\\messagePanel.fxml"));

            System.out.println("Arquivos FXML de janela secundária carregados.");

            secondary_scene = new Scene(exit_root, 300, 135);

            secondary_s = new Stage();
            secondary_s.setMaxHeight(150);
            secondary_s.setMaxWidth(320);
            secondary_s.setScene(secondary_scene);

            // Janela Principal

            primary_s = primaryStage;
            primary_s.setTitle("PDF Reader - Beta Version");
            primary_s.setMinWidth(1200);
            primary_s.setMinHeight(700);

            main_root = FXMLLoader.load(getClass().getResource("..\\fxml\\main.fxml"));
            dictionary_root = FXMLLoader.load(getClass().getResource("..\\fxml\\dictionary.fxml"));
            newTerm_root = FXMLLoader.load(getClass().getResource("..\\fxml\\newTerm.fxml"));
            viewer_root = FXMLLoader.load(getClass().getResource("..\\fxml\\viewer.fxml"));

            System.out.println("Arquivos FXML de janela principal carregados.");

            primary_scene = new Scene(main_root, 1200, 700);
            primary_s.setScene(primary_scene);

            primary_s.setOnCloseRequest(e -> {
                System.out.println("Saída requisitada.");
                e.consume();
                secondary_s.setTitle("Saída");
                if(secondary_scene.getRoot() != exit_root)
                    secondary_scene.setRoot(exit_root);
                secondary_s.show();
            });

            // Estilos

            File file;

            file = new File("src\\styles\\mainMenuStyle.css");

            main_root.getStylesheets().clear();
            main_root.getStylesheets().add("file:///" + file.getAbsolutePath().replace("\\", "/"));

            file = new File("src\\styles\\dictionaryStyle.css");

            dictionary_root.getStylesheets().clear();
            dictionary_root.getStylesheets().add("file:///" + file.getAbsolutePath().replace("\\", "/"));

            file = new File("src\\styles\\newTermStyle.css");

            newTerm_root.getStylesheets().clear();
            newTerm_root.getStylesheets().add("file:///" + file.getAbsolutePath().replace("\\", "/"));

            file = new File("src\\styles\\viewerStyle.css");

            viewer_root.getStylesheets().clear();
            viewer_root.getStylesheets().add("file:///" + file.getAbsolutePath().replace("\\", "/"));

            file = new File("src\\styles\\dictionary_icon_red.png");
            primary_s.getIcons().add(new Image("file:///" + file.getAbsolutePath().replace("\\", "/")));

            System.out.println("Arquivos CSS de estilo carregados.");

            // Fim

            System.out.println("Inicialização concluída.\n");

            primary_s.setMaximized(true);
            primary_s.show();

        } catch (Exception exc){
            System.out.println("Falha no método start FXApplication.");
            System.out.println("Mensagem de erro: " + exc.getMessage());
            System.out.println("Rastro de pilha:");
            exc.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Inicializando.");
        launch(args);
        System.out.println("Encerrado.");
    }
}
