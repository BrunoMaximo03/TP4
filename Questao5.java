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
public class Questao5 {

    // m1 = tamanho da tabela  21
    // m2 = tamanho da reserva  9
    // tamanho total = 30

    public static class Hash {
        Show tabela[];
        int m1,m2,tamanhoTotal,reserva;
        private int comparacoes = 0;

        public Hash(int m1, int m2) {
            this.m1 = m1;
            this.m2 = m2;
            this.tamanhoTotal = m1 + m2;
            this.tabela = new Show[tamanhoTotal];
            for(int i = 0; i < tamanhoTotal; i++) {
                tabela[i] = null;
            }
            reserva = 0; // contador da Área de reserva
        }

        public int hashT1(String nome) {
        int posicao = 0;
        for(int i = 0; i < nome.length(); i++) {
            posicao += nome.charAt(i);
        }
        return posicao % m1;
    }
        
        public int getComparacoes() {
            return this.comparacoes;
        }

        public boolean inserir(Show show) {
            boolean resp = false;
            if(show != null) {
                comparacoes++;
                int posicao = hashT1(show.getTitle());
                if(tabela[posicao] == null) {
                    tabela[posicao] = show;
                    resp = true;
                } else if(reserva < m2) {
                    comparacoes++;
                    tabela[m1 + reserva] = show;
                    reserva++;
                    resp = true;
                }
            }
            return resp;
        }

        public boolean pesquisar(String nome){
        boolean resp = false;
        int posicao = hashT1(nome);
        if(tabela[posicao] != null & tabela[posicao].getTitle().equalsIgnoreCase(nome)){
            comparacoes++;
            resp = true;
        }
        else {
            comparacoes++;
            for(int i = 0; i < reserva; i++){
                comparacoes++;
                if(tabela[m1 + i] != null && tabela[m1 + i].getTitle().equalsIgnoreCase(nome)){
                    posicao = m1 + i;
                    comparacoes++;
                    resp = true;
                    i = reserva;
                }
            }
        }
        System.out.print(" (Posicao: " + posicao + ")");
        return resp;
    }
 }

    

    

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String entrada;
        Hash hash = new Hash(21,9);

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
                        hash.inserir(espetaculo);
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

            if (hash.pesquisar(titulo) == true) {
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
            arquivo.printf("850847\t%d\t%d \n", tempoTotal, hash.getComparacoes());
            arquivo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sc.close();

    }
}
