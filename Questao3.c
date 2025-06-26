#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <ctype.h>

#define TAM_STR 256
#define TAM_ARR 50
#define TAM_LINHA 1024

typedef struct
{
    char id_show[TAM_STR];
    char tipo[TAM_STR];
    char titulo[TAM_STR];
    char diretores[TAM_ARR][TAM_STR];
    int qtd_diretores;
    char elenco[TAM_ARR][TAM_STR];
    int qtd_elenco;
    char pais[TAM_STR];
    time_t data_adicao;
    int ano_lancamento;
    char classificacao[TAM_STR];
    char duracao[TAM_STR];
    char categorias[TAM_ARR][TAM_STR];
    int qtd_categorias;
} Espetaculo;

typedef struct Nodo
{
    Espetaculo *espetaculo;
    struct Nodo *esquerda;
    struct Nodo *direita;
    int alt;
} Nodo;

typedef struct
{
    Nodo *raiz;
    int total_comp;
} AVL;

void inicializarEspetaculo(Espetaculo *e)
{
    e->id_show[0] = '\0';
    e->tipo[0] = '\0';
    e->titulo[0] = '\0';
    e->qtd_diretores = 0;
    e->qtd_elenco = 0;
    e->pais[0] = '\0';
    e->data_adicao = 0;
    e->ano_lancamento = 0;
    e->classificacao[0] = '\0';
    e->duracao[0] = '\0';
    e->qtd_categorias = 0;
}

Espetaculo *duplicarEspetaculo(Espetaculo *e)
{
    Espetaculo *copia = (Espetaculo *)malloc(sizeof(Espetaculo));
    inicializarEspetaculo(copia);
    strcpy(copia->id_show, e->id_show);
    strcpy(copia->tipo, e->tipo);
    strcpy(copia->titulo, e->titulo);
    copia->qtd_diretores = e->qtd_diretores;
    for (int i = 0; i < e->qtd_diretores; i++)
    {
        strcpy(copia->diretores[i], e->diretores[i]);
    }
    copia->qtd_elenco = e->qtd_elenco;
    for (int i = 0; i < e->qtd_elenco; i++)
    {
        strcpy(copia->elenco[i], e->elenco[i]);
    }
    strcpy(copia->pais, e->pais);
    copia->data_adicao = e->data_adicao;
    copia->ano_lancamento = e->ano_lancamento;
    strcpy(copia->classificacao, e->classificacao);
    strcpy(copia->duracao, e->duracao);
    copia->qtd_categorias = e->qtd_categorias;
    for (int i = 0; i < e->qtd_categorias; i++)
    {
        strcpy(copia->categorias[i], e->categorias[i]);
    }
    return copia;
}

void ordenarStrings(char arr[][TAM_STR], int n)
{
    for (int i = 0; i < n - 1; i++)
    {
        int menor = i;
        for (int j = i + 1; j < n; j++)
        {
            if (strcmp(arr[menor], arr[j]) > 0)
            {
                menor = j;
            }
        }
        if (menor != i)
        {
            char temp[TAM_STR];
            strcpy(temp, arr[menor]);
            strcpy(arr[menor], arr[i]);
            strcpy(arr[i], temp);
        }
    }
}

void separarArray(const char *entrada, char saida[][TAM_STR], int *qtd)
{
    *qtd = 0;
    if (strlen(entrada) == 0)
    {
        strcpy(saida[0], "NaN");
        *qtd = 1;
        return;
    }
    char temp[TAM_LINHA];
    strcpy(temp, entrada);
    char *token = strtok(temp, ",");
    while (token && *qtd < TAM_ARR)
    {
        while (*token == ' ')
            token++;
        char *fim = token + strlen(token) - 1;
        while (fim > token && *fim == ' ')
            fim--;
        *(fim + 1) = '\0';
        strcpy(saida[*qtd], token);
        (*qtd)++;
        token = strtok(NULL, ",");
    }
    ordenarStrings(saida, *qtd);
}

void lerData(const char *data_str, time_t *saida)
{
    if (strlen(data_str) == 0)
    {
        *saida = 0;
        return;
    }
    struct tm tm = {0};
    char mes[20];
    int dia, ano;
    if (sscanf(data_str, "%s %d, %d", mes, &dia, &ano) == 3)
    {
        const char *meses[] = {"January", "February", "March", "April", "May", "June",
                               "July", "August", "September", "October", "November", "December"};
        for (int i = 0; i < 12; i++)
        {
            if (strcmp(mes, meses[i]) == 0)
            {
                tm.tm_mon = i;
                break;
            }
        }
        tm.tm_mday = dia;
        tm.tm_year = ano - 1900;
        tm.tm_isdst = -1;
        *saida = mktime(&tm);
    }
    else
    {
        *saida = 0;
    }
}

