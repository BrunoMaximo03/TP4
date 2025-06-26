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
public class Questao4 {

    // CLASSE NÓ

    public static class NoAn {
        public boolean cor;
        public Show elemento;
        public NoAn esq, dir;


        public NoAn(Show show) {
            this.elemento = show;
            this.esq = null; this.dir = null;
            this.cor = false;
        }

        public NoAn(Show elemento, boolean cor) {
            this.elemento = elemento;
            this.cor = cor;
            this.esq = null;
            this.dir = null;
        }

    }

    // CLASSE ÁRVORE

    public static class Alvinegra {
        NoAn raiz;
        private int comparacoes = 0;

        public Alvinegra() {
            this.raiz = null;
        }

        public int getComparacoes() {
            return this.comparacoes;
        }

        // Pesquisar na REDBLACK
        public boolean pesquisar(String name) {
            System.out.print("=>raiz ");
            return pesquisar(name,raiz);
        }

        private boolean pesquisar(String name, NoAn noAtual) {
            boolean resp;
            if(noAtual == null) {
                comparacoes++;
                resp = false;
            } else if(name.equalsIgnoreCase(noAtual.elemento.getTitle())) {
                comparacoes++;
                resp = true;
            } else if(name.compareTo(noAtual.elemento.getTitle()) < 0) {
                comparacoes++;
                System.out.print("esq ");
                resp = pesquisar(name, noAtual.esq);
            } else {
                comparacoes++;
                System.out.print("dir ");
                resp = pesquisar(name,noAtual.dir);
            }
            return resp;

        }

        // Métodos de rotação

        private NoAn rotacionarDireita(NoAn no) {
            NoAn temp = no.esq;
            no.esq = temp.dir; // tirando a referência do avo para o pai
            temp.dir = no;

            return temp;
        }

        private NoAn rotacionarEsquerda(NoAn no) {
            NoAn temp = no.dir;
            no.dir = temp.esq; // tirando a referência do avo para o pai
            temp.esq = no;

            return temp;
        }

        private NoAn rotacionarDireitaEsquerda(NoAn no) {
            no.dir = rotacionarDireita(no.dir);
            return rotacionarEsquerda(no);
        }

        private NoAn rotacionarEsquerdaDireita(NoAn no) {
            no.esq = rotacionarEsquerda(no.esq);
            return rotacionarDireita(no);
        }


        public void inserir(Show show){
        if(raiz == null){
            raiz = new NoAn(show);
        }
        else if(raiz.esq == null && raiz.dir == null){
            if(show.getTitle().compareTo(raiz.elemento.getTitle()) < 0){
                raiz.esq = new NoAn(show);
            }
            else{
                raiz.dir = new NoAn(show);
            }
        }
        else if(raiz.esq == null){
            if(show.getTitle().compareTo(raiz.elemento.getTitle()) < 0){
                raiz.esq = new NoAn(show);
            }
            else if(show.getTitle().compareTo(raiz.dir.elemento.getTitle()) < 0){
                raiz.esq = new NoAn(raiz.elemento);
                raiz.elemento = show;
            }
            else{
                raiz.esq = new NoAn(raiz.elemento);
                raiz.elemento = raiz.dir.elemento;
                raiz.dir.elemento = show;
            }

            raiz.esq.cor = false; raiz.dir.cor = false;
        }
        else if(raiz.dir == null){
            if(show.getTitle().compareTo(raiz.elemento.getTitle()) > 0){
                raiz.dir = new NoAn(show);
            }
            else if(show.getTitle().compareTo(raiz.esq.elemento.getTitle()) > 0){
                raiz.dir = new NoAn(raiz.elemento);
                raiz.elemento = show;
            }
            else{
                raiz.dir = new NoAn(raiz.elemento);
                raiz.elemento = raiz.esq.elemento;
                raiz.esq.elemento = show;
            }

            raiz.esq.cor = false; raiz.dir.cor = false;
        }
        else{
            inserir(show, null, null, null, raiz);
        }
        raiz.cor = false;
    }

