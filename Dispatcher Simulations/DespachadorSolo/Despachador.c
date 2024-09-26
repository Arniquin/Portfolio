//Librerias
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
int ID_GLOBAL;

//enum de Estado
enum state {LISTO,EJECUCION};

//Estructura de nodo
struct nodo {
    char nombre[10];
    int ID;
    int tiempoEjecucion;
    int tiempoEspera;
    int cuantum;
    enum state estado;
    struct nodo *next;
};
//Estructura de declaracion de Lista
struct Lista{
    struct nodo* last;
};
//Metodo que se utliza para inicializar la lista cono vacia
struct Lista inicializarLista(struct Lista L){
    L.last=NULL;
    return L;
}
//Metodo utilizado para insertar un elemento al final de la  lista
struct Lista insertarFinal(char nom[],int ID, int tiempoEj, int tiempoEs,struct Lista L){
    struct nodo* temp;
    int i;
    temp = (struct nodo*)malloc(sizeof(struct nodo));
    strcpy(temp->nombre,nom);
    temp->ID=ID;
    temp->tiempoEjecucion=tiempoEj;
    temp->tiempoEspera=tiempoEs;
    temp->estado=LISTO;
    if (L.last == NULL) {
        temp->next = temp;
        L.last = temp;
    }
    else {
        temp->next = L.last->next;
        L.last->next = temp;
        L.last = temp;
    }
    return L;
}
//Eliminar elemento de la lista
struct Lista eliminarNodo(int ID,struct Lista L){
    if (L.last == NULL)
        printf("\nLista vacia\n");
    else{
        if(ID==L.last->ID&&L.last==L.last->next){
            L.last=NULL;
            return L;
        }
        struct nodo* temp;
        temp = (struct nodo*)malloc(sizeof(struct nodo));
        struct nodo* temp1;
        temp1 = (struct nodo*)malloc(sizeof(struct nodo));
        temp = L.last->next;
        if(ID==L.last->next->ID)L.last->next=L.last->next->next;
        else{
            while (temp->next->ID!=ID)temp=temp->next;  
            if(temp->next==L.last){
                temp->next=L.last->next;
                L.last=temp;
            }else{
                temp1=temp->next;
                temp->next=temp1->next;
            }  
        }       
    }
    return L;
}
//Metodo que muestra el contenido de la lista
void verLista(struct Lista L){
    if (L.last == NULL)
        printf("\nLista Vacia\n");
    else {
        struct nodo* temp;
        temp = (struct nodo*)malloc(sizeof(struct nodo));
        temp = L.last->next;
        do {
            printf("Nombre: %s | ID: %d | Tiempo de Ejecucion:%d | Tiempo de Espera: %d\n", temp->nombre,temp->ID,temp->tiempoEjecucion,temp->tiempoEspera);
            temp = temp->next;
        } while (temp != L.last->next);
    }
}
//Metodo que lee numeros enteros de un archivo y los guarda en la lista
struct Lista archivo(struct Lista L){
    char *delim = ";";
    char *token;
    char *nombre;
    char *t;
    int i=0;
    const char* filename = "Procesos.txt";
    FILE* input_file = fopen(filename, "r");
    if (!input_file)
        exit(EXIT_FAILURE);
    char *contents = NULL;
    size_t len = 0;
    while (getline(&contents, &len, input_file) != -1){
        token = strtok(contents,delim);
        nombre=token;
        token = strtok(NULL,delim);
        t=token;
        L=insertarFinal(nombre,ID_GLOBAL,atoi(t),0,L);
        ID_GLOBAL++;
    }
    fclose(input_file);
    free(contents);
    return L;
}

//Regresa un numero aleatorio en base al rango recibido
int aleatorio(int n, int m) {
	return (rand()%(m-n+1))+n;
}

//Generador de Cuantum
int cuantum(struct nodo* temp){
    int cuantum;
    if(temp->tiempoEjecucion==0)return 0;
    do{
        cuantum=aleatorio(1,3);
    }while(temp->tiempoEjecucion<cuantum);
    return cuantum;
}
//Regresa el tiempo de espera
int tiempoEspera(struct nodo* temp,struct Lista L){
    int Te=0,tiempo=0;
    struct nodo* temp1;
    temp1 = (struct nodo*)malloc(sizeof(struct nodo));
    temp1 = L.last->next;
    do {
            if(temp1->tiempoEjecucion<(temp->tiempoEjecucion-temp->cuantum)&&temp!=temp1){
                Te=Te+temp1->tiempoEjecucion;
            }
            temp1 = temp1->next;
        } while (temp1 != L.last->next);
    if(Te==0)return temp->tiempoEjecucion;
    else return Te-(temp->tiempoEjecucion);
}

//Regresa el nodo con menor tiempo
struct nodo* menor(struct Lista L){
    struct nodo* temp;
    struct nodo* aux;
    temp = (struct nodo*)malloc(sizeof(struct nodo));
    aux = (struct nodo*)malloc(sizeof(struct nodo));
    aux=L.last->next;
    temp=aux;
    do{
        if(aux->tiempoEjecucion<temp->tiempoEjecucion)temp=aux;
        aux=aux->next;
    }while(aux!=L.last->next);
    return temp;
}
//Simulacion del despachador
void despachador(struct Lista L){
    struct nodo* temp;
    temp = (struct nodo*)malloc(sizeof(struct nodo));
    L=archivo(L);
    do{
        temp=menor(L);
        temp->estado=EJECUCION;
        temp->cuantum=cuantum(temp);
        temp->tiempoEspera=tiempoEspera(temp,L);
        printf("Nombre: %s | ID: %d | Tiempo de Ejecucion:%d | Tiempo de Espera:%d | Cuantum:%d\n\n", temp->nombre,temp->ID,temp->tiempoEjecucion,temp->tiempoEspera,temp->cuantum);
        /*printf("\nEstado de la lista:\n");
        verLista(L);
        printf("\n");*/
        sleep(temp->cuantum);
        temp->tiempoEjecucion=temp->tiempoEjecucion-temp->cuantum;
        temp->estado=LISTO;
        if(temp->tiempoEjecucion==0){
            L=eliminarNodo(temp->ID,L);
            printf("Proceso Eliminado\n\n");
        }
        L=archivo(L);
        //tiempo de espera
    }while(L.last!=NULL);
}

int main(){
    struct Lista L;
    L=inicializarLista(L);
    despachador(L);
}