void lerLinha(Espetaculo *e, const char *linha)
{
    char partes[11][TAM_LINHA];
    int idx_parte = 0;
    int aspas = 0;
    char atual[TAM_LINHA] = {0};
    int idx_atual = 0;

    for (int i = 0; linha[i] && idx_parte < 11; i++)
    {
        if (linha[i] == '"')
        {
            aspas = !aspas;
        }
        else if (linha[i] == ',' && !aspas)
        {
            atual[idx_atual] = '\0';
            char *trim = atual;
            while (*trim == ' ')
                trim++;
            char *fim = trim + strlen(trim) - 1;
            while (fim > trim && *fim == ' ')
                fim--;
            *(fim + 1) = '\0';
            strcpy(partes[idx_parte++], trim);
            idx_atual = 0;
        }
        else
        {
            atual[idx_atual++] = linha[i];
        }
    }
    atual[idx_atual] = '\0';
    char *trim = atual;
    while (*trim == ' ')
        trim++;
    char *fim = trim + strlen(trim) - 1;
    while (fim > trim && *fim == ' ')
        fim--;
    *(fim + 1) = '\0';
    strcpy(partes[idx_parte++], trim);

    strcpy(e->id_show, partes[0]);
    strcpy(e->tipo, partes[1][0] ? partes[1] : "NaN");
    strcpy(e->titulo, partes[2][0] ? partes[2] : "NaN");
    separarArray(partes[3], e->diretores, &e->qtd_diretores);
    separarArray(partes[4], e->elenco, &e->qtd_elenco);
    strcpy(e->pais, partes[5][0] ? partes[5] : "NaN");
    lerData(partes[6], &e->data_adicao);
    e->ano_lancamento = atoi(partes[7]);
    strcpy(e->classificacao, partes[8][0] ? partes[8] : "NaN");
    strcpy(e->duracao, partes[9][0] ? partes[9] : "NaN");
    separarArray(partes[10], e->categorias, &e->qtd_categorias);
}

void exibirEspetaculo(Espetaculo *e)
{
    printf("=> %s ## %s ## %s ## ", e->id_show, e->titulo, e->tipo);
    for (int i = 0; i < e->qtd_diretores; i++)
    {
        printf("%s", e->diretores[i]);
        if (i < e->qtd_diretores - 1)
            printf(", ");
    }
    printf(" ## [");
    for (int i = 0; i < e->qtd_elenco; i++)
    {
        printf("%s", e->elenco[i]);
        if (i < e->qtd_elenco - 1)
            printf(", ");
    }
    printf("] ## %s ## ", e->pais);
    if (e->data_adicao != 0)
    {
        char data_str[50];
        strftime(data_str, sizeof(data_str), "%B %d, %Y", localtime(&e->data_adicao));
        printf("%s ## ", data_str);
    }
    else
    {
        printf("NaN ## ");
    }
    printf("%d ## %s ## %s ## [", e->ano_lancamento, e->classificacao, e->duracao);
    for (int i = 0; i < e->qtd_categorias; i++)
    {
        printf("%s", e->categorias[i]);
        if (i < e->qtd_categorias - 1)
            printf(", ");
    }
    printf("] ##\n");
}

AVL *criarAVL()
{
    AVL *arvore = (AVL *)malloc(sizeof(AVL));
    arvore->raiz = NULL;
    arvore->total_comp = 0;
    return arvore;
}

int obterAltura(Nodo *no)
{
    return no ? no->alt : 0;
}

int obterFatorBal(Nodo *no)
{
    return no ? obterAltura(no->esquerda) - obterAltura(no->direita) : 0;
}

Nodo *rotacaoDir(Nodo *y)
{
    Nodo *x = y->esquerda;
    Nodo *T2 = x->direita;
    x->direita = y;
    y->esquerda = T2;
    y->alt = (obterAltura(y->esquerda) > obterAltura(y->direita) ? obterAltura(y->esquerda) : obterAltura(y->direita)) + 1;
    x->alt = (obterAltura(x->esquerda) > obterAltura(x->direita) ? obterAltura(x->esquerda) : obterAltura(x->direita)) + 1;
    return x;
}

Nodo *rotacaoEsq(Nodo *x)
{
    Nodo *y = x->direita;
    Nodo *T2 = y->esquerda;
    y->esquerda = x;
    x->direita = T2;
    x->alt = (obterAltura(x->esquerda) > obterAltura(x->direita) ? obterAltura(x->esquerda) : obterAltura(x->direita)) + 1;
    y->alt = (obterAltura(y->esquerda) > obterAltura(y->direita) ? obterAltura(y->esquerda) : obterAltura(y->direita)) + 1;
    return y;
}

