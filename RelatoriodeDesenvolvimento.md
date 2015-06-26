# Relatório de Desenvolvimento #

Esta página contém o relatório de implementação do compilador desenvolvido, onde exaltamos as funcionalidades implementadas e apontamos suas limitações.

## Objetivo ##
O objetivo deste trabalho reportado é aplicar os conceitos e técnicas relacionados à construção de um compilador vistos na disciplina.

Toda a implementação está focada na linguagem Mini Java, uma linguagem criada para fins didáticos e cujas estruturas são herdadas da linguagem de programação Java. A Mini Java possui apenas construções simples e essenciais a uma linguagem de computação, deixando de lado funcionalidades extras tais como: sobrecarga de métodos, classes anônimas, operações com números reais ou negativos etc. embora ainda faça uso do paradigma de orientação a objetos presente no Java.[
<a href='http://www.cambridge.org/us/features/052182060X/'>Referência MiniJava</a>]

Todo o trabalho está sendo feito com o JAVACC que é um gerador de analisador sintático que produz código Java. Ele permite que uma determinada linguagem seja definida de maneira simples, por meio de uma notação semelhante à EBNF. Como saída produz o código-fonte de algumas classes Java que implementam os analisadores léxico e sintático para aquela linguagem. Provê também maneiras de incluir, junto à definição da linguagem, código Java para, por exemplo, construir-se a árvore de derivação do programa analisado.

## Índice ##
<ul>
<li><a href='http://code.google.com/p/compilador-mini-java/wiki/Introducao'>Introdução</a></li>
<li><a href='http://code.google.com/p/compilador-mini-java/wiki/AnaliseL'>Análise Léxica</a></li>
<li><a href='http://code.google.com/p/compilador-mini-java/wiki/AnaliseS'>Análise sintática</a></li>
<li><a href='#'>Análise Semântica</a></li>
<ul>
<li><a href='http://code.google.com/p/compilador-mini-java/wiki/ASB'>Árvore de Sintaxe Abstrata</a></li>
<li><a href='http://code.google.com/p/compilador-mini-java/wiki/AnaliseSemantica'>Tabela de Símbolos</a></li>
</ul>
<li><a href='http://code.google.com/p/compilador-mini-java/wiki/RI'>Representação Intermediária</a></li>
<li><a href='http://code.google.com/p/compilador-mini-java/wiki/Blocos'>Blocos Básicos e Traços</a></li>
<li><a href='http://code.google.com/p/compilador-mini-java/wiki/Selecao'>Seleção de Instrução</a></li>
<li><a href='http://code.google.com/p/compilador-mini-java/wiki/Longevidade'>Análise de Longevidade </a></li>
<li><a href='http://code.google.com/p/compilador-mini-java/wiki/Registradores'>Alocação de Registradores</a></li>
<li><a href='http://code.google.com/p/compilador-mini-java/wiki/Testes'>Testes</a></li>
<li><a href='http://code.google.com/p/compilador-mini-java/wiki/Arquivos'>Arquivos</a></li>
<li><a href='http://code.google.com/p/compilador-mini-java/wiki/Conclusao'>Conclusão</a></li>
<li><a href='http://code.google.com/p/compilador-mini-java/wiki/Modificacoes'>Modificações</a></li>
</ul>