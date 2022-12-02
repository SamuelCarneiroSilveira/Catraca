# Documentação Gate Controller 

## Teste de comandos 
 

### <mark>**TestMenu.jar**</mark>


> Abre um menu com todas as funções da catraca 

&nbsp;
*** 

## Classe de Teste da Placa
 

### <mark>**TestaPlaca**</mark>


> Aciona todos os componentes simultaneamente, pede a leitura dos sensores, e emite um sinal visual e sonoro para indicar o funcionamento deles.

&nbsp;
*** 


## Classes de Comportamento



>Deve-se definir um comportamento padrão, o código deve ser orientado pra sempre voltar para ele.


<!-- &nbsp; -->




## <mark> **Standard** </mark> 
> Classe responsável por identificar o comportamento padrão, e bloquear ou liberar entradas sem ticket

<!-- &nbsp; -->

### <mark> **ENUM Comportamento** </mark> 
> Enumeração para traduzir o comportamento padrão para a classe standard.
#### <mark> **NENHUM_LIVRE** </mark> 
> Entrada sem ticket bloqueada para os dois lados.
#### <mark> **DIREITA_LIVRE** </mark> 
> Entrada sem ticket bloqueada para o lado esquerdo.
#### <mark> **ESQUERDA_LIVRE** </mark> 
> Entrada sem ticket bloqueada para o lado direito.

### <mark> **setComportamento** </mark> 
> Esta classe define o comportamento, deve receber um enum entre os 3 enums sitados acima, através da função ordinal.



&nbsp;
***

## Classes Caso Ticket Aprovado
>A função aguarda 6 segundos pela leitura do sensor (Tentativa de passagem).

> Caso a pessoa tente passar na direção errada, a catraca trava, e a função retorna false

> Caso passe na direção certa, a catraca entra em um loop para verificar se o giro foi concluido, a catraca retorna true caso concluido, e false caso o giro não seja completo.
  


## <mark> **Giro** </mark> 
> Classe responsável por identificar o padrão de giro, bloquear entradas para o lado oposto, e fazer a contagem do giro.

<!-- &nbsp; -->


### <mark> **ENUM Giro** </mark> 
> Enumeração para traduzir o comportamento padrão de giro para a classe Giro.

#### <mark> **AMBOS** </mark> 
> Libera a passagem para os dois lados e faz a contagem do giro. 

#### <mark> **DIREITA** </mark> 
> Libera a passagem para a direita,, faz a contagem do giro e bloqueia na outra direção.

#### <mark> **ESQUERDA** </mark> 
> Libera a passagem para a esquerda, faz a contagem do giro, e bloqueia na outra direção.

### <mark> **setGiro** </mark> 
> Esta classe define a direção do giro, deve receber um enum entre os 3 enums sitados acima, através da função ordinal.

&nbsp;

***

## Classes Caso Ticket Negado
### <mark> **TicketNegado** </mark> 
> informa por 2s que o ticket foi negado e bloqueia a passagem caso algum dos sensores seja acionado(tentativa de passagem). 

&nbsp;
***



> ![texto alt](https://media.giphy.com/media/tIeCLkB8geYtW/giphy.gif)
> 
> Obrigado!