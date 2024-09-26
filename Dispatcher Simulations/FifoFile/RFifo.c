//Arnold Richards Alvarez
//Lectura de archivo
#include <stdio.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <unistd.h>
#include <fcntl.h>

int main(){
    int fd;
    char ch[2]="";
    mkfifo("fifo",0666);
    fd=open("fifo",O_RDONLY);
    while(ch[1]!='e'){
        read(fd,ch,sizeof(ch));
        if(ch[1]!='e')printf("%s",ch);
    }
    printf("\n");
    close(fd);
}