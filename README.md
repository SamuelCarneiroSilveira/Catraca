# Documentação Gate Controller 



## Classe de Teste da Placa
 

### <mark>**TestaPlaca**</mark>



> Aciona todos os componentes simultaneamente, pede a leitura dos sensores, e emite um sinal visual e sonoro para indicar o funcionamento deles.

&nbsp;
*** 


## Classes de Comportamento



>Deve-se definir um comportamento padrão entre os 3 abaixo, o código deve ser orientado pra sempre voltar para um deles.

<!-- &nbsp; -->

### <mark> **Standart** </mark> 
> Entrada sem ticket bloqueada para os dois lados.
### <mark> **DireitaLivre** </mark> 
> Entrada sem ticket bloqueada para o lado esquerdo.
### <mark> **EsquerdaLivre** </mark> 
> Entrada sem ticket bloqueada para o lado direito.

&nbsp;
***

## Classes Caso Ticket Aprovado
>As duas funções aguardam 6 segundos pela leitura do sensor (Tentativa de passagem).

> Caso a pessoa tente passar na direção errada, a catraca trava, e a função retorna false

> Caso passe na direção certa, a catraca retorna true e aguarda 4s para passagem.
  
- Aqui será adicionado um contador de giro, estou em contato com o Ariel pra entender a resposta que os sensores passam de acordo com o giro, enquanto isso ela libera a passagem por 4s) 

&nbsp;

### <mark> **GiroEsquerda** </mark> 
> Libera a passagem para a esquerda, e bloqueia na outra 

### <mark> **GiroDireita** </mark> 
> Libera a passagem para a direita, e libera na outra direção
libera a passagem para a esquerda, e bloqueia na outra 
Boolean **** - direção

&nbsp;

***

## Classes Caso Ticket Negado
### <mark> **TicketNegado** </mark> 
> informa por 2s que o ticket foi negado e bloqueia a passagem caso algum dos sensores tente ler 

  


&nbsp;



&nbsp;

***


> Importante que após o uso das classes de giro e ticketNegado o program
>a volte para um dos comportamentos padrão 
> 
> ![texto alt](https://media.giphy.com/media/tIeCLkB8geYtW/giphy.gif)
> 
> Obrigado!