    private void inserir(Show show, NoAn bisavo, NoAn avo, NoAn pai, NoAn i){
        if(i == null){
            if( show.getTitle().compareTo(pai.elemento.getTitle()) < 0){
                i = pai.esq = new NoAn(show, true);
            }
            else{
                i = pai.dir = new NoAn(show, true);
            }
            if(pai.cor == true){
                balancear(bisavo, avo, pai, i);
            }
        }
        else{
            if(i.esq != null && i.dir != null && i.esq.cor == true && i.dir.cor == true){
                i.cor = true; 
                i.esq.cor = false; 
                i.dir.cor = false;

                if(i == raiz){
                    i.cor = false;
                }
                else if(pai.cor == true){
                    balancear(bisavo, avo, pai, i);
                }
            }
            if(show.getTitle().compareTo(i.elemento.getTitle()) < 0){
                inserir(show, avo, pai, i, i.esq);
            }            
            else if(show.getTitle().compareTo(i.elemento.getTitle()) > 0){
                inserir(show, avo, pai, i, i.dir);
            }
        }
    }

    private void balancear(NoAn bisavo, NoAn avo, NoAn pai, NoAn i){
        comparacoes++; 
        if(pai.cor == true){
            comparacoes++;
            if(pai.elemento.getTitle().compareTo(avo.elemento.getTitle()) > 0){
                comparacoes++;
                if(i.elemento.getTitle().compareTo(pai.elemento.getTitle()) > 0){
                    avo = rotacionarEsquerda(avo);
                }
                else{
                    avo = rotacionarDireitaEsquerda(avo);
                }
            }
            else{
                comparacoes++;
                if(i.elemento.getTitle().compareTo(pai.elemento.getTitle()) < 0){
                    avo = rotacionarDireita(avo);
                }
                else{
                    avo = rotacionarEsquerdaDireita(avo);
                }
            }
            if(bisavo == null){
                raiz = avo;
            } 
            else if(avo.elemento.getTitle().compareTo(bisavo.elemento.getTitle()) < 0){
                bisavo.esq = avo;
            } 
            else {
                bisavo.dir = avo;
            }
            avo.cor = false;
            avo.esq.cor = avo.dir.cor = true;
        }
    }



       
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String entrada;
        Alvinegra arvore = new Alvinegra();

        long tempoInicial = System.currentTimeMillis();

        try {
            while (!(entrada = sc.nextLine()).equalsIgnoreCase("FIM")) {
                BufferedReader leitor = new BufferedReader(new FileReader("/tmp/disneyplus.csv"));
                String line = leitor.readLine(); // está pulando o cabeçalho
                boolean achou = false;

                line = leitor.readLine(); // le a primeira linha "válida"

                while (line != null & !achou) {
                    if (line.startsWith(entrada + ",")) {
                        Show espetaculo = new Show();
                        espetaculo.readCSV(line);
                        arvore.inserir(espetaculo);
                        achou = true;
                    } else {
                        line = leitor.readLine(); // ler o restante
                    }
                }
                leitor.close();
            }
        } catch (Exception e) {
            System.out.println("Erro ao achar!" + e.getMessage());
        }

        String titulo = sc.nextLine();
        while (!titulo.equals("FIM")) {

            if (arvore.pesquisar(titulo) == true) {
                System.out.println(" SIM");
            } else {
                System.out.println(" NAO");
            }
            titulo = sc.nextLine();
        }

        long tempoFinal = System.currentTimeMillis();
        long tempoTotal = tempoFinal - tempoInicial;

        try {
            java.io.PrintWriter arquivo = new java.io.PrintWriter("matricula_arvoreBinaria.txt", "UTF-8");
            arquivo.printf("850847\t%d\t%d \n", tempoTotal, arvore.getComparacoes());
            arquivo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sc.close();

    }
}
