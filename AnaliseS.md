## Análise Sintática ##
Na fase sintática, também de responsabilidade do JAVACC, definimos as produções da gramática apresentada no Apêndice A do livro-texto. Esta fase analisa como os tokens são colocados juntos para formar sentenças válidas na linguagem Mini Java. <br />
Também não foram encontradas grandes dificuldades nesta fase.

As produções da gramática foram definidas e podem ser vistas <a href='http://code.google.com/p/compilador-mini-java/downloads/detail?name=RegrasGramaticais.txt&can=2&q='>aqui</a>

Algumas modificações na gramática do livro-texto foram necessárias, de forma a retirar as suas ambiguidades.

A compilação deste arquivo pelo utilitário do JAVACC também se deu sem erros ou avisos, o que nos mostra que as definições das produções estão corretas. Como nenhuma construção mostrada na gramática apresentada pelo livro foi esquecida, acreditamos que esta fase está completa com êxito.

Diversos testes também foram realizados a fim de testar esta fase. Todos eles finalizaram com sucesso sob a implementação atual, sem que erros inesperados ocorressem.

Essa fase foi dividida da seguinte forma: Danusa e Camila ficaram responsáveis pela implementação; Lucas e Paulo Jr ficaram responsáveis pela documentação do projeto.

<b>Implementação:</b> <font color='#1E90FF'> terminada </font>

<b>Funcionalidade Implementadas:</b> <font color='#1E90FF'>parser da sequencia de tokens de entrada, validação do programa</font>

<b>Funcionalidades Remanescentes:</b> <font color='#1E90FF'> nenhuma. </font>

<b>Testes:</b> <font color='#1E90FF'>completados com sucesso.</font>

<b>Aval Final:</b> <font color='#1E90FF'>classe completamente implementada.</font>


<div>
<a href='http://code.google.com/p/compilador-mini-java/wiki/AnaliseL'>preview</a> |<br>
<a href='http://code.google.com/p/compilador-mini-java/wiki/ASB'> next</a>
</div>