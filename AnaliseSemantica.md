# Tabela de Símbolos #

A análise semântica tem como objetivo conectar a definição de variáveis ao seu uso, checando se cada expressão tem um tipo correto, o que é feito com a checagem de tipo, e traduzir a sintaxe abstrata em uma representação adequada para gerar código de máquina. Esta fase se caracteriza pela manutenção de uma tabela de símbolos, que mapeia identificadores aos seus tipos e localização.

Como trata-se de uma linguagem imperativa (já que é uma restrição da linguagem Java), a tabela de símbolos para a MiniJava foi implementada utilizando-se tabelas de dispersão (hashtables).

As classes referentes à implementação da tabela de símbolos podem ser encontrados nos pacotes: SymbolTable, onde são encontradas as classes que representam a estrutura de dados que armazena a tabela de símbolos e o Visitor que, a partir da árvore sintática constrói a tabela; TypeChecking, onde é definido o Visitor que faz a checagem de tipos.

Nessa parte da implementação todos fizeram uma parte, principalmente na parte de verificação de tipos, que foi o que mais tivemos dificuldades para a implementação, pois, primeiro estávamos com dificuldades para entender como construir a tabelas de símbolos e verificar cada tipo.

Essa fase foi dividida da seguinte forma: Danusa e Camila ficaram responsáveis pela criação da Tabela de Símbolo; Lucas e Paulo Jr ficaram responsáveis pela implementação dos Visitors.

Todo esse módulo foi totalmente implementado e testado.


<b>Implementação:</b> <font color='#1E90FF'> terminada </font>

<b>Funcionalidade Implementadas:</b> <font color='#1E90FF'>construção da tabela de tipos e efetuação da checagem de tipos<br>
do programa.</font>

<b>Funcionalidades Remanescentes:</b> <font color='#1E90FF'> nenhuma. </font>

<b>Testes:</b> <font color='#1E90FF'>completados com sucesso.</font>

<b>Aval Final:</b> <font color='#1E90FF'>classe completamente implementada.</font>


<div><a href='http://code.google.com/p/compilador-mini-java/wiki/ASB'>preview</a> | <a href='http://code.google.com/p/compilador-mini-java/wiki/RI'>next</a>
</div>