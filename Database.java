package classes;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public final class Database {
    
    private Connection con; 
    
    public Database(){
        try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            con = DriverManager.getConnection("jdbc:derby:pdf;create=true;user=root;password=admin");
            
            System.out.println("Conexão bem sucedida com o banco de dados");
            criarSchema("ROOT");
            //apagarTabela();
            //criarTabela();
            
        }catch(ClassNotFoundException | SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    private void criarSchema(String nome){
        try{
            Statement stm = con.createStatement();
            stm.executeUpdate("CREATE SCHEMA "+nome);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    private void criarTabela(){
        try{
            Statement stm = con.createStatement();
            stm.executeUpdate("CREATE TABLE PALAVRAS(PORTUGUES VARCHAR(64) NOT NULL, ENGLISH VARCHAR(64) NOT NULL, SIGNIFICADO_pt VARCHAR(512), SIGNIFICADO_en VARCHAR(512), SOM VARCHAR(255), PRIMARY KEY(ENGLISH))");
            System.out.println("Tabela criada");
        }catch(SQLException e){
             System.out.println(e.getMessage());
         }
    }
    
    private void apagarTabela(){//Use com sabedoria
        try{
            Statement stm = con.createStatement();
            stm.executeUpdate("DROP TABLE PALAVRAS");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    public void inserir(String portugues, String english, String significado_pt, String significado_en,String  som) throws SQLException{
            Statement stm = con.createStatement();
            stm.executeUpdate("INSERT INTO PALAVRAS(PORTUGUES, ENGLISH, SIGNIFICADO_pt, SIGNIFICADO_en, SOM) VALUES ('" + portugues + "','" + english + "','" + significado_pt + "','" + significado_en + "','" + som + "')");
    }
    
    public void alterarTupla(String portugues, String english, String significado_pt, String significado_en, String som) throws SQLException{
        Statement stm = con.createStatement();
        stm.executeUpdate("UPDATE PALAVRAS SET PORTUGUES ='"+portugues+"',SIGNIFICADO_pt ='"+significado_pt+"',SIGNIFICADO_en ='"+significado_en+", SOM = '"+som+"'  WHERE ENGLISH = '"+english+"'");
    }
        
    public void apagarTupla(String selecionado) throws SQLException{
        Statement stm = con.createStatement();
        stm.executeUpdate("DELETE FROM PALAVRAS WHERE ENGLISH ='"+selecionado+"'");
    }

    public Palavra buscaPalavraPortugues(String selecionado) throws SQLException{
        Palavra aux = new Palavra();
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try{
            pstm = con.prepareStatement("SELECT * FROM PALAVRAS WHERE PORTUGUES = '"+selecionado+"'");
            rs = pstm.executeQuery();

            if(rs.next()){
                aux.setPortugues(rs.getString(1));
                aux.setEnglish(rs.getString(2));
                aux.setSignificado_pt(rs.getString(3));
                aux.setSignificado_en(rs.getString(4));
                aux.setSom(rs.getString(5));
                System.out.println("Buscado com sucesso!");
                //1º Coluna = portugues, 2º coluna = Inglês, 3º coluna = Descrição PT 4º coluna = Descrição EN 5ºcoluna = som
            }
            else{
                System.out.println("Nada encontrado!");
                return null;
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
            //Criar um jdialog;
        }
        return aux;
    }

    public Palavra buscaPalavra(String selecionado) throws SQLException{
        Palavra aux = new Palavra();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        
        try{
            pstm = con.prepareStatement("SELECT * FROM PALAVRAS WHERE ENGLISH = '"+selecionado+"'");
            rs = pstm.executeQuery();
            
            if(rs.next()){
                aux.setPortugues(rs.getString(1));
                aux.setEnglish(rs.getString(2));
                aux.setSignificado_pt(rs.getString(3));
                aux.setSignificado_en(rs.getString(4));
                aux.setSom(rs.getString(5));
                System.out.println("Buscado com sucesso!");
            //1º Coluna = portugues, 2º coluna = Inglês, 3º coluna = Descrição PT 4º coluna = Descrição EN 5ºcoluna = som
            }
            else{
                System.out.println("Nada encontrado!");
                return null;
            }
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
            //Criar um jdialog;
        }
        return aux;
    }

    public ArrayList lerBanco() throws SQLException{

        ArrayList<Palavra> lista = new ArrayList<>();

        PreparedStatement pstm = null;
        ResultSet rs = null;

        try{
            pstm = con.prepareStatement("SELECT * FROM PALAVRAS");
            rs = pstm.executeQuery();

            while(rs.next()){
                Palavra aux = new Palavra();
                aux.setPortugues(rs.getString(1));
                aux.setEnglish(rs.getString(2));
                aux.setSignificado_pt(rs.getString(3));
                aux.setSignificado_en(rs.getString(4));
                aux.setSom(rs.getString(5));
                lista.add(aux);
                //1º Coluna = portugues, 2º coluna = Inglês, 3º coluna = Descrição PT 4º coluna = Descrição EN 5ºcoluna = som
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return lista;
    }
}