/*ONDE EXISTIR "//" SIGNIFICA QUE É UM COMENTÁRIO REFERENTE A LINHA*/

//INCLUSÃO DAS BIBLIOTECAS NECESSÁRIAS PARA A EXECUÇÃO DO CÓDIGO

#include <SPI.h>
#include <Client.h>
#include <Ethernet.h>
#include <Server.h>
#include <Udp.h>
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED }; // NÃO PRECISA MEXER
byte ip[] = { 192, 168, 0, 177 }; // COLOQUE UMA FAIXA DE IP DISPONÍVEL DO SEU ROTEADOR. EX: 192.168.1.110  **** ISSO VARIA, NO MEU CASO É: 192.168.0.177
byte gateway[] = { 192, 168, 0, 1 };// MUDE PARA O GATEWAY PADRÃO DO SEU ROTEADOR **** NO MEU CASO É O 192.168.0.1
byte subnet[] = { 255, 255, 255, 0 }; //NÃO PRECISA MEXER
EthernetServer server(80); //CASO OCORRA PROBLEMAS COM A PORTA 80, UTILIZE OUTRA (EX:8082,8089)
byte sampledata=50;            

#define Rele1 7 // DEFINE O PINO UTILIZADO PELO RELÉ
int st = 1; // VARIÁVEL PARA GUARDAR O STATUS

String readString = String(30); //CRIA UMA STRING CHAMADA "readString"

String LAMP1; // DECLARAÇÃO DE VARIÁVEL DO TIPO STRING

void setup(){

  pinMode(Rele1,OUTPUT); // DEFINE O PINO COMO SAÍDA
  digitalWrite(Rele1, HIGH); // INICIA O PINO COM SINAL ALTO

  Ethernet.begin(mac, ip, gateway, subnet); // INICIALIZA A CONEXÃO ETHERNET
}

boolean isOnline(){
  // FAZ ATÉ 100 LEITURAS PROCURANDO VALOR ACIMA DE 250
for (int i = 0; i < 100; i++){ // PARA "i" IGUAL 0; ENQUANTO "i" FOR MENOR QUE 100;  "i" É INCREMENTADO
// A PARTIR DE 200 CONSIDERA-SE LIGADO
if (analogRead(A2) > 250){ // O VALOR "250" PODE SER AUMENTADO OU DIMINUIDO DE ACORDO COM SUA NECESSIDADE
return true; // RETORNA VERDADEIRO

}
} 
// SE EM 100 LEITURAS NÃO ACHOU VALOR ACIMA DE 250, ENTÃO ESTÁ DESLIGADO
return false;
}

void loop(){
  if(isOnline() == true){ // SE FOR VERDADEIRO FAZ
      LAMP1 = "AC1,"; // STRING RECEBE AC1
  }else{ // SENÃO
        if(isOnline() == false){ // SE FOR FALSO FAZ
                LAMP1 = "AP1,"; // STRING RECEBE AP1
        }
  }
EthernetClient client = server.available(); // CRIA UMA VARIÁVEL CHAMADA client
  if (client) { //SE EXISTE CLIENTE
    while (client.connected()) { // ENQUANTO  EXISTIR CLIENTE CONECTADO
   if (client.available()) { // SE EXISTIR CLIENTE HABILITADO
    char c = client.read(); // CRIA A VARIÁVEL c

    if (readString.length() < 100) // SE O ARRAY FOR MENOR QUE 100
      {
        readString += c; // "readstring" VAI RECEBER OS CARACTERES LIDO
      }
        if (c == '\n') { // SE ENCONTRAR "\n" É O FINAL DO CABEÇALHO DA REQUISIÇÃO HTTP
          if (readString.indexOf("?") <0) //SE ENCONTRAR O CARACTER "?"
          {
          }
          else // SENÃO
        if(readString.indexOf("L=1") >0){ // SE ENCONTRAR O PARÂMETRO "L=1"
        
        if (st == 0){ // SE VARIÁVEL É IGUAL A 0 FAZ
            digitalWrite(Rele1, HIGH); // SETA A O PINO EM ALTO
            st = 1; // VARIÁVEL RECEBE 1
          }else{ // SENÃO
              digitalWrite(Rele1, LOW); // SETA A O PINO EM BAIXO
              st = 0; // VARIÁVEL RECEBE 0
          }
        }             
          client.println("HTTP/1.1 200 OK"); // ESCREVE PARA O CLIENTE A VERSÃO DO HTTP
          client.println("Content-Type: text/html"); // ESCREVE PARA O CLIENTE O TIPO DE CONTEÚDO(texto/html)
          client.println();
          
           client.println(LAMP1); // RETORNA PARA O CLIENTE O STATUS DO LED 1
           client.println(st); // RETORNA VARIÁVEL DE STATUS
           readString="";
          client.stop(); // FINALIZA A REQUISIÇÃO HTTP
            }
          }
        }
      }
 }
