package classes;

public class Palavra {
    
    private String portugues, english, significado_pt, significado_en, som;
    public static String idiomaAtual = "pt";
    
    public Palavra(String portugues, String english, String significado_pt, String significado_en, String som) {
        this.portugues = portugues;
        this.english = english;
        this.significado_pt = significado_pt;
        this.significado_en = significado_en;
        this.som = som;
    }
    
    public Palavra(){
    }
    
    public String getPortugues() {
        return portugues;
    }

    public void setPortugues(String portugues) {
        this.portugues = portugues;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }
    
    public String getSom(){
        return this.som;
    }
    
    public void setSom(String som){
        this.som = som;
    }

    public String getSignificado_pt() {
        return significado_pt;
    }

    public void setSignificado_pt(String significado_pt) {
        this.significado_pt = significado_pt;
    }

    public String getSignificado_en() {
        return significado_en;
    }

    public void setSignificado_en(String significado_en) {
        this.significado_en = significado_en;
    }
}
