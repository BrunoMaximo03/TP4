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

    // CLASSE NÓ

    // Nó da primeira árvore onde irá guardar os RELEASE_YEAR % 15 e onde cada nó
    // aponta para uma árvore
    public static class No1 {
        int chave; // irá ser o releaseYear % 15
        No1 esq, dir;
        Arvore2 arvore;

        public No1(int chave) {
            this.chave = chave;
            this.esq = null;
            this.dir = null;
            this.arvore = new Arvore2();
        }
    }

    // CLASSE ÁRVORE - que irá guardar chaves - RELEASE_YEAR % 15

    public static class Arvore1 {
        No1 raiz;

        public Arvore1() {
            int[] arvoreGerada = { 7, 3, 11, 1, 5, 9, 13, 0, 2, 4, 6, 8, 10, 12, 14 };
            for (int chave : arvoreGerada) {
                inserirReleaseYear(chave); // criando a Primeira arvore já com os valores
            }
        }

        public void inserirReleaseYear(int chave) {
            raiz = inserirReleaseYear(raiz, chave);
        }

        private No1 inserirReleaseYear(No1 noAtual, int chave) {
            if (noAtual == null) {
                noAtual = new No1(chave);
            } else if (chave < noAtual.chave) {
                noAtual = inserirReleaseYear(noAtual.esq, chave);
            } else {
                noAtual = inserirReleaseYear(noAtual.dir, chave);
            }
            return noAtual;
        }

        // inserir PUBLIC - Primeira Arvore - RELEASE_YEAR % 15 PRIMEIRA ARVORE
        public void inserirPrimeiraArvore(Show show) {
            int calculaMod15Release_year = (show.getRelease_year() % 15);
            inserirPrimeiraArvore(raiz, calculaMod15Release_year, show);
        }

        // inserir PRIVATE - Primeira Arvore - RELEASE_YEAR % 15 PRIMEIRA ARVORE
        public void inserirPrimeiraArvore(No1 noAtual, int chave, Show show) {
            if (noAtual == null)
                return;
            if (chave == noAtual.chave)
                noAtual.arvore.inserirSegundaArvore(show);
            else if (chave < noAtual.chave)
                inserirPrimeiraArvore(noAtual.esq, chave, show);
            else
                inserirPrimeiraArvore(noAtual.dir, chave, show);
        }

        public boolean pesquisaChaves(String titulo) {
            return pesquisaChaves(raiz, titulo, true);
        }

        private boolean pesquisaChaves(No1 noAtual, String titulo, boolean imprimirRaiz) {
            if (noAtual == null) {
                return false;
            }
            boolean achou = noAtual.arvore.pesquisaSegundaArvore(titulo, imprimirRaiz);
            if (achou) {
                return true;
            }
            System.out.print("ESQ ");
            if (pesquisaChaves(noAtual.esq, titulo, false)) {
                return true;
            }
            System.out.print("DIR ");
            if (pesquisaChaves(noAtual.dir, titulo, false)) {
                return true;
            }
            return false;
        }
    }

    // Nó da segunda árvore onde irá guardar os TITULOS
    public static class No2 {
        String titulo;
        Show show;
        No2 esq, dir;

        public No2(Show show) {
            this.show = show;
            this.titulo = show.getTitle();
            this.esq = null;
            this.dir = null;
        }
    }

    // CLASSE ÁRVORE - que irá guardar Strings - TÍTULO

    public static class Arvore2 {
        No2 raiz;

        public Arvore2() {
            this.raiz = null;
        }

        // inserir PUBLIC - Segunda Arvore - TITULOS SEGUNDA ARVORE
        public void inserirSegundaArvore(Show show) {
            raiz = inserirSegundaArvore(raiz, show);
        }

        // inserir PRIVATE - Segunda Arvore - TITULOS SEGUNDA ARVORE
        private No2 inserirSegundaArvore(No2 noAtual, Show show) {
            if (noAtual == null) {
                noAtual = new No2(show);
            } else if (show.getTitle().compareTo(noAtual.titulo) < 0) {
                noAtual.esq = inserirSegundaArvore(noAtual.esq, show);
            } else {
                noAtual.dir = inserirSegundaArvore(noAtual.dir, show);
            }
            return noAtual;
        }

        // pesquisa PUBLIC SEGUNDA ARVORE
        public boolean pesquisaSegundaArvore(String linha, boolean imprimirRaiz) {
            if (imprimirRaiz) {
                System.out.print("raiz ");
            }
            return pesquisaSegundaArvore(raiz, linha);
        }

        // pesquisa PRIVATE SEGUNDA ARVORE
        private boolean pesquisaSegundaArvore(No2 noAtual, String linha) {
            if (noAtual == null) {
                return false;
            } else if (linha.equals(noAtual.titulo)) {
                return true;
            } else if (linha.compareTo(noAtual.titulo) < 0) {
                System.out.print("esq ");
                return pesquisaSegundaArvore(noAtual.esq, linha);
            } else {
                System.out.print("dir ");
                return pesquisaSegundaArvore(noAtual.dir, linha);
            }
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String entrada;

        Long tempoInicial = System.currentTimeMillis();
        Arvore1 arvore1 = new Arvore1();

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
                        arvore1.inserirPrimeiraArvore(espetaculo);
                        achou = true;
                    } else {
                        line = leitor.readLine(); // ler o restante
                    }
                }
                if (!achou) {
                    System.out.println("Show ID " + entrada + " não encontrado.");
                }
                leitor.close();
            }
        } catch (Exception e) {
            System.out.println("Erro ao achar!" + e.getMessage());
        }

        String titulo = sc.nextLine();
        while (!titulo.equals("FIM")) {

            boolean achou = arvore1.pesquisaChaves(titulo);

            if (achou) {
                System.out.println("SIM");
            } else {
                System.out.println("NAO");
            }
            titulo = sc.nextLine();
        }
        sc.close();
        long tempoFinal = System.currentTimeMillis();
        long tempoTotal = tempoFinal - tempoInicial;

        try {
            java.io.PrintWriter arquivo = new java.io.PrintWriter("matricula_arvoreBinaria.txt", "UTF-8");
            arquivo.printf("850847\t%d\t%d \n", tempoTotal);
            arquivo.close();
        } catch (Exception e) {
            System.out.println("Erro ao criar o arquivo " + e.getMessage());
        }
    }
}
