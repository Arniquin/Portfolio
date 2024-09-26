#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <time.h>

#define PUERTO 8080

int aleatorio(int,int);



void generaProceso(char proc[]){
	char tiempo[3];
	sprintf(proc,"P%d",aleatorio(300,500));
	sprintf(tiempo,"%d",aleatorio(3,9));
	strcat(proc,";");
	strcat(proc,tiempo);
}

void conection(){
	int n=5;
	char buf[7*n];
	char proc[7];
	int fd,fd2,longitud_cliente;
	struct sockaddr_in server;
 	struct sockaddr_in client;

	server.sin_family= AF_INET; //Familia TCP/IP
 	server.sin_port = htons(PUERTO); //Puerto
 	server.sin_addr.s_addr = INADDR_ANY; //Cualquier cliente puede conectarse
 	bzero(&(server.sin_zero),8); 

	if ((fd=socket(AF_INET,SOCK_STREAM,0) )<0){
 		perror("Error de apertura de socket");
 		exit(-1);
 	}

	if(bind(fd,(struct sockaddr*)&server, sizeof(struct sockaddr))==-1) {
 		printf("error en bind() \n");
 		exit(-1);
 	}

	if(listen(fd,5) == -1) {
 		printf("error en listen()\n");
 		exit(-1);
 	}

	while(1) {
 		longitud_cliente= sizeof(struct sockaddr_in);
 		if ((fd2 = accept(fd,(struct sockaddr *)&client,&longitud_cliente))==-1) {
 			printf("error en accept()\n");
 			exit(-1);
 		}
		//n=aleatorio(1,5);
		for(int i=0;i<n;i++){
			generaProceso(proc);

				if(i==0){
					strcpy(buf,proc);
					strcat(buf,"|");
				}
				else {
					strcat(buf,proc);
					if(i!=n-1)	strcat(buf,"|");
				}
		}
		printf("%s\n",buf);
		write(fd2,buf,sizeof(buf));
		close(fd2); /* cierra fd2 */
		sleep(aleatorio(5,10));
 	}
	close(fd);
}


int main(void) {
	char proc[6];	
	srand(getpid());
	conection();
}
//Regresa un numero aleatorio en base al rango recibido
int aleatorio(int n, int m) {
	return (rand()%(m-n+1))+n;
}