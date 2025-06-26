import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

class Show {
    // atributos
    private String show_id;
    private String type;
    private String title;
    private String director;
    private String[] cast;
    private String country;
    private Date date_added;
    private int release_year;
    private String rating;
    private String duration;
    private String[] listed_in;

    public Show() { // contrutor padrão
        this.show_id = this.type = this.title = this.director = this.country = this.rating = this.duration = "NaN";
        this.cast = new String[0];
        this.listed_in = new String[0];
        this.date_added = null;
        this.release_year = -1;
    }

    // nos sets e nos gets e sets ja posso tratar o ID arrancando o "s" e
    // tranformando o id em int.

    public String getShow_id() {
        return show_id;
    }

    public void setShow_id(String show_id) {
        this.show_id = show_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String[] getCast() {
        return cast;
    }

    public void setCast(String[] cast) {
        this.cast = cast;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getDate_added() {
        return date_added;
    }

    public void setDate_added(Date date_added) {
        this.date_added = date_added;
    }

    public int getRelease_year() {
        return release_year;
    }

    public void setRelease_year(int release_year) {
        this.release_year = release_year;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String[] getListed_in() {
        return listed_in;
    }

    public void setListed_in(String[] listed_in) {
        this.listed_in = listed_in;
    }

    public void readCSV(String linhaCSV) {
        try {
            List<String> camposSeparados = new ArrayList<>();
            StringBuilder bufferCampo = new StringBuilder();
            boolean dentroAspas = false;

            for (int i = 0; i < linhaCSV.length(); i++) {
                char caractere = linhaCSV.charAt(i);

                if (caractere == '"') {
                    dentroAspas = !dentroAspas;
                } else if (caractere == ',' && !dentroAspas) {
                    camposSeparados.add(bufferCampo.toString().trim());
                    bufferCampo.setLength(0);
                } else {
                    bufferCampo.append(caractere);
                }
            }

            camposSeparados.add(bufferCampo.toString().trim());

            String[] campos = camposSeparados.toArray(new String[0]);

            this.show_id = campos[0];
            this.type = campos[1];
            this.title = campos[2].replace("\"", "");
            this.director = campos[3].isEmpty() ? "NaN" : campos[3];
            this.cast = campos[4].isEmpty() ? new String[] { "NaN" } : campos[4].split(",\\s*");
            Arrays.sort(this.cast);
            this.country = campos[5].isEmpty() ? "NaN" : campos[5];

            try {
                SimpleDateFormat formatoData = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
                this.date_added = campos[6].isEmpty() ? null : formatoData.parse(campos[6]);
            } catch (Exception e) {
                this.date_added = null;
            }

            this.release_year = campos[7].isEmpty() ? -1 : Integer.parseInt(campos[7]);
            this.rating = campos[8].isEmpty() ? "NaN" : campos[8];
            this.duration = campos[9].isEmpty() ? "NaN" : campos[9];
            this.listed_in = campos[10].isEmpty() ? new String[] { "NaN" } : campos[10].split(",\\s*");
            Arrays.sort(this.listed_in);

        } catch (Exception e) {
            System.out.println("Erro ao ler linha: " + e.getMessage());
        }
    }

    // Método de impressoão

    public void imprimir() {
        System.out.print("=> " + show_id + " ## " + title + " ## " + type + " ## " + director + " ## [");
        System.out.print(String.join(", ", cast));
        System.out.print("] ## " + country + " ## ");

        if (date_added != null) {
            SimpleDateFormat Saida = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
            System.out.print(Saida.format(date_added));
        } else {
            System.out.print("NaN");
        }

        System.out.print(" ## " + release_year + " ## " + rating + " ## " + duration + " ## [");
        System.out.print(String.join(", ", listed_in));
        System.out.println("] ##");
    }

    // Método de clone

    public Show clone() {
        Show copia = new Show();
        copia.show_id = this.show_id;
        copia.type = this.type;
        copia.title = this.title;
        copia.director = this.director;
        copia.cast = this.cast.clone();
        copia.country = this.country;
        copia.date_added = this.date_added;
        copia.release_year = this.release_year;
        copia.rating = this.rating;
        copia.duration = this.duration;
        copia.listed_in = this.listed_in.clone();
        return copia;
    }
}

// classe main
public class Questao2 {

    public static class NoArvoreSecundaria {
    String chave;
    Show show;
    NoArvoreSecundaria esq, dir;
    public NoArvoreSecundaria(Show show) {
        this.chave = show.getTitle();
        this.show = show;
        this.esq = this.dir = null;
    }
}

public static class ArvoreSecundaria {
    NoArvoreSecundaria raiz;
    public ArvoreSecundaria() {
        raiz = null;
    }
    public void inserir(Show show) {
        raiz = inserir(raiz, show);
    }
    private NoArvoreSecundaria inserir(NoArvoreSecundaria no, Show show) {
        if (no == null) {
            no = new NoArvoreSecundaria(show);
        } else if (show.getTitle().compareTo(no.chave) < 0) {
            no.esq = inserir(no.esq, show);
        } else if (show.getTitle().compareTo(no.chave) > 0) {
            no.dir = inserir(no.dir, show);
        }
        return no;
    }
    public boolean pesquisa(String titulo, boolean printRaiz) {
        if (printRaiz) System.out.print("raiz ");
        return pesquisa(raiz, titulo);
    }
    private boolean pesquisa(NoArvoreSecundaria no, String titulo) {
        if (no == null) return false;
        if (titulo.equals(no.chave)) return true;
        else if (titulo.compareTo(no.chave) < 0) {
            System.out.print("esq ");
            return pesquisa(no.esq, titulo);
        } else {
            System.out.print("dir ");
            return pesquisa(no.dir, titulo);
        }
    }
}

public static class NoArvorePrincipal {
    int chave;
    NoArvorePrincipal esq, dir;
    ArvoreSecundaria arvoreSecundaria;
    public NoArvorePrincipal(int chave) {
        this.chave = chave;
        this.esq = this.dir = null;
        this.arvoreSecundaria = new ArvoreSecundaria();
    }
}

public static class ArvorePrincipal {
    NoArvorePrincipal raiz;
    public ArvorePrincipal() {
        int[] chaves = {7,3,11,1,5,9,13,0,2,4,6,8,10,12,14};
        for (int chave : chaves) inserirAno(chave);
    }
    
    private void inserirAno(int chave) {
        raiz = inserirAno(chave, raiz);
    }

    private NoArvorePrincipal inserirAno(int chave, NoArvorePrincipal no) {
        if (no == null) return new NoArvorePrincipal(chave);
        if (chave < no.chave) no.esq = inserirAno(chave, no.esq);
        else if (chave > no.chave) no.dir = inserirAno(chave, no.dir);
        return no;
    }

    public void inserir(Show s) {
        int mod = s.getRelease_year() % 15;
        inserir(s, mod, raiz);
    }
    private void inserir(Show s, int mod, NoArvorePrincipal no) {
        if (no == null) return;
        if (mod == no.chave) no.arvoreSecundaria.inserir(s);
        else if (mod < no.chave) inserir(s, mod, no.esq);
        else inserir(s, mod, no.dir);
    }
    public boolean pesquisa(String titulo) {
        return pesquisa(raiz, titulo, true);
    }
    private boolean pesquisa(NoArvorePrincipal no, String titulo, boolean printRaiz) {
        if (no == null) return false;
        boolean achou = no.arvoreSecundaria.pesquisa(titulo, printRaiz);
        if (achou) return true;
        System.out.print("ESQ ");
        if (pesquisa(no.esq, titulo, false)) return true;
        System.out.print("DIR ");
        if (pesquisa(no.dir, titulo, false)) return true;
        return false;
    }
}

    public static void main(String[] args) {
        long inicio = System.currentTimeMillis();
        Scanner sc = new Scanner(System.in);
        String entrada;
        ArvorePrincipal arvore = new ArvorePrincipal();
        try {
            while (!(entrada = sc.nextLine()).equalsIgnoreCase("FIM")) {
                BufferedReader leitor = new BufferedReader(new FileReader("/tmp/disneyplus.csv"));
                String linha = leitor.readLine();
                boolean achou = false;
                linha = leitor.readLine();
                while (linha != null && !achou) {
                    if (linha.startsWith(entrada + ",")) {
                        Show s = new Show();
                        s.readCSV(linha);
                        arvore.inserir(s);
                        achou = true;
                    } else {
                        linha = leitor.readLine();
                    }
                }
                leitor.close();
            }
        } catch (Exception e) {
            System.out.println("Erro ao achar!" + e.getMessage());
        }
        String titulo = sc.nextLine();
        while (!titulo.equals("FIM")) {
            boolean achou = arvore.pesquisa(titulo);
            if (achou) System.out.println("SIM");
            else System.out.println("NAO");
            titulo = sc.nextLine();
        }
        sc.close();
        long fim = System.currentTimeMillis();
        long tempo = fim - inicio;

        try {
            PrintWriter log = new PrintWriter("Arvore_de_arvores" + "_arvoreArvore.txt");
            log.println("850847" + "\t" + tempo + "\t0");
            log.close();
        } catch (IOException e) {
            System.out.println("Erro ao gravar log: " + e.getMessage());
        }
    }
 }

