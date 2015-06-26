# Representação Intermediária #

A motivação para a construção de uma árvore de representação intermediária(IR) para o programa é que ela independe de linguagem fonte e da arquitetura de máquina alvo, e ainda torna mais fácil o processo de tradução para código de máquina.

Para construir a árvore de representação intermediária, percorremos a árvore sintática abstrata usando o padrão Visitor, tal que cada nó da árvore é um objeto Exp que possui uma expressão como atributo.

As classes do pacote treeIR representam a estrutura de dados que armazena o código intermediário, no nosso caso uma árvore IR. No pacote translate são encontradas as classes necessárias para a tradução do código. Na tradução de código, para cada método definido na árvore sintática, um fragmento de código é gerado com o auxílio de um objeto Frame. O objeto Frame foi necessário para gerar o código do prólogo e epílogo dos fragmentos.

Essa fase foi dividida da seguinte forma: Danusa e Lucas ficaram responsáveis pela implementação do código; Camila e Paulo Jr ficaram responsáveis pela Documentação

A maior dificuldade encontrada nessa etapa foi construir a árvore IR gerando os fragmentos utilizando o Frame e entender como mostrar o resultado dessa fase. Contudo, mesmo sob as diversas dificuldades a equipe se reuniu e conseguiu finalizar todos os módulos.

<b>Implementação:</b> <font color='#1E90FF'> terminada </font>

<b>Funcionalidade Implementadas:</b> <font color='#1E90FF'>geração da árvore IR para a árvore abstrata dada como entrada e<br>
os fragmentos do código</font>

<b>Funcionalidades Remanescentes:</b> <font color='#1E90FF'> nenhuma. </font>

<b>Testes:</b> <font color='#1E90FF'>completados com sucesso.</font>

<b>Aval Final:</b> <font color='#1E90FF'>classe completamente implementada.</font>

<div><a href='http://code.google.com/p/compilador-mini-java/wiki/AnaliseSemantica'>preview</a> | <a href='http://code.google.com/p/compilador-mini-java/wiki/Blocos'>next</a>
</div>