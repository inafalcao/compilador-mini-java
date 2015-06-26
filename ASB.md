# Árvore de Sintaxe Abstrata #

Devido às próprias funcionalidades nativas do JavaCC, não foi necessário programar o código para gerar a Árvore de Sintaxe Abstrata correspondente ao código-fonte sendo compilado.

Foi utilizado algumas classes (syntaxtree, visitor) do framework (http://www.cambridge.org/resources/052182060X/)

A partir dessa definição, o próprio JavaCC se encarrega de gerar o código citado e as respectivas classes representativas dos nós da árvore. Com isso, através de comparações instanceof do próprio Java, podemos determinar com qual das regras da AST estamos lidando.

Como o código gerado é automático, nenhum teste foi realizado, bastando verificar a corretude das regras de transformação aplicadas.

Todos implementaram algumas funções e métodos relacionados a esse módulo do compilador.

<b>Implementação:</b> <font color='#1E90FF'> terminada </font>

<b>Funcionalidade Implementadas:</b> <font color='#1E90FF'>geração da árvore abstrata.</font>

<b>Funcionalidades Remanescentes:</b> <font color='#1E90FF'> nenhuma. </font>

<b>Testes:</b> <font color='#1E90FF'>completados com sucesso.</font>

<b>Aval Final:</b> <font color='#1E90FF'>classe completamente implementada.</font>

<div>
<a href='http://code.google.com/p/compilador-mini-java/wiki/AnaliseS'>preview</a> |<br>
<a href='http://code.google.com/p/compilador-mini-java/wiki/AnaliseSemantica'> next</a>
</div>