Nodo *inserirNodo(Nodo *no, Espetaculo *e, AVL *arvore)
{
    if (!no)
    {
        Nodo *novo = (Nodo *)malloc(sizeof(Nodo));
        novo->espetaculo = duplicarEspetaculo(e);
        novo->esquerda = novo->direita = NULL;
        novo->alt = 1;
        return novo;
    }

    int cmp = strcmp(e->titulo, no->espetaculo->titulo);
    if (cmp < 0)
    {
        no->esquerda = inserirNodo(no->esquerda, e, arvore);
    }
    else if (cmp > 0)
    {
        no->direita = inserirNodo(no->direita, e, arvore);
    }
    else
    {
        return no;
    }

    no->alt = (obterAltura(no->esquerda) > obterAltura(no->direita) ? obterAltura(no->esquerda) : obterAltura(no->direita)) + 1;
    int balance = obterFatorBal(no);

    if (balance > 1 && strcmp(e->titulo, no->esquerda->espetaculo->titulo) < 0)
    {
        return rotacaoDir(no);
    }
    if (balance < -1 && strcmp(e->titulo, no->direita->espetaculo->titulo) > 0)
    {
        return rotacaoEsq(no);
    }
    if (balance > 1 && strcmp(e->titulo, no->esquerda->espetaculo->titulo) > 0)
    {
        no->esquerda = rotacaoEsq(no->esquerda);
        return rotacaoDir(no);
    }
    if (balance < -1 && strcmp(e->titulo, no->direita->espetaculo->titulo) < 0)
    {
        no->direita = rotacaoDir(no->direita);
        return rotacaoEsq(no);
    }

    return no;
}

void inserirAVL(AVL *arvore, Espetaculo *e)
{
    arvore->raiz = inserirNodo(arvore->raiz, e, arvore);
}

int buscarNodo(Nodo *no, const char *titulo, AVL *arvore)
{
    if (!no)
    {
        arvore->total_comp++;
        return 0;
    }
    int cmp = strcmp(titulo, no->espetaculo->titulo);
    if (cmp == 0)
    {
        arvore->total_comp++;
        return 1;
    }
    printf(cmp < 0 ? " esq" : " dir");
    arvore->total_comp++;
    return buscarNodo(cmp < 0 ? no->esquerda : no->direita, titulo, arvore);
}

int buscarAVL(AVL *arvore, const char *titulo)
{
    printf("raiz");
    return buscarNodo(arvore->raiz, titulo, arvore);
}

void liberarNodo(Nodo *no)
{
    if (no)
    {
        liberarNodo(no->esquerda);
        liberarNodo(no->direita);
        free(no->espetaculo);
        free(no);
    }
}

void liberarAVL(AVL *arvore)
{
    liberarNodo(arvore->raiz);
    free(arvore);
}

int main()
{
    AVL *arvore = criarAVL();
    clock_t inicio = clock();
    char entrada[TAM_STR];
    FILE *arquivo;

    while (fgets(entrada, TAM_STR, stdin) && strcmp(entrada, "FIM\n") != 0)
    {
        entrada[strcspn(entrada, "\n")] = '\0';
        arquivo = fopen("disneyplus.csv", "r");
        if (!arquivo)
        {
            printf("Erro ao acessar o arquivo\n");
            continue;
        }
        char linha[TAM_LINHA];
        fgets(linha, TAM_LINHA, arquivo);
        int achado = 0;

        while (fgets(linha, TAM_LINHA, arquivo) && !achado)
        {
            char prefixo_id[TAM_STR];
            snprintf(prefixo_id, sizeof(prefixo_id), "%s,", entrada);
            if (strncmp(linha, prefixo_id, strlen(prefixo_id)) == 0)
            {
                Espetaculo temp;
                inicializarEspetaculo(&temp);
                lerLinha(&temp, linha);
                inserirAVL(arvore, &temp);
                achado = 1;
            }
        }
        if (!achado)
        {
            printf("Show ID %s não encontrado.\n", entrada);
        }
        fclose(arquivo);
    }

    while (fgets(entrada, TAM_STR, stdin) && strcmp(entrada, "FIM\n") != 0)
    {
        entrada[strcspn(entrada, "\n")] = '\0';
        if (buscarAVL(arvore, entrada))
        {
            printf(" SIM\n");
        }
        else
        {
            printf(" NAO\n");
        }
    }

    clock_t fim = clock();
    double tempo_exec = (double)(fim - inicio) / CLOCKS_PER_SEC * 1000;

    FILE *log = fopen("matricula_avl.txt", "w");
    if (log)
    {
        fprintf(log, "850847\t%.0f\t%d\n", tempo_exec, arvore->total_comp);
        fclose(log);
    }
    else
    {
        printf("Erro ao criar arquivo de log\n");
    }

    liberarAVL(arvore);
    return 0;
}