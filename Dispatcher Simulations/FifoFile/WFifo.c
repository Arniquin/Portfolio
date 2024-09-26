//Arnold Richards Alvarez
//Lectura de FiFo 
#include <stdio.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>

void File(char *filename){
    int fd;
    char ch[2];
    FILE* file;
    file = fopen(filename, "r");
    mkfifo("fifo",0666);
    fd=open("fifo",O_WRONLY);
    while(feof(file)==0){
        ch[0]=fgetc(file);
        if(feof(file)==0)write(fd,ch,sizeof(ch));
    }
    ch[1]='e';
    write(fd,ch,sizeof(ch));
    fclose(file);
    close(fd);
}

int main(){
    File("binario.dat");
}